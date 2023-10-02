package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class WriteFiles {

//    * Genera 25 o menos particulas con posiciones al azar
    public void generateStaticFile(String staticFileName, double particleRadius, int n, double mass, double lineLength) throws IOException {
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

//            System.out.println("Particle " + j + " generated");

            // Velocidad aleatoria en dirección positiva o negativa en x
            double u = (Math.random() * 3) + 9; // Genera un número aleatorio entre 9 y 12
            double velocityX = u;

            // Coordenada y en 0 ya que las partículas se mueven solo en x
            double velocityY = 0.0; // Velocidad aleatoria en dirección positiva o negativa en x


            staticWriter.printf("%f\t%f\t%f\t%f\t%f\t%f\t%f\n", x, y, velocityX, velocityY, u, particleRadius, mass);

            // Agrega la nueva partícula a la lista de partículas existentes
            particles.add(new Particle(j+1, x, y, velocityX, velocityY, u, particleRadius, mass, 0.0, 0.0, x));
        }

        staticWriter.close();
    }



    //    * Genera mas de 25 particulas una al lado de la otra separadas de forma equidistante
    public void generateStaticFile2(String staticFileName, double particleRadius, int n, double mass, double lineLength) throws IOException {
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
            double u = (Math.random() * 3) + 9; // Genera un número aleatorio entre 9 y 12
            double velocityX = u;
            double velocityY = 0.0; // Velocidad aleatoria en dirección positiva o negativa en x

            staticWriter.printf("%f\t%f\t%f\t%f\t%f\t%f\t%f\n", x, y, velocityX, velocityY, u, particleRadius, mass);

            // Agrega la nueva partícula a la lista de partículas existentes
            particles.add(new Particle(j+1, x, y, velocityX, velocityY, u, particleRadius, mass, 0.0, 0.0, x));
        }

        staticWriter.close();
    }


    public void generateOutputFile(String fileName, List<Particle> particles, double time) throws IOException {
        PrintWriter outputWriter = new PrintWriter(new FileWriter(fileName, true));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(time).append("\n");

        for(Particle particle : particles) {
            stringBuilder.append(String.format(Locale.US ,"%d\t%f\t%f\t%f\t%f\t%f\t%f\t%f\n",
                    particle.getId(),
                    particle.getX(),
                    particle.getY(),
                    particle.getVelX(),
                    particle.getVelY(),
                    particle.getForceX(),
                    particle.getForceY(),
                    particle.getRadius()));
        }

        outputWriter.write(stringBuilder.toString());
        outputWriter.close();

    }





    public void generateOutputFile2(String fileName, List<Particle> particles, double time) throws IOException {
        PrintWriter outputWriter = new PrintWriter(new FileWriter(fileName, true));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(time).append("\n");

        for(Particle particle : particles) {
            stringBuilder.append(String.format(Locale.US ,"%d\t%f\t%f\t%f\t%f\t%f\t%f\t%f\n",
                    particle.getId(),
                    particle.getxNoPeriodic(),
                    particle.getY(),
                    particle.getVelX(),
                    particle.getVelY(),
                    particle.getForceX(),
                    particle.getForceY(),
                    particle.getRadius()));
        }

        outputWriter.write(stringBuilder.toString());
        outputWriter.close();

    }
}
