package com.aries.template.xiaoyu.dapinsocket;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocThread {
    private String ip = "172.16.11.17";
    private int port = 9000;
    private String TAG = "socket====> ";
    private int timeout = 10000;

    public Socket client = null;
    BufferedReader in;
    public boolean isRun = false;
    Handler inHandler;
    Handler outHandler;
    Context ctx;
    private String TAG1 = "===send===";
    SharedPreferences sp;
    OutputStream outputStream;
    private boolean isConnecting = false;
    private boolean isConnected = false;
    //创建基本线程池
    ExecutorService singleThreadExecutor ;
    ExecutorService sendSingleThreadExecutor ;
    private boolean isSendRun = false;
    private List<byte[]> messageList = new ArrayList<>();

    public static int CONNECT_SUCCESS = 100;
    public static int CONNECT_FAIL = 200;

    public SocThread(Handler handlerin, Handler handlerout, Context context) {
        inHandler = handlerin;
        outHandler = handlerout;
        ctx = context;
        Log.i(TAG, "创建线程socket");
    }

    public void setIport(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 连接socket服务器
     */
    public synchronized void conn() {
        if (isConnecting){
            return;
        }
        isConnecting = true;
        try {
//            initdate();
            Log.i(TAG, "连接中……");
            if (client != null && client.isConnected()){
                client.close();
            }
            client = null;
            client = new Socket();
            //client.setSoTimeout(timeout);// 设置阻塞时间
            client.connect(new InetSocketAddress(ip,port),timeout);
            Log.e(TAG, "连接成功");
            isConnected = true;
            in = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            outputStream = client.getOutputStream();
            isRun = true;
            Log.i(TAG, "输入输出流获取成功");
            isConnecting = false;
            inHandler.sendEmptyMessage(CONNECT_SUCCESS);
            read();
        } catch (UnknownHostException e) {
            Log.i(TAG, "连接错误UnknownHostException 重新获取");
            e.printStackTrace();
            isRun = false;
            isConnecting = false;
            inHandler.sendEmptyMessage(CONNECT_FAIL);
        } catch (IOException e) {
            Log.i(TAG, "连接服务器io错误");
            isRun = false;
            isConnecting = false;
            inHandler.sendEmptyMessage(CONNECT_FAIL);
            e.printStackTrace();
        } catch (Exception e) {
            isConnecting = false;
            isRun = false;
            inHandler.sendEmptyMessage(CONNECT_FAIL);
            Log.i(TAG, "连接服务器错误Exception" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initdate() {
        sp = ctx.getSharedPreferences("SP", Context.MODE_PRIVATE);
        ip = sp.getString("ipstr", ip);
        port = Integer.parseInt(sp.getString("port", String.valueOf(port)));
        Log.i(TAG, "获取到ip端口:" + ip + ";" + port);
    }

    private void read() {
        String line = "";
        while (isRun) {
            try {
                byte[] all = null;
                if (client != null) {
                    //Log.i(TAG, "2.检测数据");
                    while ((line = in.readLine()) != null) {
                        byte[] bytes = line.getBytes();
                        /*for (int i = 0; i < bytes.length; i++) {
                            Log.e("TAG","parse--> " + bytes[i]);
                        }*/
                        //判断起始位，作为一个新数据
                        if (bytes[0] == 65 && bytes[1] == 65){
                            all = new byte[]{};
                        }
                        if (all != null){
                            all = NumberUtil.concat(all,bytes);
                        }
                        //判断结束符
                        if (all != null && all[all.length - 1] == 68 && all[all.length - 2] == 68){
                            SessionContext.getSessiontContext().onReceiveData(all);
                            //parse(all);
                        }
                        Log.i(TAG, " len=" + line.length());
                        //Log.i(TAG, "4.start set Message");
                    }
                }
            } catch (Exception e) {
                isRun = false;
                //Log.i(TAG, "数据接收错误" + e.getMessage());
                //e.printStackTrace();
            }
        }
    }

    private void sendMessage() {
        if(isSendRun){
            return;
        }
        Log.e(TAG,"sendMessageTask======start");
        isSendRun = true;
        while (isSendRun) {
            byte[] bytes = new byte[0];
            if(messageList.size()>0){
                bytes = messageList.get(0);
                if(bytes!=null && bytes.length>0) {
                    try {
                        if (client != null) {
                            outputStream.write(bytes);
                            outputStream.flush();
                            Log.i(TAG1, "发送成功");
                            Message msg = outHandler.obtainMessage();
                            msg.obj = new String(bytes);
                            msg.what = 1;
                            // 结果返回给UI处理
                            outHandler.sendMessage(msg);
                        } else {
                            Log.i(TAG, "client 不存在");
                            Message msg = outHandler.obtainMessage();
                            msg.obj = new String(bytes);
                            msg.what = 0;
                            // 结果返回给UI处理
                            outHandler.sendMessage(msg);
                            //Log.i(TAG, "连接不存在重新连接");
                            isConnected = false;
                            isRun = false;
                            //executeConn();
                        }
                    } catch (Exception e) {
                        Message msg = outHandler.obtainMessage();
                        msg.obj = new String(bytes);
                        msg.what = 0;
                        // 结果返回给UI处理
                        outHandler.sendMessage(msg);
                        isConnected = false;
                        isRun = false;
                        //executeConn();
                        Log.i(TAG1, "发送 error");
                        e.printStackTrace();
                    }

                }
                messageList.remove(0);
            }
        }
    }


    /**
     * 解析byte[]数据到DataBean，返回给页面
     * @param bytes
     */
    private void parse(byte[] bytes){
        byte[] dataType = new byte[4];
        System.arraycopy(bytes, 2, dataType, 0, 4);
        byte[] messageType = new byte[4];
        System.arraycopy(bytes, 6, messageType, 0, 4);
        byte[] length = new byte[4];
        System.arraycopy(bytes, 10, length, 0, 4);
        byte[] body = new byte[bytes.length - 16];
        System.arraycopy(bytes, 14, body, 0, body.length);
        DataBean bean = new DataBean();
        bean.setDataType(NumberUtil.byteArrayToInt(dataType));
        bean.setMessageType(NumberUtil.byteArrayToInt(messageType));
        bean.setLength(NumberUtil.byteArrayToInt(length));
        bean.setBody(new String(body));
        Log.e("TAG","parse--> " + JSON.toJSONString(bean));
        Message msg = inHandler.obtainMessage();
        msg.obj = bean;
        inHandler.sendMessage(msg);// 结果返回给UI处理
    }
    public static DataBean getDataBean(byte[] bytes){
        byte[] dataType = new byte[4];
        System.arraycopy(bytes, 2, dataType, 0, 4);
        byte[] messageType = new byte[4];
        System.arraycopy(bytes, 6, messageType, 0, 4);
        byte[] length = new byte[4];
        System.arraycopy(bytes, 10, length, 0, 4);
        byte[] body = new byte[bytes.length - 16];
        System.arraycopy(bytes, 14, body, 0, body.length);
        DataBean bean = new DataBean();
        bean.setDataType(NumberUtil.byteArrayToInt(dataType));
        bean.setMessageType(NumberUtil.byteArrayToInt(messageType));
        bean.setLength(NumberUtil.byteArrayToInt(length));
        bean.setBody(new String(body));
        Log.e("TAG","parse--> " + JSON.toJSONString(bean));
        return bean;
    }


    /**
     * 发送数据
     *
     * @param
     */
    public void send(byte[] bytes) {
        if(isSendRun) {
            byte[] headByte = new byte[]{65, 65};
            byte[] endByte = new byte[]{68, 68};
            String end = "\n";
            byte[] concat = NumberUtil.concat(NumberUtil.concat(NumberUtil.concat(headByte, bytes), endByte), end.getBytes());
            messageList.add(concat);
        }
    }


    public void sendNew(byte[] bytes) {
        if(isSendRun) {
//            byte[] headByte = new byte[]{65, 65};
//            byte[] endByte = new byte[]{68, 68};
//            String end = "\n";
//            byte[] concat = NumberUtil.concat(NumberUtil.concat(NumberUtil.concat(headByte, bytes), endByte), end.getBytes());
            messageList.add(bytes);
        }
    }

    /**
     * 重连线程池
     */
    public void executeConn() {
        //避免多余的线程
        if (isConnected){
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.e("TAG","====== conn");
                conn();
            }
        };
        //new Thread(runnable).start();
        if (singleThreadExecutor == null){
            singleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        singleThreadExecutor.execute(runnable);

        Runnable sendRunnable = new Runnable() {
            @Override
            public void run() {
                sendMessage();
            }
        };
        if (sendSingleThreadExecutor == null){
            sendSingleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        sendSingleThreadExecutor.execute(sendRunnable);
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            /*if (threadPoolExecutor != null){
                threadPoolExecutor.shutdown();
            }*/
            isSendRun = false;
            if (client != null) {
                isRun = false;
                client.close();
                Log.e(TAG, "close client");
                outputStream.close();
                Log.e(TAG, "close out");
                in.close();
                Log.e(TAG, "close in");
            }
        } catch (Exception e) {
            Log.i(TAG, "close err");
            e.printStackTrace();
        }finally {
            try {
                if (client != null) {
                    client.close();
                    Log.e(TAG, "close client");
                    outputStream.close();
                    Log.e(TAG, "close out");
                    in.close();
                    Log.e(TAG, "close in");
                }
            } catch (Exception e) {
                Log.i(TAG, "close err");
                e.printStackTrace();
            }
        }

    }
}