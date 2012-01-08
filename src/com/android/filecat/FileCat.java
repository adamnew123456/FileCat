package com.android.filecat;

import java.io.*;

import android.app.*;
import android.content.*;
import android.view.*;
import android.util.*;
import android.widget.*;

import android.os.Bundle;

public class FileCat extends Activity
{
    EditText e_ipaddr, e_port, e_filename;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        e_ipaddr = (EditText)findViewById(R.id.ipaddr);
        e_port = (EditText)findViewById(R.id.port);
        e_filename = (EditText)findViewById(R.id.fname);

        Button b_send = (Button)findViewById(R.id.sendfile);
        Button b_recv = (Button)findViewById(R.id.recvfile);

        b_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)    
            {
                try
                {
                    int port = Integer.parseInt(e_port.getText().toString());
                    Networking.do_write(e_ipaddr.getText().toString(), 
                            port, 
                            e_filename.getText().toString());
                }
                catch (Exception e)
                {
                    Log.e("FileCat", e.toString());
                }
            }
        });

        b_recv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                try 
                {
                    int port = Integer.parseInt(e_port.getText().toString());
                    Networking.do_read(port, e_filename.getText().toString());
                }
                catch (Exception e)
                {
                    Log.e("FileCat", e.toString());
                }
            }
        });
    }
}
