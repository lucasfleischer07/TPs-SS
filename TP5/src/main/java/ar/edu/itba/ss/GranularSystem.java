package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Simulation;
import ar.edu.itba.ss.models.Vertex;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.utils.Configuration;
import ar.edu.itba.ss.utils.WriteFiles;

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
    private final List<Vertex> limitsList;
    private final List<Double> timesList = new ArrayList<>();
    private final List<Double> energyList = new ArrayList<>();
    private final Simulation simulation;

    public GranularSystem(double dt, double iterations, double frequency, String outputFileName, List<Particle> particles) {
        this.limitsList = new ArrayList<>();
        this.dt = dt;
        this.iterations = (int)(iterations/dt);
        this.frequency = frequency;
        this.outputFileName = outputFileName;
        this.particleList = particles.stream().map(Particle::particleClone).collect(Collectors.toList());

        Vertex vertex1 = new Vertex(Configuration.getW(), Configuration.getL() + Configuration.getL() /10);
        this.limitsList.add(vertex1);
        Vertex vertex2 = new Vertex(0.0, Configuration.getL() /10);
        this.limitsList.add(vertex2);
        Vertex vertex3 = new Vertex(Configuration.getW(), 0.0);
        this.limitsList.add(vertex3);
        Vertex leftHoleVertex = new Vertex(Configuration.getW() / 2 - Configuration.getD() / 2, Configuration.getL() /10);
        this.limitsList.add(leftHoleVertex);
        Vertex rightHoleVertex = new Vertex(Configuration.getW() / 2 + Configuration.getD() / 2, Configuration.getL() /10);
        this.limitsList.add(rightHoleVertex);

        this.simulation = new Simulation(vertex1, vertex2, Configuration.getD());

        simulation.addAll(this.particleList);
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            simulation.siloMovement(i * dt, frequency);

            particleList.forEach(Particle::prediction);
            particleList.forEach(Particle::forcesReseted);

            for (int j = 0; j < simulation.update(); j++) {
                timesList.add(i * dt);
            }

            simulation.updateForces();
            particleList.forEach(Particle::correction);
            particleList.forEach(Particle::forcesReseted);
            simulation.updateForces();

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