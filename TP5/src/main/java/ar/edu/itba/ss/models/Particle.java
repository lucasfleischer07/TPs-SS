package ar.edu.itba.ss.models;

import java.util.Objects;

public class Particle {
    private Double forceX;
    private Double forceY;
    private Double x;
    private Double y;
    private Double velocityX;
    private Double velocityY;
    private final Double radius;
    private final Double mass;
    private final int id;
    private boolean reInjected = false;

    // Beeman information
    private final Double dt;
    private final Double sqrDt;
    private Double prevAccelerationX;
    private Double prevAccelerationY;
    private Double actualAccelerationX;
    private Double actualAccelerationY;
    private Double actualVelocityX;
    private Double actualVelocityY;

    public Particle(int id, Double x, Double y, Double radius, Double mass, Double dt) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
        this.forceX = 0.0;
        this.forceY = mass * Forces.GRAVITY;
        this.velocityX = 0.0;
        this.velocityY = 0.0;
        this.dt = dt;
        this.sqrDt = Math.pow(dt, 2);

        this.prevAccelerationX = 0.0;
        this.prevAccelerationY = Forces.GRAVITY;
    }

    public Double getForceX() {
        return forceX;
    }

    public void setForceX(Double forceX) {
        this.forceX = forceX;
    }

    public Double getForceY() {
        return forceY;
    }

    public void setForceY(Double forceY) {
        this.forceY = forceY;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(Double velocityX) {
        this.velocityX = velocityX;
    }

    public Double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(Double velocityY) {
        this.velocityY = velocityY;
    }

    public Double getRadius() {
        return radius;
    }

    public Double getMass() {
        return mass;
    }

    public int getId() {
        return id;
    }

    public boolean isReInjected() {
        return reInjected;
    }

    public void setReInjected(boolean reInjected) {
        this.reInjected = reInjected;
    }

    public Double getDt() {
        return dt;
    }

    public Double getSqrDt() {
        return sqrDt;
    }

    public Double getPrevAccelerationX() {
        return prevAccelerationX;
    }

    public void setPrevAccelerationX(Double prevAccelerationX) {
        this.prevAccelerationX = prevAccelerationX;
    }

    public Double getPrevAccelerationY() {
        return prevAccelerationY;
    }

    public void setPrevAccelerationY(Double prevAccelerationY) {
        this.prevAccelerationY = prevAccelerationY;
    }

    public Double getActualAccelerationX() {
        return actualAccelerationX;
    }

    public void setActualAccelerationX(Double actualAccelerationX) {
        this.actualAccelerationX = actualAccelerationX;
    }

    public Double getActualAccelerationY() {
        return actualAccelerationY;
    }

    public void setActualAccelerationY(Double actualAccelerationY) {
        this.actualAccelerationY = actualAccelerationY;
    }

    public Double getActualVelocityX() {
        return actualVelocityX;
    }

    public void setActualVelocityX(Double actualVelocityX) {
        this.actualVelocityX = actualVelocityX;
    }

    public Double getActualVelocityY() {
        return actualVelocityY;
    }

    public void setActualVelocityY(Double actualVelocityY) {
        this.actualVelocityY = actualVelocityY;
    }

    public Particle copy(){
        return new Particle(id, x, y, radius, mass, dt);
    }

    public void resetForce() {
        this.setForceX(0.0);
        this.setForceY(0.0);
    }

    public void addToForce(double x, double y) {
        this.setForceX(this.getX() + x);
        this.setForceY(this.getY() + y);
    }

//    TODO: Esta no esta adaptada porque creo que hace lo mismo que la de arriba pero le pasa un pair
//    public void addToForce(Pair<Double> pair) {
//        force.setX(force.getX() + pair.getX());
//        force.setY(force.getY() + pair.getY());
//    }

    public Double getAccelerationX() {
        // La fuerza viene en Newtons
        return (this.getForceX() * 100) / getMass();
    }

    public Double getAccelerationY() {
        // La fuerza viene en Newtons
        return (this.getForceY() * 100) / getMass();
    }

    public void reInject(){
        reInjected = true;
    }

    @Override
    public String toString() {
        return "Particle id = " + this.getId() + "x = " + this.getX() + ", y = " + this.getY() + ", velX = " + this.getVelocityX() + ", velY = " + this.getVelocityY() + ", radius = " + radius + ", forceX = " + this.getForceX() + ", forceY = " + this.getForceY();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Particle)) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


//    TODO: Todavia no esta adaptado esto de aca abajo (solo para que no tire error)
    // BEEMAN
    public void prediction() {
        double newX = this.getX() + this.getVelocityX() * dt + (2.0 / 3.0) * this.getAccelerationX() * sqrDt - (1.0 / 6.0) * this.getPrevAccelerationX() * sqrDt;
        double newY = this.getY() + this.getVelocityY() * dt + (2.0 / 3.0) * this.getAccelerationY() * sqrDt - (1.0 / 6.0) * this.getPrevAccelerationY() * sqrDt;

        double predictedVx = this.getVelocityX() + 1.5 * this.getAccelerationX() * dt - 0.5 * this.getPrevAccelerationX() * dt;
        double predictedVy = this.getVelocityY() + 1.5 * this.getAccelerationY() * dt - 0.5 * this.getPrevAccelerationX() * dt;

        this.actualVelocityX = this.getActualVelocityX();
        this.actualVelocityY = this.getActualVelocityY();

        this.actualAccelerationX = this.getActualAccelerationX();
        this.actualAccelerationY = this.getActualAccelerationY();

        this.x = newX;
        this.y = newY;

        this.velocityX = predictedVx;
        this.velocityY = predictedVy;
    }

    public void correction(){
        if (reInjected){
            this.velocityX = 0.0;
            this.velocityY = 0.0;
            reInjected = false;
            this.prevAccelerationX = 0.0;
            this.prevAccelerationY = Forces.GRAVITY;
        } else {
            double newVx = this.getActualVelocityX() + (1.0/3.0) * this.getAccelerationX() * dt + (5.0/6.0) * this.getActualAccelerationX() * dt - (1.0/6.0) * this.getPrevAccelerationX() * dt;
            double newVy = this.getActualVelocityY() + (1.0/3.0) * this.getAccelerationY() * dt + (5.0/6.0) * this.getActualAccelerationY() * dt - (1.0/6.0) * this.getPrevAccelerationY() * dt;
            this.velocityX = newVx;
            this.velocityY = newVy;

            this.prevAccelerationX = this.actualAccelerationX;
            this.prevAccelerationY = this.actualAccelerationY;
        }

    }


}
