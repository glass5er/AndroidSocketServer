package com.app.moveriodev.androidsocketserver;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {

    private ServerTask serverTask = new ServerTask(this);

    private EditText editTextIP;
    private EditText editTextPort;
    private EditText editTextEcho;

    private String mStrIP = "";
    private final int mPort = 39999;
    private boolean mIsActive = false;

    private Button buttonStart;
    private Button buttonStop;
    private Button buttonUpdate;

    private Handler mHandler = new Handler();
    private String mStateString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextIP = (EditText) this.findViewById(R.id.editTextIP);
        editTextPort = (EditText) this.findViewById(R.id.editTextPort);
        editTextEcho = (EditText) this.findViewById(R.id.editTextEcho);

        // get own IP address -> show
        try {
            mStrIP = getIPAddress();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        editTextIP.setText(mStrIP);

        buttonStart = (Button) this.findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mIsActive) {
                    return;
                }
                mIsActive = true;

                // establish server
                serverTask.init(mStrIP, 39999);
                serverTask.start();

                // post-execution
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mIsActive = true;
                    }
                });
            }
        });

        buttonStop = (Button) this.findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!mIsActive) {
                    return;
                }
                mIsActive = false;

                serverTask.setStopFlag(true);

                // post-execution
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mIsActive = false;
                    }

                });
            }
        });

        buttonUpdate = (Button) this.findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                editTextEcho.setText("state : " + mStateString);
            }
        });

        buttonStart.callOnClick();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getIPAddress() throws IOException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface
                .getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface network = interfaces.nextElement();
            Enumeration<InetAddress> addresses = network.getInetAddresses();

            while (addresses.hasMoreElements()) {
                String address = addresses.nextElement().getHostAddress();

                boolean isIPv6 = address.matches(".*:.*");
                Log.d("getIPAddress", (isIPv6 ? "IPv6" : "IPv4") + " : "
                        + address);
                if (isIPv6) {
                    continue;
                }
                // 127.0.0.1と0.0.0.0以外のアドレスが見つかったらそれを返す
                if (!"127.0.0.1".equals(address) && !"0.0.0.0".equals(address)) {
                    return address;
                }
            }
        }

        return "127.0.0.1";
    }

    public void setStateText(String s) {
        mStateString = s;
    }

}
