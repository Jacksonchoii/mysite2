package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;


@WebServlet("/board")
public class BoardController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.println("BoardController");
		
		String action = request.getParameter("action");
		System.out.println("action=" + action);
		
		if("list".equals(action)) {
			System.out.println("게시판 리스트");
			//리스트 출력
			BoardDao boardDao = new BoardDao();
			List<BoardVo> boardList = boardDao.getBoardList();
			
			//데이터 전달
			request.setAttribute("boardList", boardList);
			
			WebUtil.forword(request, response, "WEB-INF/views/board/list.jsp");
		
		} else if("read".equals(action)) {
			System.out.println("게시글 보기");
			
			WebUtil.forword(request, response, "WEB-INF/views/board/read.jsp");
		
		} else if("writeForm".equals(action)) {
			System.out.println("게시판 글쓰기");
			
			WebUtil.forword(request, response, "WEB-INF/views/board/writeForm.jsp");
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
