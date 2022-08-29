//package com.aries.template.utility;
//
//import android.os.Build;
//import android.util.Log;
//
//import androidx.annotation.RequiresApi;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.parser.Feature;
//import com.decard.dc_licensesdk.bean.BaseResponse;
//import com.google.gson.Gson;
//
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.security.SignatureException;
//import java.util.*;
//
///**
// * @author jthealth-NZH
// * @Date 2019/6/19 16:31
// * @Describe
// * @Version 1.0
// */
//public class SignStr {
//
//    private static Set<String> keySet = new TreeSet();
//    private static String SIGN = "sign";
//    static {
//        keySet.add("sign");
//    }
//    //2020021901    杭州金投健康网络科技有限公司
//    public static String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCbuJD3hjC6oiWxHLfHOdtvR8WR8my8tja4h1rt8McXjF20WyZiVcu0ognhxVQ6GpHajdyC6hS1SdN9uR2QMQ5Aki3CnSxJFWFZwryflJABMBRFOK+brGs02XNXMeeG3tfYJBQMYPM79gf3X26OtaoJD2/umeMnGHKbZujyg8bQKapZaZEJ2hQyWG1Rpj2kop90ZJ03nlMBPfsQb0JSvJ4wHm1hYI1xGKRk9iJZWp9p8zBEnYU6klxcU3gbAjZTrYtLO+jCSgrXlqhZij+y7oVmc3Mni4Oy0TtjgGyxZtI2zfevDDp+6uvfrOCWhjz1+Csjh/TEx9tIFOU+SGnUZYKlAgMBAAECggEARJ3JUyiQJEz6qtZqrKDz7k8D4dGe1HC+IAWUrbAhKmExkR1xBnejsU6NaCyZdpjqmBFWj2EyCsgjJw0A196l1xISk+TJ9NI0+WZz0yWVZtCWvgHCb6hD/R+sUUmuKaHcHmOXafT2eP9qdzhX8Q5fhGqG+e5ZR4rgRVXNY79pRaPFJDmc0rv2fhfzmYHzsIXA9dJZlSDkhv6kQO93ksFUQjhuBDWWIHvvh0EphM0lf/SIG5GlPHyghcda016IMX7tHYyci1dTx8Bsaas6St09EgIpO1ALe/zstDLA/jsZikcZdr4zZssAbv7Hwxzg6BJ95w2IjnLWLjX/eu/+0iWEwQKBgQDhnUhNHZU0vPatUzb/sm1UxonfFUTaIX5JN9kv6+xUChGnUloYMwmu9K+4uQ1AcwaIHVA1BlGPaF4KpqQGgk85hZQmjNKJBR1qRaMAJNdI4Madb6Rpmb8LbYQeiUlbCl5p+gbOkVCCXTbwhTEZhiYgY1hRg/YUIcR5c4uPfoakuQKBgQCwsYDNPKGNnhFUnAnRMATgVvwZAPpWWuzAsImDw4O5OsUXE/mwh7URmcGuRrCsNA5udcIlMMeAmuKujUQlST9E6e7NCsrczT3UH7bq0t5grgjuEOUS3SiXdQ8/czMmqxLh47BaWWD5fjZnwyN+s4AT2aIfSm8x5CmvLKnv9ugvTQKBgQChD0frDUBTusYaa7pqhTS5fVY1NBjH43skx8K7DB/Uyq9ZlFeOyPiTtkevhiNoq/SwvMRiZhVZW/cugVG898+Yz7w4XPV7LXSXRgkb9DDf7N68y1NCs6gHf3H+3qDIZjmKTBwJ4QDpX6P1xg+MGhEjs+f7oUnNi2GYFh0+HeP+uQKBgEXTb84rlsBrZffkzjBf5J1vYqLS8xsrRYKEgWOT4TtTEU4kc7kieBjjhVnEz87Np6JZYQbndv/Itao7Y2HEx38JX3iiOjb12Og6PEQiXwUf4aGLBavlwEPSW9s8QEuYsZaZLzkVNzCBgyiQQjew4f12NQ5ekQDLZPm4EdQl4+3FAoGADJxTbGNg3j1WH1yRdV7MIL0ujbF2BTwQ+I3REIuClv5SReW8BmrBtSJ6kMtfkHZtHjB/yMJaaZuJ19Y3lcduoXNm6t8dxC6WhFNh0TNw1wdE7bd0h4yTT9ysJ7x7TM/Y0oO7PepB4W/5xI81sIy9VINH/JwpYwiT42JT13Z7yCk=";
//
//    public static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm7iQ94YwuqIlsRy3xznbb0fFkfJsvLY2uIda7fDHF4xdtFsmYlXLtKIJ4cVUOhqR2o3cguoUtUnTfbkdkDEOQJItwp0sSRVhWcK8n5SQATAURTivm6xrNNlzVzHnht7X2CQUDGDzO/YH919ujrWqCQ9v7pnjJxhym2bo8oPG0CmqWWmRCdoUMlhtUaY9pKKfdGSdN55TAT37EG9CUryeMB5tYWCNcRikZPYiWVqfafMwRJ2FOpJcXFN4GwI2U62LSzvowkoK15aoWYo/su6FZnNzJ4uDstE7Y4BssWbSNs33rww6furr36zgloY89fgrI4f0xMfbSBTlPkhp1GWCpQIDAQAB";
//
////    public static String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAPbeiw0UZTeAGuZsw2GFhj278hGfnOgNDZq2vjhCOx3nywv2vuqQEyLBQpH3Nivw10MnU9r+/3/0pM6e1qPRBGXgeEsZnXoO3ZxMFoWnfn2eDLzeiTX+ryYARefdIn89CfNHdABVgxqFvIbJ4Ad6MCB6NHSQ0MSho5lAKeB54AlnAgMBAAECgYB+SMdO8JD6fDDGkPq9h2VtbRhehYzpc8vyyeqyssa0/J70xstRCFSRodUhABoZDkG/231c+LbQyOAJxl08Pa8GQUBQIx1W8k5pks8xICf60kHc4mViU5Cyxmukj2FOB/X/IhsmxxvekZ6Nh3iHsa938B7TqKcCtCVFDSRbU9MkUQJBAP3GcKwP+mfqErQ3HAPy7xUkVEHKDB5gV1dV2QVFT2vmnO0xLX1/g1q044ijmPvNIrtHOPCASUiPZ+Ww7+aC1fkCQQD5CJqaKrHcttysGElpcPnIKur9LFqr1RnCjbppKC9yyUi+iTi84rwff5eKdzpv0stKJlRk4Kf6z7+9kBivyDJfAkEAhX7dCHZDSGSmwCx5TU5/HEUHGbCanWQarfOX/SDPvbgLdu62ulzYkV+0jc1ZjaQEkDXQKxug8U2W3woruLu8qQJBAN+RFNJAcAqYc4VgDE/Hp8ZGmU2OCxxmJEkf2XDcLbSb9V1wbWpNFtwkPJSHMwP+tpgrzun9MXCeHyF8Al2UmXsCQQCQ3Dg9N2N/IKQpg7yZZhbI+712rrc5cTIkKa2tE2CaQTtRd1PVn4jrGCmmfeeVeo/cHuk7nds8LMwaL/nqgbyL";
//
////    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD23osNFGU3gBrmbMNhhYY9u/IRn5zoDQ2atr44Qjsd58sL9r7qkBMiwUKR9zYr8NdDJ1Pa/v9/9KTOntaj0QRl4HhLGZ16Dt2cTBaFp359ngy83ok1/q8mAEXn3SJ/PQnzR3QAVYMahbyGyeAHejAgejR0kNDEoaOZQCngeeAJZwIDAQAB";
//
////    public static void main(String[] args) {
////        f1();
//////        v2();
////    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static void f1(String request1) {
//        keySet.add("sign");
////        String input = "{\n" +
////                "    \"bizContent\": {\n" +
////                "        \"list\": [\n" +
////                "            {\n" +
////                "                \"hospType\": \"6\",\n" +
////                "                \"serviceAddress\": \"http://111.0.83.240:8062/\",\n" +
////                "                \"telephone\": \"0571-89710576\",\n" +
////                "                \"hospState\": \"0\",\n" +
////                "                \"hospId\": \"MA2CF6M4233010315D1202\",\n" +
////                "                \"hospName\": \"孙泰和国医国药馆\",\n" +
////                "                \"hospImgUrl\": \"http://111.0.83.240:8089/static/57172.jpg\",\n" +
////                "                \"locationCode\": \"330102\",\n" +
////                "                \"hospLevelCode\": \"00\"\n" +
////                "            },\n" +
////                "            {\n" +
////                "                \"hospType\": \"6\",\n" +
////                "                \"serviceAddress\": \"http://111.0.83.240:8062/\",\n" +
////                "                \"telephone\": \"0571-89710576\",\n" +
////                "                \"hospState\": \"0\",\n" +
////                "                \"hospId\": \"MA2CF6M4233010315D1202\",\n" +
////                "                \"hospName\": \"孙泰和国医国药馆\",\n" +
////                "                \"hospImgUrl\": \"http://111.0.83.240:8089/static/57172.jpg\",\n" +
////                "                \"locationCode\": \"330102\",\n" +
////                "                \"hospLevelCode\": \"00\"\n" +
////                "            },\n" +
////                "            {\n" +
////                "                \"hospType\": \"6\",\n" +
////                "                \"serviceAddress\": \"http://111.0.83.240:8062/\",\n" +
////                "                \"telephone\": \"0571-89710576\",\n" +
////                "                \"hospState\": \"0\",\n" +
////                "                \"hospId\": \"MA2CF6M4233010315D1202\",\n" +
////                "                \"hospName\": \"孙泰和国医国药馆\",\n" +
////                "                \"hospImgUrl\": \"http://111.0.83.240:8089/static/57172.jpg\",\n" +
////                "                \"locationCode\": \"330102\",\n" +
////                "                \"hospLevelCode\": \"00\"\n" +
////                "            }\n" +
////                "        ]\n" +
////                "    },\n" +
////                "    \"logTraceId\": \"111111\",\n" +
////                "    \"methodCode\": \"123456\",\n" +
////                "    \"sign\": \"\"\n" +
////                "}";
////        BaseRequest request1 = JSONObject.parseObject(input, BaseRequest.class);
////        System.out.println(JsonUtil.serialize(request1));
////        request1.setBizContent(request1.getBizContent().toString());
////        System.out.println(JsonUtil.serialize(request1));
////
////        request.setLogTraceId("aaaaasssssdddddfffffggggghhhh102");
////
////
////        SMKInfoDTO smkInfoDTO = new SMKInfoDTO();
////        smkInfoDTO.setName("王郭亮");
////        smkInfoDTO.setMedCardNo("1234");
////        request.setBizContent(JSONObject.toJSONString(smkInfoDTO));
////        System.out.println("请求入参" + request.toString());
//
////        String signStr = InitSignStr(request);
////        System.out.println(signStr);
////        String sign2 = getSignStr("bizContent={\"paperNo\":\"bJLf0y/nYWLhNC/CtMuqctHBir4zC5gjcHYRZYmTCUI=\",\"logTraceID\":\"1613076535737000\",\"name\":\"钱文学\"}&interfaceMethod=104&logTraceId=1613076535737000&merchantId=2021020801&subMerchantId=20210208562918&termId=20210208977834",privateKey);
////        System.out.println("获取sign2签名字符串\n"+sign2);
////        request.setSign(sign);
//        String signStr = InitSignStr(request1);
//        System.out.println(signStr);
//        String sign = createSign(request1, privateKey);
//        System.out.println("获取的签名字符串结果：");
//        System.out.println(sign);
//
////编码
//
////        String sign = "e1+WHvBKgg7A6n/gecWvxYbMsJxY2roAREuYf9t8U6GrmjXpYJFoCAh9VbuD+WgPM47qwsgSf91wwEehcTcPrxbYG41hCUAMdtoJ5aQ+x15LZYKLawMDGWKAQNWoVAuCZo9WIEuTSnuy5mZoWtZpLL6zj2YlkPc9g5YxNhc4U1l11xD0YD+kbwIDNSQBT+liqGgQ0l/j18XBFrcEGqDcraiue0OmupACwxWbJLglM9ZRFgvt1GSOpY9M82TngMVSPUtJaStMgnvI5Wp3YlmlrNe/yujkqVwNy3KYs2VTUSJOm/slc5a+2b61xuZTPIFT8cECyJiXZNUcVHFiBhp0Vw==";
////        String originData = "value={\"password\":\"123\",\"username\":\"test2\"}";
//        try {
//            String str = signStr;
////                    "bizContent={\"paperNo\":\"ivASryn2HwRRAE1I2uucmYXG7ZPR2ZudakQeo4x8u0A=\",\"logTraceID\":\"65f7af3c7c024548bdc8c24c760d89e0\",\"name\":\"吕渊渊\"}&logTraceId=logTraceId\n";
//            String inputSign = sign;
////                    "gmpzWpq0iITbNpqIvepDWwNZ916m3Gq/YhK6neFbCsT9kFM/Vl8MlHYiLLZd0MrbGu845ecBWXUq6cM8GfOY59SInnMq78SIT+JM0b+qNHGBBtqqO1MOtN+gg04yOBS7N5yQdRTm74iS3Goe71FVfF2XpMOoV4vcioIXGz6PSAE+LYtvtQ8mve2fqcUCB8tOOcaBVMJBrGN2wxFOSXypOFr/y5HQWc+Yt8meeday0KdyGsdhKcepEHdjn+eCBC5kDOpPOmmMi9XpmKHKcJnzbRY9ZYhy4hJCzcUnAMBB24QjXRYZWVkxKMiqqavbMJ4qIr7mJb6mrDrofQPAUibfFQ==";
//
//            //            boolean b = RSAUtil.verify(InitSignStr(request), sign, publicKey);
//            boolean b = RSAUtil.verify(str, inputSign, publicKey);
//            System.out.println(b);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (SignatureException e) {
//            e.printStackTrace();
//        }
//    }
//
////    public static void f2(){
////        String str = "bizContent={\"hospType\":\"6\",\"hospId\":\"47011661433010211A1001\",\"hospName\":\"方法急急急\",\"hospImgUrl\":\"\",\"locationCode\":\"3301020000000000000000\",\"hospLevelCode\":\"23\",\"hospState\":\"0\"}&interfaceMethod=10001&logTraceId=e3567f54-c7cb-43c5-bcec-e04a900c13ab&merchantId=2021042296";
////
////        String inputSign = "TdXuuWkLsYZLlg9/HkwoC8qPa+calz0ZMGi7PFZwnCqiemGasnVeH3JCCrpH9+VfavXFbWVZgcNlNt1BTvaifNIMwFE+D/cg3AIm8ILTwwouHEOzEMSFiHR1ESXHQYKUgNUpoVM7Gdytj9uc+FvztrIFSuVE75HETHOCOAY+Q8QeNr9pa7Qni4+/2p3/71Jele3biyxrlw6jICBtNJLjFJpIqjPxAvxodEkFf1qUlZTm7QWGI0JdKJWaASF3+SUemi/O1deXkl/UH8MKwde88rXNRU/Nrg3FtJ5YLi+JXt5Q7ZaDoSL0gRgGxOWJUyipkjUxXgG91sP+Xj+I5mQ2+g==";
////
////        String sign = getSignStr(str,privateKey);
////        System.out.println("获取的签名字符串结果：");
////        System.out.println(sign);
////        boolean b = false;
////        try {
////            b = RSAUtil.verify(str, sign, publicKey);
////        } catch (NoSuchAlgorithmException e) {
////            e.printStackTrace();
////        } catch (InvalidKeyException e) {
////            e.printStackTrace();
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        } catch (SignatureException e) {
////            e.printStackTrace();
////        }
////        System.out.println(b);
////
////    }
//
////    public static void f3(){
////        String str = "bizContent={\"list\":[{\"hospType\":\"6\",\"hospId\":\"MA2CF6M4233010315D1202\",\"hospName\":\"孙泰和国医国药馆\",\"hospImgUrl\":\"http://111.0.83.240:8089/static/57172.jpg\",\"serviceAddress\":\"http://111.0.83.240:8062/\",\"telephone\":\"0571-89710576\",\"locationCode\":\"330102\",\"hospLevelCode\":\"00\",\"hospState\":\"0\"}]}&interfaceMethod=10001&logTraceId=80eebff54a2940a6b110b8fa94205985&merchantId=2021042296";
////
////
////        String sign = getSignStr(str,privateKey);
////        System.out.println("获取的签名结果：");
////        System.out.println(sign);
////        boolean b = false;
////        try {
////            b = RSAUtil.verify(str, sign, publicKey);
////        } catch (NoSuchAlgorithmException e) {
////            e.printStackTrace();
////        } catch (InvalidKeyException e) {
////            e.printStackTrace();
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        } catch (SignatureException e) {
////            e.printStackTrace();
////        }
////        System.out.println(b);
////    }
//
////    public static void f4(){
////        String result = "";
////        JSONObject ret = new JSONObject();
////        ret.put("aaa","aaa");
////        ret.put("bbb","");
////        ret.put("ccc",111);
////        ret.put("ddd",null);
////        BaseResponse r = BaseResponse.success(ret);
////        System.out.println(r.toString());
////        String signedData = SignStr.createSign(r,privateKey);
////        System.out.println(signedData);
////    }
//
////    public static void v2(){
////        String str = "bizContent={\"paperNo\":\"dJKUn3U0ep3d9R47YZ9GEJzKJ/yPfDpljKnsZ3mk1j8ElRNjQvhOUPszJWeQIQ+8\",\"name\":\"泮俊羲\"}&interfaceMethod=1007&logTraceId=NucleicAcid-1629767167036&merchantId=2021081799";
////
////        String sign = getSignStr(str,privateKey);
////        System.out.println(sign);
////
////        try {
////            System.out.println("待签名字符串："+str);
////
////            System.out.println("公钥为："+publicKey);
////            //            boolean b = RSAUtil.verify(InitSignStr(request), sign, publicKey);
////            boolean b = RSAUtil.verify(str, sign, publicKey);
////            System.out.println("验证结果:"+b);
////        } catch (NoSuchAlgorithmException e) {
////            e.printStackTrace();
////        } catch (InvalidKeyException e) {
////            e.printStackTrace();
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        } catch (SignatureException e) {
////            e.printStackTrace();
////        }
////
////
////    }
//
////    private static void v1(){
////        try {
//////            String str = "bizContent={\"list\":[{\"hospType\":\"6\",\"serviceAddress\":\"http://111.0.83.240:8062/\",\"telephone\":\"0571-89710576\",\"hospState\":\"0\",\"hospId\":\"MA2CF6M4233010315D1202\",\"hospName\":\"杭州孙泰和国医馆\",\"hospImgUrl\":\"http://111.0.83.240:8089/static/57170.jpg\",\"locationCode\":\"330103\",\"hospLevelCode\":\"00\",\"introduction\":\"\"}]}interfaceMethod=10001logTraceId=54331f5cfcdf46e5a8ce21d662150a63merchantId=2021042296";
////            String str = "bizContent={\"appId\":\"2021050866\",\"channel\":\"ALIPAY\",\"merCode\":\"330100810026\",\"reqSeq\":\"20201014000000036520\",\"settleOrderNo\":\"202105120947442991238797\",\"version\":\"2.0.0\"}&interfaceMethod=10052&logTraceId=40b6053a0265444697aa15f66650bf8e&merchantId=2021050866";
//////            String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhIUhVERUIB2rPLS4aRepFSiImwFjWBQoxzgZL22sQxphy4VZ48942DOf++MSmzHeW38GNCkZ8QBAJbnwDwDGLygXSY+fgEX43a7KJbwRtM5J9orJdPvHSmfSwe1nbZ3ZCOH7rl2zZJ33yWMxFwr554nv5COFEJDjVkpXgqwE2pJy+f3nNp8boBdDcaDE4sWk9+pOH8MZlAdfwo002jxUd+tgxki58aLcxFWaUSbEcteCoZfhEE46upZmhvihEYs88Sz5F27h+snlqSISfGguY+KeogiR7aVK5VkStvmtPWcq7QBGLFeaeiTYksk+4eWzhD0E+kRf35Isbz+Pjv+z6QIDAQA";
////            String inputSign = "VrSFqvySi8ff8e0zJSKaWX/iGkSjklbSyNc82BowLQ1qcrOcIPh/8GSNWnb+Kyl9fKdkuas13Tf/aHr1BHlusXrWV/ctEaOoL8f8gtTofBVRyjgMbHW0wUQ5xfbJJFJdGMMYyo+l5PkUOLNiqBHkbdFLCZcLWm8ZfBhRAiJ9m8w4tedV2KfmQKv40bC1qrNOERybnMxaj7Zj9JGuVGZbl5p/+KepuUAQEILYrtqItoaeucJJg8Q2eahO5UkuyZNA7naCdZoVqL2ongofFpgVCc++UpdDzloT5F7xOBK9M4JNV9tmuE0GMUWafv7nMBjlSM5Ogu7moGdfZO/SfgktEQ==";
////
////
////
////            System.out.println("待签名字符串："+str);
////            System.out.println("签名串："+inputSign);
////            System.out.println("公钥为："+publicKey);
////            //            boolean b = RSAUtil.verify(InitSignStr(request), sign, publicKey);
////            boolean b = RSAUtil.verify(str, inputSign, publicKey);
////            System.out.println("验证结果:"+b);
////        } catch (NoSuchAlgorithmException e) {
////            e.printStackTrace();
////        } catch (InvalidKeyException e) {
////            e.printStackTrace();
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        } catch (SignatureException e) {
////            e.printStackTrace();
////        }
////    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static String createSign(String input, String privateKey) {
//        return getSignStr(InitSignStr(input), privateKey);
//    }
//
////    public static String createSign(Object input, HttpServletRequest request, String privateKey) {
////        return getSignStr(InitSignStr(input, request), privateKey);
////    }
//
////    /**
////     * 获得待加签字符串
////     *
////     * @param o
////     * @return
////     */
////
////    public static String InitSignStr(Object o) {
////        return InitSignStr(o, null);
////    }
//
//    /**
//     * 获得待加签字符串，顺序不乱，请使用此方法获取待签名字符串
//     */
//    public static String InitSignStr(String str) {
//
//        LinkedHashMap<String, String> map = JSON.parseObject(JSONObject.toJSONString(o),LinkedHashMap.class, Feature.OrderedField);
//
//        Log.d("TAG","InitSortSignStr map:{}"+map);
//        StringBuilder stringBuilder = new StringBuilder();
//        List<Map.Entry<String, String>> list = sortMap(map);
//        for (Map.Entry<String, String> entry : list) {
//            if(!keySet.isEmpty()){
//                if (!keySet.contains(entry.getKey())) {
//                    stringBuilder.append(entry.getKey() + "=" + String.valueOf(entry.getValue()) + "&");
//                }
//            }
//
//        }
//        if (stringBuilder.length() > 0) {
//            stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
//        }
//
//        // System.out.println("InitSignStr Encode前：" + stringBuilder.toString());
//        String sign = stringBuilder.toString();
//        Log.d("TAG","【InitSignStr签名字符串】：" +sign);
//        return sign;
//    }
//
//
//    /**
//     * 将要加签的字段按照ASCII码排序
//     *
//     * @param map
//     * @return
//     */
//    public static List<Map.Entry<String, String>> sortMap(Map<String, String> map) {
//        List<Map.Entry<String, String>> infos = new ArrayList<Map.Entry<String, String>>(map.entrySet());
//
//        // 重写集合的排序方法：按字母顺序
//        Collections.sort(infos, new Comparator<Map.Entry<String, String>>() {
//            @Override
//            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
//                return (o1.getKey().compareTo(o2.getKey()));
//            }
//        });
//
//        return infos;
//    }
//
//    /**
//     * 将要加签的字段按照ASCII码排序
//     *
//     * @param map
//     * @return
//     */
//    public static List<Map.Entry<String, String>> sortMap(LinkedHashMap<String, String> map) {
//        List<Map.Entry<String, String>> infos = new ArrayList<Map.Entry<String, String>>(map.entrySet());
//
//        // 重写集合的排序方法：按字母顺序
//        Collections.sort(infos, new Comparator<Map.Entry<String, String>>() {
//            @Override
//            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
//                return (o1.getKey().compareTo(o2.getKey()));
//            }
//        });
//
//        return infos;
//    }
//
//    /**
//     * 获得签名字符串
//     *
//     * @param input
//     * @param privateKey
//     * @return
//     */
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static String getSignStr(String input, String privateKey) {
//        String signStr = null;
//        try {
//            signStr = RSAUtil.sign((input), privateKey);
//        } catch (SignatureException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return signStr;
//    }
//
//
//}
