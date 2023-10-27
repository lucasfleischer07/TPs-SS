package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.io.IOException;
import java.util.Locale;

public class WriteFiles {

    public static <T> void GenerateListFile(String fileName, List<T> list) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        PrintWriter outputWriter = new PrintWriter(new FileWriter(fileName, true));
        StringBuilder stringBuilder = new StringBuilder();
        for (T item : list) {
            stringBuilder.append(item).append("\n");
        }
        outputWriter.write(stringBuilder.toString());
        outputWriter.close();

    }

    public static void GenerateBestFrequencyFile(String fileName, Double bestFrequency) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        PrintWriter outputWriter = new PrintWriter(new FileWriter(fileName, true));
        outputWriter.write(bestFrequency + "\n");
        outputWriter.close();

    }

    public static void GenerateOutputFile(String fileName, List<Particle> particles, double time, double limit1, double limit2) throws IOException {
        PrintWriter outputWriter = new PrintWriter(new FileWriter(fileName, true));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(time).append("\t").append(limit1).append("\t").append(limit2).append("\n");

        for(Particle particle : particles) {
            stringBuilder.append(String.format(Locale.US ,"%d\t%f\t%f\t%f\t%f\t%f\t%f\t%f\t%s\n",
                    particle.getId(),
                    particle.getPosition().getX(),
                    particle.getPosition().getY(),
                    particle.getVelocity().getX(),
                    particle.getVelocity().getY(),
                    particle.getFuerzas().getX(),
                    particle.getFuerzas().getY(),
                    particle.getParticleRadius(),
                    particle.getColor()));
        }

        outputWriter.write(stringBuilder.toString());
        outputWriter.close();

    }
}
