package com.android.filecat;

import java.io.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.util.*;
import android.widget.*;

public class FileCat extends Activity
{
    EditText e_ipaddr, e_port, e_filename;
    TextView t_log;
    
    class SendTask extends AsyncTask<Object,Void,Boolean>
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

        protected void onPostExecute(Boolean success)
        {
            if (success.booleanValue())
            {
                logging.append("Succeeded\n");
            }
            else
            {
                logging.append("Failed\n");
            }
        }

        protected Boolean doInBackground(final Object... args)
        {
            boolean success = true;
            try
            {
                int port = Integer.parseInt(s_port);
                Networking.do_write(s_ipaddr, port, s_filename);
            }
            catch (Exception e)
            {
                success = false;
            }

            return new Boolean(success);
        }
    }

    class ReadTask extends AsyncTask<Object,Void,Boolean>
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

        protected void onPostExecute(Boolean success)
        {
            if (success.booleanValue())
            {
                logging.append("Succeeded\n");
            }
            else
            {
                logging.append("Failed\n");
            }
        }

        protected Boolean doInBackground(final Object... args)
        {
            boolean success = true;
            try 
            {
                int port = Integer.parseInt(s_port);
                Networking.do_read(port, s_filename);
            }
            catch (Exception e)
            {
                success = false;
            }

            return new Boolean(success);
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
