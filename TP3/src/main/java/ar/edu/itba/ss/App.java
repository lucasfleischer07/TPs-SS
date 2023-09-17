package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.utils.Collision;
import ar.edu.itba.ss.utils.ParticleGeneration;
import ar.edu.itba.ss.utils.WriteFiles;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws IOException {
        //  Lectura del archivo JSON
        InputStream inputStream = Files.newInputStream(Paths.get("config.json"));
        InputStreamReader reader = new InputStreamReader(inputStream);
        JsonParser jsonParser = new JsonParser();
        JsonObject configObject = jsonParser.parse(reader).getAsJsonObject();

        //  Geteo la informacion del config.json
        String staticFileName = configObject.get("staticFileName").getAsString();
        String outputFileName = configObject.get("outputFileName").getAsString();

        double enclosure1X = configObject.get("enclosure1X").getAsDouble();
        double enclosure1Y = configObject.get("enclosure1Y").getAsDouble();
        double enclosure2X = configObject.get("enclosure2X").getAsDouble();
        double enclosure2Y = configObject.get("enclosure2Y").getAsDouble();

        int n = configObject.get("N").getAsInt();
        double particleRadius = configObject.get("particleRadius").getAsDouble();
        double velocity = configObject.get("velocity").getAsDouble();
        int mass = configObject.get("mass").getAsInt();
        int iterationsAmount = configObject.get("iterations").getAsInt();

        WriteFiles writeFiles = new WriteFiles();

        double[] lValues = {0.03, 0.05, 0.07, 0.09};
        for(double l : lValues) {
            // Escribo el archivo static.txt
            writeFiles.generateStaticFile(staticFileName + l + ".txt", particleRadius, n, mass, velocity, enclosure1X, enclosure1Y, l);

            // En base a la info del archivo static.txt genero las particulas
            Parameters parameters = ParticleGeneration.generateParticles(staticFileName + l + ".txt", enclosure1X, l);
            Collision collision = new Collision(parameters.getParticles(), enclosure1X, l);

            long elapsedTime = 0;
            Object[] actualTime;
            for(int i = 1; i <= iterationsAmount; i++) {
                long startTime = System.currentTimeMillis();
                actualTime = collision.nextIteration();
                long endTime = System.currentTimeMillis();
                elapsedTime = elapsedTime + endTime - startTime;
                writeFiles.generateOutputFile(outputFileName + l + ".txt", parameters.getParticles(), (double) actualTime[0], (int) actualTime[1], actualTime[2].toString());
            }
        }

        writeFiles.generateDataFile(n, iterationsAmount, particleRadius, mass, velocity, enclosure1X, enclosure1Y, enclosure2X);

    }
}
