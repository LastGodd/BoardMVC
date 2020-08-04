package com.springbook.board.common;

import org.mindrot.jbcrypt.BCrypt;

public class MyUtils {
	
	public static String gensalt() {
		return BCrypt.gensalt();
	}
	
	public static String hashPassword(String pw, String salt) {
		return BCrypt.hashpw(pw, salt);
	}
	
	//랜덤 숫자 (sNo~eNo 범위)
	public static int makeRandomNumber(int sNo, int eNo) {
		return (int)(Math.random() * (eNo - sNo) + sNo);
	}
	
	//len:길이 (0~9사이의 숫자)	
	public static String makeRandomNumber(int len) {
		String result = "";		
		for(int i=0; i<len; i++) {
			result += (int)(Math.random() * 10);
		}
		return result;
	}
}

