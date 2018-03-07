package com.soialab.askaruly.uploadftp2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Button connectServer;
    Button downloadServer;
    Button startCode;

/*    String FTPSERVER = "mico.knu.ac.kr";
    int PORT = 21;
    String USERNAME = "sanzhar";
    String PASSWORD = "unist";*/

    //./adb push 'C:\Users\iptea\Pictures\download.jpg' '/data/'

    String host = "mico.knu.ac.kr";
    int port = 22;
    int FTP_TIMEOUT = 100;
    String user = "sanzhar";
    String pass = "unist";
    //String sourceDirectory = "/storage/emulated/0/Android/data/com.example.android.camera2video/files/1519781704861.mp4";
    //String sourceDirectory = "/sdcard/57529.jpg";
    //String sourceDirectory = "My files/Internal storage/Android/data/com.example.android.camera2video/files/1519781704861.mp4";
    //String sourceDirectory = "/storage/Android/data/com.example.android.camera2video/files/1519781704861.mp4";
    //String sourceDirectory = "/storage/Android/data/com.example.android.camera2video/files/1519781697754.mp4";
    String sourceDirectory = "/storage/emulated/0/Android/data/com.example.android.camera2video/files/1519781704861.mp4";
    String destinationDirectory = "/home/sanzhar/1519781704861.mp4";

    String remote_src_path = "/home/sanzhar/1519781704861.mp4";
    String lcl_src_path = "/storage/emulated/0/Android/data/com.example.android.camera2video/files/jai.mp4";

    //String destinationDirectory = "/home/sanzhar/57529.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);
        connectServer = (Button) findViewById(R.id.connectServer);
        downloadServer = (Button) findViewById(R.id.downloadServer);
        startCode = (Button) findViewById(R.id.startServerCode);

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

        downloadServer.setOnClickListener(new View.OnClickListener() {
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
                    sftp.get(remote_src_path,lcl_src_path);
                    Boolean success = true;

                    if(success){
                        //
                        System.out.println("The file has been downloaded succesfully");
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


        startCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSch jsch = new JSch();
                Session session = null;
                String command =("python simple.py");

                try {
                    Environment.getExternalStorageDirectory().getAbsolutePath();

                    session = jsch.getSession("sanzhar", "mico.knu.ac.kr", 22); //default port is 22
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.setPassword("unist".getBytes());
                    session.connect();
                    //Channel channel = session.openChannel("sftp");
                    //channel.connect();


                    //ChannelSftp sftp = (ChannelSftp) channel;

                    //SSH Channel
                    ChannelExec channelssh = (ChannelExec)session.openChannel("exec");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    channelssh.setOutputStream(baos);

                    // Execute Command
                    channelssh.setCommand("python simple.py");
                    channelssh.connect();
                    System.out.println("Connected");

                    //sftp.cd("home/sanzhar/");
                    //InputStream is = new ByteArrayInputStream(command.getBytes());
                    //sftp.setInputStream(is);

                    Boolean success = true;

                    if(success){
                        //
                        System.out.println("Command run successfully!");
                    }

                    channelssh.disconnect();
                    //channel.disconnect();
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

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
