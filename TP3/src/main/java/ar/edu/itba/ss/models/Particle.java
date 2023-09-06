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
        this.inferiorY = tableWidth - l/2;
        this.superiorY = tableWidth - l/2;
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
        tc = Double.MAX_VALUE; // porque sino vale infinito

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
        tc = Double.MAX_VALUE; //porque sino vale infinito

        //va a chocar con la pared derecha
        if(velY > 0 && (x >= tableWidth)) {
            tc = ((superiorY+l) - radius - y) / velY;
        }

        if(velY > 0) {
            tc = (tableWidth - radius - y) / velY;
        }

        //va a chocar con la pared izquierda
        if(velY < 0 && (x >= tableWidth)) {
            tc = (superiorY + radius - y) / velY;
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

//    TODO: Esto creo que no lo usamos
//    public double distanceTo(Particle p2, boolean isPeriodic, double l) {
//        double deltaX = Math.abs(this.x - p2.getX());
//        double deltaY = Math.abs(this.y - p2.getY());
//
//        if(isPeriodic) {
//            //se verifica si la diferencia en la coordenada (deltaX) es mayor que la mitad del tamaño del espacio (l/2).
//            // Si es mayor, significa que la distancia es más corta a través de los bordes del espacio periódico, por lo que se ajusta la diferencia
//            // para que sea la más corta posible.
//            // Esto se hace al restar la diferencia del tamaño del espacio (l) para obtener la distancia correcta a través de los bordes.
//            if(deltaX > l/2) {
//                deltaX = l - deltaX;
//            }
//            if(deltaY > l/2) {
//                deltaY = l - deltaY;
//            }
//
//        }
//        return Math.sqrt(deltaX * deltaX + deltaY * deltaY) - this.radius - p2.getRadius();
//    }

//    TODO: Esto creo que no lo usamos
//    public void updateAngle(List<Particle> neighbours, double eta){ //1 -> 2,3,4
//        double totalSin = Math.sin(this.getTheta());
//        double totalCos = Math.cos(this.getTheta());
//
//        for(Particle p : neighbours){
//            totalSin += Math.sin(p.getTheta());
//            totalCos += Math.cos(p.getTheta());
//        }
//        double avgSin = totalSin / (neighbours.size() + 1);
//        double avgCos = totalCos / (neighbours.size() + 1);
//
//        this.setTheta( Math.atan2(avgSin, avgCos) + Math.random()*eta - eta/2);
//    }

//    TODO: Esto creo que no lo usamos
//    public void updateParticlePosition(double l, int dt) {
//
//        double vx = this.getV() * Math.cos(this.getTheta());
//        double vy = this.getV() * Math.sin(this.getTheta());
//
//
//        double updatedX = ((this.getX() + vx * dt) % l) < 0 ?
//                ((this.getX() + vx * dt) % l) + l : ((this.getX() + vx * dt) % l) ;
//        double updatedY = ((this.getY() + vy * dt) % l) < 0 ?
//                ((this.getY() + vy * dt) % l) + l : ((this.getY() + vy * dt) % l) ;
//
//        this.setX(updatedX);
//        this.setY(updatedY);
//
//    }


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
