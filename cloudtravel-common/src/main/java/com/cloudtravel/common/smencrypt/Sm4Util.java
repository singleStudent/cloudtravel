package com.cloudtravel.common.smencrypt;

import com.cloudtravel.common.exception.CloudTravelException;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

/**
 * @description : SM4加密算法工具类
 * 属于对称加密算法,可用于替代DES/AES等算法,采用分组加密 ,且SM4算法同AES算法具有相同的密钥长度和分组长度,都是128bits[16进制==>32位].
 * 整个加解密部分可以拆分成密钥扩展和加解密两步
 * 密钥扩展:
 *   一堆线性函数我是没看太懂.简单理解下感觉就是
 *      1.对密钥进行分组后得到MK=(MK0,MK1,MK2,MK3)
 *      2.根据(MK0,MK1,MK2,MK3)得到(K0,K1,K2,K3)=(MK0 ⊕ FK0,MK1 ⊕ FK1 , MK2 ⊕ FK2 , MK3 ⊕ FK3)
 *          其中:FK为系统参数.16进制表示类似于: FK0=(a3b1c6)知道这个就好了.具体的数学学得太垃圾.搞不懂
 *      3.因为加密时要进行非线性迭代32次 . 所以这里生成32位轮密钥:rk0~rk31
 *        rk[i] = k[i+4] = K[i] ⊕ T'(K[i+1] ⊕ K[i+2] ⊕ k[i+3] ⊕ CKi)
 *        其中:
 *          1.T变换和加密算法中的轮函数基本相同 , 只是将其中的线性变换L换成了:L[B] = B ⊕ (B <<< 13) ⊕ (B <<< 23)
 *          2. 固定参数CK的取值方法为:设ck[i,j]为CK[i]的第j字节(i=0~31 , j=0~3),
 *              即CK[i] = (ck[i,0],ck[i,1],ck[i,2],ck[i,3])∈(Z[下标2,上标8]4次方)-依稀记得以前见过这个写法.忘记代表啥了
 *              其中:ck[i,j] = (4i + j) * 7 (mod 256)共得到32个CKi
 * 加密过程:包括32次轮迭代[就是非线性迭代]和反序迭代[异或]两步
 *  32次轮迭代:
 *      分组明文拆分为(A0 ,A1,A2,A3)结合密码扩展得到的32组轮密码.轮函数F进行32次迭代
 *      Xi+4=F(Xi,Xi+1,Xi+2,Xi+3,rki)=Xi⊕T(Xi+1⊕Xi+2⊕Xi+3⊕rki),i=0,1,⋯,31
 *      合成置换 T:Z322→Z322 是一个可逆变换，由非线性变换 τ 和线性变换 L复合而成，即 T(⋅)=L(τ(⋅))。
 *      线性变换 L： L(B)=B⊕(B<<<2)⊕(B<<<10)⊕(B<<<18)⊕(B<<<24)。
 *      非线性变换 τ： τ(B)，τ 由4个并行的S盒构成。
 * 反序迭代:得到的x4~x35中得到x32~x35
 * 总结下来就是
 *  1. 先将128比特密钥 MK 扩展为32个轮密钥 rk，
 *  2. 再将该轮密钥与128比特明文 X 经过轮函数进行32次迭代后，选取最后四次迭代生成的结果 X32,X33,X34,X35 进行反序变换，
 *      该变换结果作为最终的密文 Y 输出
 * 解密时算法内容大致一样 . 只不过轮密钥顺序相反
 * @author : walker
 * @date : 2022/8/24 23:45
 */
public class Sm4Util {

    /** 密钥 */
    public static final String PASSWORD = "5m28850d763e8748ff2f8d83530e0cf2";

    /** 128-32位16进制;256-64位16进制 */
    public static final int DEFAULT_KEY_SIZE = 128;

    public static final String APPEND_SEPARATOR = "/";

    private static final String EN_CODING = "UTF-8";

