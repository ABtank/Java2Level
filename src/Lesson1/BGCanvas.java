package Lesson1;


import java.awt.*;

public class BGCanvas extends Sprite {
    protected static Color color;


    BGCanvas() {
        halfHeight = MainCircles.getWindowHeight();
        halfWidth = MainCircles.getWindowWidth();
    }


    void update(GameCanvas canvas, float deltaTime) {

    }

    @Override
    void render(GameCanvas canvas, Graphics g) {

        Color color = new Color(
                (int) (Math.random() * 255), //R
                (int) (Math.random() * 255), //G
                (int) (Math.random() * 255)  //B
        );
        g.setColor(color);
        g.fillRect((int) x, (int) y,
                (int) getWidth(), (int) getHeight());
    }
}
