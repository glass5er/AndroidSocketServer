package com.app.moveriodev.androidsocketserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import android.util.Log;

public class SubTaskThread extends Thread {
    private ServerTask mServer = null;
    private Socket     mSocket = null;
    private int        mPort = 0;

    InputStream m_is = null;
    InputStreamReader m_ir = null;
    BufferedReader m_br = null;
    PrintStream m_os = null;

    public SubTaskThread(ServerTask server) {
        // TODO Auto-generated constructor stub
    }

    public void setSocket(Socket socket) {
        this.mSocket = socket;
        this.mPort   = mSocket.getPort();
    }

    /*
     * @CORE
     */
    public void run() {
        // System.out.println("Thread start");
        if (mSocket == null) {
            System.out.println("Fail : No Socket");
            return;
        }

        try {
            // @NOTE : #port is assigned automatically (different from
            // server.port)
            System.out.println("Connect(" + mPort + ")");
            // create input stream
            m_is = mSocket.getInputStream();
            m_ir = new InputStreamReader(m_is);
            m_br = new BufferedReader(m_ir);
            // create output stream
            m_os = new PrintStream(mSocket.getOutputStream());

            // InputStreamÇ™èÄîıÇ≈Ç´ÇÈÇ‹Ç≈ë“Ç¬
            while (m_is.available() == 0)
                ;

            while (true) {
                // wait for client request
                String line = m_br.readLine();
                // echo (upper case)
                m_os.println(line.toUpperCase());
                mServer.setStateString(line);
                Log.d("TaskThread", "Read(" + mPort + "): " + line);
                System.out.println("Read(" + mPort + "): " + line);
            }
        } catch (NullPointerException e) {
            // disconnect
            // e.printStackTrace();
            System.out.println("Disconnect(" + mPort + ")");
            try {
                if (m_br != null) {
                    m_br.close();
                }
                if (m_ir != null) {
                    m_ir.close();
                }
                if (m_is != null) {
                    m_is.close();
                }
                if (m_os != null) {
                    m_os.close();
                }
            } catch (IOException ie) {
                ie.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // System.out.println("Thead end");
    }

}