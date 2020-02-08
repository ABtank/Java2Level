package ru.gb.jt.chat.server.core;

import ru.gb.jt.chat.common.Library;
import ru.gb.jt.network.SocketThread;
import ru.gb.jt.network.SocketThreadListener;

import java.net.Socket;

public class ClientThread extends SocketThread {

    private String nickname;
    private boolean isAuthorized;

    public ClientThread(SocketThreadListener listener, String name, Socket socket) {
        super(listener, name, socket);
    }

    public boolean isAuthorized(){
        return isAuthorized;
    }

    public String getNickname() {
        return nickname;
    }

    /**
     * Говорим клиенту что он авторизован
     * Он ставит галочку авторизации
     * устанавливает nickname
     * и он посылает собщение в SocketThread о том что он Accepted
     */
    void authAccept(String nickname){
        isAuthorized=true;
        this.nickname=nickname;
        sendMessage(Library.getAuthAccept(nickname));
    }

    void authFail(){
        sendMessage(Library.getAuthDenied());
        close();
    }

    void msgFormatError(String msg){
        sendMessage(Library.getMsgFormatError(msg));
        close();
    }

}
