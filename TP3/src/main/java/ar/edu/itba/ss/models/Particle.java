package ar.edu.itba.ss.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Particle {
    private final int id;
    private double mass, radius, x, y, tc, velX, velY, inferiorY, superiorY;
    private int collisionCount;
    private final double tableWidth;
    private final double l;

    public Particle(int id, double x, double y, double mass, double velX, double velY, double radius, double tableWidth, double l) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.mass = mass;
        this.radius = radius;
        this.collisionCount = 0;
        this.tableWidth = tableWidth;
        this.l = l;
        this.inferiorY = tableWidth/2 - l/2;
        this.superiorY = tableWidth/2 + l/2;
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

    public double getVx() {
        return velX;
    }

    public double getVy() {
        return velY;
    }

    public void setVx(double vx) {
        this.velX = vx;
    }

    public void setVy(double vy) {
        this.velY = vy;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public int getCollisionCount() {
        return collisionCount;
    }

    public void setCollisionCount(int collisionCount) {
        this.collisionCount = collisionCount;
    }

    // Se muestran las funciones si la particula choca con una pared, tanto para el tiempo de choque como para velocidades.
    public void impactXWall() {
        this.velX = -velX;
        this.collisionCount++;

    }

    public void impactYWall() {
        this.velY = -velY;
        this.collisionCount++;

    }

    public double impactXWallTime() {
        tc = Double.MAX_VALUE;

        // Va a chocar con la pared derecha
        if(velX > 0 && (inferiorY < y && superiorY > y) && (x >= tableWidth)) {
            tc = ((2*tableWidth) - radius - x) / velX;
        }

        if(velX > 0) {
            tc = (tableWidth - radius - x) / velX;
        }

        //va a chocar con la pared izquierda
        if(velX < 0) {
            tc = (0 + radius - x) / velX;
        }

        return tc;
    }

    public double impactYWallTime() {
        tc = Double.MAX_VALUE;

        //va a chocar con la pared derecha
        if(velY > 0 && (x >= tableWidth)) {
            tc = ((superiorY) - radius - y) / velY;
        }

        if(velY > 0) {
            tc = (tableWidth - radius - y) / velY;
        }

        //va a chocar con la pared izquierda
        if(velY < 0 && (x >= tableWidth)) {
            tc = (inferiorY + radius - y) / velY;
        }

        if(velY < 0) {
            tc = (0 + radius - y) / velY;
        }

        return tc;
    }

    // Se muestran las funciones por si la particula choca con otra particula, tanto su tiempo de choque como velocidades.
    public double collideWithParticleTime(Particle p2) {

        double[] dv = {p2.getVx() - this.velX, p2.getVy() - this.velY};
        double[] dr = {p2.getX() - this.getX(), p2.getY() - this.getY()};

        if(dotProduct(dv, dr) >= 0) {
            return tc = Double.MAX_VALUE;
        }

        double sigma = this.radius + p2.radius;

        double dx = p2.getX() - this.getX();
        double dy = p2.getY() - this.getY();
        double dr2 = Math.pow((dx), 2) + Math.pow((dy), 2);

        double dvelX = p2.getVx() - this.velX;
        double dvelY = p2.getVy() - this.velY;
        double dv2 = Math.pow((dvelX), 2) + Math.pow((dvelY), 2);

        double dvdr = (dvelX * dx) + (dvelY * dy);

        double d = Math.pow(dvdr, 2) - (dv2 * (dr2 - Math.pow(sigma, 2)));

        if(d < 0) {
            return tc = Double.MAX_VALUE;
        }

        return  tc = -((dvdr + Math.sqrt(d))/dv2);

    }

    public void collideWithParticle(Particle p2) {

        double[] dv = {p2.getVx() - this.velX, p2.getVy() - this.velY};
        double[] dr = {p2.getX() - this.getX(), p2.getY() - this.getY()};
        double sigma = this.radius + p2.radius;

        double dvelX = p2.getVx() - this.velX;
        double dvelY = p2.getVy() - this.velY;

        double dx = p2.getX() - this.getX();
        double dy = p2.getY() - this.getY();

        double dvdr = (dvelX * dx) + (dvelY * dy);

        double j = (((2*this.mass*p2.getMass())*(dvdr))/(sigma*(this.mass+p2.getMass())));

        double jx = (j * dr[0]) / sigma;
        double jy = (j * dr[1]) / sigma;

        this.velX = velX + jx / this.mass;
        this.velY = velY + jy / this.mass;

        p2.setVx(p2.getVx() - jx / p2.getMass());
        p2.setVy(p2.getVy() - jy / p2.getMass());

        this.collisionCount++;
        p2.collisionCount++;

    }

    // No lo uso nunca. ver si es mas rapido que hacer las cuentas a mano como hice
    public static double dotProduct(double[] vector1, double[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Los vectores deben tener la misma longitud.");
        }

        double result = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            result += vector1[i] * vector2[i];
        }

        return result;
    }

    @Override
    public String toString() {
        return "Particle " + id + " = {" + "x = " + x + ", y = " + y + ", radius = " + radius + ", mass = " + mass + ", velocityX = " + velX + ", velocityY = " + velY + '}';
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
