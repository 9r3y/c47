package com.y3r9.c47.dog.demo.ws;

import org.apache.cxf.tools.wsdlto.WSDLToJava;

public class WSCodeGenerator {
	
	public static String NAME_SPACE = "http://ttdev.com/ss";
	public static String FOLDER_GENERATED_IN = "src";
	public static String PACKAGE = "com.ws.client";
	public static String WSDL_FILE_PATH = "src/com/resource/SimpleService.wsdl";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		createClient();
		
	}
	
	public static void createClient() {
		WSDLToJava.main(new String[] {
				"-client",
				"-d", FOLDER_GENERATED_IN,
				"-p", NAME_SPACE + "=" + PACKAGE,
				WSDL_FILE_PATH });
		System.out.println("Done!");
	}
	
	public static void createServer() {
		WSDLToJava.main(new String[] {
				"-server",
				"-d", FOLDER_GENERATED_IN,
				"-p", NAME_SPACE + "=" + PACKAGE,
				WSDL_FILE_PATH });
		System.out.println("Done!");
	}

}
