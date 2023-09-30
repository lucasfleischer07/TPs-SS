package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class WriteFiles {

//    TODO: Falta ver el tema de la velocidad angular inicial y el angulo theta
//    TODO: Codigo que genera las particulas circulares
//public void generateStaticFile(String staticFileName, double particleRadius, int n, double mass, double velocity, double circleRadius) throws IOException {
//    // Creo los archivos para poder escribirlos
//    File file = new File(staticFileName);
//    if (!file.exists()) {
//        file.getParentFile().mkdirs();
//        file.createNewFile();
//    }
//
//    PrintWriter staticWriter = new PrintWriter(new FileWriter(file));
//    staticWriter.printf("0.0\n");
//
//    Random random = new Random();
//    List<Particle> particles = new ArrayList<>();
//
//    for (int j = 0; j < n; j++) {
//        boolean isOverlap;
//        double x, y;
//        do {
//            isOverlap = false;
//            double angle = random.nextDouble() * 2 * Math.PI;
//            x = circleRadius * Math.cos(angle);
//            y = circleRadius * Math.sin(angle);
//
//            // Verifica si la nueva partícula se superpone con alguna partícula existente
//            for (Particle existingParticle : particles) {
//                double distance = Math.sqrt(Math.pow(x - existingParticle.getX(), 2) + Math.pow(y - existingParticle.getY(), 2));
//                if (distance < 2 * particleRadius) {
//                    isOverlap = true;
//                    break; // Superposición, regenera las coordenadas
//                }
//            }
//        } while (isOverlap);
//        System.out.println(j);
//
//
//        // Genera velocidades aleatorias en todas las direcciones
//        double angle = random.nextDouble() * 2 * Math.PI;
//        double velocityX = velocity * Math.cos(angle);
//        double velocityY = velocity * Math.sin(angle);
//
//        staticWriter.printf("%f\t%f\t%f\t%f\t%f\t%f\n", x, y, velocityX, velocityY, particleRadius, mass);
//
//        // Agrega la nueva partícula a la lista de partículas existentes
//        particles.add(new Particle(x, y, velocityX, velocityY, particleRadius, mass));
//    }
//
//    staticWriter.close();
//}

//    * Genera 25 o menos particulas con posiciones al azar
    public void generateStaticFile(String staticFileName, double particleRadius, int n, double mass, double velocity, double lineLength) throws IOException {
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
            double x = random.nextDouble() * lineLength;
            double y = 0;

            boolean isOverlap;

            do {
                isOverlap = false;

                // Verifica si la nueva partícula se superpone con alguna partícula existente
                for (Particle existingParticle : particles) {
                    double distance = Math.abs(x - existingParticle.getX());
                    if (distance < 2 * particleRadius) {
                        isOverlap = true;
                        // Regenera la coordenada x si hay superposición
                        x = random.nextDouble() * lineLength;
                        break;
                    }
                }
            } while (isOverlap);

            System.out.println("Particle " + j + " generated");

            // Velocidad aleatoria en dirección positiva o negativa en x
            double velocityX = (random.nextBoolean() ? 1 : -1) * velocity;

            // Coordenada y en 0 ya que las partículas se mueven solo en x
            double velocityY = 0.0; // Velocidad aleatoria en dirección positiva o negativa en x


            staticWriter.printf("%f\t%f\t%f\t%f\t%f\t%f\n", x, y, velocityX, 0.0, particleRadius, mass);

            // Agrega la nueva partícula a la lista de partículas existentes
            particles.add(new Particle(x, y, velocityX, velocityY, particleRadius, mass));
        }

        staticWriter.close();
    }



//    * Genera mas de 25 particulas una al lado de la otra separadas de forma equidistante
public void generateStaticFile2(String staticFileName, double particleRadius, int n, double mass, double velocity, double lineLength) throws IOException {
    // Creo los archivos para poder escribirlos
    File file = new File(staticFileName);
    if (!file.exists()) {
        file.getParentFile().mkdirs();
        file.createNewFile();
    }

    PrintWriter staticWriter = new PrintWriter(new FileWriter(file));
    staticWriter.printf("0.0\n");

    List<Particle> particles = new ArrayList<>();
    double requiredSpacing = 2 * particleRadius; // Espacio requerido para cada partícula

    if (requiredSpacing * n > lineLength) {
        throw new IllegalArgumentException("No es posible colocar " + n + " partículas en un espacio de longitud " + lineLength + " con un radio de " + particleRadius);
    }

    double unusedSpace = lineLength - (requiredSpacing * n); // Espacio no utilizado
    double spacing = unusedSpace > 0 ? unusedSpace / (n - 1) : 0; // Espacio adicional entre partículas

    for (int j = 0; j < n; j++) {
        double x = j * (requiredSpacing + spacing); // Coordenada (x) con espacio adicional
        double y = 0; // Coordenada y en 0 ya que las partículas se mueven solo en x
        double velocityX = (Math.random() < 0.5 ? 1 : -1) * velocity; // Velocidad aleatoria en dirección positiva o negativa en x
        double velocityY = 0.0; // Velocidad aleatoria en dirección positiva o negativa en x

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
