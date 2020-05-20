package com.pos.n5.terminal.util;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


/**
 * The Class Utils.
 */
public class Utils {

	/** The Constant ZERO_PADDING. */
	public final static String ZERO_PADDING = "0000000000000000";

	// public static void clearCharArray(char[] buffer) {
	// int i= 0, length = buffer.length;
	// for (; i<length; i++) {
	// buffer[i]= (char)0;
	// }
	// }

	/**
	 * Clear byte array.
	 * 
	 * @param buffer
	 *            the buffer
	 */
	public static void clearByteArray(final byte[] buffer) {
		int i = 0;
		final int length = buffer.length;
		for (; i < length; i++) {
			buffer[i] = (byte) 0;
		}
	}

	/**
	 * To hex string.
	 * 
	 * @param data
	 *            the data
	 * @param length
	 *            the length
	 * @return the string
	 */
	public static String toHexString(final byte[] data, final int length) {
		final String digits = "0123456789ABCDEF";
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			final int b = data[i];
			char c = digits.charAt((b >> 4) & 0xf);
			sb.append(c);
			c = digits.charAt(b & 0xf);
			sb.append(c);
		}

		return sb.toString();
	}

	/**
	 * BC darray to string.
	 * 
	 * @param data
	 *            the data
	 * @param offset
	 *            the offset
	 * @param length
	 *            the length
	 * @return the string
	 */
	public static String BCDarrayToString(final byte[] data, final int offset,
			final int length) {
		String s = "";
		byte hi;
		byte lo;
		Integer hiInteger;
		Integer loInteger;
		boolean maskZeros = true;

		if ((offset >= data.length) || (offset + length > data.length)) {
			throw new ArrayIndexOutOfBoundsException();
		}

		for (int i = offset; i < (offset + length); i++) {
			hi = (byte) ((data[i] >>> 4) & 0xF);
			lo = (byte) (data[i] & 0xF);

			if (hi > 9 || lo > 9)
				throw new IllegalArgumentException();

			hiInteger = new Integer(hi);
			loInteger = new Integer(lo);

			if (!maskZeros || (hi != 0)) {
				maskZeros = false;
				s = s + hiInteger.toString();
			}

			if (!maskZeros || (lo != 0)) {
				maskZeros = false;
				s = s + loInteger.toString();
			}

			if (i == (offset + length - 2)) {

				s = s + ".";
				maskZeros = false;
			}
		}

		if ((s.equalsIgnoreCase("")) || (s.charAt(0) == '.'))
			s = "0" + s;

		return s;
	}

	/**
	 * Encode byte array.
	 * 
	 * @param s
	 *            the s
	 * @return the byte[]
	 */
	public static byte[] encodeByteArray(final byte[] s) {
		int i = 0, j = 0;

		final int originalLength = s.length;

		final int max = s.length - (originalLength % 2);
		final byte[] buf = new byte[(originalLength + (originalLength % 2)) / 2];

		if (originalLength % 2 == 1) {
			buf[j++] = (byte) ((s[i++] - '0'));
		}
		while (i < max) {
			buf[j++] = (byte) ((((s[i++] - '0') << 4) | (s[i++] - '0')));
		}

		return buf;
	}

	// public static byte[] encodeBCDString(String s) {
	// int i = 0, j = 0;
	// int max = s.length() - (s.length() % 2);
	// byte[] buf = new byte[(s.length() + (s.length() % 2)) / 2];
	// while (i < max) {
	// buf[j++] = (byte) ((((s.charAt(i++) - '0') << 4) | (s.charAt(i++) -
	// '0')));
	// }
	// if ((s.length() % 2) == 1) { // If odd, add pad char
	// buf[j] = (byte) ((s.charAt(i++) - '0') << 4 | 0x0A);
	// }
	// return buf;
	// }

	/** Hexadecimal string prefix. */
	private static final String HEX_PREFIX = "0x";

	/**
	 * Returns the entire array as a hexadecimal string that starts with the
	 * prefix '0x'.
	 * 
	 * @param data
	 *            bytes to be converted into a hex string
	 * @return a hexadecimal string.
	 */
	public static String getAsHexString(final byte[] data) {
		return getAsHexString(data, false);
	}

	/**
	 * Make short.
	 * 
	 * @param byte1
	 *            the byte1
	 * @param byte2
	 *            the byte2
	 * @return the short
	 */
	public static short makeShort(final byte byte1, final byte byte2) {

		short tmp = (short) (0x00FF & byte1);
		tmp = (short) ((0x00ff & tmp) << 8);
		short tmp2 = (short) (0x00FF & byte2);
		return (short) (tmp | tmp2);
	}

	/**
	 * Returns the entire array as a hexadecimal string.
	 * 
	 * @param data
	 *            bytes to be converted into a hex string
	 * @param prefix
	 *            if true then string will start with '0x'
	 * @return a hexadecimal string.
	 */
	public static final String getAsHexString(final byte[] data,
			final boolean prefix) {
		return getAsHexString(data, 0, data.length, prefix);
	}

	// public static final String getAsHexString(int value, int npad)
	// {
	// String s = getAsHexString(value, false);
	// int need = npad - s.length();
	// if((need <= 0) || (need > ZERO_PADDING.length())) return s;
	// return ZERO_PADDING.substring(0, need) + s;
	// }

	/**
	 * Returns the given integer as a hexadecimal string that starts with the
	 * prefix '0x'.
	 * 
	 * @param value
	 *            the integer value.
	 * @param prefix
	 *            the prefix
	 * @return a hexadecimal string.
	 */
	// public static final String getAsHexString(int value) {
	// return getAsHexString(value, true);
	// }

	/**
	 * Returns the given integer as a hexadecimal string.
	 * 
	 * @param value
	 *            the integer value.
	 * @param prefix
	 *            prefix if true then string will start with '0x'
	 * @return a hexadecimal string.
	 */
	public static final String getAsHexString(final int value,
			final boolean prefix) {
		return (prefix ? HEX_PREFIX : "") + Integer.toHexString(value);
	}

	/**
	 * Checks if the given value falls into the unsigned short value range
	 * (0-65535). This range is used when defining block numbers and amounts in
	 * the read and write requests.
	 * 
	 * @param data
	 *            the data
	 * @param offset
	 *            the offset
	 * @param len
	 *            the len
	 * @return the as hex string
	 */
	// public static final boolean isShort(int value) {
	// return (value >= 0 && value <= 65535);
	// }

	/**
	 * Returns the given byte value as a hexadecimal string.
	 * 
	 * @param b
	 *            the byte value
	 * @param prefix
	 *            prefix if true then string will start with '0x'
	 * @return a hexadecimal string.
	 */
	// public static final String getAsHexString(byte b, boolean prefix) {
	// StringBuffer sb = new StringBuffer();
	//
	// if (prefix) {
	// sb.append(HEX_PREFIX);
	// }
	//
	// appendByte(b, sb);
	// return sb.toString();
	// }

	/**
	 * Returns the given byte array as a hexadecimal string without splitting it
	 * to multiple lines. The returned string will start with the prefix '0x'.
	 * 
	 * @param data
	 *            bytes to be converted into a hex string
	 * @param offset
	 *            offset into the data array
	 * @param len
	 *            maximum length to be read starting from offset (will stop at
	 *            array end if too big)
	 * @return a hexadecimal string.
	 */
	public static final String getAsHexString(final byte[] data,
			final int offset, final int len) {
		return getAsHexString(data, offset, len, false);
	}

	/**
	 * Returns the given byte array as a hexadecimal string without splitting it
	 * to multiple lines.
	 * 
	 * @param data
	 *            bytes to be converted into a hex string
	 * @param offset
	 *            offset into the data array
	 * @param len
	 *            maximum length to be read starting from offset (will stop at
	 *            array end if too big)
	 * @param prefix
	 *            if true then string will start with '0x'
	 * @return a hexadecimal string.
	 */
	public static final String getAsHexString(final byte[] data,
			final int offset, int len, final boolean prefix) {
		final StringBuffer sb = new StringBuffer();

		if (offset + len > data.length) {
			len = data.length - offset;
		}

		for (int i = offset; i < offset + len; i++) {
			final byte b = data[i];
			appendByte(b, sb);
		}

		return sb.toString();
	}

	/**
	 * Returns the given byte array as a hexadecimal string that is split into
	 * lines of 16 bytes. Each line starts with a offset number.
	 * 
	 * @param hexString
	 *            the hex string
	 * @return the byte[]
	 */
	// public static final String getAsHexStringL(byte[] data) {
	// return getAsHexStringL(data, 16, true);
	// }

	/**
	 * Returns the given byte array as a hexadecimal string that is split into
	 * lines of arbitrary number of bytes.
	 * 
	 * @param perLine
	 *            how many bytes there should be per line.
	 * @param showAddr
	 *            if true then displays byte index in the beginning of lines.
	 * @return given bytes as a hexadecimal string.
	 */
	// public static final String getAsHexStringL(byte[] data, int perLine,
	// boolean showAddr) {
	// StringBuffer sb = new StringBuffer();
	// int count = 0;
	// appendAddress(count, sb);
	//
	// for (int i = 0; i < data.length; i++) {
	// appendByte(data[i], sb);
	//
	// if (i < (data.length - 1)) {
	// count++;
	//
	// if ((count % perLine) == 0) {
	// sb.append('\n');
	//
	// if (showAddr) {
	// appendAddress(count, sb);
	// }
	//
	// } else {
	// sb.append(' ');
	// }
	//
	// }
	//
	// }
	//
	// return sb.toString();
	// }

    public static String str2HexStr(String str)  
    {    
  
        char[] chars = "0123456789ABCDEF".toCharArray();    
        StringBuilder sb = new StringBuilder("");  
        byte[] bs = str.getBytes();    
        int bit;    
          
        for (int i = 0; i < bs.length; i++)  
        {    
            bit = (bs[i] & 0x0f0) >> 4;    
            sb.append(chars[bit]);    
            bit = bs[i] & 0x0f;    
            sb.append(chars[bit]);  
            //sb.append(' ');  
        }    
        return sb.toString().trim();    
    } 
    
	/**
	 * Constructs a byte array from the given hexadecimal string. The string may
	 * begin with the prefix '0x'.
	 * 
	 * @param hexString
	 *            the hexadecimal string.
	 * @return a byte array. Never <code>null</code>.
	 * 
	 * @throws NumberFormatException
	 *             if the string has invalid characters.
	 */
	public static final byte[] readHexString(String hexString) {

		if (hexString == null || hexString.length() == 0
				|| hexString.equals(HEX_PREFIX)) {
			return new byte[] {};
		} else {

			if (hexString.startsWith(HEX_PREFIX)) {
				hexString = hexString.substring(2);
			}

			final byte[] data = new byte[hexString.length() / 2];

			for (int i = 0; i < data.length; i++) {
				data[i] = (byte) (Integer.parseInt(
						hexString.substring(i * 2, i * 2 + 2), 16) & 0xFF);
			}

			return data;
		}
	}

	/**
	 * Appends a address (array offset) to the given string.
	 * 
	 * @param byteValue
	 *            the byte value
	 * @param sb
	 *            the sb
	 */
	// private static final void appendAddress(int addr, StringBuffer sb) {
	// appendByte(addr, sb);
	// sb.append("-: ");
	// }

	/**
	 * Appends a byte to the given string.
	 */
	private static final void appendByte(final int byteValue,
			final StringBuffer sb) {
		final String bStr = Integer.toHexString(byteValue & 0xFF);

		if (bStr.length() < 2) {
			sb.append('0');
		}

		sb.append(bStr);
	}

	/**
	 * Reads an (signed) integer from the given byte array starting at the
	 * specified array index.
	 * 
	 * @param data
	 *            byte array containing the 4 integer bytes.
	 * @param offset
	 *            beginning of the integer value in the array.
	 * @param littleEndian
	 *            true if little endian byte order is used. If false then the
	 *            integer is read using big endian.
	 * @return the read integer value.
	 */
	// public static final int readInt(byte[] data, int offset, boolean
	// littleEndian) {
	//
	// int tmpLength = (data.length - offset);
	// if( tmpLength < 4) {
	// byte[] tmpBuffer = {(byte)0, (byte)0, (byte)0, (byte)0};
	// System.arraycopy(data, 0, tmpBuffer, (4 - tmpLength), tmpLength);
	// data = tmpBuffer;
	// }
	// if (littleEndian) {
	// return (((data[offset + 3] & 0xff) << 24) |
	// ((data[offset + 2] & 0xff) << 16) |
	// ((data[offset + 1] & 0xff) << 8) |
	// (data[offset + 0] & 0xff));
	// } else {
	// return (((data[offset + 0] & 0xff) << 24) |
	// ((data[offset + 1] & 0xff) << 16) |
	// ((data[offset + 2] & 0xff) << 8) |
	// (data[offset + 3] & 0xff));
	// }
	// }

	/**
	 * Reads an integer from the given byte array starting at the specified
	 * array index using big endian byte order.
	 * 
	 * @return the read integer value.
	 */
	// public static final int readInt(byte[] data, int offset) {
	// return readInt(data, offset, false);
	// }

	/**
	 * Reads a (signed) short integer from the byte array.
	 * 
	 * @param data
	 *            byte array containing the 2 integer bytes.
	 * @param offset
	 *            beginning of the short integer value in the array.
	 * @param littleEndian
	 *            true if little endian byte order is used. If false then the
	 *            integer is read using big endian.
	 * @return the read short integer value.
	 */
	public static final int readShort(final byte[] data, final int offset,
			final boolean littleEndian) {

		if (littleEndian) {
			return ((data[offset + 1] << 8) | (data[offset] & 0xFF)) & 0xFFFF;
		} else {
			return ((data[offset] << 8) | (data[offset + 1] & 0xFF)) & 0xFFFF;
		}
	}

	/**
	 * Reads a (signed) short integer from the byte array using big endian byte
	 * order.
	 * 
	 * @param data
	 *            the data
	 * @param offset
	 *            the offset
	 * @return the short
	 */
	public static final short readShort(final byte[] data, final int offset) {
		return (short) (readShort(data, offset, false));
	}

	/**
	 * Writes a short integer (2B) to the byte array.
	 * 
	 * @param aArray
	 *            the a array
	 * @param aOffset
	 *            the a offset
	 * @param bArray
	 *            the b array
	 * @param bOffset
	 *            the b offset
	 * @param length
	 *            the length
	 * @return true, if successful
	 */
	// public static final void writeShort(byte[] data, int offset, int value,
	// boolean littleEndian) {
	//
	// if (littleEndian) {
	// data[offset + 1] = (byte)((value >> 8) & 0xFF);
	// data[offset + 0] = (byte)(value & 0xFF);
	// } else {
	// data[offset + 0] = (byte)((value >> 8) & 0xFF);
	// data[offset + 1] = (byte)(value & 0xFF);
	// }
	// }

	/**
	 * Writes a short integer (2B) to the byte array using big endian byte
	 * order.
	 */
	// public static final void writeShort(byte[] data, int offset, int value) {
	// writeShort(data, offset, value, false);
	// }

	/**
	 * Writes a integer (4B) to the byte array.
	 * 
	 * @param data
	 *            a byte array.
	 * @param offset
	 *            defines the offset of the integer in the array.
	 * @param value
	 *            the value to be written.
	 * @param littleEndian
	 *            if true then little endianness is used. Otherwise big endian.
	 */
	// public static final void writeInt(byte[] data, int offset, long value,
	// boolean littleEndian) {
	//
	// if (littleEndian) {
	// data[offset + 3] = (byte)((value >> 24) & 0xFF);
	// data[offset + 2] = (byte)((value >> 16) & 0xFF);
	// data[offset + 1] = (byte)((value >> 8) & 0xFF);
	// data[offset + 0] = (byte)(value & 0xFF);
	// } else {
	// data[offset + 0] = (byte)((value >> 24) & 0xFF);
	// data[offset + 1] = (byte)((value >> 16) & 0xFF);
	// data[offset + 2] = (byte)((value >> 8) & 0xFF);
	// data[offset + 3] = (byte)(value & 0xFF);
	// }
	// }

	/**
	 * Writes a integer (4B) to the byte array using big endian byte order.
	 */
	// public static final void writeInt(byte[] data, int offset, long value) {
	// writeInt(data, offset, value, false);
	// }

	/**
	 * Returns a copy of the given byte array.
	 */
	// public static final byte[] arrayCopy(byte[] src) {
	//
	// if (src == null) {
	// return null;
	// } else {
	// byte[] ret = new byte[src.length];
	// System.arraycopy(src, 0, ret, 0, src.length);
	// return ret;
	// }
	// }

	/**
	 * Compare two byte arrays.
	 * 
	 * @param aArray
	 *            the a array
	 * @param aOffset
	 *            the a offset
	 * @param bArray
	 *            the b array
	 * @param bOffset
	 *            the b offset
	 * @param length
	 *            the length
	 * @return true, if successful
	 */
	public static final boolean arrayCompare(final byte[] aArray,
			final int aOffset, final byte[] bArray, final int bOffset,
			final int length) {

		if ((aOffset + length > aArray.length)
				|| (bOffset + length > bArray.length)) {
			return false;
		}

		for (int index = 0; index < length; index++) {
			if (aArray[aOffset + index] != bArray[bOffset + index]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if the two given byte arrays have matching content or if
	 * they both are <code>null</code>.
	 * 
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @param aOffset
	 *            defines where to start comparing bytes in the <code>a</code>
	 *            array.
	 * @param bOffset
	 *            defines where to start comparing bytes in the <code>b</code>
	 *            array.
	 * @param len
	 *            defines how many bytes should be compared.
	 * @return true, if successful
	 */
	public static final boolean equals(final byte[] a, final byte[] b,
			final int aOffset, final int bOffset, final int len) {

		if (a == null && b == null) {
			return true;
		} else if (a != null && b != null && (aOffset + len) <= a.length
				&& (bOffset + len) <= b.length) {

			for (int i = 0; i < len; i++) {
				if (a[aOffset + i] != b[bOffset + i]) {
					return false;
				}
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if the two given byte arrays are equal in length and in
	 * their contents match, or if they both are <code>null</code>.
	 * 
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @return true, if successful
	 */
	public static final boolean equals(final byte[] a, final byte[] b) {

		if (a == null && b == null) {
			return true;
		} else if (a != null && b != null && a.length == b.length) {

			for (int i = 0; i < a.length; i++) {
				if (a[i] != b[i]) {
					return false;
				}
			}

			return true;
		} else {
			return false;
		}
	}

	public static boolean isNumeric(String str){
	    Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(str).matches();   
	 } 
	
	public static String fill(String input, int size, char symbol) {
		while (input.length() < size) {
			input = input + symbol;
		}
		return input;
	} 
	
	public static String fillbyRight(String input, int size, char symbol) {
		while (input.length() < size) {
			input = symbol + input;
		}
		return input;
	} 
	
	/**
	 * Compares two sets of bytes. Both sets are in the given byte array at
	 * specified indices. No bounds checking is done for the parameters.
	 * 
	 * @param tag
	 *            the tag
	 * @param buffer
	 *            the buffer
	 * @param offset
	 *            the offset
	 * @param length
	 *            the length
	 * @return <code>true</code> if the two sets are equal.
	 * @throws CardException
	 *             the card exception
	 */
	// public static final boolean byteEquals(byte[] bytes, int aOffset, int
	// bOffset, int len, boolean inverted) {
	//
	// for (int i = 0; i < len; i++) {
	// if ((inverted && bytes[aOffset + i] != (~bytes[bOffset + i])) ||
	// (!inverted && (bytes[aOffset + i] != bytes[bOffset + i])))
	// {
	// return false;
	// }
	// }
	//
	// return true;
	// }

	// public static byte getByteValue(byte[] buffer, int bitoffset, int length)
	// {
	// int offset = bitoffset/8;
	// int inbitoffset = bitoffset%8;
	// int short_value = ((buffer[offset]<<8)+(0x00FF&buffer[offset+1]));
	// short return_value = (short)(((byte)( short_value>>>(8-inbitoffset))));
	// return_value &= (0x00ff);
	//
	// byte ret = (byte) ((return_value)>>>(8-length));
	//
	// return ret;
	// }

	public static int findTag(final byte tag, final byte[] buffer,
			final int offset, final int length) throws RuntimeException {
		for (int i = 0; i < length; i++) {
			if (buffer[offset + i] == tag) {
				return (offset + i);
			}
		}
		throw new RuntimeException();
	}

	/**
	 * Find tag.
	 * 
	 * @param tag
	 *            the tag
	 * @param buffer
	 *            the buffer
	 * @param offset
	 *            the offset
	 * @param length
	 *            the length
	 * @return the int
	 * @throws CardException
	 *             the card exception
	 */
	public static int findTag(final short tag, final byte[] buffer,
			final int offset, final int length) throws RuntimeException {
		for (int i = 0; i < length; i++) {
			if (readShort(buffer, (offset + i)) == tag) {
				return (offset + i);
			}
		}
		throw new RuntimeException();
	}

	// public static String getValueAsString(byte[] buffer, int tagOffset, int
	// length) throws CardException {
	//
	// int tmpLength = buffer[tagOffset+1];
	// // If the buffer size is too short to contain the whole value
	// if((length-2) < tmpLength)
	// throw new CardException();
	// else {
	// byte[] tmpBuffer = new byte[tmpLength];
	// System.arraycopy(buffer, tagOffset+2, tmpBuffer, 0, tmpLength);
	// return new String(tmpBuffer);
	// }
	// }

	// public static byte[] getValueasByteArray(byte[] buffer, int tagOffset,
	// int length) throws CardException {
	//
	// int tmpLength = buffer[tagOffset+1];
	// // If the buffer size is too short to contain the whole value
	// if((length-2) < tmpLength)
	// throw new CardException();
	// else {
	// byte[] tmpBuffer = new byte[tmpLength];
	// System.arraycopy(buffer, tagOffset+2, tmpBuffer, 0, tmpLength);
	// return tmpBuffer;
	// }
	// }

	// public static int getInt24Value(byte[]buffer, int bitoffset){
	//
	// int value ;
	// value = getByteValue(buffer, bitoffset+16,8 )&0x00FF;
	//
	// value += (getByteValue (buffer, bitoffset+8, 8)<< 8 ) &0x00FF;
	//
	// value += (getByteValue (buffer, bitoffset+0, 8)<< 16 ) &0x00FF;
	//
	// return value;
	// }

	// public static int lastIndexOf(String input, String str){
	// int index = -1;
	// int tempIndex = 0;
	// while(tempIndex != -1){
	// tempIndex = input.indexOf(str, index + 1);
	// if(tempIndex != -1){
	// index = tempIndex;
	// }
	// }
	// return index;
	// }

	// public static int countIndexOf(String input, char c){
	// int lastIndex = 0;
	// int count = 0;
	//
	// while(lastIndex != -1){
	//
	// lastIndex = input.indexOf(c,lastIndex + 1);
	//
	// if( lastIndex != -1){
	// count ++;
	// }
	// }
	// return count;
	// }
    public static String unPadLeft(String s, char c)
    {
        if(s.trim().length() == 0 && c == ' ')
            return Character.toString(c);
        if(s.trim().length() == 0)
            return s;
        String sTrim = s.trim();
        int fill = 0;
        int end;
        for(end = sTrim.length(); fill < end && sTrim.charAt(fill) == c; fill++);
        return fill >= end ? sTrim.substring(fill - 1, end) : sTrim.substring(fill, end);
    }
    
    public static String addCharForNum(String str, int strLength) {
    	int strLen = str.length();
    	if (strLen < strLength) {
    	while (strLen < strLength) {
    	StringBuffer sb = new StringBuffer();
    	//sb.append("F").append(str);
    	 sb.append(str).append("F");
    	str = sb.toString();
    	strLen = str.length();
    	}
    	}

    	return str;
    }
    
    public static String hexify (byte bytes[]) {

		char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', 
				'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

		StringBuffer buf = new StringBuffer(bytes.length * 2);

	    for (int i = 0; i < bytes.length; ++i) {
	    	buf.append(hexDigits[(bytes[i] & 0xf0) >> 4]);
	        buf.append(hexDigits[bytes[i] & 0x0f]);
	    }

	    return buf.toString();
	}
    
    public static String addZeroForNum(String str, int strLength) {

		int strLen = str.length();
		StringBuffer sb = null;
		while (strLen < strLength) {
			sb = new StringBuffer();
			sb.append("0").append(str);// 左补0
			// sb.append(str).append("0");//�?�补0
			str = sb.toString();
			strLen = str.length();
		}
		return str;
	}
    
    /**
	 * @功能: BCD�?转为10进制串
	 * @�?�数: BCD�?
	 * @结果: 10进制串
	 */
	public static String bcd2Str(byte[] bytes) {

		StringBuffer temp = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			int h = ((bytes[i] & 0xff) >> 4) + 48;
			temp.append((char) h);
			int l = ((bytes[i] & 0x0f)) + 48;
			temp.append((char) l);
		}
		return temp.toString();
	}
 
    /**
	 * @功能: 10进制串转为BCD�?
	 * @�?�数: 10进制串
	 * @结果: BCD�?
	 */
    public static byte[] str2Bcd(String s) {

		if (s.length() % 2 != 0) {
			s = "0" + s;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		char[] cs = s.toCharArray();
		for (int i = 0; i < cs.length; i += 2) {
			int h = cs[i] - 48;
			int l = cs[i + 1] - 48;
			baos.write(h << 4 | l);
		}
		return baos.toByteArray();
	}
    
    public static Map<?,?> jsonList2Map(List<?> jsonList)
    {
    	Map<String,String> resultMap = new HashMap<String, String>();
    	for (int i = 0; i<jsonList.size(); i++)
    	{
    		String tmpStr = (String) jsonList.get(i);
    		String key = tmpStr.split("=")[0];
    		String value = tmpStr.split("=")[1];
    		resultMap.put(key, value);
    	}
		return resultMap;
    	
    }
    
    public static String getTxnId() {
		StringBuffer txnId = new StringBuffer();
		   Date date = new Date();
		   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		   txnId.append(sdf.format(date));
		   Random random = new Random(date.getTime());
		   for(int i=0; i<3; i++){
		      int r = random.nextInt(10);
		      txnId.append(r);
		   }
		   return txnId.toString();
	}
}
