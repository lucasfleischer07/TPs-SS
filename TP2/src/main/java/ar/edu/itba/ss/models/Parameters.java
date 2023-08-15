package ar.edu.itba.ss.models;

import java.util.List;

public class Parameters {
    private List<Particle> particles;
    private double L;

    public void addParticle(Particle particle) {
        particles.add(particle);
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    public double getL() {
        return L;
    }

    public void setL(double L) {
        this.L = L;
    }


}
