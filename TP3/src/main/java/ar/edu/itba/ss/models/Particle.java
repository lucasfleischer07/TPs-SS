package ar.edu.itba.ss.models;

import java.text.DecimalFormat;
import java.util.*;

public class Particle implements Comparable<Particle>{

    private double x, y; // coordenadas de la particula
    private double radius, mass; // radio y masa de la particula
    private double velX, velY; // velicidad en x e y de la particula
    private final int WALLS_AMOUNT = 8;

    public Particle(double x, double y, double velX, double velY, double radius, double mass) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
        this.velX = velX;
        this.velY = velY;
    }


    /*       1
//*    ______________
//*   |              |2
//*   |              |__________
//* 0 |                   3     | 4
//*   |               __________|
//*   |            6 |       5
//*   |______________|
//*          7
//* */

    public void impactToWallTime(int i, double[][] tc, List<Particle> particleList, double tableWidth, double l) {
        tc[i][particleList.size()] = (this.getRadius() - this.getX()) / this.getVx();
        tc[i][particleList.size() + 1] = (tableWidth -this.getRadius() - this.getY()) / this.getVy();
        tc[i][particleList.size() + 2] = (tableWidth - this.getRadius() - this.getX()) / this.getVx();
        tc[i][particleList.size() + 3] = ((tableWidth + l) / 2 - this.getRadius() - this.getY()) / this.getVy();
        tc[i][particleList.size() + 4] = (tableWidth + tableWidth - this.getRadius() - this.getX()) / this.getVx();
        tc[i][particleList.size() + 5] = (((tableWidth - l) / 2) + this.getRadius() - this.getY()) / this.getVy();
        tc[i][particleList.size() + 6] = (tableWidth - this.getRadius() - this.getX()) / this.getVx();
        tc[i][particleList.size() + 7] = (0 + this.getRadius() - this.getY()) / this.getVy();

        for (int j = particleList.size(); j < particleList.size() + WALLS_AMOUNT; j++) {
            if (tc[i][j] < 0) {
                tc[i][j] = Double.MAX_VALUE;
            } else {
                double futureX = this.getX() + this.getVx() * tc[i][j];
                double futureY = this.getY() + this.getVy() * tc[i][j];

                if(j % particleList.size() == 0) {
                    tc[i][j] = (0 + this.getRadius() <= futureY && futureY <= tableWidth - this.getRadius()) ? tc[i][j] : Double.MAX_VALUE;
                } else if((j % particleList.size() == 1) || (j % particleList.size() == 7)) {
                    tc[i][j] = (0 + this.getRadius() <= futureX && futureX <= tableWidth - this.getRadius()) ? tc[i][j] : Double.MAX_VALUE;
                } else if(j % particleList.size() == 2) {
                    tc[i][j] = ((tableWidth + l) / 2 - this.getRadius() <= futureY && futureY <= tableWidth -this.getRadius()) ? tc[i][j] : Double.MAX_VALUE;
                } else if((j % particleList.size() == 3) || (j % particleList.size() == 5)) {
                    tc[i][j] = (tableWidth - this.getRadius() <= futureX && futureX <= tableWidth + tableWidth - this.getRadius()) ? tc[i][j] : Double.MAX_VALUE;
                } else if(j % particleList.size() == 4) {
                    tc[i][j] = (((tableWidth - l) / 2) + this.getRadius() <= futureY && futureY <= (tableWidth + l) / 2 - this.getRadius())  ? tc[i][j] : Double.MAX_VALUE;

                } else if(j % particleList.size() == 6) {
                    tc[i][j] = (0 + this.getRadius() <= futureY && futureY <= ((tableWidth - l) / 2) + this.getRadius()) ? tc[i][j] : Double.MAX_VALUE;
                }
            }
        }
    }


    public double collideWithParticleTime(Particle p2) {
        double[] dv = {p2.getVx() - this.velX, p2.getVy() - this.velY};
        double[] dr = {p2.getX() - this.getX(), p2.getY() - this.getY()};

        if(dotProduct(dv, dr) >= 0) {
            return Double.MAX_VALUE;
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
            return Double.MAX_VALUE;
        }

        return -((dvdr + Math.sqrt(d))/dv2);

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

        double j = 2 * this.mass * p2.getMass() * (dvdr) / (sigma * (this.mass + p2.getMass()));

        double jx = j * dr[0] / sigma;
        double jy = j * dr[1] / sigma;

        p2.setVx(p2.getVx() - jx / p2.getMass());
        p2.setVy(p2.getVy() - jy / p2.getMass());

        this.setVx(this.getVx() + jx / this.getMass());
        this.setVy(this.getVy() + jy / this.getMass());

    }

    public void impactXWall() {
        this.velX = -velX;
    }

    public void impactYWall() {
        this.velY = -velY;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return velX;
    }

    public double getVy() {
        return velY;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVx(double velocityX) {
        this.velX = velocityX;
    }

    public void setVy(double velocityY) {
        this.velY = velocityY;
    }

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
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

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
