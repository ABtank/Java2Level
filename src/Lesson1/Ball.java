package Lesson1;

import java.awt.*;

public class Ball extends Sprite {

    private final Color color = new Color(
            (int) (Math.random() * 255), //R
            (int) (Math.random() * 255), //G
            (int) (Math.random() * 255)  //B
    );
    //скорость
    private float vX = (float) (100f + (Math.random() * 200f));
    private float vY = (float) (100f + (Math.random() * 200f));


    Ball() {
        //размеры мяча
        halfHeight = 20 + (float) (Math.random() * 50f);
        halfWidth = halfHeight;
    }

    @Override
    void update(GameCanvas canvas, float deltaTime) {
        x += vX * deltaTime;
        y += vY * deltaTime;
        if (getLeft() < canvas.getLeft()) {
            setLeft((canvas.getLeft()));
            vX = -vX;
        }
        if (getRight() > canvas.getRight()) {
            setRight((canvas.getRight()));
            vX = -vX;
        }
        if (getTop() < canvas.getTop()) {
            setTop((canvas.getTop()));
            vY = -vY;
        }
        if (getBottom() > canvas.getBottom()) {
            setBottom((canvas.getBottom()));
            vY = -vY;
        }
    }

    @Override
    void render(GameCanvas canvas, Graphics g) {
       //установка цвета

        g.setColor(color);
        //отрисовка фигуры
        g.fillOval((int) getLeft(), (int) getTop(),
                (int) getWidth(), (int) getHeight());
    }

}
