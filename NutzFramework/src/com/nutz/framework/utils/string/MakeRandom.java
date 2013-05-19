package com.nutz.framework.utils.string;

import java.util.Random;

/**   
 * MyRandom 概要说明  
 * 自定义随机生成类
 */
public class MakeRandom {
	
	/**
	 * randomStr方法概要说明
	 * 产生随机字符串
	 * @param strLength 字符串长度
	 * @return
	 */
	public static String randomStr(int strLength){
		Random random=new Random();
		String s="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // 设置备选验证码:包括"a-z"和数字"0-9" 		
		String strRand=""; 
		for(int i=0;i<strLength;i++){ 
		   String ch=String.valueOf(s.charAt(random.nextInt(s.length()))); 
		   strRand+=ch; 
		}
		return strRand;
	}
	
	/**
	 * randomStr方法概要说明
	 * 产生随机数字
	 * @param strLength 长度
	 * @return
	 */
	public static Integer randomInt(int strLength){
		Random random=new Random();
		String s="0123456789"; // 设置备选验证码:数字"0-9" 		
		String strRand=""; 
		for(int i=0;i<strLength;i++){ 
		   String ch=String.valueOf(s.charAt(random.nextInt(s.length()))); 
		   strRand+=ch; 
		}
		Integer randInt=Integer.parseInt(strRand);
		return randInt;
	}

}
