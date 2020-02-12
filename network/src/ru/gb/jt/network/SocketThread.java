package ru.gb.jt.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Отдельный поток
 * Задача слать сообщения
 * Ожидающий прилета строки
 * Запись строки в ответный ему сокет
 */
public class SocketThread extends Thread {

    private final SocketThreadListener listener;
    private final Socket socket;
    private DataOutputStream out;

    public SocketThread(SocketThreadListener listener, String name, Socket socket) {
        super(name); // имя сокета
        this.listener = listener;
        this.socket = socket;
        start();
    }


    @Override
    public void run() {
        try {
            listener.onSocketStart(this, socket);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            listener.onSocketReady(this,socket);//сообщаем что готовы получать сообщения.
            while (!isInterrupted()) {
                String msg = in.readUTF();
                listener.onReceiveString(this, socket, msg);
            }
        } catch (IOException e) {
            listener.onSocketException(this, e);
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                listener.onSocketException(this,e);
            }
            listener.onSocketStop(this);
        }
    }

    /**
     * Отправка сообщения в выходящий поток текущего сокета
     * @param msg
     * @return
     */
    public synchronized boolean sendMessage(String msg){
       try{
           out.writeUTF(msg);
           out.flush();
           return true;
       }catch (IOException e){
           listener.onSocketException(this,e);
           close();
           return false;
       }
    }

    /**
     *
     */
    public synchronized void close() {
        interrupt();
        try{
            socket.close();
        }catch (IOException e){
            listener.onSocketException(this,e);
        }
    }
}