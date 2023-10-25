package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.utils.Configuration;
import ar.edu.itba.ss.utils.GenerateParticle;

import java.io.IOException;
import java.util.List;

public class App {
    public static void main( String[] args ) throws IOException {
        List<Particle> particles = GenerateParticle.generateParticles(Configuration.getStaticFile());
        int iterations = (int)(Configuration.getIterations() / Configuration.getDt());

        Simulation simulation = new Simulation(Configuration.getW(), Configuration.getL() + Configuration.getL()/10, 0.0, Configuration.getL()/10, Configuration.getD());
        List<Particle> reInjectParticles;

        // ------------ Este es el item a que pide varias w ------------------------
//        int[] frequencies = {5, 10, 15, 20, 30, 50};
        int[] frequencies = {20};
        for(int frequency : frequencies) {
            for(int i = 0; i < iterations; i++) {
                simulation.movement(i * Configuration.getDt(), frequency);

                for(Particle particle : particles) {
                    particle.prediction();
                }

                for(Particle particle : particles) {
                    particle.resetForce();
                }

                reInjectParticles = simulation.update();
            }
        }

    }
}
