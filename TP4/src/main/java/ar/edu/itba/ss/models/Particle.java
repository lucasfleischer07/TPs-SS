package ar.edu.itba.ss.models;

import java.util.Objects;

public class Particle implements Comparable<Particle> {
    private double x, y, x2, x3, x4, x5;            // coordenadas de la particula
    private double radius, mass, forceX, forceY;    // radio y masa de la particula
    private double velX, velY, u;      // velicidad en x e y de la particula

    public Particle(double x, double y, double velX, double velY, double u, double radius, double mass, double forceX, double forceY) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
        this.velX = velX;
        this.velY = velY;
        this.u = u;
        this.forceX = forceX;
        this.forceY = forceY;

        this.x2 = 0;
        this.x3 = 0;
        this.x4 = 0;
        this.x5 = 0;
    }

    public Particle(double x, double y, double velX, double velY, double u, double radius, double mass, double forceX, double forceY, double x2, double x3, double x4, double x5) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
        this.velX = velX;
        this.velY = velY;
        this.u = u;
        this.forceX = forceX;
        this.forceY = forceY;

        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.x5 = x5;
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

    public double getU() {
        return u;
    }

    public void setU(double u) {
        this.u = u;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getX3() {
        return x3;
    }

    public void setX3(double x3) {
        this.x3 = x3;
    }

    public double getX4() {
        return x4;
    }

    public void setX4(double x4) {
        this.x4 = x4;
    }

    public double getX5() {
        return x5;
    }

    public void setX5(double x5) {
        this.x5 = x5;
    }

    public double getForceX() {
        return forceX;
    }

    public void setForceX(double forceX) {
        this.forceX = forceX;
    }

    public double getForceY() {
        return forceY;
    }

    public void setForceY(double forceY) {
        this.forceY = forceY;
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
