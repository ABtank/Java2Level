package Lesson1;

import javax.swing.*;
import java.awt.*;

/**
 * Чистый лист бумаги
 */

public class GameCanvas extends JPanel {

    MainCircles gameController; //Чтоб вызывать из канвы методы MainCircles
    long lastFrameTime; // время последнего обновления (предыдущее время)
    static float deltaTime;

    /**
     * Конструктор канвы
     * @param gameController для получения ссылки на MainCircles и сохрание его,
     *                      чтоб с его помощью вызывать метод onDrowFrame.
     */
    GameCanvas(MainCircles gameController) {
        //setBackground(Color.GRAY);  //установка цвета
        this.gameController = gameController;
        lastFrameTime = System.nanoTime();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //отрисовываем панель
        // g.drawOval(0,0,100,100);

        /**
         * 60fps = 1000/60=16,6 мсек
         */
        long currentTime = System.nanoTime(); //текущее время
        deltaTime = (currentTime - lastFrameTime) * 0.000000001f;
        gameController.onDrowFrame(this, g, deltaTime);
        lastFrameTime = currentTime; //обновляем время последнего обновления
        try {
            Thread.sleep(17); // заставить канву заснуть на 17мсек
        } catch (InterruptedException e) {
            e.printStackTrace(); //отработка исключения
        }
        setBackground(BGCanvas.color);
        repaint();  //перерисовка
    }

    /**
     * Размеры конвы
     * @return
     */
    public int getLeft(){ return 0;}        // Левая граница конвы
    public int getRight(){ return getWidth() -1;} // Правая граница
    public int getTop(){return 0;}          // Верхняя граница конвы
    public int getBottom(){return getHeight() - 1;} //Нижняя граница
}
