package ar.edu.itba.ss.models;

import java.util.ArrayList;
import java.util.List;

public class CellIndex {

    private final List<Particle> particles = new ArrayList<>();

    public void addParticles(Particle p){
        particles.add(p);
    }

    public List<Particle> getParticlesList() {
        return particles;
    }

    public boolean removeParticles(Particle p){
        return particles.remove(p);
    }

    public List<Particle> getParticles() {
        return particles;
    }
    @Override
    public String toString() {
        return "CellIndex{" +
                "particles=" + particles +
                '}';
    }
}
