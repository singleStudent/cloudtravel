package com.cloudtravel.common.smencrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

/**
 * @description : SM4加密算法工具类
 * 属于对称加密算法,可用于替代DES/AES等算法,且: SM4算法同AES算法具有相同的密钥长度和分组长度,都是128位.
 * @author : walker
 * @date : 2022/8/24 23:45
 */
public class SM4Utils {


    /** 向Java.security包添加加密算法支持 */
    static {
        // 删除当前的
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        //提供SM4相关加密算法支持
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String EN_CODING = "UTF-8";

    public static final String ALGORITHM_NAME = "SM4";

    /**
     * 三部分组成 : 加密算法名/分组加密模式/分组填充方式
     * 加密算法名 : SM4 , SM3 , RSA , DES等
     * 分组加密模式: ECB , CBC , CFB , OFB , CTR
     *  分组:密码加解密模式:大类有:分组密码和流密码两种.
     *      分组密码[BlockCipher]:每次处理特定的数据块.[block],其中一个分组的比特数就成为分组长度[block_size]
     *          例如:DES和SM4都是64bits[8字节].AES可以128/192/256几种选择.这类算法一次只加密对应块大小的明文,
     *          并生成对应块大小的密文.
     *      流密码:是对数据进行连续处理的一类密码算法 . 一般以1bits / 8bits /32bits等为单位进行加密和解密.
     *      这里的分组加密模式就是分组密码.
     *  模式:分组面只能对明文进行固定长度分组进行加密.当明文被拆分成多个分组时,就需要对分组密码进行迭代,便于将所有明文都加密.
     *      而迭代的方法就成为分组密码模式.也就是对分组密码进行加密的方式.
     *  ECB:[Electronic CodeBook Mode]:电子密码本模式
     *      其加密方式为:填充后的各分组各自加密后密文拼接则为输出结果.相同分组的明文加密后的结果相同.因此也成为密码本加密.
     *      攻击时只需按固定长度密文打乱顺序,解密时的明文顺序就会变化.风险较大
     *  使用分析: 支持并行计算.简单快速 . 但因为明文的重复会在密文中体现出来 . 可攻击性高 . 需要填充
     *  CBC:[Cipher Block Chaining mode]:密码分组链接模式
     *      将前密文分组和当前明文分组通过异或处理后再进行加密的.这样就避免了ECB模式的明文分组和密文分组一一对应的风险
     *      因为它的明文分组和密文分组依次进行运算相关联 . 所以这里称之为密码链接模式
     *      第一个明文分组加密时因为没有前一个密文分组 , 会准备一个长度问分组的二进制序列代码需要的密文分组作为初始化向量
     *      解密时:则逆向处理,先将当前密文分组进行解密,再和上一个密文进行异或处理,即得到当前分组的明文.依次而行.
     *  使用分析 : 明文重复不会体现在密文中. 仅解密时支持并行计算 . 需要填充
     *  CFB:[Cipher FeedBack mode]:密文反馈模式
     *      区别于CBC的地方在于,初始化向量和密钥进行加密后和明文分组进行异或处理直接得到密文分组,然后密文分组再加密后和后续的明文分组进行异或处理,
     *      依次处理.相比而言,每一组的明文加密的过程中比CBC少了一次密钥加密的处理,但是每次异或之前上一组的密文分组[或初始化向量]会先进行加密 .
     *      CFB模式中由密码算法生成的二进制序列类似于时一种流式的数据处理
     *      解密时:则逆向处理,先将上一个密文分组进行加密[注意这里依然是加密],再进行异或处理,即得到当前分组的明文.依次而行.
     *  使用分析 : 不需要填充,解密时支持并行计算 .
     *  OFB:[OutPut FeedBack mode]:输出反馈模式
     *      加密时: 初始化向量先进行加密,和第一个明文分组异或处理后得到当前明文分组对应的密文分组.
     *             下一个明文分组则 :再次对加密后的向量密文进行加密,再和明文分组进行异或处理得到密文分组.区别于CFB的是每次加密对象
     *             都是上一个初始化向量加密后的密文.而CFB则是对密文分组进行加密
     *      解密时:也是对初始化向量进行加密.再和密文分组进行异或处理得到明文分组 , 依次而行
     *      属于流密码
     *  使用分析 : 不需要填充,加密解密时可实现准备好对初始化向量的加密序列 .不支持并行
     *  CTR:[Counter Mode]:计数器模式
     *      每个分组对应一个逐次累加的计数器,并通过计数器进行加密生成密钥流.再和明文分组进行异或处理得到密文分组.
     *      解密时:同样是对计数器进行加密处理而不是解密
     *      属于流密码
     *  使用分析 : 不需要填充,持并行计算（加密、解密）
     * 分组填充方式: 加密数据时用来填充数据的一种模式.PKCS7Padding/PKCS5Padding/ZeroPadding
     *  block_size:块大小 , 进行加解密时 , 为避免明文长度过长导致一次加密的数据量太大. 会进行分块处理.
     *             而分块的时候则是参照block_size进行分.大多数分块大小默认都是64bits[8个字节] .
     *             如果明文的大小不是分块的整数倍,就需要在末尾进行填充.
     *  PKCS5Padding:可以理解为为PKCS7的子集 , 分块大小标准为8个字节.那么当最后一块的数据长度为n[n<8]时,
     *              则需要填充8-n个差值数据. 例:最后一块数据有5个字节,则需要填充3个0x03 .
     *              若明文数据为分块大小的整数倍,当使用PKCS5填充时 , 同样需要填充一组[8个]0x08
     *  PKCS7Padding:原理和PKCS5Padding一样,只是分块长度不一样 , 它的分块大小可以在1~255bytes之间.
     *  ZeroPadding : 数据长度不对齐时用0填充 . 否则不填充
     *  解密时: 由于最后一个字节肯定为填充数据的长度 , 所以在解密时可以准确删除填充的数据 . 而使用ZeroPadding
     *         的时候,由于最后填充的是0,没有办法区分真实数据和填充数据,所以只适合以\0结尾的字符串加解密
     *         \0: ASCII码为0,表示一个字符串结尾的标志.这是转义字符,底层处理时会视为整体的一个字符.内存中表现为8个0.
     *         0:位数字 . 内存中为32位的
     *         '0';字符.ASCII表示48.内存中为00110000
     */
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";


    public static final String ALGORITHM_NAME_CBC_PADDING = "SM4/CBC/PKCS5Padding";

    public static final String ALGORITHM_ECB = "ECB";

    public static final String ALGORITHM_CBC = "CBC";

    //128-32位16进制;256-64位16进制
    public static final int DEFAULT_KEY_SIZE = 128;

    //密钥:可以选择16位和32位 .
    // 16位的jdk默认支持
    // 32位:
    //  Windows下需要替换jdk/bin/jre/lib/security下的local_policy.jar和US_export_policy.jar,否则会报异常.
    //  Linux中则需要修改jdk/bin/jre/lib/security下java.security文件.解除禁用#crypto.policy=unlimited
    // 下载地址: https://www.oracle.com/java/technologies/javase-jce8-downloads.html
    private static final String EPIDEMIC_KEY = "5m28850d763e8748ff2f8d83530e0cf2";

    /**
     * 得到初始化向量--CBC/CFB等模式下使用
     * @param keySize
     * @return
     * @throws Exception
     */
    public static byte[] generateKey (int keySize)throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        kg.init(keySize, random);
        return kg.generateKey().getEncoded();
    }

