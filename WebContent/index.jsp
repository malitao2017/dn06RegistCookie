<!DOCTYPE a PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html;charset=utf-8" %>
<html>
<h1><a href="regist.jsp">注册页面：加入验证码</a></h1>
<h1><a href="login.jsp">登陆页面：session状态管理</a></h1>
<br><br><h2>以下为session和cookie的相关内容</h2>
<h1><a href="count.do">session使用：统计访问次数以及session失效时间设置</a></h1>
<h1><a href="addCookie.do">cookie使用：cookie的添加方式</a></h1>
<h1><a href="findCookie.do">cookie使用：cookie的遍历方式</a></h1>
	<br/>
	
<h1><a href="<%= response.encodeURL("count.do") %>">
<!-- <h1><a href="count.do"> -->
cookie禁用:方式 1（适用于链接、表单提交） "count.do"->response.encodeURL("count.do")
<br>href="URL"</a></h1>

<h1><a href="cookieFrom.do">
cookie禁用：方式2（适用于重定向）"count.do"->response.encodeRedirectURL("count.do")
<br>response.sendRedirect("URL")</a></h1>
<a>以上两种都由原模式 count.do 变为 count.do;jsessionid=596A1BC51F79553E341AF3B0F5257828</a>
</html>