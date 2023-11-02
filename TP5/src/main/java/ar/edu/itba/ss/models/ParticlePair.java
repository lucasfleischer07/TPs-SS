package ar.edu.itba.ss.models;

import java.util.Objects;

public class ParticlePair {
    public static final ParticlePair ZERO_VALUE = new ParticlePair(0.0, 0.0);
    private Double x, y;

    public ParticlePair(Double x, Double y) {
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

    public double dotProduct(ParticlePair particle2) {
        return (this.x * particle2.getX() + this.y * particle2.getY());
    }

    public ParticlePair pairMultiply(double scalarValue) {
        return new ParticlePair(this.x * scalarValue, this.y * scalarValue);
    }

    public ParticlePair pairSubtract(ParticlePair particle2) {
        return new ParticlePair(this.x - particle2.x,this.y - particle2.y);
    }

    public ParticlePair pairSummatory(ParticlePair particle2) {
        return new ParticlePair(this.x + particle2.x, this.y + particle2.y);
    }

    public double mathModule(ParticlePair particle2) {
        return Math.sqrt(Math.pow(x-particle2.x, 2) + Math.pow(y-particle2.y, 2));
    }

    @Override
    public String toString() {
        return "Particle Pair = {" + "x = " + x + ", y = " + y + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParticlePair particlePair = (ParticlePair) o;
        return Objects.equals(x, particlePair.x) && Objects.equals(y, particlePair.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}