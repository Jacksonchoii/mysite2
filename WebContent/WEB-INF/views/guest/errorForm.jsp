<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="com.javaex.vo.GuestbookVo" %>

<%-- <%
	int no = Integer.parseInt(request.getParameter("no"));

%>
 --%>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
		
		<p> 비밀번호를 확인해주세요 </p>
		<br>
		
		<a href="/mysite2/guest?action=deleteForm&no=${param.no}">이전화면으로 이동</a>
</body>
</html>