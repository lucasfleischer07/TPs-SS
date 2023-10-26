package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.SimColors;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateParticle {

    private static Parameters parameters;


    public static List<Particle> generateParticles(String staticFileName) {
        parameters = new Parameters();
        parameters.setParticles(new ArrayList<Particle>());

        try (BufferedReader reader = new BufferedReader(new FileReader(staticFileName))) {
            String line;

            // Saltear la primera línea del archivo
            reader.readLine();

            //  Itero sobre cada linea del archivo que existe
            int j = 0;
            while ((line = reader.readLine()) != null) {
                j++;
                //  Meto los valores de cada linea en un string
                String[] values = line.split("\t");
                double[] valuesInDoubles = new double[values.length];
                for (int i = 0; i < values.length; i++) {
                    valuesInDoubles[i] = Double.parseDouble(values[i]);
                }
                parameters.addParticle(new Particle((int)valuesInDoubles[0], valuesInDoubles[1], valuesInDoubles[2], valuesInDoubles[3], Configuration.getMass(), Configuration.getDt(), SimColors.RED));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return parameters.getParticles();
    }

    public static void generateStaticFile(String staticFileName) throws IOException {
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
        double x, y, radius;
        boolean overlap;

        for (int i = 1; i <= Configuration.getN(); i++) {
            radius = Configuration.getParticleMinRadius() + Math.random() * (Configuration.getParticleMaxRadius() - Configuration.getParticleMinRadius());
            x = radius + Math.random() * (Configuration.getW() - 2 * radius);
            y = radius + Configuration.getL()/10 + Math.random() * (Configuration.getL() - 2 * radius);
            Particle newParticle = new Particle(i, x, y, radius, Configuration.getMass(), Configuration.getDt(), SimColors.RED);

            overlap = false;

            // Check for overlap with existing particles
            for (Particle existingParticle : particles) {
                double distance = Math.sqrt(Math.pow(existingParticle.getX() - newParticle.getX(), 2) + Math.pow(existingParticle.getY() - newParticle.getY(), 2));
                if (distance < newParticle.getRadius() + existingParticle.getRadius()) {
                    overlap = true;
                    break;
                }
            }

            // If overlap detected, generate a new particle
            if (overlap) {
                i--;
            } else {
                staticWriter.printf("%d\t%f\t%f\t%f\n", i, x, y, radius);
                particles.add(newParticle);
            }
        }
        staticWriter.close();
    }

//    public static List<Particle> generateParticles(String staticFileName) throws IOException {
//        // Creo los archivos para poder escribirlos
//        File file = new File(staticFileName);
//        if (!file.exists()) {
//            file.getParentFile().mkdirs();
//            file.createNewFile();
//        }
//
//        PrintWriter staticWriter = new PrintWriter(new FileWriter(file));
//        staticWriter.printf("0.0\n");
//
//        Random random = new Random();
//        List<Particle> particles = new ArrayList<>();
//        double x, y, radius;
//        boolean overlap;
//
//        for (int i = 1; i <= Configuration.getN(); i++) {
//            radius = Configuration.getParticleMinRadius() + Math.random() * (Configuration.getParticleMaxRadius() - Configuration.getParticleMinRadius());
//            x = radius + Math.random() * (Configuration.getW() - 2 * radius);
//            y = radius + Configuration.getL()/10 + Math.random() * (Configuration.getL() - 2 * radius);
//            Particle newParticle = new Particle(i, x, y, radius, Configuration.getMass(), Configuration.getDt(), SimColors.RED);
//
//            overlap = false;
//
//            // Check for overlap with existing particles
//            for (Particle existingParticle : particles) {
//                double distance = Math.sqrt(Math.pow(existingParticle.getX() - newParticle.getX(), 2) + Math.pow(existingParticle.getY() - newParticle.getY(), 2));
//                if (distance < newParticle.getRadius() + existingParticle.getRadius()) {
//                    overlap = true;
//                    break;
//                }
//            }
//
//            // If overlap detected, generate a new particle
//            if (overlap) {
//                i--;
//            } else {
//                staticWriter.printf("%d\t%f\t%f\t%f\n", i, x, y, radius);
//                particles.add(newParticle);
//            }
//        }
//        staticWriter.close();
//        return particles;
//    }


    public static void reInjectParticles(List<Particle> particles, List<Particle> particlesToInject) {
        boolean overlap;
        double minY = ((Configuration.getL()/7) * 4); // = 40

        for (int i = 0; i < particlesToInject.size(); i++) {
            Particle p = particlesToInject.get(i);

            p.setX(p.getRadius() + Math.random() * (Configuration.getW() - 2 * p.getRadius()));
            p.setY(minY + Configuration.getL()/10 + Math.random() * ((Configuration.getL()-minY) - p.getRadius()));
            // Y e [47, 77-radio]

            overlap = false;

            // Check for overlap with existing particles
            for (Particle existingParticle : particles) {
                if(!existingParticle.equals(p)){
                    double distance = Math.sqrt(Math.pow(existingParticle.getX() - p.getX(), 2) + Math.pow(existingParticle.getY() - p.getY(), 2));
                    if (distance < p.getRadius() + existingParticle.getRadius()) {
                        overlap = true;
                        break;
                    }
                }
            }

            // If overlap detected, generate a new particle
            if (overlap) {
                i--;
            }
        }
    }

    public static boolean overlap(Particle p1, Particle p2) {
        if (!p1.equals(p2)) {
            return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2)) < p1.getRadius() + p2.getRadius();
        } else {
            return false;
        }
    }

}
