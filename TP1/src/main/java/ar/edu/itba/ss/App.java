package ar.edu.itba.ss;

import ar.edu.itba.ss.algorithms.CellIndexMethod;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.utils.WriteFiles;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.*;

public class App {
    private static int AMOUNT_OF_N_STATISTICS_RUN = 500;
    private static int STATISTICS_N_STEPS = 50;
    private static int STATISTICS_N = 5;
    private static int STATISTICS_M = 5;
    private static int AMOUNT_OF_M_STATISTICS_RUN = 13;

    public static void main( String[] args ) throws IOException {
//        Lectura del archivo JSON
        InputStream inputStream = Files.newInputStream(Paths.get("config.json"));
        InputStreamReader reader = new InputStreamReader(inputStream);
        JsonParser jsonParser = new JsonParser();
        JsonObject configObject = jsonParser.parse(reader).getAsJsonObject();

//        Geteo la informacion del config.json
        String staticFileName = configObject.get("staticFileName").getAsString();
        String dynamicFileName = configObject.get("dynamicFileName").getAsString();
        String outputFileName = configObject.get("outputFileName").getAsString();
        String timesFileName = configObject.get("timesFileName").getAsString();
        String statisticsCIMFileName = configObject.get("statisticsCIMFileName").getAsString();
        String statisticsBruteFileName = configObject.get("statisticsBruteFileName").getAsString();
        String statisticsMVariationFileName = configObject.get("statisticsMVariationFileName").getAsString();

        String method = configObject.get("method").getAsString();

        double l = configObject.get("L").getAsInt();
        double rc = configObject.get("rc").getAsDouble();
        int m = configObject.get("M").getAsInt();
        int n = configObject.get("N").getAsInt();
        int times = configObject.get("times").getAsInt();
        boolean isPeriodic = configObject.get("isPeriodic").getAsBoolean();
        boolean isStatistics = configObject.get("statistics").getAsBoolean();

        double particleRadiusMin = configObject.get("particleRadiusMin").getAsDouble();
        double particleRadiusMax = configObject.get("particleRadiusMax").getAsDouble();
        double property = configObject.get("property").getAsDouble();

//        Creo los archivos static.txt y dynamic.txt
        WriteFiles writeFiles = new WriteFiles();
        writeFiles.writeFiles(staticFileName, dynamicFileName, particleRadiusMin, particleRadiusMax, property, l, n, times, isStatistics);

        List<Particle> particleList = new LinkedList<>();

        if(!isStatistics) {
            double maxParticleRadius = generateParticles(particleList, staticFileName, dynamicFileName);
            CellIndexMethod cellIndexMethod = new CellIndexMethod(particleList, l, m, rc, isPeriodic, maxParticleRadius);
            Map<Particle, List<Particle>> neighbors;
            double startTime = System.currentTimeMillis();
            if(Objects.equals(method, "CIM")) {
                neighbors = cellIndexMethod.generateNeighbors();
            } else if(Objects.equals(method, "BRUTE")) {
                neighbors = cellIndexMethod.generateNeighborsBruteForce();
            } else {
                throw new InvalidParameterException("Invalid Method");
            }
            double endTime = System.currentTimeMillis() - startTime;
            generateOutputFile(outputFileName, neighbors);
            generateTimeFile(timesFileName, endTime);
        } else {
            statisticsNRuns(particleList, staticFileName, dynamicFileName, statisticsCIMFileName, l, m, rc, isPeriodic, particleRadiusMin, particleRadiusMax, property, "CIM");
            statisticsNRuns(particleList, staticFileName, dynamicFileName, statisticsBruteFileName, l, m, rc, isPeriodic, particleRadiusMin, particleRadiusMax, property, "BRUTE");
            statisticsMRuns(particleList, staticFileName, dynamicFileName, statisticsMVariationFileName, l, m, rc, isPeriodic, particleRadiusMin, particleRadiusMax, property);
        }

    }

// *    Este metodo es utilizado para correr las statistics. Corre varias veces los algoritmos para un determinado valor de N
    private static void statisticsNRuns(List<Particle> particleList, String staticFileName, String dynamicFileName, String statisticsFileName, double l, int m, double rc, boolean isPeriodic, double particleRadiusMin, double particleRadiusMax, double property, String method) throws IOException {
        WriteFiles generateFiles = new WriteFiles();
        SortedMap<Integer, List<Double>> mapTimes = new TreeMap<>();
        for(int i = 0; i < AMOUNT_OF_N_STATISTICS_RUN; i+= STATISTICS_N_STEPS) {
//            System.out.println("En el primer for con i = " + i);
            mapTimes.putIfAbsent(i, new ArrayList<>());
            for(int j = 0; j < 5; j++) {
//                System.out.println("En el segundo for con j = " + j);
                double maxParticleRadius = generateParticles(particleList, staticFileName, dynamicFileName);
                CellIndexMethod cellIndexMethod = new CellIndexMethod(particleList, l, m, rc, isPeriodic, maxParticleRadius);
                generateFiles.writeFiles(staticFileName, dynamicFileName, particleRadiusMin, particleRadiusMax, property, l, i, 1, true);
                double startTime = System.currentTimeMillis();
                if(Objects.equals(method, "CIM")) {
                    Map<Particle, List<Particle>> neighbors = cellIndexMethod.generateNeighbors();
                } else if(Objects.equals(method, "BRUTE")) {
                    Map<Particle, List<Particle>> neighbors = cellIndexMethod.generateNeighborsBruteForce();
                } else {
                    throw new InvalidParameterException("Invalid Method");
                }
                double endTime = System.currentTimeMillis() - startTime;
                mapTimes.get(i).add(endTime);
            }
        }
        generateStatisticsFile(statisticsFileName, mapTimes);
    }

