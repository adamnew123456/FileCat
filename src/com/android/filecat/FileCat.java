package com.android.filecat;

import java.io.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.util.*;
import android.widget.*;

import translib.FileTrans;

public class FileCat extends Activity
{
    EditText e_ipaddr, e_port, e_filename;
    TextView t_log;
    
    class SendTask extends AsyncTask<Object,Void,String>
    {
        private TextView logging;
        private String s_ipaddr, s_port, s_filename;

        protected SendTask(TextView tv, EditText ipaddr, EditText port, EditText filename)
        {
            super();
            logging = tv;
            s_ipaddr = ipaddr.getText().toString();
            s_port = port.getText().toString();
            s_filename = filename.getText().toString();
        }

        protected void onPreExecute()
        {
            logging.append("Starting... ");
        }

        protected void onPostExecute(String success)
        {
            if (success == null)
            {
                logging.append("Succeeded\n");
            }
            else
            {
                logging.append("Failed with  exception '" + success + "'\n");
            }
        }

        protected String doInBackground(final Object... args)
        {
            String success = null;
            try
            {
                int port = Integer.parseInt(s_port);
                FileTrans.doWrite(s_ipaddr, port, s_filename);
            }
            catch (Exception e)
            {
                success = e.toString();
            }

            return success;
        }
    }

    class ReadTask extends AsyncTask<Object,Void,String>
    {
        private TextView logging;
        private String s_ipaddr, s_port, s_filename;

        protected ReadTask(TextView tv, EditText ipaddr, EditText port, EditText filename)
        {
            super();
            logging = tv;
            s_ipaddr = ipaddr.getText().toString();
            s_port = port.getText().toString();
            s_filename = filename.getText().toString();
        }

        protected void onPreExecute()
        {
            logging.append("Starting... ");
        }

        protected void onPostExecute(String success)
        {
            if (success == null)
            {
                logging.append("Succeeded\n");
            }
            else
            {
                logging.append("Failed with exception '" + success + "'\n");
            }
        }

        protected String doInBackground(final Object... args)
        {
            String success = null;
            try 
            {
                int port = Integer.parseInt(s_port);
                FileTrans.doRead(port, s_filename);
            }
            catch (Exception e)
            {
                success = e.toString();
            }

            return success;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        e_ipaddr = (EditText)findViewById(R.id.ipaddr);
        e_port = (EditText)findViewById(R.id.port);
        e_filename = (EditText)findViewById(R.id.fname);

        t_log = (TextView)findViewById(R.id.log);

        Button b_send = (Button)findViewById(R.id.sendfile);
        Button b_recv = (Button)findViewById(R.id.recvfile);

        b_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                new SendTask(t_log, e_ipaddr, e_port, e_filename).execute();
            }
        });

        b_recv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                new ReadTask(t_log, e_ipaddr, e_port, e_filename).execute();
            }
        });
    }
}
