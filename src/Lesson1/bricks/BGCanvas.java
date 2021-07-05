package Lesson1.bricks;


import Lesson1.common.GameCanvas;
import Lesson1.common.GameObject;

import java.awt.*;

public class BGCanvas implements GameObject {
    protected Color color;
    private static final float AMPLITUDE = 255f / 2f;
    private float time;

    @Override
    public void update(GameCanvas canvas, float deltaTime) {
        time += deltaTime;
        color = new Color(
                (int) (Math.round(AMPLITUDE + AMPLITUDE * (float) Math.sin(time * 2f))), //R
                (int) (Math.round(AMPLITUDE + AMPLITUDE * (float) Math.sin(time * 3f))), //G
                (int) (Math.round(AMPLITUDE + AMPLITUDE * (float) Math.sin(time)))  //B
        );

    }

    @Override
    public void render(GameCanvas canvas, Graphics g) {
        canvas.setBackground(color);
    }
}
