package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Grid;
import ar.edu.itba.ss.models.Limit;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.utils.Configuration;
import ar.edu.itba.ss.utils.WriteFiles;
import sun.security.krb5.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GranularSystem implements Runnable {
    private final int iterations;
    private final double dt;
    private final double frequency;
    private final String outputFileName;
    private final List<Particle> particleList;
    private final List<Limit> limitsList;
    private final List<Double> timesList = new ArrayList<>();
    private final List<Double> energyList = new ArrayList<>();
    private final Grid grid;

    public GranularSystem(double dt, double iterations, double frequency, String outputFileName, List<Particle> particles) {
        this.limitsList = new ArrayList<>();
        this.dt = dt;
        this.iterations = (int)(iterations/dt);
        this.frequency = frequency;
        this.outputFileName = outputFileName;
        this.particleList = particles.stream().map(Particle::particleClone).collect(Collectors.toList());

        Limit limit1 = new Limit(Configuration.getW(), Configuration.getL() + Configuration.getL() /10);
        this.limitsList.add(limit1);
        Limit limit2 = new Limit(0.0, Configuration.getL() /10);
        this.limitsList.add(limit2);
        Limit limit3 = new Limit(Configuration.getW(), 0.0);
        this.limitsList.add(limit3);
        Limit leftHoleLimit = new Limit(Configuration.getW() / 2 - Configuration.getD() / 2, Configuration.getL() /10);
        this.limitsList.add(leftHoleLimit);
        Limit rightHoleLimit = new Limit(Configuration.getW() / 2 + Configuration.getD() / 2, Configuration.getL() /10);
        this.limitsList.add(rightHoleLimit);

        this.grid = new Grid(limit1, limit2, Configuration.getD());

        grid.addAll(this.particleList);
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            grid.shake(i * dt, frequency);

            particleList.forEach(Particle::prediction);
            particleList.forEach(Particle::forcesReseted);

            for (int j = 0; j < grid.update(); j++) {
                timesList.add(i * dt);
            }

            grid.updateForces();
            particleList.forEach(Particle::correction);
            particleList.forEach(Particle::forcesReseted);
            grid.updateForces();

            if (i % 100 == 0) {
                energyList.add(particleList.stream().mapToDouble(Particle::getEnergy).sum());
                try {
                    WriteFiles.GenerateOutputFile(outputFileName, particleList, i * dt, limitsList.get(0).getY(), limitsList.get(1).getY());
                    System.out.println("Iteración = " + i);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public List<Double> getTimes() {
        return timesList;
    }

    public double getCaudal(){
        return timesList.size() / (iterations * dt);
    }

    public List<Double> getEnergy() {
        return energyList;
    }
}
