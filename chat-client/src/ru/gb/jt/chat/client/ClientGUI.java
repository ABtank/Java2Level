package ru.gb.jt.chat.client;

import ru.gb.jt.network.SocketThread;
import ru.gb.jt.network.SocketThreadListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.util.Date;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, SocketThreadListener {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Alweys on top");
    private final JTextField tfLogin = new JTextField("Iurii");
    private final JPasswordField tfPassword = new JPasswordField("***");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnDisconnect = new JButton("<html><b><i>Disconnect</i></b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private boolean shownIoErrors = false;
    /**
     * Создаем сокет треад снаружи.
     * Т.к. у него будут методы не только в connect()
     */
    private SocketThread socketThread;

    /**
     * Лист для юзеров
     */
    private final JList<String> userList = new JList<>();

    private ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);//установка по центру экрана
        setSize(WIDTH, HEIGHT);
        setTitle("Client GUI");
        log.setEditable(false);//запрещаем писать непосредственно в данном поле

        /**
         * Устанавливаем скролл на панель чата и панель юзеров
         * а точнее засовываем в панель со скролом панельки которые нам надо скролить
         */
        JScrollPane scrollLog = new JScrollPane(log);
        JScrollPane scrollUser = new JScrollPane(userList);
        JScrollPane scrollMessage = new JScrollPane(tfMessage);
        /**
         * Создали список с юзерами
         */
        String[] users = {"user1", "user2", "user3", "user4", "user5", "user_with_a_efwpvd"};
        userList.setListData(users);
        /**
         * Установка размера окна с юзерами
         */
        scrollUser.setPreferredSize(new Dimension(100, 0));
        panelBottom.setPreferredSize(new Dimension(WIDTH, 60));
        /**
         * Создаем листнеры
         */
        cbAlwaysOnTop.addActionListener(this);
        btnSend.addActionListener(this);
        btnDisconnect.addActionListener(this);
        btnLogin.addActionListener(this);
        tfMessage.addActionListener(this);

        //btnSend.setFocusable(false);

        /**
         * Добавляем элементы на панель Top
         */
        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        /**
         * Добавляем элементы на панель Bottom
         */
        panelBottom.add(btnDisconnect, BorderLayout.WEST);
        panelBottom.add(scrollMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        /**
         * Добавляем панели TOP & BOTTOM на основной экран
         */
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);
        add(scrollLog, BorderLayout.CENTER);
        add(scrollUser, BorderLayout.WEST);

        setVisible(true);
    }

    /**
     * Создание своего сокета. Получаем IP адрес и port
     * Создаем скеттреад
     */
    private void connect() {
        try {
            Socket socket = new Socket(tfIPAddress.getText(), Integer.parseInt(tfPort.getText()));
            socketThread = new SocketThread(this, "Client", socket);
        } catch (IOException e) {
            showException(Thread.currentThread(), e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { //Event Dispatching Thread
                new ClientGUI();
            }
        });
        //throw new RuntimeException("Hello from main");

    }

    /**
     * Обработка нажатий кнопок на панели
     *
     * @param e
     */
    /*@Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnLogin) {

        }
        if (src == btnDisconnect) {
            System.exit(0);
        }
        if (src == btnSend) {
            System.out.println("send");
            log.setText(log.getText() + "\n" + tfMessage.getText());
            tfMessage.setText(null);
            writeTextInFile();
        }
        if (src == cbAlwaysOnTop) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        } else {
            throw new RuntimeException("Unknown source:" + src);
        }
    }*/
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        } else if (src == btnSend || src == tfMessage) {
            sendMessage();
        } else if (src == btnLogin) {
            connect();
        } else if (src == btnDisconnect) {
                if (socketThread == null) {
                    System.out.println("Client is not connect");
                } else {
                    String msg = tfMessage.getText();
                    socketThread.sendMassage(msg);
                    socketThread.interrupt();
                }
        } else {
            throw new RuntimeException("Unknown source: " + src);
        }
    }

    /**
     * Составление строки сообщения и отправка его в лог + запись в файл
     */
    private void sendMessage() {
        String msg = tfMessage.getText();
        String username = tfLogin.getText();
        if ("".equals(msg)) return;
        tfMessage.setText(null);
        tfMessage.requestFocusInWindow();
        socketThread.sendMassage(msg);
        // putLog(String.format("%s: %s", username, msg));
//        wrtMsgToLogFile(msg, username);
    }

    /**
     * Запись лога в файл
     */
    private void wrtMsgToLogFile(String msg, String username) {
        try (FileWriter out = new FileWriter("log.txt", true)) {
            out.write(username + ": " + msg + "\n");
            out.flush();
        } catch (IOException e) {
            if (!shownIoErrors) {
                shownIoErrors = true;
                showException(Thread.currentThread(), e);
            }
        }
    }

    /**
     * Добавление строки в гол в отдельном потоке
     * Все что происходит в графическом интерфейсе происходит в спец потоке EventDespetcherThread
     * кторый мы запускаем методом SwingUtilities.invokeLater(new Runnable()
     */
    private void putLog(String msg) {
        if ("".equals(msg)) return;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    /**
     * Получение массива строк текста исключения
     * Составление строки с самой важной информацией
     * Вывод ошибки в диалоговом окне
     */
    private void showException(Thread t, Throwable e) {
        String msg;
        //чтоб получить текст исключения создаем массив
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0) {
            msg = "Empty Stacktrace";
        } else {
            msg = "Exception in " + t.getName() + " " +
                    e.getClass().getCanonicalName() + ": " +
                    e.getMessage() + "\n\t at" + ste[0];
        }
        JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Обрабоотка исключений
     *
     * @param t поток
     * @param e исключение
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        showException(t, e);
        System.exit(1);
    }

    /**
     * Socket thread listener methods
     */

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        putLog("Start");
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        putLog("Stop");
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        putLog("Ready");
    }

    /**
     * Обработка получения строки
     */
    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        putLog(msg);
    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {
        showException(thread, exception);
    }
}

