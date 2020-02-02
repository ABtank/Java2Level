package SnakeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {

    private final int SIZE = 300; // размер поля
    private final int DOT_SIZE = 16; //секция змейки
    private final int ALL_DOTS = 400; //всего игровых единиц
    private Image dot; //поле картинки под игровую яцейку
    private Image apple; // поле картинки под яблоко
    private int appleX; //позиция яблока по х
    private int appleY; //позиция яблока по у
    private int[] x = new int[ALL_DOTS]; //массив чтоб сохрянять все положения змейки по х
    private int[] y = new int[ALL_DOTS];
    private int dots; // размер змейки в данный момент
    private Timer timer; //стандартный таймер
    /**
     * Поля для определения текущего направления змейки
     */
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;

    public GameField() {
        addKeyListener(new FieldKeyListner());
        setFocusable(true);
        setBackground(Color.BLACK);
        loadImages();
        initGame();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);//рисуем яблоко
            String str = ("Score:" + (dots - 3));
            g.setColor(Color.WHITE);
            g.drawString(str, 260, 24);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this); //перерисовываем всю змейку
            }
        } else {
            String str = "Game Over";
            g.setColor(Color.WHITE);
            g.drawString(str, 125, SIZE / 2);
        }
    }

    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i + DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(350, this); //скорость отрисовки
        timer.start();
        createApple();
    }

    public void createApple() {
        int check;
        do {
            check = 0;
            appleX = new Random().nextInt(20) * DOT_SIZE;
            appleY = new Random().nextInt(20) * DOT_SIZE;
            for (int i = dots; i > 0; i--) {
                if (x[i] == appleX && y[i] == appleY) {
                    check++;
                }
            }
        } while (check > 0);

    }

    public void loadImages() {
        ImageIcon iiApple = new ImageIcon("apple.png");
        apple = iiApple.getImage();
        ImageIcon iiDot = new ImageIcon("dot.png");
        dot = iiDot.getImage();
    }


    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            createApple();
        }
    }

    public void checkCollisions() {
        //столкнулась ли сама с собой
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        //не вышла ли за пределы поля
        if (x[0] > SIZE) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (y[0] > SIZE) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollisions();
            move();
        }
        repaint(); //перерисовка
    }

    class FieldKeyListner extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down) {
                up = true;
                left = false;
                right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                down = true;
                left = false;
                right = false;
            }
            if (key == KeyEvent.VK_ENTER && inGame == false) {
                inGame = true;
                dots = 3;
                for (int i = 0; i < dots; i++) {
                    x[i] = 48 - i + DOT_SIZE;
                    y[i] = 48;
                }
                createApple();
            }
        }
    }
}
