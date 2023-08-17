package ar.edu.itba.ss.models;

import java.util.List;
import java.util.Objects;

public class Particle {
    private final int id;
    private double x, y;
    private double v, theta, radius;

    public Particle(int id, double x, double y, double v, double theta, double radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.v = v;
        this.theta = theta;
        this.radius = radius;
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

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double distanceTo(Particle p2, boolean isPeriodic, double l) {
        double deltaX = Math.abs(this.x - p2.getX());
        double deltaY = Math.abs(this.y - p2.getY());

        if(isPeriodic) {
            //se verifica si la diferencia en la coordenada (deltaX) es mayor que la mitad del tamaño del espacio (l/2).
            // Si es mayor, significa que la distancia es más corta a través de los bordes del espacio periódico, por lo que se ajusta la diferencia
            // para que sea la más corta posible.
            // Esto se hace al restar la diferencia del tamaño del espacio (l) para obtener la distancia correcta a través de los bordes.
            if(deltaX > l/2) {
                deltaX = l - deltaX;
            }
            if(deltaY > l/2) {
                deltaY = l - deltaY;
            }

        }
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY) - this.radius - p2.getRadius();
    }

    public void updateAngle(List<Particle> neighbours, double eta){
        //TODO CHEQUEAR ESTA CUENTA DE ATAN2
        // "Promedio de todos los ángulos de todas las particulas dentro de r incluyendo a la propia"
        double totalSin = 0;
        double totalCos = 0;
        totalSin += Math.sin(this.getTheta());
        totalCos += Math.cos(this.getTheta());
        for(Particle p : neighbours){
            totalSin += Math.sin(p.getTheta());
            totalCos += Math.cos(p.getTheta());
        }
        double avgSin = totalSin / (neighbours.size() + 1);
        double avgCos = totalCos / (neighbours.size() + 1);
        double val = Math.atan2(avgSin, avgCos);
        double randomFactor = ((eta/2)*(Math.random())*(Math.random() <= 0.5? -1:1));
        val += randomFactor;
        this.theta = val;
    }

    public void updateParticlePositionAndVelocity(double l, int dt) {

        double vx = this.getV() * Math.cos(this.getTheta());
        double vy = this.getV() * Math.sin(this.getTheta());

        //posicion, ecuacion sacada de a teorica (fisica I)
        double updatedX = (this.getX() + vx * dt) % l;
        double updatedY = (this.getY() + vy * dt) % l;

        // Updatea y asegura que la nueva posición esté dentro de los límites [0, L)
        if(updatedX < 0) {
            updatedX += l;
        }
        if(updatedY < 0) {
            updatedY += l;
        }
        this.setX(updatedX);
        this.setY(updatedY);

    }

    @Override
    public String toString() {
        return "Particle " + id + " = {" + "x = " + x + ", y = " + y + ", radius = " + radius + '}';
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