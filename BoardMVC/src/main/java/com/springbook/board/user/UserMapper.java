package com.springbook.board.user;

import org.apache.ibatis.annotations.Mapper;

@Mapper
//mybatis가 DB에서 값을 가져오기 위해 사용
public interface UserMapper {
	public int join(UserVO param);
	public UserVO login(UserVO param);
}
