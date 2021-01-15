package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

@WebServlet("/user")
public class UserController extends HttpServlet {
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("UserController");
		
		
		String action = request.getParameter("action");
		System.out.println("action=" + action);
		
		if("joinForm".equals(action)) {
			System.out.println("회원가입폼");
			WebUtil.forword(request, response, "/WEB-INF/views/user/joinForm.jsp");	
		
		}else if("join".equals(action)) {
			System.out.println("회원가입");
		
			// dao --> insert() 저장
			
			// 파라미터 값 꺼내기
			String id = request.getParameter("uid");
			String password = request.getParameter("pw");
			String name = request.getParameter("uname");
			String gender = request.getParameter("gender");
			
			// vo로 묶기 --> vo만들기    기존 vo에 4개 생성자 추가 
			UserVo userVo = new UserVo(id, password, name, gender);
			System.out.println(userVo.toString());
			
			// dao클래스 insert(vo) 사용 --> 저장 --> 회원가입
			UserDao userDao = new UserDao();
			userDao.insert(userVo);
			
			//forword 포워드 --> joinOK.jsp
			WebUtil.forword(request, response, "WEB-INF/views/user/joinOk.jsp");
			
		}else if("loginForm".equals(action)) {
			System.out.println("로그인폼");
			
			//forword 포워드 --> loginForm.jsp
			WebUtil.forword(request, response, "WEB-INF/views/user/loginForm.jsp");
		
		}else if("login".equals(action)) {
			System.out.println("로그인");
			
			//파라미터 id pw
			String id = request.getParameter("id");
			String pw = request.getParameter("pw");
			
			//dao --> getUser();
			UserDao userDao = new UserDao();
			UserVo authVo = userDao.getUser(id, pw);
			
			System.out.println(authVo); //id pw 있으면 ---> no, name 가져옴 테스트
			
			if(authVo==null) { //로그인 실패
				System.out.println("로그인 실패");
				//리다이렉트-->로그인폼
				WebUtil.redirect(request, response, "/mysite2/user?action=loginForm&result=fail");
				
			}else { //성공일때
				System.out.println("로그인 성공");
			
				//Session영역(브라우저 닫힐 때까지 유지되는 공간)의 의 어트리뷰트에 브라우저를 구별해낼 수 있는 값 보내기
				//성공일때
				HttpSession session = request.getSession();
				session.setAttribute("authUser", authVo);
				
				WebUtil.redirect(request, response, "/mysite2/main"); //성공하고 메인으로
			}
			
		}else if("logout".equals(action)) {
			System.out.println("로그아웃");
			
			//세션영역에 있는 vo를 삭제해야함 /  servlet에서는(java)코드에서는 달라고해야함
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			
			WebUtil.redirect(request, response, "/mysite2/main");
			
		}else if("modifyForm".equals(action)) {
			System.out.println("회원정보수정폼");
			
			//세션의 넘버 값 가져오기
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");//session 오브젝트 -->형변환 잊어먹고 왜 안되는건가 고민하지말 것 
			//System.out.println(authUser); 확인
			
			int no = authUser.getNo();
			
			//dao --> 세션의 넘버 값으로 누군지 알아야해서  사람정보 가져옴(회원정보 가져오기)
			UserDao userDao = new UserDao();
			UserVo userVo = userDao.getUser(no); //Dao업데이트
			
			//userVo 받았는지 확인
			System.out.println("getUser(no)" + userVo);
			
			//데이터 전달
			request.setAttribute("userVo", userVo);
			
			/* 수정전 틀린내용 --> 세션의 값은 최소로 가지고 있으려하는데 굳이 보내줄 필요 없음 -> 처음 의도랑 달라짐. request 어트리뷰트로 보내자
			//누군지 알게된 정보를 세션 어트리뷰트에 보냄
			//session.setAttribute("authMdUser", authVo);
			*/
			
			//포워드
			WebUtil.forword(request, response, "WEB-INF/views/user/modifyForm.jsp");
			
		} else if("modify".equals(action)) {
			System.out.println("회원정보수정");
			
			//파라미터 값
			String password = request.getParameter("pw");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			//수정값 확인
			System.out.println("회원정보 수정값 확인" + password + name + gender);
			
			//세션에서 넘버(no) 값 가져오기
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			
			int no = authUser.getNo();
			
			//vo묶기 -> Vo 생성자 만들기
			UserVo userVo = new UserVo(no, password, name, gender);
			//UserVo userVo = new UserVo(no, "", password, name, gender); tip- id값을 ""비워놓아도 된다 --> 추천하진 않음
			
			//확인하기
			System.out.println(userVo);
			
			//dao --> userModify()
			UserDao userDao = new UserDao();
			
			//정보 수정
			userDao.userModify(userVo); 
			
			
			//수정되고난후 - 이름 바뀌면 메인페이지 이름도 바뀐 값으로 --> 세션값도 바꿔야 함 modify해야함
			//세션의 name값만 변경하면됨 가져오기                                                 ------ 강사님 풀이 전 혼자 했을 때 이 부분 꼬임 --> 다시 공부해볼 것
			authUser.setName(name);
			
			
			WebUtil.redirect(request, response, "/mysite2/main");
			
		} 
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
