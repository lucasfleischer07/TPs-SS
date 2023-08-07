package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.CellIndexMethod;
import ar.edu.itba.ss.models.Particle;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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

            for(int i = 0; i < n; i++) {
                Particle particle = new Particle(i, dynamicFileScanner.nextDouble(), dynamicFileScanner.nextDouble(), staticFileScanner.nextDouble(), staticFileScanner.nextDouble());
                particleList.add(particle);
            }

            CellIndexMethod cellIndexMethod = new CellIndexMethod(particleList, l, m, rc, isPeriodic);

            long startTime = System.currentTimeMillis();
            Map<Particle, List<Particle>> neighbors = cellIndexMethod.generateNeighbors();
            long endTime = System.currentTimeMillis() - startTime;

            generateOutput(outputFileName, neighbors, endTime);

            staticFileScanner.close();
            dynamicFileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file was not found: " + e.getMessage());
        }

    }

    private static void generateOutput(String fileName, Map<Particle, List<Particle>> neighbors, long endTime) throws IOException {
        PrintWriter output = new PrintWriter(new FileWriter(fileName));
        output.printf("%d\t\t", endTime);
        for(Map.Entry<Particle, List<Particle>> mapKeys : neighbors.entrySet()) {
            output.printf("%d\t\t", mapKeys.getKey().getId());
            for (Particle neighborsParticle :  mapKeys.getValue()) {
                output.printf("%d, ", neighborsParticle.getId());
            }
            output.println();
        }
        output.close();
    }
}
