package ar.edu.itba.ss.models;

import java.util.ArrayList;
import java.util.List;


public class CellIndex {
    private final List<Particle> particles = new ArrayList<>();

    public boolean removeParticle(Particle particle){
        return particles.remove(particle);
    }

    public void addParticle(Particle particle){
        particles.add(particle);
    }


    public List<Particle> getParticles() {
        return particles;
    }

}
