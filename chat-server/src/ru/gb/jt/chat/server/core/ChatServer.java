package ru.gb.jt.chat.server.core;


import ru.gb.jt.chat.common.Library;
import ru.gb.jt.network.ServerSockedThreadListener;
import ru.gb.jt.network.ServerSocketThread;
import ru.gb.jt.network.SocketThread;
import ru.gb.jt.network.SocketThreadListener;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

/**
 * Хранит информацию о всех подключенных Socket и через подключенный класс Socket их генерит
 */
public class ChatServer implements ServerSockedThreadListener, SocketThreadListener {


    private final ChatServerListener listener;
    private ServerSocketThread server;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");
    private Vector<SocketThread> clients = new Vector<>(); // список сокет потоков сервера, не клиентов

    /**
     * Принимаем на вход при создании
     */
    public ChatServer(ChatServerListener listener) {
        this.listener = listener;
    }

    public void start(int port) {
        if (server != null && server.isAlive()) {
            putLog("Server already started");
        } else {
            server = new ServerSocketThread(this, "Server", port, 2000);
        }
    }

    public void stop() {
        if (server == null || !server.isAlive()) {
            putLog("Server is not running");
        } else {
            server.interrupt();
        }
    }

    private void putLog(String msg) {
        msg = DATE_FORMAT.format(System.currentTimeMillis()) +
                Thread.currentThread().getName() + ": " + msg;
        listener.onChatServerMessage(msg);
    }


    /**
     * Server methods
     * Обработка событий треда сервера
     */

    @Override
    public void onServerStart(ServerSocketThread thread) {
        putLog("Server thread started");
        SqlClient.connect();
    }

    @Override
    public void onServerStop(ServerSocketThread thread) {
        putLog("Server thread stopped");
        SqlClient.disConnect();
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).close();
        }
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
     *
     * @param thread где
     * @param server создан
     * @param socket создан
     */
    @Override
    public void onSocketAccepted(ServerSocketThread thread, ServerSocket server, Socket socket) {
        putLog("Client connected");
        //При получении серверного сокета, мы должны его отдать(создать) в нвый поток. Чтоб не зациклится.
        String name = "SocketThread " + socket.getInetAddress() + ":" + socket.getPort();
        new ClientThread(this, name, socket);
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
        ClientThread client = (ClientThread) thread;
        clients.remove(thread);
        if (client.isAuthorized() && !client.isReconnecting()) {
            sendToAuthClients(Library.getTypeBroadcast("Server",
                    client.getNickname() + " disconnected"));
        }
        sendToAuthClients(Library.getUserList(getUsers()));
    }

    @Override
    public synchronized void onSocketReady(SocketThread thread, Socket socked) {
        //добавляем клиентов когда уже потоки ввода вывода готовы
        clients.add(thread);
    }

    /**
     * Что делать серверу при получении строки от клиента
     */
    @Override
    public synchronized void onReceiveString(SocketThread thread, Socket socket, String msg) {
        ClientThread client = (ClientThread) thread;
        if (client.isAuthorized()) {
            handleAuthMessage(client, msg);
        } else {
            handleNonAuthMessage(client, msg);
        }
    }

    /**
     * Сообщение не авторизованным
     *
     * @param client
     * @param msg
     */
    private void handleNonAuthMessage(ClientThread client, String msg) {
        String[] arr = msg.split(Library.DELIMITER);
        if (arr[0].equals((Library.AUTH_NEW_CLIENT_REQUEST)) && arr.length == 4) {
            String nickname = arr[3];
            String login = arr[1];
            String password = arr[2];
            if (null!=SqlClient.getNickname(login, password)) {
                SqlClient.setReNickname(nickname,login,password);
            } else if (SqlClient.getNotExistsClient(nickname, login)) {
                SqlClient.setNewClient(nickname, login, password);
                sendToAuthClients(Library.getTypeBroadcast("Server", nickname + "connected"));
            } else {
                putLog("REGISTRATION FAIL. Nickname: " + nickname + " or login: " + login + " if exists.");
                client.msgRegistrationDenied(nickname, login);
                return;
            }
        } else if (arr.length != 3 || !arr[0].equals(Library.AUTH_REQUEST)) {
            client.msgFormatError(msg);
            return;
        }
        String login = arr[1];
        String password = arr[2];
        String nickname = SqlClient.getNickname(login, password);
        if (nickname == null) {
            putLog("Invalid login attempt" + login);
            client.authFail();
            return;
        } else {
            ClientThread oldClient = findClientByNickname(nickname);
            client.authAccept(nickname);
            if (oldClient == null) {
                sendToAuthClients(Library.getTypeBroadcast("Server", nickname + "connected"));
            } else {
                oldClient.reconnect();
                clients.remove(oldClient);
            }
        }
        sendToAuthClients(Library.getUserList(getUsers()));
    }

    /**
     * Сообщение авторизованным
     *
     * @param
     * @param msg
     */
    private void handleAuthMessage(ClientThread client, String msg) {
        String[] arr = msg.split(Library.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case Library.TYPE_BCAST_CLIENT:
                sendToAuthClients(Library.getTypeBroadcast(client.getNickname(), arr[1]));
                break;
            default:
                client.sendMessage(Library.getMsgFormatError(msg));
        }
    }

    /**
     * Сообщение всем
     *
     * @param msg
     */
    private void sendToAuthClients(String msg) {
        //общее сообщение
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) continue;
            client.sendMessage(msg);
        }
    }

    @Override
    public synchronized void onSocketException(SocketThread thread, Exception exception) {
        exception.printStackTrace();
    }

    /**
     * Формируем строку с пользователями
     *
     * @return строка с пользователями
     */
    public String getUsers() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) continue;
            sb.append(client.getNickname()).append(Library.DELIMITER);
        }
        return sb.toString();
    }

    private synchronized ClientThread findClientByNickname(String nickname) {
        for (int i = 0; i < clients.size(); i++) {
            ClientThread client = (ClientThread) clients.get(i);
            if (!client.isAuthorized()) {
                continue;
            }
            if (client.getNickname().equals(nickname)) {
                return client;
            }
        }
        return null;
    }
}
