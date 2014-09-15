package com.y3r9.c47.dog.demo.nio2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class BlockingServer {
	public static void main(String[] args) {
		final int DEFAULT_PORT = 5555;
		final String IP = "127.0.0.1";
		ByteBuffer buffer = ByteBuffer.allocate(1024);

		try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
			if (ssc.isOpen()) {
				ssc.configureBlocking(true);
				ssc.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
				ssc.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				ssc.bind(new InetSocketAddress(IP, DEFAULT_PORT));
				System.out.println("Waiting for connection...");
				while (true) {
					try (SocketChannel sc = ssc.accept()) {
						System.out.println("Incomming connection from " + sc.getRemoteAddress());
						while (sc.read(buffer) != -1) {
							buffer.flip();
							sc.write(buffer);
							if (buffer.hasRemaining()) {
								buffer.compact();
							} else {
								buffer.clear();
							}
						}	
					} catch (IOException e) {
						
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
