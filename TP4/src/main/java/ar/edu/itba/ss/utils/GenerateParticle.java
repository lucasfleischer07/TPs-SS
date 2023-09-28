package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.models.Particle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class GenerateParticle {
    private static Parameters parameters;
    public static Parameters generateParticles(String staticFileName) {
        parameters = new Parameters();
        parameters.setParticles(new ArrayList<Particle>());

        try (BufferedReader reader = new BufferedReader(new FileReader(staticFileName))) {
            String line;

            // Saltear la primera línea del archivo
            reader.readLine();

            //  Itero sobre cada linea del archivo que existe
            while ((line = reader.readLine()) != null) {
                //  Meto los valores de cada linea en un string
                String[] values = line.split("\t");
                double[] valuesInDoubles = new double[values.length];
                for (int i = 0; i < values.length; i++) {
                    valuesInDoubles[i] = Double.parseDouble(values[i]);
                }
                parameters.addParticle(new Particle(valuesInDoubles[0], valuesInDoubles[1], valuesInDoubles[2], valuesInDoubles[3], valuesInDoubles[4], valuesInDoubles[5]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return parameters;

    }
}
