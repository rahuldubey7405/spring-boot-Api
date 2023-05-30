package com.springboot.tools;

import java.io.BufferedReader;

import jakarta.servlet.http.HttpServletRequest;

public class MiscMethods {
	public static String GetHTTPRequestBody(HttpServletRequest request) {
		String requestBody = "";

		StringBuilder builder = new StringBuilder();

		try (BufferedReader in = request.getReader()) {
			char[] buf = new char[4096];
			for (int len; (len = in.read(buf)) > 0;) {
				builder.append(buf, 0, len);
			}
		} catch (Exception ex) {
			System.out.println("Unable to get HTTP Request Body");
		}
		requestBody = builder.toString();

		return requestBody;
	}

}
