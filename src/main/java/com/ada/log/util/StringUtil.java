/*
 * @(#)com.vvideo.util.StringUtil	- Aug 10, 2010 6:20:48 PM
 *
 * Copyright        
 * type 			StringUtil.java
 * Author			
 */
package com.ada.log.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

//	public static Blowfish cipher = new Blowfish("anchau243AFngs");

	private static String[] FALSE_STRINGS = { "false", "null", "nul", "off",
			"no", "n" };

	public static String MD5="MD5";
	// MD5 密钥
	public static String passCode = "yo2kUyhndwwq";
	
	final static String regexValue="^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
	
	// 字符中只能包含数字、字母、_
    public static final String usernameRegexValue = "^[A-Za-z0-9_]+$";
    
    public static final String[] NumArr={"0","1","2","3","4","5","6","7","8","9","１","２","３","４","５","６","７","８","９","０",
    	"①","②","③","④","⑤","⑥","⑦","⑧","⑨","⑩","⑴","⑵","⑶","⑷","⑸","⑹","⑺","⑻","⑼","⑽","⒈","⒉","⒊","⒋","⒌","⒍","⒎",
    	"⒏","⒐","⒑","❶","❷","❸","❹","❺","❻","❼","❽","❾","❿","㊀","㊁","㊂","㊃","㊄","㊅","㊆","㊇","㊈","㈠","㈡","㈢","㈣","㈤","㈥",
    	"㈦","㈧","㈨","Ⅱ","Ⅲ","Ⅳ","Ⅴ","Ⅵ","Ⅶ","Ⅷ","Ⅸ","Ⅹ","Ⅺ","Ⅻ","I","II","III","IV","V","VI","VII","VIII","IX","〇",
    	"ⅰ","ⅱ","ⅲ","ⅳ","ⅴ","ⅵ","ⅶ","ⅷ","ⅸ"};
    

	/**
	 * str是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return str != null && str.trim().length() > 0;
	}

	/**
	 * 判断是否为中文字符.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isAllowedChineseName(String str) {
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.UnicodeBlock.of(chars[i]) == Character.UnicodeBlock.BASIC_LATIN) {
				if (!Character.isLetterOrDigit(chars[i]) && chars[i] != '_') {
					return false;
				}
			} else {
				if (Character.UnicodeBlock.of(chars[i]) == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isChineseName(String str) {
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!isChinese(chars[i])) {
				return false;
			}
		}
		return true;
	}

	public static boolean isChinese(char c) {
		if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
			return true;
		} else {

		}
		return false;
	}

	public static String indexOfLast(String str, String key) {
		String arr[] = str.split(key);
		if (arr != null && arr.length > 1) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < arr.length - 2; i++) {
				if (i > 0) {
					sb.append(key);
				}
				sb.append(arr[i]);
			}

		}
		return str;
	}

	/**
	 * str加密
	 * 
	 * @param str
	 * @param method
	 * @return
	 */
	public static String digest(String str, String method) {
		if (str == null) {
			return null;
		}
		if (method.equals(MD5)) {
			return createMD5(str);
		} else {
			return createMYSQL(str);
		}
	}

	// public static String changeCharset(String str) {
	// return changeCharset(str, Config.getSysEncoding(), "UTF-8");
	//
	// }

	public static String changeCharset(String str, String charset1,
			String charset2) {
		if (isEmpty(str)) {
			return "";
		}
		try {
			return new String(str.getBytes(charset1), charset2);
		} catch (UnsupportedEncodingException e) {
			return str;
		}

	}

	private static final String toHex(byte[] hash) {
		StringBuilder buf = new StringBuilder(hash.length * 2);
		int i;

		for (i = 0; i < hash.length; i++) {
			if (((int) hash[i] & 0xff) < 0x10) {
				buf.append("0");
			}

			buf.append(Long.toString((int) hash[i] & 0xff, 16));
		}
		String ret = buf.toString();
		buf = null;
		return ret;
	}

	private static String createMD5(String passwordSource) {
		String Password;
		byte[] PasswordByte;
		Password = passwordSource;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Password.getBytes());
			PasswordByte = md.digest();
		} catch (Exception e) {
			return passwordSource;
		}

		String ReturnPassword = toHex(PasswordByte);

		return ReturnPassword;
	}

	private static String createMYSQL(String passwordSource) {
		String Password;
		long resultLong1;
		long resultLong2;
		long tempLong1 = 1345345333;
		long tempLong2 = 0x12345671;
		long tempLong3;
		long addLong = 7;
		char charOne;
		Password = passwordSource;

		for (int i = 0; i < Password.length(); i++) {
			charOne = Password.charAt(i);

			if ((charOne == ' ') || (charOne == '\t')) {
				continue;
			}

			tempLong3 = (long) charOne;
			tempLong1 ^= (((tempLong1 & 63) + addLong) * tempLong3)
					+ (tempLong1 << 8);
			tempLong2 += (tempLong2 << 8) ^ tempLong1;
			addLong += tempLong3;
		}

		resultLong1 = tempLong1 & (((long) 1 << 31) - (long) 1);
		resultLong2 = tempLong2 & (((long) 1 << 31) - (long) 1);

		String ReturnPassword;
		String ReturnPassword1;
		int j = 0;
		ReturnPassword = Long.toHexString(resultLong1);
		j = ReturnPassword.length();

		for (int i = 0; i < (8 - j); i++) {
			ReturnPassword = "0" + ReturnPassword;
		}

		ReturnPassword1 = Long.toHexString(resultLong2);
		j = ReturnPassword1.length();

		for (int i = 0; i < (8 - j); i++) {
			ReturnPassword1 = "0" + ReturnPassword1;
		}

		ReturnPassword = ReturnPassword + ReturnPassword1;

		return ReturnPassword;
	}

	public static String[] parseEnglishTerms(String str) {
		List<String> terms = new ArrayList<String>(5);
		char[] chars = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		int i = 0;
		char c = 0;
		boolean wordsBegins = false;
		boolean wordsEnds = false;
		while (i < chars.length) {
			c = chars[i];
			if (!Character.isLetterOrDigit(c) || Character.isSpaceChar(c)) {
				if (wordsBegins) {
					wordsEnds = true;
				}
			} else {
				wordsBegins = true;
				sb.append(c);
			}

			if (wordsEnds) {
				terms.add(sb.toString());
				sb.setLength(0);
				wordsEnds = false;
				wordsBegins = false;
			}
			i++;
		}
		if (sb.length() > 0) {
			terms.add(sb.toString());
		}
		return (String[]) terms.toArray(new String[terms.size()]);
	}

	public static final String highlightEnglishWords(String str, String[] words) {
		String tmp = null;
		try {
			tmp = highlightEnglishWordsInHtml(str, words);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (tmp == null) {
			return str;
		}
		return tmp;
	}

	public static final String highlightWords(String str, String[] words) {
		String tmp = null;
		try {
			tmp = highlightWordsInHtml(str, words);
		} catch (Exception ex) {
		}
		if (tmp == null) {
			return str;
		}
		return tmp;
	}

	final static String[] startHighlight = new String[] {
			"<span class='highlight1'>", "<span class='highlight2'>",
			"<span class='highlight3'>" };

	final static String endHighlight = "</span>";

	private static final String highlightEnglishWordsInHtml(String string,
			String[] words) throws Exception {
		if (string == null || words == null) {
			return null;
		}
		char[] source = null;
		StringBuilder sb = new StringBuilder(string);
		for (int wk = 0; wk < words.length; wk++) {
			if (words[wk] == null) {
				continue;
			}
			source = sb.toString().toCharArray();
			sb.setLength(0);
			int sourceOffset = 0;
			int sourceCount = source.length;
			char[] target = words[wk].toLowerCase().toCharArray();
			int targetOffset = 0;
			int targetCount = target.length;
			int fromIndex = 0;
			if (fromIndex >= sourceCount) {
				continue;
			}
			if (targetCount == 0) {
				continue;
			}
			char first = target[targetOffset];
			int i = sourceOffset + fromIndex;
			int max = sourceOffset + (sourceCount - targetCount);

			int sbPos = 0;
			int tags1 = 0;
			char c = 0;
			startSearchForFirstChar: while (true) {
				while (i <= max) {
					c = source[i];
					switch (c) {
					case '<':
						tags1++;
						break;
					case '>':
						if (tags1 > 0)
							tags1--;
						break;
					case ',':
					case '\n':
						tags1 = 0;
						break;
					default:
					}
					if (Character.toLowerCase(c) == first) {
						break;
					}
					i++;
				}

				if (i > max) {
					break;
				}

				if (tags1 != 0
						|| (i > 0 && Character.isLetterOrDigit(source[i - 1]))) {
					i++;
					continue startSearchForFirstChar;
				}

				/*
				 * Found first character, now look at the rest of v2
				 */
				int j = i + 1;
				int end = j + targetCount - 1;
				int k = targetOffset + 1;
				while (j < end) {
					if (Character.toLowerCase(source[j++]) != target[k++]) {
						i++;
						/*
						 * Look for str's first char again.
						 */
						continue startSearchForFirstChar;
					}
				}
				if (end < source.length - 1
						&& Character.isLetterOrDigit(source[end])) {
					i++;
					continue startSearchForFirstChar;
				}
				int pos = i - sourceOffset; /*
											 * Found whole string.
											 */
				sb.append(source, sbPos, pos - sbPos);
				sb.append(startHighlight[wk % startHighlight.length]);
				sb.append(source, pos, targetCount);
				sb.append(endHighlight);
				sbPos = pos + targetCount;
				i += targetCount;
			}
			sb.append(source, sbPos, sourceCount - sbPos);
		}
		return sb.toString();
	}

	private static final String highlightWordsInHtml(String string,
			String[] words) throws Exception {
		if (string == null || words == null) {
			return null;
		}
		char[] source = null;
		StringBuilder sb = new StringBuilder(string);
		for (int wk = 0; wk < words.length; wk++) {
			if (words[wk] == null) {
				continue;
			}
			source = sb.toString().toCharArray();
			sb.setLength(0);
			int sourceOffset = 0;
			int sourceCount = source.length;
			char[] target = words[wk].toLowerCase().toCharArray();
			int targetOffset = 0;
			int targetCount = target.length;
			int fromIndex = 0;
			if (fromIndex >= sourceCount) {
				continue;
			}
			if (targetCount == 0) {
				continue;
			}
			char first = target[targetOffset];
			int i = sourceOffset + fromIndex;
			int max = sourceOffset + (sourceCount - targetCount);

			int sbPos = 0;
			int tags1 = 0;
			char c = 0;
			startSearchForFirstChar: while (true) {
				while (i <= max) {
					c = source[i];
					switch (c) {
					case '<':
						tags1++;
						break;
					case '>':
						if (tags1 > 0)
							tags1--;
						break;
					case ',':
					case '\n':
						tags1 = 0;
						break;
					default:
					}
					if (Character.toLowerCase(c) == first) {
						break;
					}
					i++;
				}
				if (i > max) {
					break;
				}

				if (tags1 != 0) {
					i++;
					continue startSearchForFirstChar;
				}

				/*
				 * Found first character, now look at the rest of v2
				 */
				int j = i + 1;
				int end = j + targetCount - 1;
				int k = targetOffset + 1;
				while (j < end) {
					if (Character.toLowerCase(source[j++]) != target[k++]) {
						i++;
						/*
						 * Look for str's first char again.
						 */
						continue startSearchForFirstChar;
					}
				}
				int pos = i - sourceOffset; /*
											 * Found whole string.
											 */
				sb.append(source, sbPos, pos - sbPos);
				sb.append(startHighlight[wk % startHighlight.length]);
				sb.append(source, pos, targetCount);
				sb.append(endHighlight);
				sbPos = pos + targetCount;
				i += targetCount;
			}
			sb.append(source, sbPos, sourceCount - sbPos);
		}
		return sb.toString();
	}

	/**
	 * arr数组转String，用逗号隔开
	 * 
	 * @param arr
	 * @return
	 */
	public static String arr2str(String[] arr) {
		return arr2str(arr, ",");
	}

	public static int[] arr2int(String[] sChecked) {
		if (sChecked == null || sChecked.length <= 0) {
			return null;
		}

		int[] iChecked = new int[sChecked.length];

		for (int i = 0; i < sChecked.length; i++) {
			iChecked[i] = Integer.parseInt(sChecked[i]);
		}

		return iChecked;
	}

	public static String arr2str(String[] arr, String key) {
		if (arr == null) {
			return ("");
		}

		if (arr.length == 0) {
			return ("");
		}

		int length = arr.length;
		StringBuilder s = new StringBuilder();

		if (arr[0] != null && arr[0].length() > 0) {
			s.append(encode(arr[0]));
		} else {
			s.append("");

		}
		for (int i = 1; i < length; i++) {
			s.append(key);

			if (arr[i] != null && arr[i].length() > 0) {
				s.append(encode(arr[i]));
			} else {
				s.append("");
			}
		}

		return (s.toString());
	}

	public static int[] str2intarr(String str) {
		if (str == null || str.length() < 1) {
			return new int[0];
		}

		StringTokenizer st = new StringTokenizer(str, ",");
		int[] new_int = new int[st.countTokens()];
		int i = 0;

		while (st.hasMoreTokens()) {
			String tmp = (String) st.nextToken();

			try {
				new_int[i++] = Integer.parseInt(tmp);
			} catch (Exception e) {
			}

		}

		return new_int;
	}

	public static long[] str2longarr(String str) {
		if (str == null || str.length() < 1) {
			return new long[0];
		}

		StringTokenizer st = new StringTokenizer(str, ",");
		long[] new_long = new long[st.countTokens()];
		int i = 0;

		while (st.hasMoreTokens()) {
			String tmp = (String) st.nextToken();

			try {
				new_long[i++] = Long.parseLong(tmp);
			} catch (Exception e) {
			}

		}

		return new_long;
	}

	/**
	 * 将逗号隔开的数组转换成数组
	 * 
	 * @param str
	 * @return
	 */
	public static String[] str2arr(String str) {
		return str2arr(str, ',');
	}

	public static String[] str2arr(String str, char key) {
		if (str == null || str.length() < 1) {
			return new String[0];
		}

		int counter = 0;
		int pos = -1;
		int maxPosition = str.length() - 1;
		while (pos < maxPosition) {
			pos++;
			if (str.charAt(pos) == key) {
				if (pos == 0 || str.charAt(pos - 1) != '\\') {
					counter++;
				}
			}
		}
		String[] new_str = new String[counter + 1];

		int cur = -1;
		int i = 0;
		pos = -1;
		boolean should_decode = false;
		while (pos < maxPosition) {
			pos++;
			if (str.charAt(pos) == key) {
				if (pos != 0 && str.charAt(pos - 1) == '\\') {
					// skip
					should_decode = true;
				} else {
					if (should_decode) {
						new_str[i++] = decode(str.substring(cur + 1, pos));
						should_decode = false;
					} else {
						new_str[i++] = str.substring(cur + 1, pos);
					}
					cur = pos;
				}
			}
		}
		if (should_decode) {
			new_str[counter] = decode(str.substring(cur + 1));
		} else {
			new_str[counter] = str.substring(cur + 1);
		}
		return new_str;
	}

	/**
	 * str转换成Hashtable，以分号隔开，等号前字符为key，等号后字符为value
	 * 
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Hashtable str2hash(String str) {
		return str2hash(str, ',', "=");
	}

	@SuppressWarnings("unchecked")
	public static Hashtable str2hash(String str, char k, String indexOf) {
		Hashtable hash = new Hashtable();

		if (str == null || str.length() < 1) {
			return hash;
		}

		String[] pairs = str2arr(str, k);

		for (int i = 0; i < pairs.length; i++) {
			try {
				int index = pairs[i].indexOf(indexOf);
				String key1 = decode(pairs[i].substring(0, index));
				if (index == pairs[i].length() || index < 0) {
					hash.put(key1, "");
				} else {
					hash.put(key1, pairs[i].substring(index + 1));
				}
			} catch (Exception e) {
			}

		}

		return hash;
	}

	@SuppressWarnings("unchecked")
	public static String hash2str(Map hash) {
		return hash2str(hash, ",", "=");
	}

	@SuppressWarnings("unchecked")
	public static String hash2str(Map hash, String k, String indexOf) {
		if (hash == null) {
			return "";
		}

		int max = hash.size() - 1;
		StringBuilder buf = new StringBuilder();
		Iterator it = hash.entrySet().iterator();

		for (int i = 0; i <= max; i++) {
			Map.Entry e = (Map.Entry) (it.next());
			buf.append(encode((String) e.getKey()) + indexOf
					+ encode((String) e.getValue()));

			if (i < max) {
				buf.append(k);
			}
		}

		return buf.toString();
	}

	/**
	 * 字符串转换成boolean. 转换规则如下: 如果字符串看起来像一个整数, 且整数值等于0, 则为false, 否则true;
	 * 如果字符串为空串或null, 则返回指定默认值; 如果字符串为下列值之一(大小写不敏感): "false", "null", "nul",
	 * "off", "no", "n" 则为false, 否则为true.
	 * 
	 * @param String
	 *            字符串
	 * @param Boolean
	 *            默认值
	 * @return Boolean
	 */
	public static Boolean asBoolean(String str, Boolean defaultValue) {
		try {
			str = str.trim();
			return Integer.decode(str).intValue() != 0 ? Boolean.TRUE
					: Boolean.FALSE;
		} catch (NullPointerException e) {
			return defaultValue;
		} catch (NumberFormatException e) {
			if (str.equals("")) {
				return defaultValue;
			}
			for (int i = 0; i < FALSE_STRINGS.length; i++) {
				if (str.equalsIgnoreCase(FALSE_STRINGS[i])) {
					return Boolean.FALSE;
				}
			}
			return Boolean.TRUE;
		}
	}

	/**
	 * 字符串转换成boolean. 转换规则如下: 如果字符串看起来像一个整数, 且整数值等于0, 则为false, 否则true;
	 * 如果字符串为空串或null, 则返回指定默认值; 如果字符串为下列值之一(大小写不敏感): "false", "null", "nul",
	 * "off", "no", "n" 则为false, 否则为true.
	 * 
	 * @param String
	 *            字符串
	 * @param boolean 默认值
	 * @return boolean
	 */
	public static boolean asBoolean(String str, boolean defaultValue) {
		try {
			str = str.trim();
			return Integer.decode(str).intValue() != 0;
		} catch (NullPointerException e) {
			return defaultValue;
		} catch (NumberFormatException e) {
			if (str.equals("")) {
				return defaultValue;
			}
			for (int i = 0; i < FALSE_STRINGS.length; i++) {
				if (str.equalsIgnoreCase(FALSE_STRINGS[i])) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * 字符串转换成long. 如果字符串为null, 字符串不是整数或整数超过取值范围, 则返回0.
	 * 
	 * @param String
	 *            字符串
	 * @return long
	 */
	public static long asLong(String str) {
		return asLong(str, 0L);
	}

	/**
	 * 字符串转换成long. 如果字符串为null, 字符串不是整数或整数超过取值范围, 则返回指定默认值.
	 * 
	 * @param String
	 *            字符串
	 * @param Long
	 *            默认值
	 * @return Long
	 */
	public static Long asLong(String str, Long defaultValue) {
		try {
			return Long.decode(str.trim());
		} catch (NullPointerException e) {
			return defaultValue;
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static float asFloat(String str) {
		try {
			return Float.parseFloat(str.trim());
		} catch (NullPointerException e) {
			return 0.0f;
		} catch (NumberFormatException e) {
			return 0.0f;
		}
	}

	/**
	 * 字符串转换成long. 如果字符串为null, 字符串不是整数或整数超过取值范围, 则返回指定默认值.
	 * 
	 * @param String
	 *            字符串
	 * @param long 默认值
	 * @return long
	 */
	public static long asLong(String str, long defaultValue) {
		try {
			return Long.decode(str.trim()).longValue();
		} catch (NullPointerException e) {
			return defaultValue;
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static Integer asInteger(String str) {
		return asInteger(str, 0);
	}

	/**
	 * 字符串转换成int. 如果字符串为null, 字符串不是整数或整数超过取值范围, 则返回指定默认值.
	 * 
	 * @param String
	 *            字符串
	 * @param Integer
	 *            默认值
	 * @return Integer
	 */
	public static Integer asInteger(String str, Integer defaultValue) {
		try {
			return Integer.decode(str.trim());
		} catch (NullPointerException e) {
			return defaultValue;
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * 字符串转换成int. 如果字符串为null, 字符串不是整数或整数超过取值范围, 则返回指定默认值.
	 * 
	 * @param String
	 *            字符串
	 * @param int 默认值
	 * @return int
	 */
	public static int asInteger(String str, int defaultValue) {
		try {
			return Integer.decode(str.trim()).intValue();
		} catch (NullPointerException e) {
			return defaultValue;
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static String encode(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char[] cs = s.toCharArray();
		StringBuilder sb = new StringBuilder(cs.length + 2);
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] == ',') {
				sb.append('\\');
			}
			sb.append(cs[i]);
		}
		return sb.toString();
	}

	public static String encodeURL(String url, String encoding) {
		try {
			return URLEncoder.encode(url, encoding);
		} catch (Exception ex) {
			return url == null ? "" : url;
		}
	}

	public static String decodeURL(String str, String encoding) {
		try {
			return URLDecoder.decode(str, encoding);
		} catch (Exception ex) {
			return str == null ? "" : str;
		}
	}

	public static String decode(String s) {
		if (s == null || s.length() < 2) {
			return s;
		}
		char[] cs = s.toCharArray();
		StringBuilder sb = new StringBuilder(cs.length);

		for (int i = 0, n = cs.length; i < n; i++) {
			if (!(cs[i] == '\\' && i < n - 1 && cs[i + 1] == ',')) {
				sb.append(cs[i]);
			}
		}
		return sb.toString();
	}

	public static String html2txt(String html) {
		if (html == null) {
			return "";
		}
		char[] chars = html.toCharArray();
		StringBuilder sb = new StringBuilder();
		int n = 0;
		int i = 0;
		char c = 0;
		int tags1 = 0;
		int tags2 = 0;
		while (i < html.length()) {
			c = chars[i++];
			switch (c) {
			case '<':
				tags1++;
				continue;
			case '>':
				if (tags1 > 0)
					tags1--;
				continue;
			case '[':
				tags2++;
				continue;
			case ']':
				if (tags2 > 0)
					tags2--;
				continue;
			case ',':
			case '\n':
				tags1 = 0;
				tags2 = 0;
				continue;
			default:
			}
			if (tags1 == 0 && tags2 == 0) {
				sb.append(c);
				n++;
			}
		}
		return sb.toString();
	}

	public static String summarize(String str, int len) {
		if (str == null) {
			return "";
		}
		str = str.trim();
		char[] chars = str.toCharArray();
		len = Math.min(chars.length, len);
		StringBuilder sb = new StringBuilder(len);
		int n = 0;
		int i = 0;
		char c = 0;
		int tags1 = 0;
		int tags2 = 0;
		while (n < len && i < str.length()) {
			c = chars[i++];
			switch (c) {
			case '<':
				tags1++;
				continue;
			case '>':
				if (tags1 > 0)
					tags1--;
				continue;
			case '[':
				tags2++;
				continue;
			case ']':
				if (tags2 > 0)
					tags2--;
				continue;
			case ',':
			case '\n':
				tags1 = 0;
				tags2 = 0;
				continue;
			default:
			}
			if (tags1 == 0 && tags2 == 0) {
				sb.append(c);
				n++;
			}
		}
		if (sb.length() < str.length()) {
			sb.append(" ...");
		}
		return sb.toString();
	}

	public static String[] parse(String pathInfo, int level) {
		String[] values = new String[level];
		int pos1 = pathInfo.lastIndexOf(".") - 1;
		if (pos1 < pathInfo.lastIndexOf('/')) {
			pos1 = pathInfo.length() - 1;
		}
		for (int i = level - 1; i >= 0; i--) {
			int tmp = pathInfo.lastIndexOf("/", pos1);
			values[i] = pathInfo.substring(tmp + 1, pos1 + 1);
			pos1 = tmp - 1;
		}
		return values;
	}

	public static String toUpperCase(String key) {
		StringBuilder sb = new StringBuilder();
		try {
			for (int j = 0; j < key.length(); j++) {
				char c = key.charAt(j);
				if (c == '_') {
					sb.append(Character.toUpperCase(key.charAt(++j)));
				} else if (j == 0) {
					sb.append(Character.toUpperCase(c));
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		} catch (Exception ex) {
			return key;
		}
	}

	public static boolean isNumber(String str) {
		if (str == null) {
			return false;
		}
		char[] asc = str.toCharArray();
		for (int i = 0; i < asc.length; i++) {
			if (!Character.isDigit(asc[i])) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNumber1(String str) {
		if (str == null) {
			return false;
		}
		if (str.startsWith("-")) {
			str = str.substring(1);
		}
		char[] asc = str.toCharArray();
		for (int i = 0; i < asc.length; i++) {
			if (!Character.isDigit(asc[i])) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isIp(String str) {
		if (str == null) {
			return false;
		}
		str=str.replaceAll(".", "");
		char[] asc = str.toCharArray();
		for (int i = 0; i < asc.length; i++) {
			if (!Character.isDigit(asc[i])) {
				return false;
			}
		}
		return true;
	}

	private static Random randGen = new Random();

	/**
	 * Array of numbers and letters of mixed case. Numbers appear in the list
	 * twice so that there is a more equal chance that a number will be picked.
	 * We can use the array to get a random number or letter by picking a random
	 * array index.
	 */
	private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
			+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

	public static final String randomString(int length) {
		if (length < 1) {
			return null;
		}
		// Create a char buffer to put random letters and numbers in.
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	public static final int randomInt(int length) {
		if (length < 1 && length > 9) {
			throw new java.lang.ArithmeticException(
					"the length of random int must be between 0 and 9");
		}
		int sum = 0;
		int n = 1;
		int r = 0;
		for (int i = 1; i < length; i++) {
			r = randGen.nextInt(10);
			sum += r * n;
			n = n * 10;
		}
		r = 1 + randGen.nextInt(9);
		sum += r * n;
		return sum;
	}
	
    /**
     * 密码强度
     * Z = 字母 S = 数字 T = 特殊字符
     * @return 
     */
    public static boolean checkPasswords(String passwordStr) {
        String regexZ = "\\d*";
        String regexS = "[a-zA-Z]+";
        String regexT = "\\W+$";
//      String regexZT = "\\D*";
//      String regexST = "[\\d\\W]*";
//      String regexZS = "\\w*";
//      String regexZST = "[\\w\\W]*";

        if (passwordStr.matches(regexZ)) {
            return false;
        }
        if (passwordStr.matches(regexS)) {
            return false;
        }
        if (passwordStr.matches(regexT)) {
            return false;
        }
        
        return true;
    }

	public static boolean isEmail(String value) {
		if (value == null) {
			value = "";
		}
		Pattern p = Pattern.compile(regexValue);
		Matcher m = p.matcher(value);
		return m.find();
	}
	
	/**
     * 
     *〈一句话功能简述〉对PHP的json_encode进行解码 <br/>
     * 〈功能详细描述〉str中原文如果是带数字的无法解析出来，只会解析中文的部分
     * @param str
     * @return
     * @see  [类、类#方法、类#成员]
     */
    public static String unescapeUnicodeI(String str)
    {
        // 去除所有双引号
        str = str.replaceAll("\"", "");
        
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile("\\\\u([0-9a-zA-Z]{4})").matcher(str);
        while(m.find())
        {
            m.appendReplacement(sb, Character.toString((char)Integer.parseInt(m.group(1), 16)));
        }
        m.appendTail(sb);
        
        return sb.toString();
    }
    
    // 判断一个字符串是否含有数字  
    public static boolean hasNumber(String content) {  
        boolean flag = false;  
        Pattern p = Pattern.compile(".*\\d+.*");  
        Matcher m = p.matcher(content);  
        if (m.matches()){
            flag = true;
        } 
        return flag;  
    }  
    
    /**
     * 
     *〈一句话功能简述〉字符中半角转全角 <br/>
     * 〈功能详细描述〉
     * @param str
     * @return
     * @see  [类、类#方法、类#成员]
     */
    public static String halfSingle2All(String str)
    {
        if(isEmpty(str))
        {
            return str;
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            switch (c)
            {
                case '>':
                    sb.append("＞");// 转义大于号
                    break;
                case '<':
                    sb.append("＜");// 转义小于号
                    break;
                case '\'':
                    sb.append("＇");// 转义单引号
                    break;
                case '\"':
                    sb.append("＂");// 转义双引号
                    break;
                case '&':
                    sb.append("＆");// 转义&
                    break;
                case '#':
                    sb.append("＃");// 转义#
                    break;
                case ';':
                    sb.append("；");// 转义;
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        
        return sb.toString();
    }
    
    /**
     * 数字 每3位以逗号分隔
     *〈一句话功能简述〉 <br/>
     * 〈功能详细描述〉
     * @param data 要分隔的数据
     * @return
     * @see  [类、类#方法、类#成员]
     */
    public static String formatTosepara(int data) 
    {
        try
        {
            DecimalFormat df = new DecimalFormat("#,###");
            
            return df.format(data);
        }
        catch(Exception e)
        {
        }
        
        return "";
    }
    
    /**
     * 
     *〈一句话功能简述〉字符中是否只包含匹配的内容 <br/>
     * 〈功能详细描述〉
     * @param username
     * @return
     * @see  [类、类#方法、类#成员]
     */
    public static boolean isValiUserName(String username)
    {
        Pattern p = Pattern.compile(usernameRegexValue);
        Matcher m = p.matcher(username);
        if(!m.find())
        {
            return false;
        }
        return true;
    }
    
    /**
     * 替换数字
     * @param content
     * @return
     */
    public static String replaceNum(String content) {  
    	char[] c = content.toCharArray();
    	String rs = "";
    	for(int i =0; i < c.length; i++){
            char s = c[i];
            rs = String.valueOf(s);
            
            for (int j = 0; j < NumArr.length; j++) {
    				if(rs.equals(NumArr[j])){
    					content=content.replaceAll(rs, "*");
    					break;
    			}
    		}
         }
	    return content;  
    }  
}
