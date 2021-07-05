package Lesson1.common;


import java.awt.*;

/**
 * Основа двумерного объекта
 * Центр которого это х,у.
 */
public class Sprite implements GameObject {

    //координаты
    protected float x; // по х
    protected float y; // по у
    protected float halfWidth;  //
    protected float halfHeight; //

    protected float getLeft() {return x - halfWidth;}

    protected void setLeft(float left) {x = left + halfWidth;}

    protected float getRight() {return x + halfWidth;}

    protected void setRight(float right) {x = right - halfWidth;}

    protected float getTop() {
        return y - halfWidth;
    }

    protected void setTop(float top) {
        y = top + halfWidth;
    }

    protected float getBottom() {
        return y + halfWidth;
    }

    protected void setBottom(float bottom) {
        y = bottom - halfWidth;
    }

    protected float getHeight(){
        return 2f*halfHeight;
    }

    protected float getWidth(){
        return 2f*halfWidth;
    }

    @Override
    public void update(GameCanvas canvas, float deltaTime){} //обновление своих координат
    @Override
    public void render(GameCanvas canvas, Graphics g){}   // отрисовывается


}