    /**
     * 初始化向量转换为String
     * @return
     * @throws Exception
     */
    public static String generateKey() throws Exception{
        return ByteUtils.toHexString(generateKey(DEFAULT_KEY_SIZE));
    }


    /**
     * 初始化ECB模式下的加解算法类
     * @param algorithmName
     * @param mode
     * @param key
     * @return
     * @throws Exception
     */
    private static Cipher generateEcbCipher(String algorithmName ,
                                            int mode ,
                                            byte[] key) throws Exception {
        //Cipher:为加密和解密提供密码功能. 是JCE的核心.
        //初始化该类需提供需要转换的算法名
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key , ALGORITHM_NAME);
        cipher.init(mode , sm4Key);
        return cipher;
    }


    /**
     * 初始化CBC模式下的加解算法类
     * @param algorithmName
     * @param mode
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    private static Cipher generateCbcCipher(String algorithmName ,
                                            int mode ,
                                            byte[] key ,
                                            byte[] iv) throws Exception {
        //Cipher:为加密和解密提供密码功能. 是JCE的核心.
        //初始化该类需提供需要转换的算法名
        Cipher cipher = Cipher.getInstance(ALGORITHM_NAME_CBC_PADDING, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key , ALGORITHM_NAME);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(mode , sm4Key , ivParameterSpec);
        return cipher;
    }


    public static String encrypt(String paramStr , String groupCryptMode) {
        try {
            if(paramStr != null && !"".equals(paramStr)){
                String cipherText = "";
                // 16进制字符串 ---> byte[]
                byte[] keyData = ByteUtils.fromHexString(EPIDEMIC_KEY);
                // String ---> byte[]
                byte[] srcData = paramStr.getBytes(EN_CODING);
                // 加密后的byte数组
                byte[] cipherArray = null;
                switch (groupCryptMode) {
                    case "ECB":
                        cipherArray = encrypt_Ecb_Padding(keyData , srcData);
                        cipherText = ByteUtils.toHexString(cipherArray);
                        break;
                    case "CBC":
                        byte [] iv = generateKey(DEFAULT_KEY_SIZE);
                        cipherArray = encrypt_CBC_Padding(keyData , srcData , iv);
                        cipherText = ByteUtils.toHexString(cipherArray);
                        cipherText = cipherText + "_" + ByteUtils.toHexString(iv);
                }
                return cipherText;
            }else{
                return paramStr;
            }
        } catch (Exception e) {
            return paramStr;
        }
    }


    /**
     * 加密 , 加密过程如下:简单讲就是密钥拓展和加密[或解密]两部
     * 一. 密钥拓展
     * 1.将密钥
     * @param paramStr
     * @return
     */
    public static String encryptWithEcb(String paramStr) {
        try {
            if(paramStr != null && !"".equals(paramStr)){
                String cipherText = "";
                // 16进制字符串 ---> byte[]
                byte[] keyData = ByteUtils.fromHexString(EPIDEMIC_KEY);
                // String ---> byte[]
                byte[] srcData = paramStr.getBytes(EN_CODING);
                // 加密后的byte数组
                byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);
                // byte[] ---> hexString
                cipherText = ByteUtils.toHexString(cipherArray);
                return cipherText;
            }else{
                return paramStr;
            }
        } catch (Exception e) {
            return paramStr;
        }
    }

    /**
     * 加密 , 加密过程如下:简单讲就是密钥拓展和加密[或解密]两部
     * 一. 密钥拓展
     * 1.将密钥
     * @param paramStr
     * @return
     */
    public static String encryptWithCBC(String paramStr , byte[] iv) {
        try {
            if(paramStr != null && !"".equals(paramStr)){
                String cipherText = "";
                // 16进制字符串 ---> byte[]
                byte[] keyData = ByteUtils.fromHexString(EPIDEMIC_KEY);
                // String ---> byte[]
                byte[] srcData = paramStr.getBytes(EN_CODING);
                // 加密后的byte数组
                byte[] cipherArray = encrypt_CBC_Padding(keyData, srcData , iv);
                // byte[] ---> hexString
                cipherText = ByteUtils.toHexString(cipherArray);
                return cipherText;
            }else{
                return paramStr;
            }
        } catch (Exception e) {
            return paramStr;
        }
    }


    public static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static byte[] encrypt_CBC_Padding(byte[] key, byte[] data, byte[] iv) throws Exception {
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.ENCRYPT_MODE, key , iv);
        return cipher.doFinal(data);
    }

    public static String decrypt(String cipherText , String groupCryptMode) {
        if (cipherText != null && !"".equals(cipherText)) {
            // 用于接收解密后的字符串
            String decryptStr = "";
            byte[] keyData = ByteUtils.fromHexString(EPIDEMIC_KEY);
            byte[] srcData = new byte[0];
            byte[] cipherData = null;
            try {
                switch (groupCryptMode) {
                    case "ECB":
                        cipherData = ByteUtils.fromHexString(cipherText);
                        srcData = decrypt_Ecb_Padding(keyData, cipherData);
                        break;
                    case "CBC":
                        String ivStr = cipherText.substring(cipherText.indexOf("_")+1);
                        cipherText = cipherText.substring(0 , cipherText.indexOf("_"));
                        cipherData = ByteUtils.fromHexString(cipherText);
                        srcData = decrypt_Cbc_Padding(keyData, cipherData , ByteUtils.fromHexString(ivStr));
                }
                decryptStr = new String(srcData, EN_CODING);
                return decryptStr;
            } catch (Exception e) {
                return cipherText;
            }
        }else{
            return cipherText;
        }
    }

    /**
     * 解密
     */
    public static String decryptWithEcb(String cipherText) {
        if (cipherText != null && !"".equals(cipherText)) {
            // 用于接收解密后的字符串
            String decryptStr = "";
            byte[] keyData = ByteUtils.fromHexString(EPIDEMIC_KEY);
            byte[] cipherData = ByteUtils.fromHexString(cipherText);
            byte[] srcData = new byte[0];
            try {
                srcData = decrypt_Ecb_Padding(keyData, cipherData);
                decryptStr = new String(srcData, EN_CODING);
                return decryptStr;
            } catch (Exception e) {
                return cipherText;
            }
        }else{
            return cipherText;
        }
    }

    public static String decryptWithCbc(String cipherText , byte[] iv) {
        if (cipherText != null && !"".equals(cipherText)) {
            // 用于接收解密后的字符串
            String decryptStr = "";
            byte[] keyData = ByteUtils.fromHexString(EPIDEMIC_KEY);
            byte[] cipherData = ByteUtils.fromHexString(cipherText);
            byte[] srcData = new byte[0];
            try {
                srcData = decrypt_Cbc_Padding(keyData, cipherData , iv);
                decryptStr = new String(srcData, EN_CODING);
                return decryptStr;
            } catch (Exception e) {
                return cipherText;
            }
        }else{
            return cipherText;
        }
    }


    public static byte[] decrypt_Ecb_Padding(byte[] key, byte[] cipherText) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    public static byte[] decrypt_Cbc_Padding(byte[] key, byte[] cipherText ,byte[] iv) throws Exception {
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.DECRYPT_MODE, key ,iv);
        return cipher.doFinal(cipherText);
    }


    public static boolean verifyEcb(String hexKey, String cipherText, String paramStr) throws Exception {
        // 用于接收校验结果
        boolean flag = false;
        // hexString--&gt;byte[]
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        // 将16进制字符串转换成数组
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        // 解密
        byte[] decryptData = decrypt_Ecb_Padding(keyData, cipherData);
        // 将原字符串转换成byte[]
        byte[] srcData = paramStr.getBytes(EN_CODING);
        // 判断2个数组是否一致
        flag = Arrays.equals(decryptData, srcData);
        return flag;
    }


    public static void main(String[] args) throws Exception {
        String data = "你哈哈记得哈健康的哈客户打开链接 按计划大健康的哈利的哈利的好了哈来得及哈萨克的" +
                "骄傲和看到啦活动卡技术科技阿哈利科技  哈就是大家来客户打卡记录的哈就开始打哈进口量的哈快乐就好" +
                "你哈哈记得哈健康的哈客户打开链接 按计划大健康的哈利的哈利的好了哈来得及哈萨克的" +
                "骄傲和看到啦活动卡技术科技阿哈利科技  哈就是大家来客户打卡记录的哈就开始打哈进口量的哈快乐就好" +
                "你哈哈记得哈健康的哈客户打开链接 按计划大健康的哈利的哈利的好了哈来得及哈萨克的" +
                "骄傲和看到啦活动卡技术科技阿哈利科技  哈就是大家来客户打卡记录的哈就开始打哈进口量的哈快乐就好"+
                "骄傲和看到啦活动卡技术科技阿哈利科技  哈就是大家来客户打卡记录的哈就开始打哈进口量的哈快乐就好" +
                "你哈哈记得哈健康的哈客户打开链接 按计划大健康的哈利的哈利的好了哈来得及哈萨克的" +
                "骄傲和看到啦活动卡技术科技阿哈利科技  哈就是大家来客户打卡记录的哈就开始打哈进口量的哈快乐就好";
        String a = "8424096784240967";
        //5c6c0a1406853bedfef1c85a5c28f820 f2eb31e858a2c57a225026988670e050
        //5c6c0a1406853bedfef1c85a5c28f820 5c6c0a1406853bedfef1c85a5c28f820 f2eb31e858a2c57a225026988670e050
        String encryptDATA = encryptWithEcb(a);
        System.out.println("ECB加密结果1：" + encryptDATA);
        System.out.println("ECB解密结果1：" + decryptWithEcb(encryptDATA));

        System.out.println("CBC加密:");
        byte [] iv = generateKey(DEFAULT_KEY_SIZE);
        System.out.println(iv);
        System.out.println("初始化向量:IV = " + ByteUtils.toHexString(iv));
        encryptDATA = encryptWithCBC(a , iv);
        System.out.println("CBC加密结果1：" + encryptDATA);
        System.out.println("CBC解密结果1：" + decryptWithCbc(encryptDATA , iv));

        System.out.println("使用包装方法 ===> : ");
        encryptDATA = encrypt(a , ALGORITHM_ECB);
        System.out.println("ECB加密后 2: " +  encryptDATA);
        System.out.println("ECB解密后 2: " +  decrypt(encryptDATA , ALGORITHM_ECB));

        encryptDATA = encrypt(a , ALGORITHM_CBC);
        System.out.println("CBC加密后 2: " +  encryptDATA);
        System.out.println("CBc解密后 2: " +  decrypt(encryptDATA , ALGORITHM_CBC));
    }

}
