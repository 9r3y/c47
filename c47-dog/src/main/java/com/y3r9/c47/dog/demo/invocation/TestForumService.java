package com.y3r9.c47.dog.demo.invocation;

import java.lang.reflect.Proxy;

/**
 * 代理模式
 * @author Administrator
 *
 */
public class TestForumService {
	
	public static void main(String[] args) {
		ForumService target = new ForumServiceImpl();
		
		PerformanceHandler handler = new PerformanceHandler(target);
		
		ForumService proxy = (ForumService) Proxy.newProxyInstance(
				target.getClass().getClassLoader(), target.getClass().getInterfaces(), handler);
		proxy.removeForum(10);
		proxy.removeTopic(1012);
		
	}

}
