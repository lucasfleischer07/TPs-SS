package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.models.Particle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ParticleGeneration {
    private static Parameters parameters;
    public static Parameters generateParticles(String staticFileName) {
        parameters = new Parameters();
        parameters.setParticles(new ArrayList<Particle>());

        try (BufferedReader reader = new BufferedReader(new FileReader(staticFileName))) {
            String line;
            //  Itero sobre cada linea del archivo que existe
            while ((line = reader.readLine()) != null) {
                //  Meto los valores de cada linea en un string
                String[] values = line.split("\t");

                //  Para cada valor que corresponde a esa linea, lo agarro
                for (String value : values) {
                    System.out.print(value + "\t");
                }
                System.out.println();  // Nueva línea después de cada línea del archivo
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
