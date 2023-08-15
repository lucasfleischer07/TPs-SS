package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.models.Particle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ParticleGeneration {
    private static Parameters parameters;
    public static Parameters generateParticles(String staticFileName) {
        parameters = new Parameters();
        parameters.setParticles(new ArrayList<Particle>());

        try (BufferedReader reader = new BufferedReader(new FileReader(staticFileName))) {
            String line;
            AtomicInteger particleId = new AtomicInteger(0);
            //  Itero sobre cada linea del archivo que existe
            while ((line = reader.readLine()) != null) {
                //  Meto los valores de cada linea en un string
                String[] values = line.split("\t");
                double[] valuesInDoubles = new double[values.length];
                for (int i = 0; i < values.length; i++) {
                    valuesInDoubles[i] = Double.parseDouble(values[i]);
                }
                parameters.addParticle(new Particle(particleId.getAndAdd(1), valuesInDoubles[1], valuesInDoubles[2], valuesInDoubles[3], valuesInDoubles[4], valuesInDoubles[0]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return parameters;

    }
}
