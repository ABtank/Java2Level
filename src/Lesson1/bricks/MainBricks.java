package Lesson1.bricks;

import Lesson1.common.CanvasListener;
import Lesson1.common.GameCanvas;
import Lesson1.common.GameObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ИГРОВОЙ ЦИКЛ
 * 1)Пользовательский ввод
 * 2)Действие
 * 3)Отрисовка
 */
public class MainBricks extends JFrame implements CanvasListener {
    //задаем размеры окра
    private static final int POS_X = 400;
    private static final int POS_Y = 200;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private GameObject[] gameObjects = new GameObject[1];
    private int gameObjectCount;


    public static void main(String[] args) {
        /**
         * Запуск окошка
         */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainBricks();
            }
        });
    }

    /**
     * Конструктор окна
     */
    private MainBricks() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // делаем закрытие окна по нажатию кнопки X
        setBounds(POS_X, POS_Y, WINDOW_WIDTH, WINDOW_HEIGHT); // устанавливаем границы нашего окна
        initApplication();
        GameCanvas canvas = new GameCanvas(this); // создаем канву в окне
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println(e.getButton());
                if (e.getButton() == MouseEvent.BUTTON1)
                    addGameObject(new Brick(e.getX(), e.getY()));
                else if (e.getButton() == MouseEvent.BUTTON3)
                    removeGameObject();
            }
        });
        add(canvas, BorderLayout.CENTER); // добавляем канву в окно
        setTitle("Circles"); // навание окна
        setVisible(true);  // делаем окно видимым
    }


    /**
     * Создаем мячи и фон
     */
    private void initApplication() {
        addGameObject(new BGCanvas());
        addGameObject(new Brick());
    }

    /**
     * Удаление спрайтов с экрана
     */
    private void removeGameObject() {
        if (!(gameObjects[gameObjectCount - 1] instanceof BGCanvas))
            gameObjectCount--;
    }
    /**
     * добавление спрайтов на экран
     */
    private void addGameObject(GameObject sprite) {
        if (gameObjectCount == gameObjects.length) {
            GameObject[] newGameObjects = new GameObject[gameObjects.length * 2];
            System.arraycopy(gameObjects, 0, newGameObjects, 0, gameObjects.length);
            gameObjects = newGameObjects;
        }
        gameObjects[gameObjectCount++] = sprite;
    }

    /**
     * Метод по факту отрисовки канвы
     *
     * @param canvas    какая конва отрисовалась
     * @param g         обьект графики
     * @param deltaTime сколько времени прошло с предыдущей отрисовки
     */
    @Override
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
        for (int i = 0; i < gameObjectCount; i++) {
            gameObjects[i].update(canvas, deltaTime);
        }
    }

    /**
     * Метод отрисовки
     *
     * @param canvas на какой конве отрисовать
     * @param g      объект графики с помощью которого отрисовать
     */
    private void render(GameCanvas canvas, Graphics g) {
        for (int i = 0; i < gameObjectCount; i++) {
            gameObjects[i].render(canvas, g);
        }
    }

}
