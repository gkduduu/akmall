package com.ak.android.akmall.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

public class Cookies extends HashMap<String, String> {

	private static final long serialVersionUID = 4925260533722876773L;

	private static final char SEMICOLON = ';';
	private static final char EQUAL = '=';
	private static final char WHITE_SPACE = ' ';

	/**
	 * @param only support Cookie, Unsupport Set-Cookie
	 */
	public Cookies(String rawCookie) {

		if (rawCookie == null) {
			return;
		}

		parse(rawCookie);
	}

	private void parse(final String rawCookie) {

		int index = 0;
		int length = rawCookie.length();

		String name;
		String value;
		int semicolonIndex;
		int equalIndex;

		while(index >= 0 && index < length) {

			// skip white space and special char
			char head = rawCookie.charAt(index);
			if(head == WHITE_SPACE
					|| head == SEMICOLON
					|| head == EQUAL) {
				index++;
				continue;
			}

			semicolonIndex = rawCookie.indexOf(SEMICOLON, index);
			equalIndex = rawCookie.indexOf(EQUAL, index);

			// 1. "foo" or "foo;"
			// 2. "foo; user=..."
			// 3. "foo=1;user="
			if(semicolonIndex == -1) {
				semicolonIndex = length;
			}

			if(semicolonIndex < equalIndex
					||  equalIndex == -1) {

				name = rawCookie.substring(index, semicolonIndex);
				value = "";

			} else {
				name = rawCookie.substring(index, equalIndex);
				value = rawCookie.substring(equalIndex + 1, semicolonIndex);
			}

			index = semicolonIndex;

			this.put(name, value);
		}
	}

	public String getValue(String key) {
		String value = this.get(key);
		return (value != null ? value : "");
	}

	public List<Cookie> toList(String domain) {

		List<Cookie> list = new ArrayList<Cookie>();

		Set<String> keys = keySet();
		BasicClientCookie cookie;
		for (String key : keys) {
			cookie = new BasicClientCookie(key, getValue(key));
			cookie.setDomain(domain);
			list.add(cookie);
		}

		return list;
	}
}
