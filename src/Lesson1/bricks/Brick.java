package Lesson1.bricks;

import Lesson1.common.GameCanvas;
import Lesson1.common.Sprite;

import java.awt.*;

public class Brick extends Sprite {

    private final Color color = new Color(
            (int) (Math.random() * 255), //R
            (int) (Math.random() * 255), //G
            (int) (Math.random() * 255)  //B
    );
    //скорость
    private float vX = (float) (100f + (Math.random() * 200f));
    private float vY = (float) (100f + (Math.random() * 200f));


    Brick() {
        //размеры мяча
        halfHeight = 20 + (float) (Math.random() * 50f);
        halfWidth = halfHeight;
    }

    Brick(int x, int y) {
        this();
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(GameCanvas canvas, float deltaTime) {
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
   public void render(GameCanvas canvas, Graphics g) {
        //установка цвета

        g.setColor(color);
        //отрисовка фигуры
        g.fillRect((int) getLeft(), (int) getTop(),
                (int) getWidth(), (int) getHeight());
    }

}
