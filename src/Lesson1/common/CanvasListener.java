package Lesson1.common;

import java.awt.*;

public interface CanvasListener {
    /**
     * Метод по факту отрисовки канвы
     *
     * @param canvas    какая конва отрисовалась
     * @param g         обьект графики
     * @param deltaTime сколько времени прошло с предыдущей отрисовки
     */
    void onDrowFrame(GameCanvas canvas, Graphics g, float deltaTime);
}
