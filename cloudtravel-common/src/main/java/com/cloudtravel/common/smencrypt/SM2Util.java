package com.cloudtravel.common.smencrypt;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * @description: SM2加密算法--非对称加密
 * SM2椭圆曲线公钥密码算法是我国自主涉及的公钥密码算法,包括SM2-1椭圆曲线数字签名算法,SM2-2椭圆曲线密钥交换协议,
 * SM2-3椭圆曲线公钥加密算法.分别用于实现数据签名密钥协商和数据加密等功能 .
 * SM2和RSA算法的区别在于:SM2是基于椭圆曲线上点群离散对数难题 . 相对于RSA算法.256位的密码强度已经比RSA的2048位密码强度高
 * 一. ECC算法:SM2的基础
 * 1. 用户A选定一条适合加密的椭圆曲线Ep(a,b)(如y2=x3+ax+b).并取椭圆上一点.作为基点G
 * 2. 用户A选择一个私有密钥k,生成公开密钥(即公钥)K=kG
 * 3. A将Ep(a,b)和点K(即公钥),G传给用户B
 * 4. 用户B接到信息后,将待传输的明文(M)编码到椭圆Ep(a,b)上一点M,并产生一个随机整数r(r<n).开始加密
 * 5. 用户B计算点C1 = M+rK . C2 = rG , 并将C1,C2传给用户A
 * 6. 用户A接收到C1 & C2 . 计算C1-kC2 . 即 M+rK-k(rG) = M + rK - r(kG:第二步中K = kG.)= M
 * 再对点M进行解码就可以得到明文
 * 二.构成
 * SM2包括了:总则 , 数据签名算法 , 密钥交换协议 , 公钥加密算法四个部分
 * @author: walker
 * @DATE: 005-2022/9/5
 */
@Slf4j
public class SM2Util {

    /**
     * SM2算法生成密钥对
     * @return
     */
    public static KeyPair generateSm2KeyPair() {
        try {
            //用于生成椭圆曲线(EC)域参数的参数集
            final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("prime256v1");
            //获取一个椭圆曲线类型的密钥对生成器
            final KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC",new BouncyCastleProvider());
            //生成随机变量
            SecureRandom random = new SecureRandom();
            //使用SM2的算法区域初始化密钥生成器
            kpg.initialize(sm2Spec , random);
            //使用密钥对生成器生成密钥对
            KeyPair keyPair = kpg.generateKeyPair();
            return keyPair;
        }catch (Exception e) {
            log.error("SM2Utils==>Error:generateSm2KeyPair error, message = " + e.getMessage());
            return null;
        }
    }

    /**
     * 公钥加密
     * @param param
     * @param publicKey
     * @return
     */
    public static String encryptWithPublicKey(String param , String publicKey) {
        if(StringUtils.isNotBlank(param) && StringUtils.isNotBlank(publicKey)) {
            byte [] data = param.getBytes(StandardCharsets.UTF_8);
            byte [] publicKeyArr = Base64.getDecoder().decode(publicKey);
            byte [] res = encryptWithPublicKey(data , publicKeyArr);
            return Base64.getEncoder().encodeToString(res);
        }else {
            log.error("SM2Utils ==> encryptWithPublicKey error : param = {} , publicKey = {}" , param , publicKey);
        }
        return null;
    }

    /**
     * SM2公钥加密
     * @param data 参数
     * @param key 公钥
     * @return
     */
    public static byte[] encryptWithPublicKey(byte [] data , byte [] key) {
        try {
            //公钥的ASN.1编码 , 用来初始化公钥 , 对公钥编码转换
            KeySpec keySpec = new X509EncodedKeySpec(key);
            PublicKey publicKey = KeyFactory.getInstance("EC", new BouncyCastleProvider())
                    .generatePublic(keySpec);
            ECPublicKeyParameters parameters = (ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(publicKey);
            CipherParameters publicKeyParameters = new ParametersWithRandom(parameters);
            SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C2C3);
            engine.init(true , publicKeyParameters);
            return engine.processBlock(data , 0 , data.length);
        }catch (Exception e) {
            log.error("SM2Utils==>encrypt error: data = {} , key = {} , msg = {}" , data , key , e.getMessage());
        }
        return null;
    }


    /**
     * 私钥解密
     * @param cipherText 加密后的密文
     * @param privateKey 私钥
     * @return
     */
    public static String decryptWithPrivate(String cipherText , String privateKey) {
        if(StringUtils.isNotBlank(cipherText) && StringUtils.isNotBlank(privateKey)){
            //这里使用Base64.getDecoder().decode是为了和加密时返回那里对称
            byte [] cipherBytes = Base64.getDecoder().decode(cipherText);
            byte [] privateKeyBytes = Base64.getDecoder().decode(privateKey);
            byte [] paramText = decryptWithPrivate(cipherBytes , privateKeyBytes);
            //因为加密时入参就是param.getBytes(xxx).这里和加密对称
            return new String(paramText , StandardCharsets.UTF_8);
        }else {
            log.error("SM2Utils==>decryptWithPrivate error: param is illegal. cipherText={} , privateKey = {}",
                    cipherText , privateKey);
        }
        return null;
    }

