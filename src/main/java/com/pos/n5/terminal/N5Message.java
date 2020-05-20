package com.pos.n5.terminal;


import com.pos.n5.terminal.util.Utils;

public class N5Message {
	public final static byte STX = 0x02;
	public final static byte ETX = 0x03;
	
	private final static int BytesOfLengthIndicator = 2;
	//private final static int BytesOfMAC = 32;
	
	private byte[] body;
	private byte[] mac;
	private boolean isDeviceDisplay;
	private final static String DEVICE_DISPLAY_ALIAS = "devicedisplay:";
	public N5Message()
	{}
	
	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
		
		String tmpStr = new String(body);
		if (tmpStr.startsWith(DEVICE_DISPLAY_ALIAS))
			isDeviceDisplay = true;
		else
			isDeviceDisplay = false;
	}

	public byte[] getMac() {
		return mac;
	}

	public void setMac(byte[] mac) {
		this.mac = mac;
	}

//	private boolean parse(byte[] src)
//	{
//		int msglength = 0;
//		
//		if (src.length < (1 + 1 + BytesOfLengthIndicator + BytesOfMAC))
//			return false;
//		if (src[0] != STX)
//			return false;
//		if (src[src.length - 1] != ETX)
//			return false;
//		
//		byte[] len = new byte[] { src[1], src[2] };
//		String lenStr = Utils.bcd2Str(len);
//		msglength = Integer.parseInt(lenStr);
//		
//		if (src.length != (1 + 1 + msglength + BytesOfLengthIndicator + BytesOfMAC))
//			return false;
//		
//		System.arraycopy(src, 3, body, 0, msglength);
//		System.arraycopy(src, src.length - 2 - BytesOfMAC, mac, 0, BytesOfMAC);
//		return true;
//	}
	
	public boolean isDeviceDisplay() {
		return isDeviceDisplay;
	}

	public byte[] toBytes(){
		if (null == body || null == mac)
			return null;
		byte[] result = new byte[2 + BytesOfLengthIndicator + body.length + mac.length];
		byte[] len = Utils.str2Bcd(Utils.addZeroForNum(Integer.toString(body.length), 4));
		result[0] = STX;
		result[1] = len[0];
		result[2] = len[1];
		
		System.arraycopy(body, 0, result, 3, body.length);
		System.arraycopy(mac, 0, result, body.length + 3, mac.length);
		result[result.length-1] = ETX;
		return result;
	}
	
	
}
