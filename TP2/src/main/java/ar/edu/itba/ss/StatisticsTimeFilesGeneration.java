package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.simulation.OffLatticeSimulation;
import ar.edu.itba.ss.utils.ParticleGeneration;
import ar.edu.itba.ss.utils.WriteFiles;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StatisticsTimeFilesGeneration {
    public static int ETA_MAX = 5;
    public static int N_MAX = 3600;
    public static void main(String[] args) throws IOException {
        //  Lectura del archivo JSON
        InputStream inputStream = Files.newInputStream(Paths.get("config.json"));
        InputStreamReader reader = new InputStreamReader(inputStream);
        JsonParser jsonParser = new JsonParser();
        JsonObject configObject = jsonParser.parse(reader).getAsJsonObject();

        //  Geteo la informacion del config.json
        String staticFileName = "src/main/resources/statisticsTime/static";
        String outputFileName = "src/main/resources/statisticsTime/output";

        double particleRadius = configObject.get("particleRadius").getAsDouble();
        double rc = configObject.get("rc").getAsDouble();
        double l = configObject.get("L").getAsDouble();
        int iterations = configObject.get("iterations").getAsInt();
        double velocity = configObject.get("velocity").getAsDouble();

        WriteFiles writeFiles = new WriteFiles();

//        * Este for es para el ruido
//        int n = configObject.get("N").getAsInt();
//        for(int eta = 0; eta <= ETA_MAX; eta += 2) {
//            if(eta == 4) {
//                eta += 1;
//            }
//            // Escribo el archivo static.txt
//            writeFiles.generateStaticFile(staticFileName + "Eta" + eta + "N" + n + "L" + (int)l + ".txt", particleRadius, l, n, rc, velocity);
//
//            // En base a la info del archivo static.txt genero las particulas
//            Parameters particles = ParticleGeneration.generateParticles(staticFileName + "Eta" + eta + "N" + n + "L" + (int)l + ".txt");
//
//            // Inicializo la instancia de la simulacion
//            OffLatticeSimulation offLatticeSimulation = new OffLatticeSimulation(eta, particles, l);
//
//            // Hago la simulacion
//            for (int j = 0; j < iterations; j++) {
//                offLatticeSimulation.nextIteration();
//                writeFiles.generateOutputFile(outputFileName + "Eta" + eta + "N" + n + "L" + (int)l + ".txt", offLatticeSimulation.getParticles(), offLatticeSimulation.getTime());
//            }
//        }

//        * Este es para la densidad
        double eta = configObject.get("eta").getAsDouble();
        for(int n = 200; n <= N_MAX; n += 1400) {
            if(n == 3000) {
                n += 600;
            }
            // Escribo el archivo static.txt
            writeFiles.generateStaticFile(staticFileName + "Eta" + eta + "N" + n + "L" + (int)l + ".txt", particleRadius, l, n, rc, velocity);

            // En base a la info del archivo static.txt genero las particulas
            Parameters particles = ParticleGeneration.generateParticles(staticFileName + "Eta" + eta + "N" + n + "L" + (int)l + ".txt");

            // Inicializo la instancia de la simulacion
            OffLatticeSimulation offLatticeSimulation = new OffLatticeSimulation(eta, particles, l);

            // Hago la simulacion
            for (int j = 0; j < iterations; j++) {
                offLatticeSimulation.nextIteration();
                writeFiles.generateOutputFile(outputFileName + "Eta" + eta + "N" + n + "L" + (int)l + ".txt", offLatticeSimulation.getParticles(), offLatticeSimulation.getTime());
            }
        }

    }
}
