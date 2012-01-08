import java.io.*;

public class CLI {
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            help();
        }
        else if (args[0].equals("send"))
        {
            if (args.length != 4)
            {
                help();
            }
            else
            {
                try
                {
                    Networking.do_write(args[1], Integer.parseInt(args[2]), args[3]);
                }
                catch (IOException e)
                {
                    System.out.println(">> " + e.toString() + " <<");
                }
            }
        }
        else if (args[0].equals("read"))
        {
            if (args.length != 3)
            {
                help();
            }
            else
            {
                try
                {
                    Networking.do_read(Integer.parseInt(args[1]), args[2]);
                }
                catch (IOException e)
                {
                    System.out.println(">> " + e.toString() + " <<");
                }
            }
        }
        else
        {
            System.out.println(">> " + args[0] + " <<");
        }
    }

    static void help()
    {
        System.out.println("java CLI ([send ip port input] || [read port output])");
    }
}
