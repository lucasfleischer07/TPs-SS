package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.simulation.OffLatticeSimulation;
import ar.edu.itba.ss.utils.ParticleGeneration;
import ar.edu.itba.ss.utils.WriteFiles;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

public class App {
    public static void main( String[] args ) throws IOException {
        //  Lectura del archivo JSON
        InputStream inputStream = Files.newInputStream(Paths.get("config.json"));
        InputStreamReader reader = new InputStreamReader(inputStream);
        JsonParser jsonParser = new JsonParser();
        JsonObject configObject = jsonParser.parse(reader).getAsJsonObject();

        //  Geteo la informacion del config.json
        String staticFileName = configObject.get("staticFileName").getAsString();
        String outputFileName = configObject.get("outputFileName").getAsString();

        double particleRadius = configObject.get("particleRadius").getAsDouble();
        double rc = configObject.get("rc").getAsDouble();
        double l = configObject.get("L").getAsDouble();
        int n = configObject.get("N").getAsInt();
        int iterations = configObject.get("iterations").getAsInt();
        double eta = configObject.get("eta").getAsDouble();
        double velocity = configObject.get("velocity").getAsDouble();

        // Escribo el archivo static.txt
        WriteFiles writeFiles = new WriteFiles();
        writeFiles.generateStaticFile(staticFileName, particleRadius, l, n, rc, velocity);

        // En base a la info del archivo static.txt genero las particulas
        Parameters particles = ParticleGeneration.generateParticles(staticFileName);

        // Inicializo la instancia de la simulacion
        OffLatticeSimulation offLatticeSimulation = new OffLatticeSimulation(eta, particles, l);

        // Hago la simulacion
        for (int i = 0; i < iterations; i++) {
            offLatticeSimulation.nextIteration();
            writeFiles.generateOutputFile(outputFileName, offLatticeSimulation.getParticles(), offLatticeSimulation.getTime());
        }

    }

}
