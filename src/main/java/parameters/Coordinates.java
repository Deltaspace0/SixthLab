package parameters;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private int x;
    private double y; //Максимальное значение поля: 60

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
