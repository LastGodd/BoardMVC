package com.springbook.board;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
// mybatis가 DB에서 값을 가져오기 위해 사용
public interface BoardMapper {
	public int insBoard(BoardVO param);
	public List<BoardVO> selBoardList();
	public BoardVO selBoard(BoardVO param);
	public int updBoard(BoardVO param);
	public int delBoard(int param);
}
