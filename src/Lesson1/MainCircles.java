package Lesson1;

import javax.swing.*;
import java.awt.*;

/**
 * ИГРОВОЙ ЦИКЛ
 * 1)Пользовательский ввод
 * 2)Действие
 * 3)Отрисовка
 */
public class MainCircles extends JFrame {
    //задаем размеры окра
    private static final int POS_X = 400;
    private static final int POS_Y = 200;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    Sprite[] sprites = new Sprite[10];
    Sprite backGround = new Sprite();


    public static void main(String[] args) {
        /**
         * Запуск окошка
         */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainCircles();
            }
        });
    }

    /**
     * Конструктор окна
     */
    private MainCircles() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // делаем закрытие окна по нажатию кнопки X
        setBounds(POS_X, POS_Y, WINDOW_WIDTH, WINDOW_HEIGHT); // устанавливаем границы нашего окна
        initApplication();
        GameCanvas canvas = new GameCanvas(this); // создаем канву в окне
        add(canvas, BorderLayout.CENTER); // добавляем канву в окно
        setTitle("Circles"); // навание окна
        setVisible(true);  // делаем окно видимым
    }

    public static int getWindowWidth() {
        return WINDOW_WIDTH;
    }

    public static int getWindowHeight() {
        return WINDOW_HEIGHT;
    }

    /**
     * Создаем мячи
     */
    private void initApplication() {
        backGround = new BGCanvas();
        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = new Ball();
        }
    }

    /**
     * Метод по факту отрисовки канвы
     *
     * @param canvas    какая конва отрисовалась
     * @param g         обьект графики
     * @param deltaTime сколько времени прошло с предыдущей отрисовки
     */
    public void onDrowFrame(GameCanvas canvas, Graphics g, float deltaTime) {
        update(canvas, deltaTime); // обноврение S=v*t -  расстояние
        render(canvas, g); // отрисовка
    }

    /**
     * Метод обновления
     *
     * @param canvas    какая канва обновилась
     * @param deltaTime сколько времени прошло
     */
    private void update(GameCanvas canvas, float deltaTime) {
        backGround.update(canvas, deltaTime);
        for (int i = 0; i < sprites.length; i++) {
            sprites[i].update(canvas, deltaTime);
        }
    }

    /**
     * Метод отрисовки
     *
     * @param canvas на какой конве отрисовать
     * @param g      объект графики с помощью которого отрисовать
     */
    private void render(GameCanvas canvas, Graphics g) {
        backGround.render(canvas, g);
        for (int i = 0; i < sprites.length; i++) {
            sprites[i].render(canvas, g);
        }
    }

}
