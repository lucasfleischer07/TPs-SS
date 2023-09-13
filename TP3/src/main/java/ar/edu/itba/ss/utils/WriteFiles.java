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
        staticWriter.printf("0.0\n");

        Random random = new Random();
        for (int j = 0; j < n; j++) {
            double x, y;

            // Genera coordenadas (x, y) aleatorias dentro del área definida por enclosure1X y enclosure1Y (teniendo en cuenta el radio)
            x = particleRadius + random.nextDouble() * (enclosure1X - 2 * particleRadius);
            y = particleRadius + random.nextDouble() * (enclosure1Y - 2 * particleRadius);

            // Genera velocidades aleatorias en todas las direcciones
            double angle = random.nextDouble() * 2 * Math.PI;
            double velocityX = velocity * Math.cos(angle);
            double velocityY = velocity * Math.sin(angle);

            staticWriter.printf("%f\t%f\t%f\t%d\t%f\t%f\n", particleRadius, x, y, mass, velocityX, velocityY);
        }

        staticWriter.close();
    }

    public void generateOutputFile(String fileName, List<Particle> particles, double time) throws IOException {
        PrintWriter outputWriter = new PrintWriter(new FileWriter(fileName, true));

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(time).append("\n");

        for(Particle particle : particles) {
            stringBuilder.append(String.format(Locale.US ,"%f\t%f\t%f\t%f\t%f\n",
//                    particle.getId(),
                    particle.getX(),
                    particle.getY(),
                    particle.getVx(),
                    particle.getVy(),
                    particle.getRadius()));
        }

        outputWriter.write(stringBuilder.toString());
        outputWriter.close();

    }
}
