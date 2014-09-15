package com.y3r9.c47.dog.demo.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * 演示移相器的用例
 * @author Administrator
 *
 */
public class ProductExchanger {
	public static Exchanger<List<Integer>> exchanger = new Exchanger<>();
	
	public static void main(String[] args) {
        Thread producer = new Thread(new Producer());
        Thread consumer = new Thread(new Consumer());
        producer.start();
        consumer.start();
        try {
        	while (System.in.read() != '\n') {
        		
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
        producer.interrupt();
        consumer.interrupt();
		
	}
}

class Producer implements Runnable {

    private static List<Integer> buffer = new ArrayList<>();
    private boolean okToRun = true;
    private final int BUFFSIZE = 10;

    @Override
    public void run() {
        int j = 0;
        while (okToRun) {
        	if (buffer.isEmpty()) {
        		try {
        			for (int i=0; i<BUFFSIZE; i++) {
        				buffer.add((int)(Math.random() * 100));
        			}
        			Thread.sleep((int)(Math.random() * 1000));
        			System.out.print("Producer Buffer: ");
        			for (int i : buffer) {
        				System.out.print(i + ", ");
        			}
        			System.out.println();
        			System.out.println("Exchanging ...");
        			buffer = ProductExchanger.exchanger.exchange(buffer);
				} catch (InterruptedException e) {
					okToRun = false;
				}
			} else {

			}
		}
    }
}

class Consumer implements Runnable {
	private static List<Integer> buffer = new ArrayList<>();
	private boolean okToRun = true;

	@Override
	public void run() {
		while (okToRun) {
			try {
				if (buffer.isEmpty()) {
					buffer = ProductExchanger.exchanger.exchange(buffer);
					System.out.println("Consumer Buffer: ");
					for (int i : buffer) {
						System.out.println(i + ", ");
					}
					System.out.println("\n");
					Thread.sleep((int)Math.random() * 1000);
					buffer.clear();
				}
				
			} catch (InterruptedException e) {
				okToRun = false;
			}
			
		}
		
	}
}
