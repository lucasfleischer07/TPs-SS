package ar.edu.itba.ss.simulation;

import ar.edu.itba.ss.algorithms.CellIndexMethod;
import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.models.Particle;

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
        InitializeGroups();

        for(Particle particle: particles) {
            particle.setX(particle.getX() + particle.getV()*Math.cos(particle.getTheta())*dt);
            particle.setY(particle.getY() + particle.getV()*Math.sin(particle.getTheta())*dt);
            particle.updateAngle(neighbourhoods.get(particle), eta);
        }

        time += dt;
    }

    public void InitializeGroups() {
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
