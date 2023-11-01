package ar.edu.itba.ss.models;

import ar.edu.itba.ss.utils.Color;
import ar.edu.itba.ss.utils.Forces;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Particle {
    private static final double ZERO_VALUE = 0.0, SCALAR1 = (2.0 / 3.0), SCALAR2 = -(1.0 / 6.0);
    private final ParticlePair fuerzas;
    private final Double radius, mass, dt, sqrDt;
    private final int id;
    private boolean attachedFromSlit = false, particleSlit = false;
    private Color color;
    private ParticlePair position, velocity, previousAcc, actualAcc, actualVel;
    public Map<Particle, ParticlePair> totalVelocitiesAcummulatedMap = new HashMap<>();

    private ParticlePair floorRelativeVelocity = ParticlePair.ZERO_VALUE, rightRelativeVelocity = ParticlePair.ZERO_VALUE, leftRelativeVelocity = ParticlePair.ZERO_VALUE, topRelativeVelocity = ParticlePair.ZERO_VALUE;

    public void forcesReseted() {
        fuerzas.setX(ZERO_VALUE);
        fuerzas.setY(ZERO_VALUE);
    }

    public double getEnergy(){
        return Math.pow(this.velocity.mathModule(ParticlePair.ZERO_VALUE), 2) * mass / 2.0;
    }

    public void addToForce(double x, double y) {
        fuerzas.setX(fuerzas.getX() + x);
        fuerzas.setY(fuerzas.getY() + y);
    }

    public Particle(int id, ParticlePair position, Double radius, Double mass, Double dt, Color color) {
        this.id = id;
        this.position = position;
        this.radius = radius;
        this.mass = mass;
        this.fuerzas = new ParticlePair(ZERO_VALUE, ZERO_VALUE);
        this.velocity = new ParticlePair(ZERO_VALUE, ZERO_VALUE);
        this.dt = dt;
        this.sqrDt = Math.pow(dt, 2);
        actualAcc = new ParticlePair(ZERO_VALUE, ZERO_VALUE);
        previousAcc = new ParticlePair(ZERO_VALUE, Forces.GRAVITY);
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

    public void addToForce(ParticlePair particlePair) {
        fuerzas.setX(fuerzas.getX() + particlePair.getX());
        fuerzas.setY(fuerzas.getY() + particlePair.getY());
    }

    public ParticlePair getAcceleration() {
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

    public ParticlePair getPosition() {
        return position;
    }

    public ParticlePair getVelocity() {
        return velocity;
    }

    public ParticlePair getFuerzas() {
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

    public ParticlePair getPreviousAcc() {
        return previousAcc;
    }

    public ParticlePair getActualAcc() {
        return actualAcc;
    }

    public ParticlePair getActualVel() {
        return actualVel;
    }

    public void setAttachedFromSlit(boolean attachedFromSlit) {
        this.attachedFromSlit = attachedFromSlit;
    }

    public void setPosition(ParticlePair position) {
        this.position = position;
    }

    public void setVelocity(ParticlePair velocity) {
        this.velocity = velocity;
    }

    public void setPreviousAcc(ParticlePair previousAcc) {
        this.previousAcc = previousAcc;
    }

    public void setActualAcc(ParticlePair actualAcc) {
        this.actualAcc = actualAcc;
    }

    public void setActualVel(ParticlePair actualVel) {
        this.actualVel = actualVel;
    }

//-------------------------------------------------------------------------------------------
    public Map<Particle, ParticlePair> getTotalVelocitiesAcummulatedMap() {
        return totalVelocitiesAcummulatedMap;
    }

    public void setTotalVelocitiesAcummulatedMap(ParticlePair vel, Particle particle) {
        this.totalVelocitiesAcummulatedMap.putIfAbsent(particle, vel);
    }

    public ParticlePair getFloorRelativeVelocity() {
        return floorRelativeVelocity;
    }

    public void setFloorRelativeVelocity(ParticlePair floorRelativeVelocity) {
        this.floorRelativeVelocity = floorRelativeVelocity;
    }

    public ParticlePair getRightRelativeVelocity() {
        return rightRelativeVelocity;
    }

    public void setRightRelativeVelocity(ParticlePair rightRelativeVelocity) {
        this.rightRelativeVelocity = rightRelativeVelocity;
    }

    public ParticlePair getLeftRelativeVelocity() {
        return leftRelativeVelocity;
    }

    public void setLeftRelativeVelocity(ParticlePair leftRelativeVelocity) {
        this.leftRelativeVelocity = leftRelativeVelocity;
    }

    public ParticlePair getTopRelativeVelocity() {
        return topRelativeVelocity;
    }

    public void setTopRelativeVelocity(ParticlePair topRelativeVelocity) {
        this.topRelativeVelocity = topRelativeVelocity;
    }

    //--------------------------------------------------------------------------------------------------------


    // ALgoritmo
    public void prediction() {
        actualAcc = this.getAcceleration();
        this.position = position.pairSummatory(velocity.pairMultiply(dt).pairSummatory(actualAcc.pairMultiply(SCALAR1).pairSummatory(previousAcc.pairMultiply(SCALAR2)).pairMultiply(sqrDt)));
        this.actualVel = velocity;
        this.velocity = this.actualVel.pairSummatory(this.actualAcc.pairMultiply(1.5 * dt).pairSummatory(previousAcc.pairMultiply(-0.5 * dt)));
    }

    public void correction(){
        if (attachedFromSlit){
            this.velocity = new ParticlePair(ZERO_VALUE, ZERO_VALUE);attachedFromSlit = false;previousAcc = new ParticlePair(ZERO_VALUE, Forces.GRAVITY);
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
