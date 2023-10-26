package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.utils.Configuration;
import ar.edu.itba.ss.utils.GenerateParticle;

import java.io.IOException;
import java.util.List;

public class App {
    public static void main( String[] args ) throws IOException {
        GenerateParticle.generateStaticFile(Configuration.getStaticFile());
        List<Particle> particles = GenerateParticle.generateParticles(Configuration.getStaticFile());


        // ------------ Este es el item a que pide varias w ------------------------
//        int[] frequencies = {5, 10, 15, 20, 30, 50};
        int[] frequencies = {5};
        for(int frequency : frequencies) {
            GranularSystem granularSystem = new GranularSystem(Configuration.getDt(),
                                                               Configuration.getIterations(),
                                                               frequency,
                                                               Configuration.getOutputFile(),
                                                               particles
            );

            granularSystem.run();
        }

    }
}
