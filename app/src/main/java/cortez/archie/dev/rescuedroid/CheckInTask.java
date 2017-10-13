package cortez.archie.dev.rescuedroid;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 10/13/2017.
 */

public class CheckInTask extends AsyncTask<Void, Void, String> {

    private final String dstAddress;
    private final int dstPort;

    private String response = "";

    private static final int PORT = 8001;
    private String data;

    CheckInTask(String destinationAddress) {
        dstAddress = destinationAddress;
        dstPort = PORT;
    }

    @Override
    protected String doInBackground(Void... arg0) {

        Socket socket = null;

        try {
            socket = new Socket(dstAddress, dstPort);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.write(data + "\n");
            printWriter.flush();
            printWriter.close();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    public void setData(String data) {
        this.data = data;
    }
}