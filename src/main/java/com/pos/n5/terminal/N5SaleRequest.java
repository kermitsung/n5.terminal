package com.pos.n5.terminal;

import com.pos.n5.terminal.util.HashUtil;
import com.pos.n5.terminal.util.MsgUtils;
import com.pos.n5.terminal.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

@Component
public class N5SaleRequest {
    private final static String macKey =  "0EAEA18F7A46B9C8765B3DB313267C75";
    private static boolean abort =false;
    private static int receiveTimeout = 1200000;
    private static ServerSocket server;
    private Socket sk = null;

    public static Map<?, ?> saleSocket (String socketAddress, BigDecimal amount) {
        System.out.println("sale per second !");
        /*try {
             server = new ServerSocket(9001);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        n5test_sale server = new n5test_sale();
        server.run();*/
        // TODO Auto-generated method stub
        BigDecimal btxnAmt = new BigDecimal(10);
        String msgBody = MsgUtils.createCardPurchaseRequestJsonMsg(Utils.getTxnId(), amount,null,"", null, false);

        byte[] macMD5Hash = HashUtil.getMD5WithoutBase64(msgBody + '&' + macKey);
        String macMD5Hex = Utils.toHexString(macMD5Hash, macMD5Hash.length);

        N5Message requestMsg = new N5Message();

            try {
            requestMsg.setBody(msgBody.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }
            requestMsg.setMac(Utils.readHexString(macMD5Hex));
        Socket n5s = null;
            try {
            n5s =  new Socket();
            //socket.setSoTimeout(5);
                System.out.println("socketAddress:"+ socketAddress);
            n5s.connect(new InetSocketAddress(socketAddress,9001),10000);
        } catch (UnknownHostException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        //JSONObject json2 = JSONObject.fromObject("");
            try {
            byte[] sendMsg = requestMsg.toBytes();
            n5s.getOutputStream().write(sendMsg);

            N5Message respMsg = waitUntilEnd(n5s, receiveTimeout);
            if (respMsg!=null)
            {
                String jsnString = new String(respMsg.getBody());
                //JSONObject json = JSONObject.fromObject(jsnString);
                JsonParser parser = new JsonParser(jsnString);
                return (Map<?, ?>) parser.parse();
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return null;

    }

    public void run()
    {

        while (true)
        {
            System.out.println("Listenning...");
            try
            {
//                  每个请求交给一个线程去处理
                sk = server.accept();
                ServerThread th = new ServerThread(sk);
                th.start();
                sleep(1000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    private static N5Message waitUntilEnd(Socket s, int timeout) {
        //mHost.display("Reading card...");
        long start = System.currentTimeMillis();
        do {
            N5Message respMsg = readFromN5(s, timeout);
            if (null != respMsg)
            {

                return respMsg;
            }
            long now = System.currentTimeMillis();
            if (now - start <= timeout)
                continue;
            abort = true;

        } while (!abort);
        return null;
    }

    private static N5Message readFromN5(Socket s, int timeOut) {

        try {
            InputStream inputStream = s.getInputStream();
            int totallen = 0;
            while ((totallen = inputStream.available()) > 0) {
                byte[] receivedMsg = new byte[4096];
                sleep(20L);

                if (totallen > 0) {
                    inputStream.read(receivedMsg);


                    byte[] len = new byte[] { receivedMsg[1], receivedMsg[2] };
                    String lenStr = Utils.bcd2Str(len);
                    int msglength = Integer.parseInt(lenStr);

                    byte[] totalMsg = new byte[msglength];
                    System.arraycopy(receivedMsg, 3, totalMsg, 0, msglength);
                    N5Message msg = new N5Message();
                    msg.setBody(totalMsg);


                    return msg;
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return null;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    BufferedReader rdr = null;
    PrintWriter wtr = null;

class ServerThread extends Thread
{

    Socket sk = null;
    public ServerThread(Socket sk)
    {
        this.sk = sk;
    }
    public void run()
    {
        try
        {
            wtr = new PrintWriter(sk.getOutputStream());
            rdr = new BufferedReader(new InputStreamReader(sk
                    .getInputStream()));
            String line = rdr.readLine();
            System.out.println("从客户端来的信息：" + line);
//              特别，下面这句得加上     “\n”,
            wtr.println("你好，服务器已经收到您的信息！'" + line + "'\n");
            wtr.flush();
            System.out.println("已经返回给客户端！");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
}
