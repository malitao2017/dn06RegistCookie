<!DOCTYPE unspecified PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="entity.User"%>
<%@page contentType="text/html;charset=utf-8" %>
<% 
	User user = (User)session.getAttribute("user");
	if(user==null){
		session.setAttribute("login_error", "没有登陆！");
// 		request.setAttribute("login_error", "没有登陆！");//只能用session有效
		response.sendRedirect("login.jsp");		
		return;
	}
%>
<html>
<head></head>
<body>
<h1>这是主页面</h1>
<h1><a href="outLogin.do">退出</a></h1>
</html>
