package ru.gb.jt.network;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Слушает порт
 * отдавать листнеру socket
 * Фабрика сокетов
 * Отдельный поток сервера
 */
public class ServerSocketThread extends Thread {

    private int port;
    private int timeuot;
    ServerSockedThreadListener listener;

    /**
     * при создании потока сервака
     * @param listener Слушатель событий. Мы требуем чтобы нас кто-то слушал.
     */
    public ServerSocketThread(ServerSockedThreadListener listener,String name, int port,int timeout) {
        super(name);
        this.port = port;
        this.timeuot=timeout;
        this.listener=listener;
        start();
    }

    /**
     * Сервер будет ожидать входящего соединения по эксепту
     * => будет ждать бесконечное количество клиентв
     * При соединении собщает об этом серверу
     * для того чтоб он создавал массив из сокетов.
     */
    @Override
    public void run() {
        listener.onServerStart(this); // сообщает о старте
//создаем сервер сокет на порте
        try (ServerSocket server = new ServerSocket(port)) {
            server.setSoTimeout(timeuot); // установка Timeout для периодического выхода из цикла, для проверки флага isinterapted
            listener.onServerTimeout(this, server); // собщает о таймауте
            while (!isInterrupted()) {
                Socket socket;
                try {
                    socket = server.accept();
                } catch (SocketTimeoutException e) {
                    listener.onServerException(this,e);  //сообщает об исключениях
                    continue;
                }
                listener.onSocketAccepted(this,server,socket);  //сообщает о соединении
            }
        } catch (IOException e) {
            listener.onServerException(this,e);
        } finally {
            listener.onServerStop(this);
        }
    }
}
