package com.y3r9.c47.dog.demo.aspectj;

public class NativeWaiter implements Waiter {

	@Override
	public void greetTo(String clientName) {
		System.out.println("NativeWaiter greet to " + clientName + "...");

	}

	@Override
	public void serveTo(String clientName) {
		System.out.println("NativeWaiter serving " + clientName + "...");
	}

}
