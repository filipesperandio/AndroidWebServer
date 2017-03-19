package com.filipesperandio.androidwebserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class Server extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        App.main(null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static class App extends NanoHTTPD {

        public App() throws IOException {
            super(8080);
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
        }

        public static void main(String[] args) {
            try {
                new App();
            } catch (IOException ioe) {
                System.err.println("Couldn't start server:\n" + ioe);
            }
        }

        @Override
        public Response serve(IHTTPSession session) {
            String msg = "<html><body><h1>Hello server</h1>\n";
            Map<String, String> parms = session.getParms();
            String cmd = parms.get("cmd");
            if (cmd != null) {
                return newFixedLengthResponse(runCmd(cmd));
            }
            if (parms.get("username") == null) {
                msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
            } else {
                msg += "<p>Hello, " + parms.get("username") + "!</p>";
            }
            return newFixedLengthResponse(msg + "</body></html>\n");
        }

        private String runCmd(String cmd) {
            Process p = null;
            try {
                p = Runtime.getRuntime().exec(cmd);
                List<String> lines = readLines(p.getInputStream());
                return lines.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "Problem running cmd: " + cmd;
            }

        }

        private List<String> readLines(InputStream inputStream) throws IOException {
            InputStreamReader input = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(input);
            List list = new ArrayList();
            String line = reader.readLine();
            while (line != null) {
                list.add(line);
                line = reader.readLine();
            }
            return list;
        }
    }
}
