package com.atguigu.crowd.handler;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloHandler {
	
	@RequestMapping("/test/spring/session/receive")
	public String testSession(HttpSession session) {
		
		String value = (String) session.getAttribute("king");
		return value;
	}

}
