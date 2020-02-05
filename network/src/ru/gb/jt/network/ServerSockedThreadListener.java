package ru.gb.jt.network;


import java.net.ServerSocket;
import java.net.Socket;

/**
 *Интерфейс Для прослойки между network и Chat
 *Он односторонний.
 * т.к. у нас есть только события сервера
 * теперь если кому-то будет интересна информация сервера может стать его слушателем
 */
public interface ServerSockedThreadListener {

    void onServerStart(ServerSocketThread thread);
    void onServerStop(ServerSocketThread thread);
    void onServerSocketCreated(ServerSocketThread thread, ServerSocket server);
    void onServerTimeout(ServerSocketThread thread, ServerSocket server);
    void onSocketAccepted(ServerSocketThread thread, ServerSocket server, Socket socket);
    void onServerException(ServerSocketThread thread, Throwable exception);

}
