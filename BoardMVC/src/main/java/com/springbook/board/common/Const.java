package com.springbook.board.common;

public class Const {
	public static final String KAKAO_CLIENT_ID = "d32db70088602a04b8cd3a8bd628aa27";
	public static final String KAKAO_AUTH_REDIRECT_URI = "http://localhost:8090/user/joinKakao";
	public static final String KAKAO_ACCESS_TOKEN_HOST = "https://kauth.kakao.com/oauth/token";
	public static final String KAKAO_API_HOST = "https://kapi.kakao.com";
	
	// 게시판 한번에 가져오는 데이터 수
	public static final int ROW_COUNT = 60;
}
