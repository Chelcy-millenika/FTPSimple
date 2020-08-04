package com.soialab.askaruly.uploadftp2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity {

    // UI elements
    Button connectServer;
    Button downloadServer;
    Button startCode;

    // Credentials: define here
    private String hostname = "something.unist.ac.kr"; // insert own hostname here
    private String username = "someuser"; // insert own username here
    private String password = "somepassword";
    private int defaultPort = 22;
    private String myBashCommand = "python simple.py";

    // For upload: source is local & destination is sever
    String sourceDirectory = "/storage/emulated/0/Android/data/com.example.android.myapp/files/somevideo.mp4"; // insert own source file path here
    String destinationDirectory = "/home/someuser/somevideo.mp4"; // insert own destination directory here

    // For download: source is server & destination is local
    String remote_src_path = "/home/someuser/videoonserver.mp4"; // insert own remote source directory here
    String lcl_src_path = "/storage/emulated/0/Android/data/com.example.android.myapp/files/videoonserver.mp4"; // insert own destination directory here


    // Permission variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check permissions
        verifyStoragePermissions(this);

        // Bind elements
        connectServer = (Button) findViewById(R.id.connectServer);
        downloadServer = (Button) findViewById(R.id.downloadServer);
        startCode = (Button) findViewById(R.id.startServerCode);

        // Building policy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // How to upload to server on buttonClick
        connectServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSch jsch = new JSch();
                Session session = null;

                try {
                    Environment.getExternalStorageDirectory().getAbsolutePath();

                    // Credentials
                    session = jsch.getSession(username, hostname, defaultPort);
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.setPassword(password.getBytes());

                    // Start session and channel
                    session.connect();
                    Channel channel = session.openChannel("sftp");
                    channel.connect();
                    System.out.println("Connected");

                    // Upload file
                    ChannelSftp sftp = (ChannelSftp) channel;
                    sftp.put(sourceDirectory,destinationDirectory);

                    Boolean success = true;
                    if(success){
                        System.out.println("The file has been uploaded succesfully");
                    }

                    // Close channel and session
                    channel.disconnect();
                    session.disconnect();

                    // Exception handling
                } catch (JSchException e) {
                    System.out.println(e.getMessage().toString());
                    e.printStackTrace(System.out);
                } catch (Exception e) {
                    System.out.println(e.getMessage().toString());
                    e.printStackTrace(System.out);
                } finally {
                    System.out.println("Disconnected");
                }
            }
        });

        // How to download from server on buttonClick
        downloadServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSch jsch = new JSch();
                Session session = null;
                try {
                    Environment.getExternalStorageDirectory().getAbsolutePath();

                    // Credentials
                    session = jsch.getSession(username, hostname, defaultPort);
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.setPassword(password.getBytes());

                    // Start session and channel
                    session.connect();
                    Channel channel = session.openChannel("sftp");
                    channel.connect();
                    System.out.println("Connected");

                    // Download file
                    ChannelSftp sftp = (ChannelSftp) channel;
                    sftp.get(remote_src_path,lcl_src_path);

                    Boolean success = true;
                    if(success){
                        System.out.println("The file has been downloaded succesfully");
                    }

                    // Close channel and session
                    channel.disconnect();
                    session.disconnect();

                    // Exception handling
                } catch (JSchException e) {
                    System.out.println(e.getMessage().toString());
                    e.printStackTrace(System.out);
                } catch (Exception e) {
                    System.out.println(e.getMessage().toString());
                    e.printStackTrace(System.out);
                } finally {
                    System.out.println("Disconnected");
                }
            }
        });

        // How to start custom script on server on buttonClick
        startCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSch jsch = new JSch();
                Session session = null;

                try {
                    Environment.getExternalStorageDirectory().getAbsolutePath();

                    // Credentials
                    session = jsch.getSession(username, hostname, defaultPort);
                    session.setConfig("StrictHostKeyChecking", "no");
                    session.setPassword(password.getBytes());

                    // Start session and channel
                    session.connect();
                    ChannelExec channelssh = (ChannelExec)session.openChannel("exec");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    channelssh.setOutputStream(baos);

                    // Execute command
                    channelssh.setCommand(myBashCommand);
                    channelssh.connect();
                    System.out.println("Connected");

                    Boolean success = true;
                    if(success){
                        System.out.println("Command run successfully!");
                    }

                    // Close channel and session
                    channelssh.disconnect();
                    session.disconnect();

                    // Exception handling
                } catch (JSchException e) {
                    System.out.println(e.getMessage().toString());
                    e.printStackTrace(System.out);
                } catch (Exception e) {
                    System.out.println(e.getMessage().toString());
                    e.printStackTrace(System.out);
                } finally {
                    System.out.println("Disconnected");
                }
            }
        });
    }

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