    static {
        //为防止当前加密支持器影响该算法,先将当前加密支持器删除
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        //添加SM加密算法相关支持器
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 默认方法: SM2/CBC/PKCS5Padiing
     * @param param
     * @return
     */
    public static String encrypt(String param) {
        return encrypt(AlgorithmNameEnums.SM_4 , AlgorithmModeEnums.CBC , AlgorithmPaddingModeEnums.PKCS5_PADDING , param);
    }

    /**
     * 加密
     * @param algorithmName 算法名
     * @param algorithmMode 分组加密模式
     * @param algorithmPadding 分组填充算法枚举
     * @param param 待加密参数
     * @return
     */
    public static String encrypt(AlgorithmNameEnums algorithmName ,
                                 AlgorithmModeEnums algorithmMode,
                                 AlgorithmPaddingModeEnums algorithmPadding ,
                                 String param) {

        String cipherText = "";
        try {
            if(StringUtils.isNotBlank(param)) {
                byte [] pwdBytes = ByteUtils.fromHexString(PASSWORD);
                byte [] paramBytes = param.getBytes(EN_CODING);
                byte [] cipherArray = null;
                boolean needIV = algorithmMode.getNeedIV();
                Cipher cipher = null;
                if(needIV){
                    byte [] iv = generateKey(algorithmName.getName() ,DEFAULT_KEY_SIZE);
                    cipher = generateCipher(algorithmName , algorithmMode , algorithmPadding , Cipher.ENCRYPT_MODE , pwdBytes , iv);
                    cipherArray = cipher.doFinal(paramBytes);
                    cipherText = ByteUtils.toHexString(cipherArray);
                    //方便展示,就把生成的初始化向量拼接在了后面,需要的话其实可以生成一个固定存为变量或者存到表中
                    cipherText = cipherText + "_" + ByteUtils.toHexString(iv);
                }else  {
                    cipher = generateCipher(algorithmName , algorithmMode , algorithmPadding , Cipher.ENCRYPT_MODE ,pwdBytes , null);
                    cipherArray = cipher.doFinal(paramBytes);
                    cipherText = ByteUtils.toHexString(cipherArray);
                }
            }else {
                return param;
            }
        }catch (Exception e) {
            throw new CloudTravelException(String.format("加密异常, algorithmName={%s}, algorithmMode={%s} ," +
                            "algorithmPadding={%s} ,param={%s},message={%s}" , algorithmName.getName(),
                    algorithmMode.getModeName() , algorithmPadding.name() , param , e.getMessage()));
        }
        return cipherText;
    }

    public static String decrypt(String param){
        return decrypt(AlgorithmNameEnums.SM_4 , AlgorithmModeEnums.CBC , AlgorithmPaddingModeEnums.PKCS5_PADDING , param);
    }

    /**
     * 解密
     * @param algorithmName 算法名
     * @param algorithmMode 分组加密模式
     * @param algorithmPadding 填充方式---解密时需逆向截取加密的明文
     * @param param 待解密参数
     * @return
     */
    public static String decrypt(AlgorithmNameEnums algorithmName ,
                                 AlgorithmModeEnums algorithmMode,
                                 AlgorithmPaddingModeEnums algorithmPadding ,
                                 String param) {
        if(StringUtils.isNotBlank(param)) {
            try {
                String decryptStr = "";
                byte [] pwdBytes = ByteUtils.fromHexString(PASSWORD);
                byte [] paramBytes = new byte [0];
                byte [] cipherArray = null;
                boolean containsIV = algorithmMode.getNeedIV();
                Cipher cipher = null;
                if(containsIV) {
                    String paramStr = param.substring(0 , param.indexOf("_"));
                    String ivStr = param.substring(param.indexOf("_") + 1);
                    byte [] ivBytes = ByteUtils.fromHexString(ivStr);
                    paramBytes = ByteUtils.fromHexString(paramStr);
                    cipher = generateCipher(algorithmName , algorithmMode , algorithmPadding , Cipher.DECRYPT_MODE , pwdBytes , ivBytes);
                }else  {
                    paramBytes = ByteUtils.fromHexString(param);
                    cipher = generateCipher(algorithmName , algorithmMode , algorithmPadding , Cipher.DECRYPT_MODE , pwdBytes , null);
                }
                cipherArray = cipher.doFinal(paramBytes);
                decryptStr = new String(cipherArray , EN_CODING);
                return decryptStr;
            }catch (Exception e) {
                throw new CloudTravelException(String.format("解密异常, algorithmName={%s}, algorithmMode={%s} ," +
                                "algorithmPadding={%s} ,param={%s},message={%s}" , algorithmName.getName(),
                        algorithmMode.getModeName() , algorithmPadding.name() , param , e.getMessage()));
            }
        }else  {
            return param;
        }
    }


    /**
     * 初始化加解密算法执行器
     * @param algorithmName 算法名
     * @param algorithmMode 分组加密模式
     * @param algorithmPadding 分组填充模式
     * @param cipherMode 加密/解密
     * @param key 密钥
     * @param iv 初始化向量
     * @return 执行器
     * @throws Exception
     */
    private static Cipher generateCipher(AlgorithmNameEnums algorithmName ,
                                         AlgorithmModeEnums algorithmMode,
                                         AlgorithmPaddingModeEnums algorithmPadding,
                                         int cipherMode ,
                                         byte[] key ,
                                         byte[] iv) throws Exception {
        String algorithmNameModePadding = algorithmName.getName() + APPEND_SEPARATOR +
                algorithmMode.getModeName() + APPEND_SEPARATOR + algorithmPadding.getCode();
        //Cipher:为加密和解密提供密码功能. 是JCE的核心.
        //初始化该类需提供需要转换的算法名
        Cipher cipher = Cipher.getInstance(algorithmNameModePadding, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key , algorithmName.getName());
        boolean needIV = algorithmMode.getNeedIV();
        if(needIV){
            //指定一个初始化向量 . 防止后续加密的过程中被篡改 . 主要用于反馈模式的加密.如CBC,CFB等
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(cipherMode , sm4Key , ivParameterSpec);
        }else {
            cipher.init(cipherMode , sm4Key);
        }
        return cipher;
    }

    /**
     * 验证明文和密文是否被篡改
     * @param algorithmName 算法名
     * @param algorithmMode 模式
     * @param algorithmPadding 填充方式
     * @param paramStr 明文
     * @param encryptStr 密文
     * @return
     * @throws Exception
     */
    public static boolean verifyEcb(AlgorithmNameEnums algorithmName ,
                                    AlgorithmModeEnums algorithmMode,
                                    AlgorithmPaddingModeEnums algorithmPadding,
                                    String paramStr,
                                    String encryptStr) throws Exception {
        // 用于接收校验结果
        boolean flag = false;
        // 解密
        String decryptResult = decrypt(algorithmName , algorithmMode , algorithmPadding , encryptStr);
        // 判断是否一致
        flag = paramStr.equals(decryptResult);
        return flag;
    }



    /**
     * 生成初始化向量--CBC/CFB等模式下使用
     * @param keySize
     * @return
     * @throws Exception
     */
    public static byte[] generateKey (String algorithmName , int keySize)throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        kg.init(keySize, random);
        return kg.generateKey().getEncoded();
    }

