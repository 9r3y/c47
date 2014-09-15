package com.y3r9.c47.dog.demo.invocation;

public class ForumServiceImpl implements ForumService {

	@Override
	public void removeTopic(int topicId) {
		System.out.println("模拟删除Topic记录" + topicId);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void removeForum(int forumId) {
		System.out.println("模拟删除Forum记录" + forumId);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
