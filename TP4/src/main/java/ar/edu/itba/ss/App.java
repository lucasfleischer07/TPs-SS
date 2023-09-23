package ar.edu.itba.ss;

import ar.edu.itba.ss.utils.Configuration;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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
        String outputFileNameEx1 = configObject.get("outputFileNameEx1").getAsString();

        Files.deleteIfExists(Paths.get(outputFileNameEx1));
        File outputFile = new File(outputFileNameEx1);
        FileWriter outputFileWriterEx1 = new FileWriter(outputFile, true);

        double dt =  Configuration.getSimulationTime();

        if(Configuration.getExercise() == 1) {
            // Parameters
            double m = 70;
            double k = Math.pow(10, 4);
            double gamma = 100;
            double A = 1;

            // Initial conditions
            double x = 1;
            double v = -A*gamma / (2*m);

            DampedPointOscillator.VerletAlgorithm(outputFileWriterEx1, x, v, k, gamma, dt, m, A);
            DampedPointOscillator.BeemanAlgorithm(outputFileWriterEx1, x, v, k, gamma, dt, m, A);
            DampedPointOscillator.GearPredictorCorrectorAlgorithm(outputFileWriterEx1, x, v, k, gamma, dt, m, A);
        }

    }
}
