package ar.edu.itba.ss.utils;

public enum Color {
    RED(255, 0, 0),
    BLUE(0, 0, 255);
    final int red;
    final int green;
    final int blue;

    Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;

    }

    @Override
    public String toString() {
        return red + " " + green + " " + blue;
    }
}
