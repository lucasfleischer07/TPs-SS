package ar.edu.itba.ss.models;

import java.util.Objects;

public class Particle implements Comparable<Particle> {
    private double x, y;            // coordenadas de la particula
    private double radius, mass;    // radio y masa de la particula
    private double velX, velY;      // velicidad en x e y de la particula

    public Particle(double x, double y, double velX, double velY, double radius, double mass) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
        this.velX = velX;
        this.velY = velY;
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

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    @Override
    public String toString() {
        return "{ " + x + ", " + y + " }";
    }
    @Override
    public int compareTo(Particle p2) {
        int cmpx = Double.compare(this.x, p2.x);
        if (cmpx != 0) {
            return cmpx;
        }
        return Double.compare(this.y, p2.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return Objects.equals(x, particle.x) && Objects.equals(y, particle.y);
    }
}
