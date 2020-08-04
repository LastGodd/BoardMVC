package com.springbook.board.user;

import java.nio.charset.Charset;
import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbook.board.common.Const;
import com.springbook.board.common.KakaoAuth;
import com.springbook.board.common.KakaoUserInfo;
import com.springbook.board.common.MyUtils;

@Service
//Service - Controller의 로직담당
public class UserService {

	@Autowired
	private UserMapper mapper;

	// 회원가입
	public int join(UserVO param) {
		int result = 0;
		String salt = MyUtils.gensalt();
		String pw = param.getUpw();
		String hashPw = MyUtils.hashPassword(pw, salt);

		param.setUpw(hashPw);
		param.setSalt(salt);
		// param.setUpw(MyUtils.hashPassword(param.getUpw()));

		result = mapper.join(param);
		return result;
	}

	// 1:로그인성공 2:아이디 없음 3:비밀번호 틀림
	public int login(UserVO param, HttpSession hs, Model model) {
		int result = 0;
		UserVO db = mapper.login(param);
		if (db != null) {
			String pw = param.getUpw();
			String salt = db.getSalt();
			System.out.println(param.getUpw());
			System.out.println(db.getSalt());
			String hashPw = MyUtils.hashPassword(pw, salt);
			if (db.getUpw().equals(hashPw)) {
				db.setUpw(null);
				hs.setAttribute("loginUser", db);
				result = 1;
			} else {
				result = 3;
			}
		} else {
			result = 2;
		}
		model.addAttribute("data", param);
		return result;
	}

	public int kakaoLogin(String code, HttpSession hs) {
		int result = 0;
		
		//--------------------------------------------------------------- 사용자 토큰 받기 --------------- [ START ]
		HttpHeaders headers = new HttpHeaders();
		Charset utf8 = Charset.forName("UTF-8");
		MediaType mediaType = new MediaType(MediaType.APPLICATION_JSON, utf8);		
		headers.setAccept(Arrays.asList(mediaType));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);		
				
		MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
		map.add("grant_type", "authorization_code");
		map.add("client_id", Const.KAKAO_CLIENT_ID);
		map.add("redirect_uri", Const.KAKAO_AUTH_REDIRECT_URI);
		map.add("code", code);
				
		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity(map, headers);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> respEntity 
		= restTemplate.exchange(Const.KAKAO_ACCESS_TOKEN_HOST, HttpMethod.POST, entity, String.class);
		
		String data = respEntity.getBody();
		System.out.println("data : " + data);
		
		ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		KakaoAuth auth = null;
		try {
			auth = om.readValue(data, KakaoAuth.class);
			
			System.out.println("access_token: " + auth.getAccess_token());
			System.out.println("refresh_token: " + auth.getRefresh_token());
			System.out.println("expires_in: " + auth.getExpires_in());
			System.out.println("refresh_token_expires_in: " + auth.getRefresh_token_expires_in());
			
		} catch (JsonMappingException e) {			
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}		
		
		//--------------------------------------------------------------- 사용자 토큰 받기 --------------- [ END ]
		
		
		//--------------------------------------------------------------- 사용자 정보 가져오기 --------------- [ START ]
		HttpHeaders headers2 = new HttpHeaders();		
		MediaType mediaType2 = new MediaType(MediaType.APPLICATION_JSON, utf8);		
		headers2.setAccept(Arrays.asList(mediaType2));
		headers2.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers2.set("Authorization", "Bearer " +  auth.getAccess_token());
		
		HttpEntity<LinkedMultiValueMap<String, Object>> entity2 = new HttpEntity("", headers2);
				
		ResponseEntity<String> respEntity2 
		= restTemplate.exchange(Const.KAKAO_API_HOST + "/v2/user/me", HttpMethod.GET, entity2, String.class);
		
		String result2 = respEntity2.getBody();
		System.out.println("result2 : " + result2);
		
		KakaoUserInfo kui = null;
		
		try {
			kui = om.readValue(result2, KakaoUserInfo.class);
			
			System.out.println("id: " + kui.getId());
			System.out.println("connected_at: " + kui.getConnected_at());
			System.out.println("nickname: " +kui.getProperties().getNickname());
			System.out.println("profile_image: " + kui.getProperties().getProfile_image());
			System.out.println("thumbnail_image: " + kui.getProperties().getThumbnail_image());
			
		} catch (JsonMappingException e) {			
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}	
		//--------------------------------------------------------------- 사용자 정보 가져오기 --------------- [ END ]
			
		//아이디 존재 체크
		UserVO param = new UserVO();
		param.setUid(String.valueOf(kui.getId()));
		
		UserVO dbResult = mapper.login(param);
		
		if(dbResult == null) { //회원가입
			param.setNm(kui.getProperties().getNickname());
			param.setUpw("");
			param.setPh("");
			param.setSalt("");
			param.setAddr("");
			mapper.join(param);
			
			dbResult = param;
		}
		
		//로그인 처리(세션에 값 add)
		hs.setAttribute("loginUser", dbResult);
		
		return result;
	}
}