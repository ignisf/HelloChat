package me.petko.hellochat;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Utils {
	public static Optional<Cookie> getCookie(String cookieName, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null) {
			cookies = new Cookie[] {};
		}
		return Arrays.stream(cookies)
				.filter(cookie -> cookie.getName().equals(cookieName))
				.findFirst();
	}
}
