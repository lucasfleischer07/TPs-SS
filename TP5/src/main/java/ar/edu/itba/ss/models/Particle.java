package ar.edu.itba.ss.models;

import ar.edu.itba.ss.utils.Color;
import ar.edu.itba.ss.utils.ForcesUtils;

import java.util.Objects;

public class Particle {
    private static final double ZERO_VALUE = 0.0;
    private final static double SCALAR1 = (2.0 / 3.0);
    private final static double SCALAR2 = -(1.0 / 6.0);
    private final Pair fuerzas;
    private final Double radius, mass, dt, sqrDt;
    private final int id;
    private boolean attachedFromSlit = false;
    private boolean particleSlit = false;
    private Color color;
    private Pair position, velocity, previousAcc, actualAcc, actualVel;

    public void forcesReseted() {
        fuerzas.setX(ZERO_VALUE);
        fuerzas.setY(ZERO_VALUE);
    }

    public double getEnergy(){
        return Math.pow(this.velocity.mathModule(Pair.ZERO_VALUE), 2) * mass / 2.0;
    }

    public void addToForce(double x, double y) {
        fuerzas.setX(fuerzas.getX() + x);
        fuerzas.setY(fuerzas.getY() + y);
    }

    public Particle(int id, Pair position, Double radius, Double mass, Double dt, Color color) {
        this.id = id;
        this.position = position;
        this.radius = radius;
        this.mass = mass;
        this.fuerzas = new Pair(ZERO_VALUE, ZERO_VALUE);
        this.velocity = new Pair(ZERO_VALUE, ZERO_VALUE);
        this.dt = dt;
        this.sqrDt = Math.pow(dt, 2);
        actualAcc = new Pair(ZERO_VALUE, ZERO_VALUE);
        previousAcc = new Pair(ZERO_VALUE, ForcesUtils.GRAVITY);
        this.color = color;
    }

    public Double getParticleRadius() {
        return radius;
    }

    public Double getParticleMass() {
        return mass;
    }
    public Particle particleClone() {
        return new Particle(id, position, radius, mass, dt, color);
    }

    public void addToForce(Pair pair) {
        fuerzas.setX(fuerzas.getX() + pair.getX());
        fuerzas.setY(fuerzas.getY() + pair.getY());
    }

    public Pair getAcceleration() {
        return fuerzas.pairMultiply(1.0 / mass);
    }

    public void attachedFromSlit() {
        attachedFromSlit = true;
        setColor(Color.RED);
    }

    public String toString() {
        return position.getX() + " " + position.getY() + " " + velocity.getX() + " " + velocity.getY() + " " + radius + " " + color;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public Pair getPosition() {
        return position;
    }

    public Pair getVelocity() {
        return velocity;
    }

    public Pair getFuerzas() {
        return fuerzas;
    }



    public Double getRadius() {
        return radius;
    }

    public Double getMass() {
        return mass;
    }

    public Double getDt() {
        return dt;
    }

    public Double getSqrDt() {
        return sqrDt;
    }

    public boolean isAttachedFromSlit() {
        return attachedFromSlit;
    }

    public Pair getPreviousAcc() {
        return previousAcc;
    }

    public Pair getActualAcc() {
        return actualAcc;
    }

    public Pair getActualVel() {
        return actualVel;
    }

    public void setAttachedFromSlit(boolean attachedFromSlit) {
        this.attachedFromSlit = attachedFromSlit;
    }

    public void setPosition(Pair position) {
        this.position = position;
    }

    public void setVelocity(Pair velocity) {
        this.velocity = velocity;
    }

    public void setPreviousAcc(Pair previousAcc) {
        this.previousAcc = previousAcc;
    }

    public void setActualAcc(Pair actualAcc) {
        this.actualAcc = actualAcc;
    }

    public void setActualVel(Pair actualVel) {
        this.actualVel = actualVel;
    }

    // ALgoritmo
    public void prediction() {
        actualAcc = this.getAcceleration();
        this.position = position.pairSummatory(velocity.pairMultiply(dt).pairSummatory(actualAcc.pairMultiply(SCALAR1).pairSummatory(previousAcc.pairMultiply(SCALAR2)).pairMultiply(sqrDt)));
        this.actualVel = velocity;
        this.velocity = this.actualVel.pairSummatory(this.actualAcc.pairMultiply(1.5 * dt).pairSummatory(previousAcc.pairMultiply(-0.5 * dt)));
    }

    public void correction(){
        if (attachedFromSlit){
            this.velocity = new Pair(ZERO_VALUE, ZERO_VALUE);attachedFromSlit = false;previousAcc = new Pair(ZERO_VALUE, ForcesUtils.GRAVITY);
        } else {
            this.velocity = actualVel.pairSummatory(this.getAcceleration().pairMultiply((1.0 / 3.0) * dt).pairSummatory(actualAcc.pairMultiply((5.0 / 6.0) * dt).pairSummatory(previousAcc.pairMultiply(-(1.0 / 6.0) * dt))));
            previousAcc = actualAcc;
        }
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

    public int getId() {
        return id;
    }

    public boolean isParticleSlit() {
        return particleSlit;
    }

    public void setParticleSlit(boolean particleSlit) {
        this.particleSlit = particleSlit;
    }
}