    // *    Este metodo es utilizado para correr las statistics. Corre varias veces los algoritmos para un determinado valor de N
    private static void statisticsMRuns(List<Particle> particleList, String staticFileName, String dynamicFileName, String statisticsFileName, double l, int m, double rc, boolean isPeriodic, double particleRadiusMin, double particleRadiusMax, double property) throws IOException {
        WriteFiles generateFiles = new WriteFiles();
        SortedMap<Integer, List<Double>> mapTimes = new TreeMap<>();
        for(int i = 0; i < AMOUNT_OF_M_STATISTICS_RUN; i++) {
//            System.out.println("En el primer for con i = " + i);
            double maxParticleRadius = generateParticles(particleList, staticFileName, dynamicFileName);
            generateFiles.writeFiles(staticFileName, dynamicFileName, particleRadiusMin, particleRadiusMax, property, l, i, 1, true);
            mapTimes.putIfAbsent(i, new ArrayList<>());
            for(int j = 0; j < 5; j++) {
//                System.out.println("En el segundo for con j = " + j);
                CellIndexMethod cellIndexMethod = new CellIndexMethod(particleList, l, m, rc, isPeriodic, maxParticleRadius);
                double startTime = System.currentTimeMillis();
                Map<Particle, List<Particle>> neighbors = cellIndexMethod.generateNeighbors();
                double endTime = System.currentTimeMillis() - startTime;
                mapTimes.get(i).add(endTime);
            }
        }
        generateStatisticsFile(statisticsFileName, mapTimes);
    }


//  *  Este metodo lo que hace es leer la informacion del archivo static y dynamic para generar las particulas con eso datos,
//  *  almacenando las particulas particleList y devolviendo el radio de la particula mas grande
    private static double generateParticles(List<Particle> particleList, String staticFileName, String dynamicFileName) throws FileNotFoundException {
        double maxParticleRadius = 0.0;
        try {
            File staticFile = new File(staticFileName);
            Scanner staticFileScanner = new Scanner(staticFile);
            File dynamicFile = new File(dynamicFileName);
            Scanner dynamicFileScanner = new Scanner(dynamicFile);

            int n = staticFileScanner.nextInt();
            double l = staticFileScanner.nextDouble();          // Son valores que no uso pero igual los tengo que scanear para que avance el puntero del scaner
            int actualTimes = dynamicFileScanner.nextInt();     // Son valores que no uso pero igual los tengo que scanear para que avance el puntero del scaner

            for (int i = 0; i < n; i++) {
                double coordinateX = dynamicFileScanner.nextDouble();
                double coordinateY = dynamicFileScanner.nextDouble();
                double radius = staticFileScanner.nextDouble();
                double property = staticFileScanner.nextDouble();
                Particle particle = new Particle(i, coordinateX, coordinateY, radius, property);
                if (maxParticleRadius < radius) {
                    maxParticleRadius = radius;
                }
                particleList.add(particle);
            }

            staticFileScanner.close();
            dynamicFileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file was not found: " + e.getMessage());
        }
        return maxParticleRadius;
    }

// *    Este metodo genera el archivo output con el formato pedido
    private static void generateOutputFile(String fileName, Map<Particle, List<Particle>> neighbors) throws IOException {
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

// *    Este metodo genera el archivo times con el tiempo de ejecucion
    private static void generateTimeFile(String fileName, double endTime) throws IOException {
        PrintWriter output = new PrintWriter(new FileWriter(fileName));
        output.printf("Time = " + endTime + "ms\n");
        output.close();
    }

// *    Este metodo genera el archivo de statistics para cada metodo correspondiendte, con el sifueinte formato:
// *    N1 t1 t2 t3 t4 ...
// *    N2 t1 t2 t3 t4 ...
// *    Siendo ti el tiempo corespondiente a varias corridas con el Ni correspondiente
    private static void generateStatisticsFile(String fileName, SortedMap<Integer, List<Double>> map) throws IOException {
        PrintWriter output = new PrintWriter(new FileWriter(fileName));
        for(int nParticle : map.keySet()) {
            output.printf("%d", nParticle);
            List<Double> timeList = map.get(nParticle);
            for(double time : timeList) {
                output.printf("\t%f", time);
            }
            output.println();
        }
        output.close();
    }
}