    /**
     * 私钥解密
     * @param data 密文
     * @param key 私钥
     * @return
     */
    public static byte [] decryptWithPrivate(byte [] data , byte [] key) {
        try {
            //私钥的ASN.1编码 . 用来初始化私钥
            KeySpec keySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("EC" , new BouncyCastleProvider());
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            CipherParameters privateParameters = ECUtil.generatePrivateKeyParameter(privateKey);
            //MODE需要和加密时选择一致
            SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C2C3);
            engine.init(false , privateParameters);
            return engine.processBlock(data , 0 , data.length);
        }catch (Exception e){
            log.error("SM2Utils==>decrypt error: data = {} , key = {} , msg = {}" , data , key , e.getMessage());
        }
        return null;
    }

    /**
     * 通过私钥签名
     * @param param 参数
     * @param privateKey 私钥
     * @return
     */
    public static String signWithPrivateKey(String param, String privateKey) {
        byte [] paramBytes = param.getBytes(StandardCharsets.UTF_8);
        byte [] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        byte [] signature = signWithPrivateKey(paramBytes , privateKeyBytes);
        return Base64.getEncoder().encodeToString(signature);
    }

    /**
     * 通过私钥签名
     * @param data data
     * @param key 私钥
     * @return
     */
    public static byte [] signWithPrivateKey(byte [] data , byte [] key) {
        try {
            SM2Signer signer = new SM2Signer();
            KeySpec keySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("EC" , new BouncyCastleProvider());
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            ECPrivateKeyParameters privateKeyParameters = (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(privateKey);
            signer.init(true , privateKeyParameters);
            signer.update(data , 0 , data.length);
            return signer.generateSignature();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param param
     * @param signature
     * @param publicKey
     * @return
     */
    public static boolean verifyWithPublicKey(String param ,
                                              String signature ,
                                              String publicKey) {
        byte [] paramBytes = param.getBytes(StandardCharsets.UTF_8);
        byte [] signatureBytes = Base64.getDecoder().decode(signature);
        byte [] key = Base64.getDecoder().decode(publicKey);
        return verifyWithPublicKey(paramBytes , signatureBytes , key);
    }

    /**
     * 通过公钥验签
     * @param data
     * @param sign
     * @param key
     * @return
     */
    public static boolean verifyWithPublicKey(byte [] data , byte [] sign , byte [] key) {
        try {
            SM2Signer sm2Signer = new SM2Signer();
            KeySpec keySpec = new X509EncodedKeySpec(key);
            PublicKey publicKey = KeyFactory.getInstance("EC" , new BouncyCastleProvider())
                    .generatePublic(keySpec);
            CipherParameters parameters = ECUtil.generatePublicKeyParameter(publicKey);
            sm2Signer.init(false , parameters);
            sm2Signer.update(data , 0 , data.length);
            return sm2Signer.verifySignature(sign);
        }catch (Exception e) {
            log.error("SM2Utils==>verify error: data = {} , key = {} , msg = {}" , data , key , e.getMessage());
        }
        return false;
    }

    public static void main(String[] args)throws Throwable {
        //test---生成密钥对
        KeyPair keyPair = generateSm2KeyPair();
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        System.out.println("SM2公钥为 = " + publicKey);
        System.out.println("SM2私钥为 = " + privateKey);
        String testData = "生活加油";
        System.out.println("当前参数 = " + testData);

        String signature = Base64.getEncoder().encodeToString(
                signWithPrivateKey(testData.getBytes(StandardCharsets.UTF_8) ,
                        Base64.getDecoder().decode(privateKey)
                )
        );
        String signature2 = signWithPrivateKey(testData , privateKey);
        System.out.println("私钥生成签名1= " + signature);
        System.out.println("私钥生成签名2= " + signature2);

        String cipherTxt = Base64.getEncoder().encodeToString(
                encryptWithPublicKey(testData.getBytes(StandardCharsets.UTF_8) ,
                        Base64.getDecoder().decode(publicKey)));
        String cipherTxt2 = encryptWithPublicKey(testData , publicKey);
        System.out.println("公钥加密后 = " + cipherTxt);
        System.out.println("公钥加密后2 = " + cipherTxt2);

        boolean verifyRes = verifyWithPublicKey(testData.getBytes(StandardCharsets.UTF_8) ,
                Base64.getDecoder().decode(signature),
                Base64.getDecoder().decode(publicKey));
        boolean verifyRes2 = verifyWithPublicKey(testData , signature , publicKey);
        boolean verifyRes3 = verifyWithPublicKey(testData , signature2 , publicKey);
        System.out.println("验签结果 = " + verifyRes);
        System.out.println("验签结果2 = " + verifyRes2);
        System.out.println("验签结果3 = " + verifyRes3);


        String decryptRes = new String(
                Objects.requireNonNull(decryptWithPrivate(
                        Base64.getDecoder().decode(cipherTxt) ,
                        Base64.getDecoder().decode(privateKey)) , "加密结果不为空"), StandardCharsets.UTF_8);
        String decryptRes2 = decryptWithPrivate(cipherTxt , privateKey);
        String decryptRes3 = decryptWithPrivate(cipherTxt2 , privateKey);
        System.out.println("解密后结果 = " + decryptRes);
        System.out.println("解密后结果2 = " + decryptRes2);
        System.out.println("解密后结果3 = " + decryptRes3);
    }
}