    public static void main(String[] args) {
        String a = "快乐编码 , 生活愉快!";

        String ecbEncrypt = encrypt(AlgorithmNameEnums.SM_4 , AlgorithmModeEnums.ECB ,
                AlgorithmPaddingModeEnums.PKCS5_PADDING , a);
        System.out.println("ECB加密:" + ecbEncrypt);
        System.out.println("ECB解密:" + decrypt(AlgorithmNameEnums.SM_4 , AlgorithmModeEnums.ECB ,
                AlgorithmPaddingModeEnums.PKCS5_PADDING , ecbEncrypt));
        System.out.println();
        String cbcEncrypt = encrypt(AlgorithmNameEnums.SM_4 , AlgorithmModeEnums.CBC ,
                AlgorithmPaddingModeEnums.PKCS7_PADDING , a);
        System.out.println("CBC加密: " + cbcEncrypt);
        System.out.println("CBC解密" + decrypt(AlgorithmNameEnums.SM_4 , AlgorithmModeEnums.CBC ,
                AlgorithmPaddingModeEnums.PKCS7_PADDING , cbcEncrypt));
        System.out.println();
        String cfbEncrypt = encrypt(AlgorithmNameEnums.SM_4 , AlgorithmModeEnums.CFB ,
                AlgorithmPaddingModeEnums.PKCS7_PADDING , a);
        System.out.println("CFB加密: " + cfbEncrypt);
        System.out.println("CFB解密:" + decrypt(AlgorithmNameEnums.SM_4 , AlgorithmModeEnums.CFB ,
                AlgorithmPaddingModeEnums.PKCS7_PADDING , cfbEncrypt));
        System.out.println();
        String ofbEncrypt = encrypt(AlgorithmNameEnums.SM_4 , AlgorithmModeEnums.OFB ,
                AlgorithmPaddingModeEnums.PKCS7_PADDING , a);
        System.out.println("OFB加密: " + ofbEncrypt);
        System.out.println("OFB解密: " + decrypt(AlgorithmNameEnums.SM_4 , AlgorithmModeEnums.OFB ,
                AlgorithmPaddingModeEnums.PKCS7_PADDING , ofbEncrypt));
        System.out.println();
        String ctrEncrypt = encrypt(AlgorithmNameEnums.SM_4 , AlgorithmModeEnums.CTR ,
                AlgorithmPaddingModeEnums.PKCS7_PADDING , a);
        System.out.println("CTR加密: " + ctrEncrypt);
        System.out.println("CTR解密:" + decrypt(AlgorithmNameEnums.SM_4 , AlgorithmModeEnums.CTR ,
                AlgorithmPaddingModeEnums.PKCS7_PADDING , ctrEncrypt));


    }
}
