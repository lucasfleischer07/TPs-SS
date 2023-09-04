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
    public void generateStaticFile(String staticFileName, double particleRadius, int n, int mass, double velocity, double enclosure1X, double enclosure1Y) throws IOException {
        // Creo los archivos para poder escribirlos
        File file = new File(staticFileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        PrintWriter staticWriter = new PrintWriter(new FileWriter(file));

        Random random = new Random();
        for(int j = 0; j < n; j++) {
            staticWriter.printf("%f\t%f\t%f\t%d\t%f\n", particleRadius, random.nextDouble() * enclosure1X, random.nextDouble() * enclosure1Y, mass, velocity);
        }

        staticWriter.close();

    }

//    TODO: Falta adaptarlo todavia
    public void generateOutputFile(String fileName, List<Particle> particles, int time) throws IOException {
        PrintWriter outputWriter = new PrintWriter(new FileWriter(fileName, true));

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(time).append("\n");

        for(Particle particle : particles) {
            stringBuilder.append(String.format(Locale.US ,"%d\t%f\t%f\t%f\n",
                    particle.getId(),
                    particle.getX(),
                    particle.getY(),
                    particle.getV()));
        }

        outputWriter.write(stringBuilder.toString());
        outputWriter.close();

    }
}
