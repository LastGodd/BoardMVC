package com.springbook.board.common;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.multipart.MultipartFile;

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
	
	//리턴값: 저장된 파일명
	// "/resources/user/??"
	public static String saveFile(String path, MultipartFile file) {
		String fileNm = null;
		UUID uuid = UUID.randomUUID();
		
		// 확장자
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		System.out.println("ext : " + ext);
		
		fileNm = String.format("%s.%s", uuid, ext);
		String saveFileNm = String.format("%s/%s", path, fileNm);
		
		System.out.println("saveFileNm : " + saveFileNm);
		File saveFile = new File(saveFileNm);
		saveFile.mkdirs();
		
		try {
			file.transferTo(saveFile); // 업로드 파일에 saveFile 위치로 저장
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		
		return fileNm;
	}
	
	//이미지 삭제
	public static boolean deleteFile(String filePath) {
		boolean result = false;
		File file = new File(filePath);
		if(file.exists()) {
			result = file.delete();
		}
		return result;
	}
	
	
	
	
}

