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
     *
     *
     *
     *
     *
     *
     */
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";


    public static final String ALGORITHM_NAME_CBC_PADDING = "SM4/CFB/PKCS5Padding";

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
        //指定一个初始化向量 . 防止后续加密的过程中被篡改 . 主要用于反馈模式的加密.如CBC,CFB等
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
     * ECB加密
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
