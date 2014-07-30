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
        // �\�P�b�g����o�͗p�̃X�g���[���̐錾
        ServerSocket echoServer = null;

        // open port (hard-coded)
        try {
            echoServer = new ServerSocket(mPort);
        } catch (IOException e) {
            System.out.println(e);
        }
        Log.d(TAG, "server established");
        mStopFlag = false;

        // @NOTE : �N���C�A���g�A�v�������グ�O�Ɏ��s���Ă�������
        // @NOTE : �ڑ����؂ꂽ���Ƃ̃��J�o���͎������Ă��Ȃ��̂Œ�~���Ď��s���K�v
        // �N���C�A���g����̗v�����󂯂�\�P�b�g���J��
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