package Lesson4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

public class ClientGUI extends JFrame implements ActionListener, KeyListener, Thread.UncaughtExceptionHandler {

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
    private final JTextArea tfMessage = new JTextArea();
    private final JButton btnSend = new JButton("Send");



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
        tfMessage.addKeyListener(this);

        btnSend.setFocusable(false);

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
     * Запись лога в файл
     */
    private void writeTextInFile() {
        try {
            FileOutputStream flLog = new FileOutputStream("Log.txt", true);
            PrintStream ps = new PrintStream(flLog);
            ps.print(log.getText());
            //flLog.write(log.getText().getBytes());
            //flLog.close();
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        } catch (IOException f) {
            System.out.println(f.getMessage());
        }
    }

    /**
     * Обработка нажатий кнопок на панели
     *
     * @param e
     */
    @Override
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
    }

    /**
     * Обрабоотка исключений
     * @param t
     * @param e
     */
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

    /**
     * Методы обработки нажатия клавиш
     *
     * @keyTyped - кроткое нажатие на клавишу
     * @keyPressed - длинные одновременные нажатия
     * @keyReleased - когда клавишу отжимаем, получаем событие в данном методе
     */

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Date date=new Date();
        System.out.print(e.getKeyChar() + "=" + e.getKeyCode() + " ");
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("Enter");
            log.setText(log.getText() +date.toGMTString()+":\n"+ tfMessage.getText());
            writeTextInFile();
            tfMessage.setText(null);
        }
    }

}
