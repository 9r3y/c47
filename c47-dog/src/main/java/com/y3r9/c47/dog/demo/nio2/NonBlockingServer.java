package com.y3r9.c47.dog.demo.nio2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NonBlockingServer {
	private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
	private ByteBuffer buffer = ByteBuffer.allocate(2* 1024);
	
	private void startEchoServer() {
		final int DEFAULT_PORT = 5555;
		try (Selector selector = Selector.open();
		     ServerSocketChannel ssc = ServerSocketChannel.open()) {
			if (ssc.isOpen() && selector.isOpen()) {
				ssc.configureBlocking(false);
				ssc.setOption(StandardSocketOptions.SO_RCVBUF, 256 * 1024);
				ssc.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				
				ssc.bind(new InetSocketAddress(DEFAULT_PORT));
				
				ssc.register(selector, SelectionKey.OP_ACCEPT);
				
				System.out.println("Waiting for connections ...");
				
				while(true) {
					selector.select();
					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
					
					while (keys.hasNext()) {
						SelectionKey key = keys.next();
						keys.remove();
						
						if (!key.isValid()) {
							continue;
						}
						
						if (key.isAcceptable()) {
							this.acceptOP(key, selector);
						} else if (key.isReadable()) {
							this.readOP(key);
						} else if (key.isWritable()) {
							this.writeOP(key);
						}
					}
				}
			} else {
				System.out.println("The server socket channel or selector cannot be opened!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void acceptOP(SelectionKey key, Selector selector) throws IOException {
		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		SocketChannel sc = ssc.accept();
		sc.configureBlocking(false);
		
		System.out.println("Incoming connection from: " + sc.getRemoteAddress());
		
		sc.write(ByteBuffer.wrap("Hello!\n".getBytes("UTF-8")));
		keepDataTrack.put(sc, new ArrayList<byte[]>());
		sc.register(selector, SelectionKey.OP_READ);
	}
	
	private void readOP(SelectionKey key) {
		try {
			SocketChannel sc = (SocketChannel) key.channel();
			buffer.clear();
			int numRead = -1;
			try {
				numRead = sc.read(buffer);
			} catch (IOException e) {
				System.out.println("Cannot read error!");
			}
			
			if (numRead == -1) {
				this.keepDataTrack.remove(sc);
				System.out.println("Connectio closed by: " +sc.getRemoteAddress());
				sc.close();
				key.cancel();
				return;
			}
			byte[] data = new byte[numRead];
			System.arraycopy(buffer.array(), 0, data, 0, numRead);
			System.out.println(new String(data, "UTF-8") + " from " + sc.getRemoteAddress());
			doEchoJob(key, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeOP(SelectionKey key) throws IOException {
		SocketChannel sc = (SocketChannel) key.channel();
		List<byte[]> channelData = keepDataTrack.get(sc);
		Iterator<byte[]> its = channelData.iterator();
		
		while (its.hasNext()) {
			byte[] it = its.next();
			its.remove();
			sc.write(ByteBuffer.wrap(it));
		}
		key.interestOps(SelectionKey.OP_READ);
		
	}
	
	private void doEchoJob(SelectionKey key, byte[] data) {
		SocketChannel sc = (SocketChannel) key.channel();
		List<byte[]> channelData = keepDataTrack.get(sc);
		channelData.add(data);
		key.interestOps(SelectionKey.OP_WRITE);
	}

	public static void main(String[] args) {
		NonBlockingServer s = new NonBlockingServer();
		s.startEchoServer();
	}

}
