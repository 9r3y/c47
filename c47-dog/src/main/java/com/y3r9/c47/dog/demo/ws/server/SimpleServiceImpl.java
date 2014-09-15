package com.y3r9.c47.dog.demo.ws.server;

public class SimpleServiceImpl implements SimpleService {

	@Override
	public String concat(ConcatRequest parameters) {
		return parameters.getS1() + parameters.getS2();
	}

}
