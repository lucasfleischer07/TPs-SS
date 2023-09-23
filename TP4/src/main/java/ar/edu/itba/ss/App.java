package ar.edu.itba.ss;


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

        File file = new File(outputFileName);
        FileWriter outputFileWriter = new FileWriter(file, true);

        int exerciseNumber =  configObject.get("exerciseNumber").getAsInt();
        double dt =  configObject.get("dt").getAsDouble();

        if(exerciseNumber == 1) {
            // Parameters
            double m = 70;
            double k = Math.pow(10, 4);
            double gamma = 100;
            double A = 1;

            // Initial conditions
            double x = 1;
            double v = -A*gamma / (2*m);

//            DampedPointOscillator.GearPredictorCorrectorAlgorithm(x, v, k, gamma, dt, m, A, outputFileWriter);

        }

    }
}
