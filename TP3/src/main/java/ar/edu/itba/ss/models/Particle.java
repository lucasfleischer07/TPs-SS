package ar.edu.itba.ss.models;

import java.util.*;

public class Particle implements Comparable<Particle>{
    private final int id;
    private double mass, radius, x, y, velX, velY, inferiorY, superiorY;
    private int collisionCount;
    private final double tableWidth;
    private final double l;
    private Map<Particle, Map<Integer, Double>> tcMap;
    private final int WALLS_AMOUNT = 7;


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
        // TODO: Sacar los numeros estos hardcodeados y poner las variables bien
        this.tcMap = new HashMap<>();
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
        double auxVel = this.velX;
        this.setVx(-auxVel);
//        this.velX = -velX;
        this.collisionCount++;
    }

    public void impactYWall() {
        double auxVel = this.velY;
        this.setVy(-auxVel);
//        this.velY = -velY;
        this.collisionCount++;
    }


/*       1
*    ______________
*   |              |2
*   |              |__________
* 0 |                   3     | 4
*   |               __________|
*   |            6 |       5
*   |______________|
*          7
* */

    public Object[] impactToWallTime() {
        tcMap.putIfAbsent(this, new HashMap<>());

        if((radius - x) > 0 && velX < 0 ){
            x = 0.0016;
            tcMap.get(this).put(0, Double.MIN_VALUE);
        }else{
            tcMap.get(this).put(0, (0 + radius - x) / velX);
        }

        tcMap.get(this).put(1, (tableWidth - radius - y) / velY);
        tcMap.get(this).put(2, (tableWidth - radius - x) / velX);
        tcMap.get(this).put(3, ((superiorY) - radius - y) / velY);
        tcMap.get(this).put(4, ((2*tableWidth) - radius - x) / velX);
        tcMap.get(this).put(5, (inferiorY + radius - y) / velY);
        tcMap.get(this).put(6, (tableWidth - radius - x) / velX);
        tcMap.get(this).put(7, (0 + radius - y) / velY);

        double minorTime = Double.MAX_VALUE;;
        int collidesWall = -1;

        for(int i = 0; i <= WALLS_AMOUNT; i++) {
            if(tcMap.get(this).get(i) < 0) {
                tcMap.get(this).put(i, Double.MAX_VALUE);
            } else {
                double futureX = x + velX * tcMap.get(this).get(i);
                double futureY = y + velY * tcMap.get(this).get(i);
                double auxTime = -1;
                if(i == 0) {
                    auxTime = (0 + radius <= futureY && futureY <= (tableWidth - radius)) ? tcMap.get(this).get(i) : Double.MAX_VALUE;
                } else if(i == 1 || i == 7) {
                    auxTime = (0 + radius <= futureX && futureX <= tableWidth - radius) ? tcMap.get(this).get(i) : Double.MAX_VALUE;;
                } else if(i == 2) {
                    auxTime = (superiorY - radius <= futureY && futureY <= tableWidth - radius) ?  tcMap.get(this).get(i) : Double.MAX_VALUE;
                } else if(i == 3 || i == 5) {
                    auxTime = (tableWidth - radius <= futureX && futureX <= (2*tableWidth) - radius) ? tcMap.get(this).get(i) : Double.MAX_VALUE;
                } else if(i == 4) {
                    auxTime = (inferiorY + radius <= futureY && futureY <= superiorY - radius)  ? tcMap.get(this).get(i) : Double.MAX_VALUE;
                } else if(i == 6) {
                    auxTime = (0 + radius <= futureY && futureY <= inferiorY + radius) ? tcMap.get(this).get(i) : Double.MAX_VALUE;;
                }
                tcMap.get(this).put(i, auxTime);
                if(auxTime < minorTime && auxTime > 0) {
                    minorTime = auxTime;
                    collidesWall = i;
                }
            }
        }

        return new Object[] {minorTime, collidesWall};
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

    @Override
    public int compareTo(Particle otherParticle) {
        int xComparison = Double.compare(this.x, otherParticle.x);
        if (xComparison != 0) {
            return xComparison;
        }
        return Double.compare(this.y, otherParticle.y);
    }
}
