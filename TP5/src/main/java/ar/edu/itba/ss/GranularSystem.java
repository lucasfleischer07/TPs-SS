package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.utils.Configuration;
import ar.edu.itba.ss.utils.WriteFiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GranularSystem implements Runnable {

    private final double dt;
    private final double frequency;
    private final List<Particle> particles;
    private final Simulation simulation;
//    private final List<Limit> limits;
    private final int iterations;
    private final String path;
    private final List<Double> times = new ArrayList<>();
    private final List<Double> energy = new ArrayList<>();


    public GranularSystem(double l, double w, double dt, double d, double maxTime, double frequency, String outputFileName, List<Particle> particles) {
        this.dt = dt;
        this.iterations = (int)(maxTime/dt);
        this.frequency = frequency;
        this.particles = particles.stream().map(Particle::copy).collect(Collectors.toList());
        this.path = outputFileName;
        simulation = new Simulation(Configuration.getW(), Configuration.getL() + Configuration.getL()/10, 0.0, Configuration.getL()/10, Configuration.getD());
        simulation.addAll(this.particles);

    }

    @Override
    public void run() {
        double auxDt = dt;
        for (int i = 0; i < iterations; i++) {
            simulation.movement(i * dt, frequency);

            particles.forEach(Particle::prediction);
            particles.forEach(Particle::resetForce);

            for (int j = 0; j < simulation.update(); j++) {
                times.add(i * dt);
            }

            simulation.updateForces();
            particles.forEach(Particle::correction);
            particles.forEach(Particle::resetForce);
            simulation.updateForces();

            if (i % 100 == 0) {
                System.out.println("Iteración = " + i);

                energy.add(particles.stream().mapToDouble(Particle::getEnergy).sum());
                try {
                    WriteFiles.GenerateOutputFile(path, particles, i*dt, simulation.getBottomLeftLimitY(), simulation.getTopRightLimitY());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public List<Double> getTimes() {
        return times;
    }

    public double getCaudal(){
        return times.size() / (iterations * dt);
    }

    public List<Double> getEnergy() {
        return energy;
    }



}
