package ru.gb.jt.chat.server.gui;

import ru.gb.jt.chat.server.core.ChatServer;
import ru.gb.jt.chat.server.core.ChatServerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, ChatServerListener {

    private static final int POS_X = 800;
    private static final int POS_Y = 200;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;

    private final ChatServer chatServer = new ChatServer(this);
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private final JPanel panelTop = new JPanel(new GridLayout(1, 2));
    private final JTextArea log = new JTextArea();

    public static void main(String[] args) {
        /**
         * SwingUtilities класс у которого есть статический метод
         * invokeLater(вызвать позже) который мы вызываем и он принимает на вход
         * реализацию интерфейса (анонимный класс Runnable)
         * который переопределяет
         * публичный ничего не возвращающий метод run
         * внутри которого создаем новый ServerGUI как обьект
         *
         * Так сделанно для того чтоб наше окошко исполнялось
         * в специально обученном отдельном потоке
         * Event Dispatching Thread
         */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { //Event Dispatching Thread
                new ServerGUI();
            }
        });
        //throw new RuntimeException("Hello from main");
    }

    private ServerGUI() {
        /**у этого потока нужно установить обработчик непойманых исключений
         * по умолчанию.
         * На вход передаем себя
         * для этого имплементим интерфейс Thread.UncaughtExceptionHandler в класс
         * переписываем метод интерфейса
         *
         */
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);

        log.setEditable(false);
        log.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(log);


        /**
         * имплементим в класс конструктор ActionListener
         * переопределяем метод
         */
        btnStart.addActionListener(this);
        btnStop.addActionListener(this);

        panelTop.add(btnStart);
        panelTop.add(btnStop);
        add(panelTop, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnStart) {
            chatServer.start(8189);
        } else if (src == btnStop) {
            //throw new RuntimeException("Hello from EDT");
            chatServer.stop();
        } else {
            throw new RuntimeException("Unknown source:" + src);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        String msg;
        //чтоб получить текст исключения создаем массив
        StackTraceElement[] ste = e.getStackTrace();
        msg = "Exception in " + t.getName() + " " +
                e.getClass().getCanonicalName() + ": " +
                e.getMessage() + "\n\t at" + ste[0];
        JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    @Override
    public void onChatServerMessage(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { //Event Dispatching Thread
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}

