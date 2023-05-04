package org.example.models;

/**
 * Class for validating and storing coordinates
 */
public class Coordinates {
    private final Integer x;
    private final Integer y;

    public Coordinates(Integer x, Integer y) {
        checkInputData(x, y);
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    /**
     * Method to check input data
     * @param x coordinate x
     * @param y coordinate y
     */
    private void checkInputData(Integer x, Integer y) {
        if (x.compareTo(-576) <= 0) {
            throw new IllegalArgumentException("Expected x > -576, but x = " + x);
        }

        if (y.compareTo(-286) <= 0) {
            throw new IllegalArgumentException("Expected y > -286, but y = " + y);
        }
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}