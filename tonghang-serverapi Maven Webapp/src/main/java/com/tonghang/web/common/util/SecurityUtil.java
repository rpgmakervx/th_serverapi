package com.tonghang.web.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import sun.misc.BASE64Encoder;

public class SecurityUtil {

	/**
	 * 获得UUID,并去掉其中的 ‘-’ 字符
	 * @return
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String getRYID(String client_id){
		List<Character> result = combineChar(client_id);
		Collections.shuffle(result);
		return listToString(result);
	}
	
	private static String listToString(List<Character> chlist){
		String result = "";
		for(char ch:chlist){
			result += ch;
		}
		return result;
	}
	private static List<Character> combineChar(String client_id){
		return getMixedCharList(client_id);
	}
	//UUID变成asc码后返回
	private static String getCharUUID(String client_id){
		StringBuffer buffer = new StringBuffer();
		for(byte ch:client_id.getBytes()){
			buffer.append(ch);
		}
		return buffer.toString();
	}
	//混淆拼接好的ry_id
	private static List<Character> getMixedCharList(String client_id){
		List<Character> chlist = new ArrayList<Character>();
		String timestamp = TimeUtil.timestamp(new Date());
		for(char ch:(getCharUUID(client_id)+timestamp).toCharArray()){
			if(chlist.size()>11)
				break;
			chlist.add(ch);
		}
		return chlist;
	}	
//	 /**
//     * 生成md5
//     * @param message
//     * @return
//     */
//    public static String getMD5(Object message) {
//        String md5str = "";
//        try {
//            //1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            //2 将消息变成byte数组
//            ObjectMapper mapper = new ObjectMapper();
//            String s = mapper.writeValueAsString(message);
////            MappingJackson2HttpMessageConverter jackson = new MappingJackson2HttpMessageConverter();
//            byte[] input = s.getBytes();
//            //3 计算后获得字节数组,这就是那128位了
//            byte[] buff = md.digest(input);
// 
//            //4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
//            md5str = bytesToHex(buff);
// 
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return md5str;
//    }
// 
//
//    
//    /**
//     * 二进制转十六进制
//     * @param bytes
//     * @return
//     */
//    public static String bytesToHex(byte[] bytes) {
//        StringBuffer md5str = new StringBuffer();
//        //把数组每一字节换成16进制连成md5字符串
//        int digital;
//        for (int i = 0; i < bytes.length; i++) {
//             digital = bytes[i];
// 
//            if(digital < 0) {
//                digital += 256;
//            }
//            if(digital < 16){
//                md5str.append("0");
//            }
//            md5str.append(Integer.toHexString(digital));
//        }
//        return md5str.toString().toUpperCase();
//    }
    public static String getMD5(String str) {
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();

			int i;

			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
			System.out.println("result: " + buf.toString());// 32位的加密
			System.out.println("result: " + buf.toString().substring(8, 24));// 16位的加密

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return buf.toString();
	}
    
    public static String getBase64(String str) {  
        byte[] b = null;  
        String s = null;  
        try {  
            b = str.getBytes("utf-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        if (b != null) {  
            s = new BASE64Encoder().encode(b);  
        }  
        return s;  
    }  

}
