/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ak.android.akplaza.common;



import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;


public class StringUtil implements java.io.Serializable
{


	private static final long serialVersionUID = 1L;

	public static String[] stringToArrayString(int ipx, String a)
	{
		int lng = a.length();
		int iNum = lng / ipx;
		int mNum = lng % ipx;
		int sIpx = 0;
		if (mNum != 0) iNum = iNum + 1;
		String[] b = new String[iNum];
		for (int i = 0; i < iNum; i++)
		{
			if (iNum != (i + 1))
			{
				b[i] = a.substring(sIpx, sIpx + ipx);
			}
			else
			{
				b[i] = a.substring(sIpx, lng);
			}
			sIpx = sIpx + ipx;
		}
		return b;
	}

	public static String arrayToString(String[] s)
	{
		String str = new String();

		if (s != null)
		{
			for (int i = 0; i < s.length; i++)
			{
				if (i != s.length - 1) str += s[i] + ",";
				else str += s[i];
			}
		}
		return str;
	}



	public static String arrayToString(String[] s, String deli)
	{
		String str = new String();

		if (s != null)
		{
			for (int i = 0; i < s.length; i++)
			{
				if (i != s.length - 1) str += s[i] + deli;
				else str += s[i];
			}
		}
		return str;
	}


	public static String arrayToStringWithAmp(String[] s)
	{
		String str = new String();

		if (s != null)
		{
			for (int i = 0; i < s.length; i++)
			{
				if (i != s.length - 1) str += "'" + s[i] + "',";
				else str += "'" + s[i] + "'";
			}
		}
		return str;
	}

	public static String vectorToStringWithAmp(Vector s)
	{
		String str = new String();
		if (s != null)
		{
			for (int i = 0; i < s.size(); i++)
			{
				if (i != s.size() - 1) str += "'" + (String) s.get(i) + "',";
				else str += "'" + (String) s.get(i) + "'";
			}
		}
		return str;
	}

	public static String arrayListToStringWithAmp(ArrayList a)
	{
		String[] s = new String[a.size()];
		if (a != null)
		{
			for (int i = 0; i < a.size(); i++)
			{
				s[i] = (String) a.get(i);
			}
		}

		String str = new String();

		if (s != null)
		{
			for (int i = 0; i < s.length; i++)
			{
				if (i != s.length - 1) str += "'" + s[i] + "',";
				else str += "'" + s[i] + "'";
			}
		}
		return str;
	}

	public static String arrayListToString(ArrayList a)
	{
		String[] s = new String[a.size()];
		if (a != null)
		{
			for (int i = 0; i < a.size(); i++)
			{
				s[i] = (String) a.get(i);
			}
		}

		String str = new String();

		if (s != null)
		{
			for (int i = 0; i < s.length; i++)
			{
				if (i != s.length - 1) str += s[i] + ",";
				else str += s[i];
			}
		}
		return str;
	}


	public static String[] stringToArray(String s, String t)
	{
		if (isNull(s)) return null;

		StringTokenizer st = new StringTokenizer(s, t);
		int size = st.countTokens();
		if (size <= 0) return null;

		String[] result = new String[size];
		for (int i = 0; i < size && st.hasMoreTokens(); i++)
		{
			result[i] = st.nextToken();
		}
		return result;
	}

	public static String stringToString(String s, String t)
	{
		if (isNull(s)) return null;

		StringTokenizer st = new StringTokenizer(s, t);
		int size = st.countTokens();
		if (size <= 0) return null;

		String result = "";
		for (int i = 0; i < size && st.hasMoreTokens(); i++)
		{
			result += st.nextToken();
		}
		return result;
	}

	public static int ArrayLength(String[][] a)
	{
		if (a == null) return 0;

		int aLen = a.length;

		if (aLen <= 0) aLen = 0;

		return aLen;
	}

	public static String nullTo(String source, String replace)
	{
		if (source != null)
		{
			return source;
		}
		else
		{
			return replace;
		}
	}


	public static String substring(String src, int start, int end)
	{
		if (src == null || "".equals(src) || start > src.length() || start > end || start < 0) return "";
		if (end > src.length()) end = src.length();

		return src.substring(start, end);
	}

	public static String lpad(String source, int n, String pad)
	{
		if (source == null) { return null; }
		if (source.length() >= n) { return source; }
		return pad.substring(0, n - source.length()) + source;
	}


	public static String getPriceFormat(int source)
	{
		String sPattern = "###,###,###,##0";
		DecimalFormat decimalformat = new DecimalFormat(sPattern);
		return decimalformat.format(source);
	}

	public static String getPriceFormat(long source)
	{
		String sPattern = "###,###,###,##0";
		DecimalFormat decimalformat = new DecimalFormat(sPattern);
		return decimalformat.format(source);
	}


	public static String getExt(String szTemp)
	{
		if (szTemp == null) return "";

		String fname = "";
		if (szTemp.indexOf(".") != -1)
		{
			fname = szTemp.substring(szTemp.lastIndexOf("."));
			return fname;
		}
		else
		{
			return "";
		}
	}

