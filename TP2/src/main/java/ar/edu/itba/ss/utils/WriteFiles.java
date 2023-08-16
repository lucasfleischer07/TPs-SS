package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class WriteFiles {
    public void generateStaticFile(String staticFileName, double particleRadius, double l, int n, double rc, double velocity) throws IOException {
        // Creo los archivos para poder escribirlos
        File file = new File(staticFileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        PrintWriter staticWriter = new PrintWriter(new FileWriter(file));

        staticWriter.printf("%f\n", rc);

        Random random = new Random();
        for(int j = 0; j < n; j++) {
            staticWriter.printf("%f\t%f\t%f\t%f\t%f\n", particleRadius, random.nextDouble() * l, random.nextDouble() * l, velocity, random.nextDouble() * 2 * Math.PI - Math.PI);
        }

        staticWriter.close();

    }

    public void generateOutputFile(String fileName, List<Particle> particles, int time) throws IOException {
        PrintWriter outputWriter = new PrintWriter(new FileWriter(fileName, true));

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(time).append("\n");

        for(Particle particle : particles) {
            stringBuilder.append(String.format(Locale.US ,"%d\t%f\t%f\t%f\t%f\n",
                                                particle.getId(),
                                                particle.getX(),
                                                particle.getY(),
                                                particle.getV(),
                                                particle.getTheta()));
        }

        outputWriter.write(stringBuilder.toString());
        outputWriter.close();

    }
}
