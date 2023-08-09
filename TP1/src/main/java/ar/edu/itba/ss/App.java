package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.CellIndexMethod;
import ar.edu.itba.ss.models.Particle;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.*;

public class App {
    public static void main( String[] args ) throws IOException {
        InputStream inputStream = Files.newInputStream(Paths.get("config.json"));
        InputStreamReader reader = new InputStreamReader(inputStream);
        JsonParser jsonParser = new JsonParser();
        JsonObject configObject = jsonParser.parse(reader).getAsJsonObject();

        String staticFileName = configObject.get("staticFileName").getAsString();
        String dynamicFileName = configObject.get("dynamicFileName").getAsString();
        String outputFileName = configObject.get("outputFileName").getAsString();
        String timesFileName = configObject.get("timesFileName").getAsString();
        String method = configObject.get("method").getAsString();

        double rc = configObject.get("rc").getAsDouble();
        int m = configObject.get("M").getAsInt();
        boolean isPeriodic = configObject.get("isPeriodic").getAsBoolean();

        List<Particle> particleList = new LinkedList<>();

        try {
            File staticFile = new File(staticFileName);
            Scanner staticFileScanner = new Scanner(staticFile);
            File dynamicFile = new File(dynamicFileName);
            Scanner dynamicFileScanner = new Scanner(dynamicFile);

            int n = staticFileScanner.nextInt();
            double l = staticFileScanner.nextDouble();
            int actualTimes = dynamicFileScanner.nextInt();
            double maxParticleRadius = 0.0;
//            TODO: Ver de agregar lo del times aca
            for(int i = 0; i < n; i++) {
                double coordinateX = dynamicFileScanner.nextDouble();
                double coordinateY = dynamicFileScanner.nextDouble();
                double radius = staticFileScanner.nextDouble();
                double property = staticFileScanner.nextDouble();
                Particle particle = new Particle(i, coordinateX, coordinateY, radius, property);
                if(maxParticleRadius < radius) {
                    maxParticleRadius = radius;
                }
                particleList.add(particle);
            }

            CellIndexMethod cellIndexMethod = new CellIndexMethod(particleList, l, m, rc, isPeriodic, maxParticleRadius);
            if(Objects.equals(method, "CIM")) {
                long startTime = System.currentTimeMillis();
                Map<Particle, List<Particle>> neighbors = cellIndexMethod.generateNeighbors();
                long endTime = System.currentTimeMillis() - startTime;
                generateOutput(outputFileName, neighbors);
                generateTimeFile(timesFileName, endTime);
            } else if(Objects.equals(method, "BRUTE")) {
                long startTime = System.currentTimeMillis();
                Map<Particle, List<Particle>> neighbors = cellIndexMethod.generateNeighborsBruteForce();
                long endTime = System.currentTimeMillis() - startTime;
                generateOutput(outputFileName, neighbors);
                generateTimeFile(timesFileName, endTime);
            } else {
                throw new InvalidParameterException("Invalid Method");
            }

            staticFileScanner.close();
            dynamicFileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file was not found: " + e.getMessage());
        }

    }

    private static void generateOutput(String fileName, Map<Particle, List<Particle>> neighbors) throws IOException {
        PrintWriter output = new PrintWriter(new FileWriter(fileName));
        for(Map.Entry<Particle, List<Particle>> mapKeys : neighbors.entrySet()) {
            output.printf("%d\t\t", mapKeys.getKey().getId());
            for (Particle neighborsParticle :  mapKeys.getValue()) {
                output.printf("%d, ", neighborsParticle.getId());
            }
            output.println();
        }
        output.close();
    }

    private static void generateTimeFile(String fileName, long endTime) throws IOException {
        PrintWriter output = new PrintWriter(new FileWriter(fileName));
        output.printf("Time = " + endTime + "ms\n");
        output.close();
    }
}
