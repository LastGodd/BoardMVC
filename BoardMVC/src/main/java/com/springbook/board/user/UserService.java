package com.springbook.board.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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

}