package com.callumm.es;

import com.callumm.es.runnable.Listen;

public class Server {

    private static Thread listenThread = null;

    public static void startServer() {
        // Start listening
        listenThread = new Thread(new Listen());
        listenThread.start();

    }

    public static void killListenThread() {
        listenThread.interrupt();
    }
}
