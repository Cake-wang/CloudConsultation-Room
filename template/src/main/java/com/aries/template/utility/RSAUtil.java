package com.aries.template.utility;

import android.os.Build;

import androidx.annotation.RequiresApi;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.TreeMap;

public class RSAUtil {

    /**
     * get PrivateKey from String.
     *
     * @param keyValue
     * @return PrivateKey
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static PrivateKey getPrivateKey(String keyValue) {
        return getPrivateKey(keyValue.getBytes());
    }

    /**
     * get PbulicKey from String
     *
     * @param keyValue public key(String)
     * @return PublicKey
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static PublicKey getPublicKey(String keyValue) {
        return getPublicKey(keyValue.getBytes());
    }

    /**
     * read key from file.
     *
     * @param keyFilePath file path
     * @return file content(byte array)
     */
    private static byte[] readKeyDatas(String keyFilePath) {
        try (InputStream inputStream = new FileInputStream(keyFilePath);
             Reader reader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(reader);) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null && !line.startsWith("---"))
                builder.append(line);
            return builder.toString().getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * generate Publickey from file path
     *
     * @param keyFilePath key file path
     * @return PublicKey
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static PublicKey getPublicKeyByFile(String keyFilePath) {
        byte[] bytesPublicBase64 = readKeyDatas(keyFilePath);
        return getPublicKey(bytesPublicBase64);
    }

    public static String getSignedData(Object o) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = o.getClass().getDeclaredMethod("getSign");
        return (String) method.invoke(o);
    }

    /**
     * generate PbulicKey by byte array(Base64 encoded).
     *
     * @param keyValueBase64 keyValueBase64
     * @return PublicKey
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static PublicKey getPublicKey(byte[] keyValueBase64) {
        byte[] bytesPublic = Base64.getDecoder().decode(keyValueBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytesPublic);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * generate PrivateKey from filePath
     *
     * @param keyFilePath file Path
     * @return PrivateKey
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static PrivateKey getPrivateKeyByFile(String keyFilePath) {
        byte[] keyValueBase64 = readKeyDatas(keyFilePath);
        return getPrivateKey(keyValueBase64);
    }

    /**
     * generate PrivateKey by byte array(Base64 encoded).
     *
     * @param keyValueBase64 keyValueBase64
     * @return PrivateKey
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressWarnings("unchecked")
    private static PrivateKey getPrivateKey(byte[] keyValueBase64) {
        byte[] bytesPrivate = Base64.getDecoder().decode(keyValueBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytesPrivate);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * encrypt,and then encode.
     *
     * @param publicKey  publicKey
     * @param originData originData
     * @return encrypt data
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encodeData(PublicKey publicKey, String originData) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytesEncrypt = cipher.doFinal(originData.getBytes());
            byte[] bytesEncryptBase64 = Base64.getEncoder().encode(bytesEncrypt);
            return new String(bytesEncryptBase64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * decode,and then decrypt.
     *
     * @param privateKey privateKey
     * @param encodeData encrypt data
     * @return original data
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decodeData(PrivateKey privateKey, String encodeData) {
        try {
            byte[] bytesEncrypt = Base64.getDecoder().decode(encodeData);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytesDecrypt = cipher.doFinal(bytesEncrypt);
            return new String(bytesDecrypt);
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sign by PrivateKey
     *
     * @param data       data to sign.
     * @param privateKey private key used for sign
     * @return the signed data
     * @throws SignatureException           ex
     * @throws NoSuchAlgorithmException     ex
     * @throws InvalidKeyException          ex
     * @throws UnsupportedEncodingException ex
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String sign(String data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        Signature signature = Signature.getInstance("Sha1WithRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes("UTF-8"));
        byte[] signed = signature.sign();
        return new String(Base64.getEncoder().encode(signed));
    }

    /**bizContent={"list":[{"hospType":"6","serviceAddress":"http://111.0.83.240:8062/","telephone":"0571-89710576","hospState":"0","hospId":"MA2CF6M4233010315D1202","hospName":"孙泰和国医国药馆","hospImgUrl":"http://111.0.83.240:8089/static/57172.jpg","locationCode":"330102","hospLevelCode":"00"}]}&interfaceMethod=10001&logTraceId=80eebff54a2940a6b110b8fa94205985&merchantId=2021042296
     * sign
     *
     * @param data       data to sign.
     * @param privateKey private key used for sign
     * @return the signed data
     * @throws SignatureException           ex
     * @throws NoSuchAlgorithmException     ex
     * @throws InvalidKeyException          ex
     * @throws UnsupportedEncodingException ex
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String sign(String data, String privateKey) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        return sign(data, getPrivateKey(privateKey));
    }

    /**
     * verify by PublicKey
     *
     * @param originData the orifinal data
     * @param signedData the data signed by the private key
     * @param publicKey  public key to verify.
     * @return ture: verify pass.
     * @throws NoSuchAlgorithmException     ex
     * @throws InvalidKeyException          ex
     * @throws UnsupportedEncodingException ex
     * @throws SignatureException           ex
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean verify(String originData, String signedData, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        Signature signature = Signature.getInstance("Sha1WithRSA");
        signature.initVerify(publicKey);
        signature.update(originData.getBytes("UTF-8"));
        return signature.verify(Base64.getDecoder().decode(signedData.getBytes("UTF-8")));
    }

    /**
     * verify
     *
     * @param originData the orifinal data
     * @param signedData the data signed by the private key
     * @param publicKey  public key to verify.
     * @return ture: verify pass
     * @throws NoSuchAlgorithmException     ex
     * @throws InvalidKeyException          ex
     * @throws UnsupportedEncodingException ex
     * @throws SignatureException           ex
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean verify(String originData, String signedData, String publicKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {

//        System.out.println("---------------验签数据-------------------");
//        log.debug("【验签数据】\noriginData :{}\nsignedData :{}\npublicKey :{}\n", originData, signedData, publicKey);
        boolean b= verify(originData, signedData, getPublicKey(publicKey));
//        log.debug("验签结果为:{}",b);
//        System.out.println("originData:" + originData);
//        System.out.println("signedData:" + signedData);
//        System.out.println("publicKey:" + publicKey);

        return b;
    }

    private static String getSignStringBymap(TreeMap<String, Object> map) {
        StringBuilder builder = new StringBuilder();
        for (String key : map.keySet()) {
            if (map.get(key) != null) {
                if (builder.length() > 0)
                    builder.append("&");
                builder.append(key);
                builder.append("=");
                builder.append(map.get(key));
            }
        }
        return builder.toString();
    }
}