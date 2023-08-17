package ar.edu.itba.ss.simulation;

import ar.edu.itba.ss.algorithms.CellIndexMethod;
import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.models.Particle;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OffLatticeSimulation {
    private int time, dt;//dt is the temporary step
    private final double l, eta;
    private List<Particle> particles;

    private Map<Particle, List<Particle>> neighbourhoods;

    private CellIndexMethod cimMethod;

    public OffLatticeSimulation(double eta, Parameters parameters, double l) {
        this.particles = parameters.getParticles();
        this.time = 0;
        this.dt = 1;
        this.eta = eta;
        this.l = l;
    }
    public void nextIteration() {
        InitializeNeighbors();

        for(Particle particle: particles) {
            particle.updateParticlePosition(l, dt);
            particle.updateAngle(neighbourhoods.getOrDefault(particle,
                                 new LinkedList<>()), eta);
        }

        time += dt;
    }
    public void InitializeNeighbors() {
        cimMethod = new CellIndexMethod(particles,true, l);
        neighbourhoods = cimMethod.generateNeighbors();
    }
    public int getTime() {
        return time;
    }
    public List<Particle> getParticles() {
        return particles;
    }

}
