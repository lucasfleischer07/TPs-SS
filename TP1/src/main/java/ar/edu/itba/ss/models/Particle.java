package ar.edu.itba.ss.models;

import java.util.Objects;

public class Particle {
    private final int id;
    private double x,y;
    private double radius, property;

    public Particle(int id, double x, double y, double radius, double property) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.property = property;

    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getProperty() {
        return property;
    }

    public void setProperty(double property) {
        this.property = property;
    }

    public double distanceTo(Particle p2, boolean isPeriodic) {
        final double deltaX = this.x - p2.getX();
        final double deltaY = this.y - p2.getY();

//        TODO: Ver lo de si es periodic o no

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY) - this.radius - p2.getRadius();
    }

    @Override
    public String toString() {
        return "Particle " + id + " = {" + "x = " + x + ", y = " + y + "radius = " + radius + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
