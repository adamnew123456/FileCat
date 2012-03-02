package translib;

import java.net.*;
import java.io.*;

public class FileTrans 
{
    static final int BUFFER_SIZE=2048;

    // Reads in all data that can be stuffed into a buffer
    private static int readChunk(InputStream input, ByteArrayOutputStream bytes, byte[] buffer) throws
        IOException
    {
        int read = input.read(buffer, 0, BUFFER_SIZE);
        while (read < BUFFER_SIZE)
        {
            read += input.read(buffer, read, BUFFER_SIZE - read);
        }
        bytes.write(buffer);
        return read;
    }

    // Reads all data over the socket that cannot be stuffed into a buffer
    private static void readLeftover(InputStream input, ByteArrayOutputStream bytes) throws
        IOException
    {
        int data = 0;
        while ((data = input.read()) != -1)
            bytes.write(data);
    }

    public static void doRead(int port, String outname) throws 
        IOException 
    {
        ServerSocket server = new ServerSocket(port);
        Socket client = server.accept();

        // Read the file header to know how much to read
        DataInputStream ds = new DataInputStream(client.getInputStream());
        int size = ds.readInt();

        FileOutputStream os = new FileOutputStream(outname);
        InputStream is = client.getInputStream();

        byte[] buffer = new byte[BUFFER_SIZE];
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        while (size >= BUFFER_SIZE)
        {
            size -= read_chunk(is, bs, buffer);
            // This prevents the stack from growing too large on
            // Android devices. Apparently, they can't fit a whole
            // 9.9Mb file into the stack.
            os.write(bs.toByteArray());
            bs.reset();
        }

        read_leftover(is, bs);
        os.write(bs.toByteArray());

        server.close();
        client.close();
        is.close();
        ds.close();
        os.close();
    }

    public static void doWrite(String host, int port, String inname) throws
        IOException
    {
        Socket client = new Socket(host, port);
        DataOutputStream ds = new DataOutputStream(client.getOutputStream());
        File input = new File(inname);

        int length = (int)(input.length());
        ds.writeInt(length);

        FileInputStream is = new FileInputStream(input);
        OutputStream os = client.getOutputStream();

        int read;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((read = is.read(buffer, 0, BUFFER_SIZE)) > 0)
        {
            os.write(buffer, 0, read);
        }

        os.flush();
        os.close();
        ds.close();
        is.close();
    }
}
