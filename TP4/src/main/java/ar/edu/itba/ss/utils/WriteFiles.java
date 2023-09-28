package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class WriteFiles {

//    TODO: Falta ver el tema de la velocidad angular inicial y el angulo theta
public void generateStaticFile(String staticFileName, double particleRadius, int n, double mass, double velocity, double circleRadius) throws IOException {
    // Creo los archivos para poder escribirlos
    File file = new File(staticFileName);
    if (!file.exists()) {
        file.getParentFile().mkdirs();
        file.createNewFile();
    }

    PrintWriter staticWriter = new PrintWriter(new FileWriter(file));
    staticWriter.printf("0.0\n");

    Random random = new Random();
    List<Particle> particles = new ArrayList<>();

    for (int j = 0; j < n; j++) {
        double angle = random.nextDouble() * 2 * Math.PI;
        double x = circleRadius * Math.cos(angle);
        double y = circleRadius * Math.sin(angle);

        // Genera velocidades aleatorias en todas las direcciones
        angle = random.nextDouble() * 2 * Math.PI;
        double velocityX = velocity * Math.cos(angle);
        double velocityY = velocity * Math.sin(angle);

        staticWriter.printf("%f\t%f\t%f\t%f\t%f\t%f\n", x, y, velocityX, velocityY, particleRadius, mass);

        // Agrega la nueva partícula a la lista de partículas existentes
        particles.add(new Particle(x, y, velocityX, velocityY, particleRadius, mass));
    }

    staticWriter.close();
}


//    public void generateOutputFile(String fileName, List<Particle> particles, double time, int collisionP1, String collisionP2) throws IOException {
//        PrintWriter outputWriter = new PrintWriter(new FileWriter(fileName, true));
//
//        StringBuilder stringBuilder = new StringBuilder();
//
//        stringBuilder.append(time).append("\n");
//        stringBuilder.append(collisionP1).append(" ").append(collisionP2).append("\n");
//
//        for(Particle particle : particles) {
//            stringBuilder.append(String.format(Locale.US ,"%f %f %f %f\n",
//                    particle.getX(),
//                    particle.getY(),
//                    particle.getVx(),
//                    particle.getVy()));
//        }
//
//        outputWriter.write(stringBuilder.toString());
//        outputWriter.close();
//
//    }
//
//    public void generateDataFile(int n, int iterations, double particleRadius, double mass, double velocity, double enclosure1X, double enclosure1Y, double enclosure2X) {
//        try {
//            BufferedWriter writerPy = new BufferedWriter(new FileWriter("src/main/resources/data.txt", true));
//            writerPy.write("N " + n + "\n");
//            writerPy.write("ITERATIONS " + iterations + "\n");
//            writerPy.write("PARTICLE_RADIUS " + particleRadius + "\n");
//            writerPy.write("PARTICLE_MASS " + mass + "\n");
//            writerPy.write("PARTICLE_INITIAL_VEL " + velocity + "\n");
//            writerPy.write("TABLE_WIDTH " + enclosure1X + "\n");
//            writerPy.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
