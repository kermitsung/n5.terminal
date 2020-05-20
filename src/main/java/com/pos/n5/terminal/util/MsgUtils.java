package com.pos.n5.terminal.util;
import java.math.BigDecimal;

/**
 * The Class Utils.
 */
public class MsgUtils {

	public static String createCardPurchaseRequestJsonMsg(String txnId, BigDecimal txnAmt, BigDecimal tipAmt, String paymentAppId, String qrcValue, boolean ifPrintSS)
	{
		StringBuffer respMsg = new StringBuffer();
		respMsg.append("{\"EVENT_NAME\":\"SALE\",");
		respMsg.append("\"TXN_ID\":\"" + txnId + "\",");
		respMsg.append("\"TXN_AMT\":\"" + txnAmt + "\",");
		if (null != tipAmt)
			respMsg.append("\"TIPS\":" + tipAmt + ",");
		respMsg.append("\"PAYMENT_APP_ID\":\"" + paymentAppId + "\",");
		if (null != qrcValue)
			respMsg.append("\"QRC_VALUE\":\"" + qrcValue + "\",");
		respMsg.append("\"PRINT_SS\":" + ifPrintSS + "}");
		
		return respMsg.toString();
	}
	
	public static String createCardRefundRequestJsonMsg(String txnId, BigDecimal txnAmt, String paymentAppId, String qrcValue, boolean ifPrintSS)
	{
		StringBuffer respMsg = new StringBuffer();
		respMsg.append("{\"EVENT_NAME\":\"REFUND\",");
		respMsg.append("\"TXN_ID\":\"" + txnId + "\",");
		respMsg.append("\"TXN_AMT\":" + txnAmt + ",");
		respMsg.append("\"PAYMENT_APP_ID\":\"" + paymentAppId + "\",");
		if (null != qrcValue)
			respMsg.append("\"QRC_VALUE\":\"" + qrcValue + "\",");
		respMsg.append("\"PRINT_SS\":" + ifPrintSS + "}");
		
		return respMsg.toString();
	}

	public static String createPaymentReversalRequestJsonMsg(String txnId)
	{
		StringBuffer respMsg = new StringBuffer();
		respMsg.append("{\"EVENT_NAME\":\"REVERSAL\",");
		respMsg.append("\"TXN_ID\":\"" + txnId + "\"}");
		
		
		return respMsg.toString();
	}
	
	public static String createSettlementRequestJsonMsg(boolean ifPrintSS)
	{
		StringBuffer respMsg = new StringBuffer();
		respMsg.append("{\"EVENT_NAME\":\"SETTLE\",");
		respMsg.append("\"PRINT_SS\":" + ifPrintSS + "}");
		
		return respMsg.toString();
	}
	
	public static String createCardVoidRequestJsonMsg(String txnId, String paymentAppId, String qrcValue, boolean ifPrintSS)
	{
		StringBuffer respMsg = new StringBuffer();
		respMsg.append("{\"EVENT_NAME\":\"VOID\",");
		respMsg.append("\"TXN_ID\":\"" + txnId + "\",");
		respMsg.append("\"PAYMENT_APP_ID\":\"" + paymentAppId + "\",");
		if (null != qrcValue)
			respMsg.append("\"QRC_VALUE\":\"" + qrcValue + "\",");
		respMsg.append("\"PRINT_SS\":" + ifPrintSS + "}");
		
		return respMsg.toString();
	}
}
