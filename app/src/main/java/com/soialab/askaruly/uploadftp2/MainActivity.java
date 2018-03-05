package com.soialab.askaruly.uploadftp2;

import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button connectServer;

/*    String FTPSERVER = "mico.knu.ac.kr";
    int PORT = 21;
    String USERNAME = "sanzhar";
    String PASSWORD = "unist";*/

    ./adb push 'C:\Users\iptea\Pictures\download.jpg' '/data/'

    String host = "mico.knu.ac.kr";
    int port = 22;
    int FTP_TIMEOUT = 100;
    String user = "sanzhar";
    String pass = "unist";
    //String sourceDirectory = "/storage/emulated/0/Android/data/com.example.android.camera2video/files/1519781704861.mp4";
    String sourceDirectory = "download.jpg";
    String destinationDirectory = "/home/sanzhar/1519781704861.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectServer = (Button) findViewById(R.id.connectServer);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        connectServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSch jsch = new JSch();
                Session session = null;

                try {
                    Environment.getExternalStorageDirectory().getAbsolutePath();

                    session = jsch.getSession("sanzhar", "mico.knu.ac.kr", 22); //default port is 22
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.setPassword("unist".getBytes());
                    session.connect();
                    Channel channel = session.openChannel("sftp");
                    channel.connect();
                    System.out.println("Connected");

                    ChannelSftp sftp = (ChannelSftp) channel;

                    //sftp.cd("home/sanzhar/");
                    sftp.put(sourceDirectory,destinationDirectory);

                    Boolean success = true;

                    if(success){
                        //
                        System.out.println("The file has been uploaded succesfully");
                    }

                    channel.disconnect();
                    session.disconnect();

                } catch (JSchException e) {
                    System.out.println(e.getMessage().toString());
                    e.printStackTrace(System.out);
                } catch (Exception e) {
                    System.out.println(e.getMessage().toString());
                    e.printStackTrace(System.out);
                } finally {
                    //session.disconnect();
                    System.out.println("Disconnected");
                }
            }
        });
    }
}
