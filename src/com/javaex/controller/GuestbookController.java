package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestbookDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestbookVo;


@WebServlet("/guest")
public class GuestbookController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("GuestbookController");
		
		String action = request.getParameter("action");
		System.out.println("action =" + action);
		
		if("addList".equals(action)) {
			System.out.println("방명록리스트");
			//리스트 출력처리
			GuestbookDao guestDao = new GuestbookDao();
			List<GuestbookVo> guestList = guestDao.getGuestList();
			
			//데이터전달
			request.setAttribute("gList", guestList);
			
			//forword 포워드시키기
			WebUtil.forword(request, response, "/WEB-INF/views/guest/addList.jsp");
			
		}else if("add".equals(action)) {
			System.out.println("방명록 등록");
			
			//파마미터값
			String name = request.getParameter("name");
			String password = request.getParameter("pass");
			String content = request.getParameter("content");
					
			GuestbookVo guestVo = new GuestbookVo(name, password, content);
			
			GuestbookDao guestDao = new GuestbookDao();
			guestDao.guestInsert(guestVo);
			
			WebUtil.forword(request, response, "/mysite2/guest?action=addList");
			
		}else if("deleteForm".equals(action)) {
			System.out.println("삭제폼");
			
			//포워드만 해주면 됨
			WebUtil.forword(request, response, "/WEB-INF/views/guest/deleteForm.jsp");
			
		} else if("delete".equals(action)) {
			System.out.println("삭제");
			
			int no = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("pass");
			
			GuestbookVo guestVo = new GuestbookVo(no, password);
			
			GuestbookDao guestDao = new GuestbookDao();
			
			int count = guestDao.guestDelete(guestVo);
			System.out.println(count); // 확인
			
			if(count == 1) {
				WebUtil.redirect(request, response, "/mysite2/guest?action=addList");
			} else {
				WebUtil.forword(request, response, "/WEB-INF/views/guest/errorForm.jsp");
			}
			
		} 
		
		
		
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
