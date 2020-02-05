package ru.gb.jt.chat.server.core;


import ru.gb.jt.network.ServerSockedThreadListener;
import ru.gb.jt.network.ServerSocketThread;
import ru.gb.jt.network.SocketThread;
import ru.gb.jt.network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 */
public class ChatServer implements ServerSockedThreadListener, SocketThreadListener {

    ServerSocketThread server;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");

    public void start(int port) {
        if (server != null && server.isAlive()) {
            System.out.println("Server already started");
        } else {
            server = new ServerSocketThread(this, "Server", port, 2000);
        }
    }

    public void stop() {
        if (server != null || server.isAlive()) {
            System.out.println("Server is not running");
        } else {
            server.interrupt();
        }
    }

    private void putLog(String msg) {
        msg = DATE_FORMAT.format(System.currentTimeMillis()) + Thread.currentThread().getName() + ": " + msg;
    }


    /**
     * Server methods
     * Обработка событий треда сервера
     */

    @Override
    public void onServerStart(ServerSocketThread thread) {
        putLog("Server thread started");
    }

    @Override
    public void onServerStop(ServerSocketThread thread) {
        putLog("Server thread stopped");
    }

    @Override
    public void onServerSocketCreated(ServerSocketThread thread, ServerSocket server) {
        putLog("Server socked created");
    }

    @Override
    public void onServerTimeout(ServerSocketThread thread, ServerSocket server) {
      //  putLog("Server socked timeout");
    }

    /**
     * на каждом соединении генерим новый поток
     * @param thread где
     * @param server создан
     * @param socket создан
     */
    @Override
    public void onSocketAccepted(ServerSocketThread thread, ServerSocket server, Socket socket) {
        putLog("Client connected");
        //При получении серверного сокета, мы должны его отдать(создать) в нвый поток. Чтоб не зациклится.
        String name="SocketThread "+socket.getInetAddress()+":"+socket.getPort();
        new SocketThread(this,name,socket);
    }

    @Override
    public void onServerException(ServerSocketThread thread, Throwable exception) {
        exception.printStackTrace();
    }


    /**
     * Socket methods
     * Обработка событий всех сокет тредов
     */

    @Override
    public synchronized void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Socket started");
    }

    @Override
    public synchronized void onSocketStop(SocketThread thread) {
        putLog("Socket stopped");
    }

    @Override
    public synchronized void onSocketReady(SocketThread thread, Socket socked) {
        putLog("Socket Ready");
    }

    /**
     * Что делать серверу при получении строки от клиента
     */
    @Override
    public synchronized void onReceiveString(SocketThread thread, Socket socket, String msg) {
        thread.sendMassage("Echo:"+msg);
    }

    @Override
    public synchronized void onSocketException(SocketThread thread, Exception exception) {
        exception.printStackTrace();
    }
}
