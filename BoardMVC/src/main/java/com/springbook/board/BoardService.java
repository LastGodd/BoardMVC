package com.springbook.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springbook.board.common.Const;

@Service
public class BoardService {	
	
	@Autowired
	private BoardMapper mapper;
	
	public int insBoard(BoardVO param) {
		return mapper.insBoard(param);
	}
	
	public List<BoardVO> selBoardList(int page, String searchText) {		
		int sIdx = (page - 1) * Const.ROW_COUNT;
		
		BoardVO param = new BoardVO();
		param.setsIdx(sIdx);
		param.setCount(Const.ROW_COUNT);
		param.setSearchText(searchText);
		
		return mapper.selBoardList(param);
	}
	
	public BoardVO selBoard(BoardVO param) {
		return mapper.selBoard(param);
	}
	
	public int updBoard(BoardVO param) {
		return mapper.updBoard(param);
	}
	
	public int delBoard(int param) {
		return mapper.delBoard(param);
	}
}