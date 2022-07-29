//package com.aries.template.utility;
//
//
//import java.io.ByteArrayOutputStream;
//import java.nio.charset.StandardCharsets;
//import java.security.KeyFactory;
//import java.security.NoSuchAlgorithmException;
//import java.security.PrivateKey;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//
//import javax.crypto.Cipher;
//
///**
// * RSA 加密工具
// * 支持，公钥加密，私钥解密
// * 支持，公钥解密，私钥加密
// * 如果两端的加密方式不一样，会导致 BadPaddingException 这个问题。
// * 目前使用的最多的是，私钥加密，公钥界面。
// * 目前使用的RSA末尾处理补全方式是 RSA/ECB/PKCS1Padding
// *
// * @author louisluo
// */
//public class JTJKRsaUtil {
//
//    private static final int MAX_DECRYPT_BLOCK = 128;
//    private static final int MAX_ENCRYPT_BLOCK = 117;
//
//    public static final String RSA = "RSA";
//    /**
//     * 主流的填充方式有
//     * RSA/NONE/OAEPPADDING
//     * RSA/ECB/PKCS1Padding
//     */
////    public static final String RSA_MODE = "RSA/NONE/OAEPPADDING";
//    public static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
//
//    /**
//     * 从字符串中加载公钥
//     *
//     * @param publicKeyStr
//     *            公钥数据字符串
//     * @throws Exception
//     *             加载公钥时产生的异常
//     */
//    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
//        try {
//            byte[] buffer = bufferBase64Utils.decode(publicKeyStr);
//            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
//            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
//        } catch (NoSuchAlgorithmException e) {
//            throw new Exception("无此算法");
//        } catch (InvalidKeySpecException e) {
//            throw new Exception("公钥非法");
//        } catch (NullPointerException e) {
//            throw new Exception("公钥数据为空");
//        }
//    }
//
//    /**
//     * 从字符串中加载私钥<br>
//     * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
//     */
//    public static PrivateKey loadPrivateKeyByStr(String privateKeyStr) throws Exception {
//        try
//        {
//            byte[] buffer = Base64Utils.decode(privateKeyStr);
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
//            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
//            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
//        } catch (NoSuchAlgorithmException e)
//        {
//            throw new Exception("无此算法");
//        } catch (InvalidKeySpecException e)
//        {
//            throw new Exception("私钥非法");
//        } catch (NullPointerException e)
//        {
//            throw new Exception("私钥数据为空");
//        }
//    }
//
//    /**
//     * 使用 私钥 将数据进行分段加密
//     *
//     * @param data   要加密的数据
//     * @param prvKey 私钥 Base64 字符串，PKCS8 格式
//     * @return 加密后的 Base64 编码数据，加密失败返回 null
//     */
//    public static String encryptByPrivateKey(String data, String prvKey) {
//        // 防御代码
//        if (data==null || "".equals(data))
//            return "";
//        if (prvKey==null || "".equals(prvKey))
//            return "";
//
//        try {
//            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
//            // 创建 Cipher 对象
//            Cipher cipher = Cipher.getInstance(RSA_MODE);
//            // 初始化 Cipher 对象，加密模式
//            cipher.init(Cipher.ENCRYPT_MODE, loadPrivateKeyByStr(prvKey));
//            int inputLen = bytes.length;
//            // 保存加密的数据
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            int offSet = 0, i = 0;
//            byte[] cache;
//            // 使用 RSA 对数据分段加密
//            while (inputLen - offSet > 0) {
//                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
//                    cache = cipher.doFinal(bytes, offSet, MAX_ENCRYPT_BLOCK);
//                } else {
//                    cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
//                }
//                // 将加密以后的数据保存到内存
//                out.write(cache, 0, cache.length);
//                i++;
//                offSet = i * MAX_ENCRYPT_BLOCK;
//            }
//            byte[] encryptedData = out.toByteArray();
//            out.close();
//            // 将加密后的数据转换成 Base64 字符串
//            String restr = new String(Base64Utils.encode(encryptedData));
//            return restr;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 使用公钥将数据进行分段加密
//     *
//     * @param data   要加密的数据
//     * @param pubKey 公钥 Base64 字符串，X509 格式
//     * @return 加密后的 Base64 编码数据，加密失败返回 null
//     */
//    public static String encryptByPublicKey(String data, String pubKey) {
//        // 防御代码
//        if (data==null || "".equals(data))
//            return "";
//        if (pubKey==null || "".equals(pubKey))
//            return "";
//        //执行
//        try {
//            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
//            // 创建 Cipher 对象
//            Cipher cipher = Cipher.getInstance(RSA_MODE);
//            // 初始化 Cipher 对象，加密模式
//            cipher.init(Cipher.ENCRYPT_MODE, loadPublicKeyByStr(pubKey));
//            int inputLen = bytes.length;
//            // 保存加密的数据
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            int offSet = 0, i = 0;
//            byte[] cache;
//            // 使用 RSA 对数据分段加密
//            while (inputLen - offSet > 0) {
//                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
//                    cache = cipher.doFinal(bytes, offSet, MAX_ENCRYPT_BLOCK);
//                } else {
//                    cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
//                }
//                // 将加密以后的数据保存到内存
//                out.write(cache, 0, cache.length);
//                i++;
//                offSet = i * MAX_ENCRYPT_BLOCK;
//            }
//            byte[] encryptedData = out.toByteArray();
//            out.close();
//            // 将加密后的数据转换成 Base64 字符串
//            String restr = new String(Base64Utils.encode(encryptedData));
//            return restr;
//        } catch (Exception e) {
//        }
//        return null;
//    }
//
//    /**
//     * 使用私钥将加密后的 Base64 字符串进行分段解密
//     *
//     * @param encryptBase64Data 加密后的 Base64 字符串
//     * @param prvKey            私钥 Base64 字符串，PKCS8 格式
//     * @return 解密后的明文，解密失败返回 null
//     */
//    public static String decryptByPrivateKey(String encryptBase64Data, String prvKey) {
//        try {
//            // 将要解密的数据，进行 Base64 解码
//            byte[] encryptedData = Base64Utils.decode(encryptBase64Data);
//            // 创建 Cipher 对象，用来解密
//            Cipher cipher = Cipher.getInstance(RSA_MODE);
//            // 初始化 Cipher 对象，解密模式
//            cipher.init(Cipher.DECRYPT_MODE, loadPrivateKeyByStr(prvKey));
//            int inputLen = encryptedData.length;
//            // 保存解密的数据
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            int offSet = 0, i = 0;
//            byte[] cache;
//            // 对数据分段解密
//            while (inputLen - offSet > 0) {
//                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
//                    cache = cipher.doFinal(encryptedData,offSet ,MAX_DECRYPT_BLOCK);
//                } else {
//                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
//                }
//                // 将解密后的数据保存到内存
//                out.write(cache, 0, cache.length);
//                i++;
//                offSet = i * MAX_DECRYPT_BLOCK;
//            }
//            byte[] decryptedData = out.toByteArray();
//            out.close();
//            String restr = new String(decryptedData);
//            return restr;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 使用 公钥 将加密后的 Base64 字符串进行分段解密
//     *
//     * @param encryptBase64Data 加密后的 Base64 字符串
//     * @param pubKey            公钥 Base64 字符串，X509 格式
//     * @return 解密后的明文，解密失败返回 null
//     */
//    public static String decryptByPublicKey(String encryptBase64Data, String pubKey) {
//        Cipher cipher = null;
//        try {
//            // 将要解密的数据，进行 Base64 解码
//            byte[] encryptedData = Base64Utils.decode(encryptBase64Data);
//            // 创建 Cipher 对象，用来解密
//            cipher = Cipher.getInstance(RSA_MODE);
//            // 初始化 Cipher 对象，解密模式
//            cipher.init(Cipher.DECRYPT_MODE, loadPublicKeyByStr(pubKey));
//            int inputLen = encryptedData.length;
//            // 保存解密的数据
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            int offSet = 0, i = 0;
//            byte[] cache;
//            // 对数据分段解密
//            while (inputLen - offSet > 0) {
//                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
//                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
//                } else {
//                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
//                }
//                // 将解密后的数据保存到内存
//                out.write(cache, 0, cache.length);
//                i++;
//                offSet = i * MAX_DECRYPT_BLOCK;
//            }
//            byte[] decryptedData = out.toByteArray();
//            out.close();
//            String restr = new String(decryptedData);
////            return new String(decryptedData, CHARSET_UTF8);
//            return restr;
//        } catch (Exception e) {
//        }
//        return null;
//    }
//
//}
