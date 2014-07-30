package com.app.moveriodev.androidsocketserver;

import java.io.*;
import java.net.*;
import java.util.Vector;

import android.util.Log;

public class ServerTask extends Thread {

    private static final String TAG = "SocketServer";
    private int mPort = 0;
    private boolean mStopFlag = false;
    private boolean mIsInitialized = false;
    private Vector<SubTaskThread> mTasks;
    // private String mStrIP = "127.0.0.1";

    private MainActivity mActivity;

    // Constructor
    public ServerTask(MainActivity activity) {
        this.mActivity = activity;
    }

    public void init(String IP, int port) {
        // this.mStrIP = IP;
        this.mPort = port;
    }

    public void setStopFlag(boolean b) {
        this.mStopFlag = b;
    }

    public void setStateString(String s) {
        Log.d(TAG, s);
        this.mActivity.setStateText(s);
    }

    public void run() {
        // ソケットや入出力用のストリームの宣言
        ServerSocket echoServer = null;

        // open port (hard-coded)
        try {
            echoServer = new ServerSocket(mPort);
        } catch (IOException e) {
            System.out.println(e);
        }
        Log.d(TAG, "server established");
        mStopFlag = false;

        // @NOTE : クライアントアプリ立ち上げ前に実行しておくこと
        // @NOTE : 接続が切れたあとのリカバリは実装していないので停止＆再実行が必要
        // クライアントからの要求を受けるソケットを開く
        try {
            while (!mStopFlag) {
                System.out.println("waiting client...");
                Socket clientSocket = echoServer.accept();

                // create thread (echo task)
                SubTaskThread thread = new SubTaskThread(this);
                thread.setSocket(clientSocket);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("server ended");
    }

}