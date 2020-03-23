package Lesson4;

import Lesson4.network.ServerSocketThread;

public class ChatServer {
    ServerSocketThread server;

    public void start(int port) {
        if (server != null && server.isAlive())
            System.out.println("Server already started");
        else
            server = new ServerSocketThread("Server", port);
    }

    public void stop() {
        if (server == null || !server.isAlive()) {
            System.out.println("Server is not running");
        } else {
            server.interrupt();
        }
    }
}
