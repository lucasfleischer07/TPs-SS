package ar.edu.itba.ss.simulation;

import ar.edu.itba.ss.algorithms.CellIndexMethod;
import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.models.Particle;

import java.util.List;

public class OffLatticeSimulation {

    private int time;
    private final double L, eta, dt; //dt is the temporary step
    private List<Particle> particles;


    public OffLatticeSimulation(double eta, Parameters parameters) {
        this.particles = this.getParticles();
        this.time = 0;
        this.dt = 1;
        this.eta = eta;
        this.L = parameters.getL();
    }


    public void group() {
        CellIndexMethod cim = new CellIndexMethod(particles,true, L);
    }


}
