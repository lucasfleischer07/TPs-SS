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
        writeFiles.writeFiles(staticFileName, particleRadius, l, n, rc, velocity);

        // En base a la info del archivo static.txt genero las particulas
        Parameters particles = ParticleGeneration.generateParticles(staticFileName);

        // Inicializo la instancia de la simulacion
        OffLatticeSimulation offLatticeSimulation = new OffLatticeSimulation(eta, particles, l);

        // Hago la simulacion
        for (int i = 0; i < iterations; i++) {
            offLatticeSimulation.nextIteration();
            System.out.println(offLatticeSimulation.getParticles());
            System.out.println("\n");
            generateOutputFile(outputFileName, offLatticeSimulation.getParticles(), offLatticeSimulation.getTime());
        }

    }

    private static void generateOutputFile(String fileName, List<Particle> particles, int time) throws IOException {
        PrintWriter outputWriter = new PrintWriter(new FileWriter(fileName, true));
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(time).append("\n");

        String formattedLine = null;
        for(Particle particle : particles) {
            formattedLine = String.format(Locale.US, "%d %.2f %.2f %.2f %.2f%n",
                    particle.getId(), particle.getX(),
                    particle.getY(), particle.getV(),
                    particle.getTheta());
            stringBuilder.append(formattedLine);
        }

        if(formattedLine != null) {
            outputWriter.write(formattedLine);
        }

        outputWriter.close();
    }
}