	public static boolean isImageFile(String fileName)
	{
		String ext = getExt(fileName);

		if (ext.equals(".gif") || ext.equals(".jpg") || ext.equals(".png") || ext.equals(".bmp"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static String[] getSplit(String str, String delimiter)
	{
		int cnt = 0;
		String[] arrayData = null;

		if (str == null) { return null; }

		if (str != null && !str.equals(""))
		{
			StringTokenizer token = new StringTokenizer(str, delimiter);
			arrayData = new String[token.countTokens()];
			while (token.hasMoreTokens())
			{
				arrayData[cnt++] = token.nextToken();
			}
		}
		return arrayData;
	}


	public static boolean isNotNull(String param)
	{
		if (param != null && "".equals(param) == false) return true;
		else return false;
	}

	public static boolean isNull(String param)
	{
		if (param == null || "".equals(param)) return true;
		else return false;
	}

	public static boolean isNull(String[] param)
	{
		if (param == null || param.length == 0) return true;
		for (int i = 0; i < param.length; i++)
		{
			if (isNotNull(param[i])) return false;
		}
		return true;
	}

	public static boolean isNotNull(String[] param)
	{
		return !isNull(param);
	}

	public static boolean isNull(Object[] param)
	{
		if (param == null || param.length == 0) return true;
		for (int i = 0; i < param.length; i++)
		{
			if (param[i] != null) return false;
		}

		return true;
	}

	public static boolean isNotNull(Object[] param)
	{
		return !isNull(param);
	}

	public static String checkNullStr(String str)
	{
		if (isNull(str))
		{
			str = "";
		}
		else
		{
			if (str.trim().equals(""))
			{
				str = "";
			}
		}
		return str;
	}

	public static String zeroPutStr(int len, String str)
	{
		for (int i = 0; i < len; i++)
			if (i >= str.length()) str = "0" + str;

		return str;
	}

	public static String getAlertMsg(String src)
	{
		src = ReplaceAll(src, "\n", "\\n");
		src = ReplaceAll(src, "\r", "");
		src = ReplaceAll(src, "\"", "'");

		return src;
	}


	public static String ReplaceAll(String buffer, String src, String dst)
	{
		if (buffer == null) return null;
		if (buffer.indexOf(src) < 0) return buffer;

		int bufLen = buffer.length();
		int srcLen = src.length();
		StringBuffer result = new StringBuffer();

		int i = 0;
		int j = 0;
		for (; i < bufLen;)
		{
			j = buffer.indexOf(src, j);
			if (j >= 0)
			{
				result.append(buffer.substring(i, j));
				result.append(dst);

				j += srcLen;
				i = j;
			}
			else break;
		}
		result.append(buffer.substring(i));
		return result.toString();
	}

	public static String calcStrBit(String root, String comp)
	{
		String retVal = "";
		char first = '0';

		for (int i = 0; i < 8; i++)
		{
			if ((first = root.charAt(i)) == '1')
			{
				if (first == comp.charAt(i)) retVal += "1";
				else retVal += "0";
			}
			else
			{
				retVal += "0";
			}
		}

		return retVal;
	}

	public static boolean equals(String src, String dst)
	{
		boolean srcNull = false;
		boolean dstNull = false;

		srcNull = isNull(src);
		dstNull = isNull(dst);

		if (srcNull && dstNull) return true;
		if (srcNull != dstNull) return false;

		return src.equals(dst);
	}

	public static boolean equalsIgnoreCase(String src, String dst)
	{
		if (src == null && dst != null) return false;
		return src.equalsIgnoreCase(dst);
	}

	public static String NVL(Object obj)
	{
		if (obj == null) return "";
		else return (String) obj;
	}

	public static String NVL(Object obj, String value)
	{
		if (obj == null) return value;
		else return String.valueOf(obj);
	}

	public static String NVL(String obj, String value)
	{
		if (obj == null) return value;
		else return String.valueOf(obj);
	}

	public static boolean checkString(String str)throws UnsupportedEncodingException
	{
		byte[] a = str.getBytes("UTF-8");
		boolean res = true;
		for (int i = 0; i < a.length; i++)
		{
			int num = a[i];
			if ((num == 95) || (num >= 97 && num <= 122) || (num >= 65 && num <= 90) || (num >= 48 && num <= 57))
			{

			}
			else
			{
				return false;
			}
		}
		return res;
	}

	public static String replace(String src, String from, String to)
	{
		if (src == null || src.length() == 0 || from == null || from.length() == 0 || to == null) { return src; }

		StringBuffer dBuff = new StringBuffer();
		int sIdx = 0; // start
		int eIdx = src.indexOf(from); // end

		int srcLen = from.length();
		while (eIdx >= sIdx)
		{
			dBuff.append(src.substring(sIdx, eIdx));
			dBuff.append(to);
			sIdx = eIdx + srcLen;
			eIdx = src.indexOf(from, sIdx);
		}

		dBuff.append(src.substring(sIdx));

		return dBuff.toString();
	}

	public static String fillLeftString(String src, char fill, int len)
	{
		StringBuffer ret;
		if (src == null)
		{
			ret = new StringBuffer(0);
			for (int i = 0; i < len; i++)
				ret.append(fill);
			return ret.toString();
		}

		byte[] bText = src.getBytes(); // Text
		int src_len = bText.length;

		if (src_len >= len) return src;

		ret = new StringBuffer(0);
		for (int i = 0; i < (len - src_len); i++)
			ret.append(fill);
		ret.append(src);
		return ret.toString();
	}


	public static String[] splits(String str, String com1)
	{

		String[] result = null;
		if (com1.length() < 1)
		{
			result = new String[1];
			result[0] = str;
			return result;
		}
		int count = 0;
		String temp = "";
		for (int i = 0; i < str.length(); i++)
		{
			int length = (i + com1.length());
			if ((i + com1.length()) > str.length()) length = str.length();
			String str1 = str.substring(i, length);
			String str2 = str.substring(i, i + 1);
			if (str1.equals(com1))
			{
				result = incArrayString(result);
				result[result.length - 1] = temp;
				temp = "";
				i = i + com1.length() - 1;
			}
			else temp = temp + str2;
		}
		result = incArrayString(result);
		result[result.length - 1] = temp;
		return result;
	}

	public static String[] incArrayString(String[] src)
	{
		if (src == null) return new String[1];
		String[] result = new String[src.length + 1];
		for (int i = 0; i < src.length; i++)
		{
			result[i] = src[i];
		}
		return result;
	}

	public static String replaceAll (String str, String changStr, String toStr)
	{
		String result = "";
		if (str == null || str.equals("")) return result;
		if (str.indexOf(changStr) == -1) return str;
		result = str;
		while (true)
		{
			result = replace(result, changStr, toStr);
			if (result.indexOf(changStr) == -1) break;
		}
		return result;
	}

	public static String getDateFormat(String date)
	{
		String result = "";
		if (date == null || "".equals(date) || date.length() < 8) return result;

		result = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
		return result;
	}



	public static String encrypString(String str){
//		MessageDigest clsMd5;
//		try{
//			clsMd5 = MessageDigest.getInstance("MD5");
//		}catch( NoSuchAlgorithmException e ){
//			return "";
//		}
//
//		clsMd5.update( str.getBytes() );
//		byte [] arrBuf = clsMd5.digest();
//		int  iLen = arrBuf.length;
//		StringBuffer clsBuffer = new StringBuffer();
//		if( clsBuffer.equals(null) ) return "";
//		for( int i = 0; i < iLen; ++i ){
//			clsBuffer.append( String.format( "%02X", 0xFF & arrBuf[i] ) );
//		}
//
//		return clsBuffer.toString();
		if ( str == null ){
			return "";
		}

		byte[] digest = null;
		try {
			digest = MessageDigest.getInstance("MD5").digest( str.getBytes() );
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		StringBuffer sb = new StringBuffer();

		for( int i = 0; i < digest.length; i++ ){
			sb.append( Integer.toString( ( digest[i] & 0xf0) >> 4, 16 ) );
			sb.append( Integer.toString( digest[i] & 0x0f, 16 ) );
		}

//		Log.d("MD5", sb.toString());
		return sb.toString();
	}

	public static String replaceImageUrl(String str){
		if(str.endsWith("_0.jpg")){
			str = str.replace("_0.", "_2.");
		}else if(str.endsWith("_9.jpg")){
			str = str.replace("_9.", "_2.");
		}
		return str;
	}

	public static String replaceImageUrl(String str, String replaceSize){
		if(str.endsWith("_0.jpg")){
			str = str.replace("_0.", "_"+ replaceSize + ".");
		}else if(str.endsWith("_9.jpg")){
			str = str.replace("_9.", "_"+ replaceSize + ".");
		}else if(str.endsWith("_E.jpg")){
			str = str.replace("_E.", "_"+ replaceSize + ".");
		}else if(str.endsWith("_2.jpg")){
			str = str.replace("_2.", "_"+ replaceSize + ".");
		}else if(str.endsWith("_3.jpg")){
			str = str.replace("_3.", "_"+ replaceSize + ".");
		}else if(str.endsWith("_1.jpg")){
			str = str.replace("_1.", "_"+ replaceSize + ".");
		}
		return str;
	}

	public static String priceString(String str){

		return str;
	}

	public static String join(Collection<?> collection, String separator) {

		Iterator<?> iterator = collection.iterator();

		if (iterator == null) {
			return null;
		}

		if (!iterator.hasNext()) {
			return "";
		}

		Object first = iterator.next();
		if (!iterator.hasNext()) {
			return first == null ? "" : first.toString();
		}

		// two or more elements
		StringBuffer buf = new StringBuffer(256);
		if (first != null) {
			buf.append(first);
		}

		while (iterator.hasNext()) {
			if (separator != null) {
				buf.append(separator);
			}
			Object obj = iterator.next();
			if (obj != null) {
				buf.append(obj);
			}
		}

		return buf.toString();
	}
}
