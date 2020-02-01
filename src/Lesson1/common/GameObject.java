package Lesson1.common;

import java.awt.*;

public interface GameObject {
    void update(GameCanvas canvas, float deltaTime); //обновление своих координат
    void render(GameCanvas canvas, Graphics g);   // отрисовывается
}
