package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;


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
			
			//리스트 데이터 전달
			request.setAttribute("boardList", boardList);
			
			//포워드
			WebUtil.forword(request, response, "WEB-INF/views/board/list.jsp");
		
		}  else if("writeForm".equals(action)) {
			System.out.println("게시판 글쓰기 이동");
			
			
			//포워드
		    WebUtil.forword(request, response, "WEB-INF/views/board/writeForm.jsp");
		
		} else if("write".equals(action)) { //에러 부적절한 열 이름 해결 못함
			System.out.println("게시글쓰기");
			
			//파라미터에서 값 가져오기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
		
			
			//세션에서 유저no 가져오기
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			int userNo = authUser.getNo();
			
			//vo로 묶기
			BoardVo boardVo = new BoardVo(title, content, userNo);
			
			//Dao write
			BoardDao boardDao = new BoardDao();
			boardDao.boardWrite(boardVo);
	
			
			//리다이렉트
			WebUtil.redirect(request, response, "/mysite2/board?action=list");
			
		} else if("delete".equals(action)) {
			System.out.println("게시글 삭제");
		
			//no값 파라미터에서 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			
			//Dao 삭제
			BoardDao boardDao = new BoardDao();
			boardDao.boardDelete(no);
			
			//리다이렉트 삭제 후 게시글없어진 화면
			WebUtil.redirect(request, response, "/mysite2/board?action=list");
		
		} else if("read".equals(action)) {
			System.out.println("게시글 보기");
			
			//파라미터 no값 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			
			//Dao read
			BoardDao boardDao = new BoardDao();
			BoardVo boardRead = boardDao.boardRead(no);
			
			//게시글 보기 후 조회수 증가 게시글번호 필요
			boardDao.hitAdd(no);
			
			//데이터 전달
			request.setAttribute("boardRead", boardRead);
			
			//포워드
			WebUtil.forword(request, response, "WEB-INF/views/board/read.jsp");
		
		} else if("modifyForm".equals(action)) {
			System.out.println("게시글 수정 폼");
			
			//파라미터 no값
			int no = Integer.parseInt(request.getParameter("no"));
			
			//Dao read
			BoardDao boardDao = new BoardDao();
			BoardVo boardRead = boardDao.boardRead(no);
			
			//데이터 전달
			request.setAttribute("boardRead", boardRead);
			
			//포워드
			WebUtil.forword(request, response, "WEB-INF/views/board/modifyForm.jsp");
			
		} else if("modify".equals(action)) {
			System.out.println("게시글 수정");
			
			//파라미터 값
			int no = Integer.parseInt(request.getParameter("no"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			//Dao modify
			BoardVo boardVo = new BoardVo(no, title, content);
			BoardDao boardDao = new BoardDao();
			
			boardDao.boardModify(boardVo);
			
			//리다이렉트
			WebUtil.redirect(request, response, "/mysite2/board?action=list");
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
