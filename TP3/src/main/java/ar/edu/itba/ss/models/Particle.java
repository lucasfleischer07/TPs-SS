package ar.edu.itba.ss.models;

import java.util.List;
import java.util.Objects;

public class Particle {
    private final int id;
    private double v, mass, radius, x, y;
    private int collisionCount;

    public Particle(int id, double x, double y, double mass, double v, double radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.v = v;
        this.mass = mass;
        this.radius = radius;
        this.collisionCount = 0;
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

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
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
        return "Particle " + id + " = {" + "x = " + x + ", y = " + y + ", radius = " + radius + ", mass = " + mass + ", velocity = " + v + '}';
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
