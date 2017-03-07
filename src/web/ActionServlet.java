/*
 * ActionServlet.java
 * Copyright: TsingSoft (c) 2015
 * Company: 北京清软创新科技有限公司
 */
package web;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDao;
import entity.User;

/**
 * 业务处理逻辑
 * @author LT
 * @version 1.0, 2015年9月21日
 */
public class ActionServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String uri = request.getRequestURI();
//		System.out.println("uri:"+uri);
//		System.out.println("url:"+request.getRequestURL());
		String path = uri.substring(uri.lastIndexOf("/"),uri.lastIndexOf("."));
		UserDao dao = new UserDao();
		/**
		 * 验证码功能
		 * 说明浏览器访问页面 test.html 时，在执行<img src="checkcode">语句时向服务器发送了checkcode 请求
		 */
		if(path.equals("/checkCode")){
			//服务器返回格式，其他的还有：text/html;text/xml;
			response.setContentType("image/jpeg");
			Random r = new Random();
			
			//1.内存映像对象
			BufferedImage image = new BufferedImage(60, 20, BufferedImage.TYPE_INT_RGB);
			//获取画笔
			Graphics g = image.getGraphics();
			//设置背景颜色
			g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
			//填充背景颜色
			g.fillRect(0, 0, 60, 20);
			//设置前景颜色
			g.setColor(new Color(0,0,0));
			
			//2.内容刻画
			String num = String.valueOf(r.nextInt(99999));
			request.getSession().setAttribute("num", num);
			g.drawString(num,5,15);
			g.drawLine(r.nextInt(60), r.nextInt(20), r.nextInt(60), r.nextInt(20));
			g.drawLine(r.nextInt(60), r.nextInt(20), r.nextInt(60), r.nextInt(20));
			
			//3.输出
			//压缩后输出,因为是图像压缩后的字节数组，不能使用printwrite
			//第一、使用统一的imageIO进行输出
			OutputStream os = response.getOutputStream();
			ImageIO.write(image, "JPEG", os);
			os.flush();
			os.close();
			//第二、这是jdk过时的写法,是sun私有的方法
//			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
//			encode.encode(image);
		}
		else if(path.equals("/regist")){
			String number = request.getParameter("number");
			String num = (String)request.getSession().getAttribute("num");
			//当用户禁用cookie后，session为null
			if(num!=null && !number.equals(num)){
				request.getSession().setAttribute("check_error","验证码不正确");
				request.getRequestDispatcher("regist.jsp").forward(request, response);
			}else{
				String username = request.getParameter("username");
				User user = dao.findByUserName(username);
				if(user==null){
					user = new User();
					user.setUserName(request.getParameter("username"));
					user.setName(request.getParameter("name"));
					user.setPwd(request.getParameter("pwd"));
					user.setGender(request.getParameter("sex"));
					dao.save(user);
					response.sendRedirect("main.jsp");
				}else{
					request.setAttribute("msg_error", "用户名已存在");
					request.getRequestDispatcher("regist.jsp").forward(request, response);
				}
			}
		}
		/**
		 * session使用1
		 * session状态判断的应用
		 */
		else if(path.equals("/login")){
			String userName = request.getParameter("username");
			String pwd = request.getParameter("pwd");
			//第一、正常的模式，使用preparedstatement 能防止sql注入
//			User user = dao.findNormal(userName,pwd);
			//第二、 使用Statement不能防止sql注入 如： 用户名（admin） 密码（ 1' or '1' = '1 ） 
			User user = dao.findPour(userName, pwd);
			if(user!=null ){
				HttpSession session = request.getSession();
				System.out.println("/login:sessionId:"+session.getId());
				session.setAttribute("user", user);
				response.sendRedirect("main.jsp");
			}else{
				if(user == null)
					request.setAttribute("msg_error", "用户名或密码不对");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		}
		else if(path.equals("/outLogin")){
			HttpSession session = request.getSession();
			session.invalidate();
			response.sendRedirect("login.jsp");
		}
//		在重定向代码后写输出语句会丌会执行？
//		会执行。 JVM 将顺序执行代码，除非遇到 return 戒 System.exit();
		
		
		//session和cookie的使用
//		浏览器访问服务器时，服务器会创建一个 session 对象(该对象有一个唯一的 id, 一般称为
//		sessionId)。服务器在缺省情况下，会将 sessionId 以 cookie 机制发送给浏览器。当浏览器再次访
//		问服务器时，会将 sessionId 发送给服务器。服务器依据 sessionId 就可以找到对应的 session 对
//		象。通过这种方式，就可以管理用户的状态。
		/**
		 * session使用
		 * 1.服务器端记录访问次数
		 * 2.设置session失效时间
		 */
		else if(path.equals("/count")){
			HttpSession session = request.getSession();
			/* 此为session失效的时间
			int activeTime = 10;
			session.setMaxInactiveInterval(activeTime);
			System.out.println("/count:sessionId:"+session.getId()+"失效时间为："+activeTime+"秒");
			*/
			Integer count = (Integer)session.getAttribute("count");
			if(count==null){
				count=1;
			}else{
				count++;
			}
			session.setAttribute("count",count);
			PrintWriter out = response.getWriter(); 
			out.println("这是你的第"+count+"次访问");
			out.close();
		}
		/**
		 * 保存和查询 Cookie 流程
			a. 浏览器向服务器发送 addCookie 请求
			服务器中的 AddCookieServlet 创建了两个 Cookie：cookie 和 cookie2
			b. 服务器端执行语句 response.addCookie(cookie);生成消息头“ set-cookie”，
			并将两个 Cookie 以键值对的方式（“ name=aaa”、“ passwd=123”）存放在消息头中发
			送给浏览器
			c. 浏览器将 Cookie 信息保存到本地内存中
			d. 浏览器继续向服务器发送请求（带着消息头 cookie）
			服务器端的 FindCookieServlet 找到 Cookie 信息，并显示给浏览器
		 */
		/**
		 * cookie使用1
		 * 	1.保存cookie
		 * 	2.cookie的值只能保存ascii码，若是中文则须进行转化
		 * 		编码ascii：URLEncoder.encode
		 * 		解析ascii：URLDecoder.decode
		 *  3.可以设置生存期
		 *  	>0 则在该时间内有效
		 *  	<0 也是默认值，浏览器一直保存在内存中，直到重启
		 *  	=0 立刻删除
		 *  4.cookie有路径设置,访问的范围为该路径下，或是其以下的目录
		 *  	默认为 /appname
		 */
		else if(path.equals("/addCookie")){
			//正常的cookie信息
			Cookie ck1 = new Cookie("username", "aaa");
			response.addCookie(ck1);
			Cookie ck2 = new Cookie("pwd","123");
			response.addCookie(ck2);
			
			//cookie只支持ascii编码
			String name = URLEncoder.encode("张三","utf-8");
			Cookie ck3 = new Cookie("name",name);
			response.addCookie(ck3);
			
			//时间限制,单位是秒
			int time = 20;
			Cookie ck4 = new Cookie("timeout", URLEncoder.encode("有效期是"+time+"秒，当是0秒的时候就是删除操作","utf-8"));
			ck4.setMaxAge(time);
			response.addCookie(ck4);
			
			//path路径的设置，只允许当前或子路径访问
			Cookie ck5 = new Cookie("path_", URLEncoder.encode("当前及以下路径有效","utf-8"));//path是关键字
			ck5.setPath("/dn06RegistCookie");
			response.addCookie(ck5);
			PrintWriter out = response.getWriter(); 
			out.println("firefox-工具-选项-隐私-移除单个cookie-中进行查看");
			out.close();
		}
		/**
		 * cookie使用2
		 * 遍历cookie
		 */
		else if(path.equals("/findCookie")){
			PrintWriter out = response.getWriter(); 
			Cookie[] cookies = request.getCookies();
			if(cookies == null){
				out.println("<h1>cookie为空</h1>");
			}
			for(Cookie cookie:cookies){
				String value = URLDecoder.decode(cookie.getValue(), "utf-8");
				out.println("<h1>cookie:"+cookie.getName()+" value:"+value+"</h1>");
			}
			out.close();
		}
		
		
		/**
		 * 禁用cookie的处理1-发出者
		 */
		else if(path.equals("/cookieFrom")){
			HttpSession session = request.getSession();
			session.setAttribute("name", "balalala...");
			
//			String URL = "cookieTo.do";//一般写法
			String URL = response.encodeRedirectURL("cookieTo.do");//cookie禁用后的写法
			System.out.println("URL写法："+URL);
			response.sendRedirect(URL);
		}
		/**
		 * 禁用cookie的处理2-接受者
		 */
		else if(path.equals("/cookieTo")){
			String name = (String)request.getSession().getAttribute("name");
			PrintWriter out = response.getWriter();
			out.println("cookieTo:"+name);
		}
		
	}
}
