package ar.edu.itba.ss.models;

import java.util.Objects;

public class Pair {
    public static final Pair ZERO_VALUE = new Pair(0.0, 0.0);
    private Double x, y;

    public Pair(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(x, pair.x) && Objects.equals(y, pair.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public double dotProduct(Pair particle2) {
        return this.x * particle2.getX()
                + this.y * particle2.getY();
    }

    public Pair pairMultiply(double scalarValue) {
        return new Pair(this.x * scalarValue,
                this.y * scalarValue);
    }

    public Pair pairSubtract(Pair particle2) {
        return new Pair(this.x - particle2.x,
                this.y - particle2.y);
    }

    public Pair pairSummatory(Pair particle2) {
        return new Pair(this.x + particle2.x,
                this.y + particle2.y);
    }

    public double mathModule(Pair particle2) {
        return Math.sqrt(Math.pow(x-particle2.x, 2) + Math.pow(y-particle2.y, 2));
    }

}