<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.javaex.vo.UserVo" %>

<%
	UserVo authUser = (UserVo)session.getAttribute("authUser"); //session 이름으로 영역이 이미 있기 때문에 바로 .사용 가능  *주의 저장된공간=session영역 

%>

		<div id="header">
					<h1><a href="/mysite2/main">MySite</a></h1>
					
					<!-- if()로그인 안했을 때 -->
					<%if(authUser==null) {%>
					<ul>
						<li><a href="/mysite2/user?action=loginForm">로그인</a></li>
						<li><a href="/mysite2/user?action=joinForm">회원가입</a></li>
					</ul>
					
					<!-- 아래는 로그인 했을 때 -->
					<%}else { %>
					<ul>
						<li><%=authUser.getName() %> 님 안녕하세요^^</li>
						<li><a href="/mysite2/user?action=logout">로그아웃</a></li>
						<li><a href="/mysite2/user?action=modifyForm">회원정보수정</a></li>
					</ul>
					<%} %>
					
				</div>
				<!-- //header -->
		
				<div id="nav">
					<ul>
						<li><a href="">방명록</a></li>
						<li><a href="">갤러리</a></li>
						<li><a href="">게시판</a></li>
						<li><a href="">입사지원서</a></li>
					</ul>
					<div class="clear"></div>
				</div>
				<!-- //nav -->