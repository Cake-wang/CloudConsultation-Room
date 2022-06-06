package com.hfi.health.cloud.hospital.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.LinkedHashMap;

/**
 * AES加密处理
 * @author jthealth-NZH
 * @Date 2021/04/22 8:47
 * @Describe
 * @Version 1.0
 */
@Slf4j
public class AESUtils {

	private static final String AESTYPE = "AES/ECB/PKCS5Padding";

	private final static String CHARSET = "UTF-8";

	public static final String PASSWORD_KEY = "BIvqigf/MN+3WLHzouhWrA==";

	//入参为对象,可使用此方法进行加密
	public static String aesEncrypt(Object o, String use_key){
		JSONObject orderedJson = convertOrderedJson(o);
		return aesEncrypt(orderedJson.toJSONString(),use_key);
	}

	//入参为对象,可使用此方法进行解密
	public static String aesDecrypt(Object o, String use_key) throws Exception {
		return aesDecrypt(o.toString(),use_key);
	}

	//密码加密
	public static String aesEncrypt4Password(String plainText) {
		return aesEncrypt(plainText,PASSWORD_KEY);
	}

	//加密
	public static String aesEncrypt(String plainText, String use_key) {
		byte[] encrypt = null;
		try {
			Key key = generateKey(use_key);
			Cipher cipher = Cipher.getInstance(AESTYPE);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encrypt = cipher.doFinal(plainText.getBytes(CHARSET));
		} catch (Exception e) {
			log.error("encrypt error!", e);
			return null;
		}
		return new String(Base64.encodeBase64(encrypt));
	}

	// 解密
	public static String aesDecrypt(String encryptData, String useKey) throws Exception {
		log.debug("AES解密入参encryptData:{}\n,useKey:{},长度:{}",encryptData,useKey,useKey.length());
		byte[] decrypt = null;
		try {
			Key key = generateKey(useKey);
			Cipher cipher = Cipher.getInstance(AESTYPE);
			cipher.init(Cipher.DECRYPT_MODE, key);
			decrypt = cipher.doFinal(Base64.decodeBase64(encryptData.getBytes(CHARSET)));
		}catch (Exception e){
			log.error("decrypt error!", e);
			throw e;
		}
		return new String(decrypt,"utf-8").trim();
	}

	public static JSONObject convertOrderedJson(Object o){
		LinkedHashMap<String, String> jsonMap
				= JSON.parseObject(JSONObject.toJSONString(o),LinkedHashMap.class, Feature.OrderedField);
		JSONObject jsonObject = new JSONObject(true);
		jsonObject.putAll(jsonMap);
		return jsonObject;
	}

	private static Key generateKey(String key) throws Exception {
		try {
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
			return keySpec;
		} catch (Exception e) {
			log.error("generateKey", e);
			throw e;
		}
	}

	public static void main(String[] args) {
		f2();
	}

	 private static void f1(){
		 //		String encStr = aesEncrypt("今晚打老虎——ABCDE_ABCDE#/'12345","BIvqigf/MN+3WLHzouhWrA==");
		 String encStr = aesEncrypt("今晚打老虎——ABCDE_ABCDE#/'12345","Vw+NoBQ/6v1XB+C6");
		 String enc2 = "StrhjmwaN4/L2OAyled9DSID/zZiS9ie+CAyHGEKZsT4sGSx0KrfWsKTKXKdC9s36XznM925dVoHT53JyVxx8coLxAE6tvID3iBQYFaoM0Hl57tDXwyk5VIKYp9kD9XtZ6Jdjv/Lo6IkGNsJANNG7EpVBloUEPWN16k/282Z3WFM1PgSaF/jiDrXUFz+kBo+6V+EO2zRJgcUP09n9s1Ba6HuLu+LhGec63q0Efpv/qMlstfjmPcnR6UwXWLGGrPn3/D9v/ZVgq579JA2PUYYNs4u28OzFmmJh+MgsIoc6Wq/VvukHQsLYBPagxWZDNzGmEFfrBzG0FKkOYxxqz6OdA==";
		 System.out.println("2YxREMmoQejEyjeQ".length());
		 try {
//			System.out.println(aesDecrypt(encStr,PASSWORD_KEY));
			 System.out.println(aesDecrypt(encStr,"Vw+NoBQ/6v1XB+C6"));
//			System.out.println(aesDecrypt("7wPQVXHVIKElIsWyy6tQubp4nkk+E2GCO6JUO65iUAD8Fh9bYL9V3I9eeLWy8NQlcFzJ5sM1OdcrE/SABWwZPqBgsp0ZQ0R06tCYf78ShytZubdba8MobQuVfNbMhFbaJnp8wp9PaHGmlnKoEKPahyxSw52fpT2+jXQlmYRSuKS9eq1uGTv3HUqSqWGwLi9x",PASSWORD_KEY));
//			System.out.println(aesDecrypt(enc2,PASSWORD_KEY));


		 }catch (Exception e) {
			 e.printStackTrace();
		 }
	 }

	private static void f2(){
		String enc2 = "c9qzta1Or579BrwxGJTF4Uy031ZK0iRCU3ToCx7Yy/NQaeQ6BDgTvTpU9qHeTdTdUH8HYltPlHFFw/24BhInUTKienvMSoUVFhwkSWPHgMPJrvikvNmUSH/eN2HPlU3Auhrnb8yrlH4PSxIrGlh26oE1LwhEQnlKTokBTARG2ms=";

		String key = "jfdjk670qEH5lm4c";
		try {
			System.out.println(aesDecrypt(enc2,key));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}