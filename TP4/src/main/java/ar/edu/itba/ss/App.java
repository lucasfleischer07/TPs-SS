package ar.edu.itba.ss;

import ar.edu.itba.ss.utils.Configuration;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

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

            if(!Configuration.isMseEx1Graph()) {
                DampedPointOscillator.VerletAlgorithm(outputFileWriterEx1, x, v, k, gamma, dt, m, A, Configuration.isMseEx1Graph());
                DampedPointOscillator.BeemanAlgorithm(outputFileWriterEx1, x, v, k, gamma, dt, m, A, Configuration.isMseEx1Graph());
                DampedPointOscillator.GearPredictorCorrectorAlgorithm(outputFileWriterEx1, x, v, k, gamma, dt, m, A, Configuration.isMseEx1Graph());
            } else {
                double[] dtTimes = {0.000001, 0.00001, 0.0001, 0.001, 0.01};
                Files.deleteIfExists(Paths.get("src/main/resources/mseDtTimesEx1.txt"));
                for(double auxDt : dtTimes) {
                    File outputMSEDtTimesFile = new File("src/main/resources/mseDtTimesEx1.txt");
                    FileWriter outputFileWriterMSEDtTimesEx1 = new FileWriter(outputMSEDtTimesFile, true);
//                    Formato del archivo:
//                    dt1
//                    error1Verlet    error1Beeman   error1Gear
//                    dt2
//                    error2Verlet    error2Beeman   error2Gear
//                    dt3
//                    error3Verlet    error3Beeman   error3Gear
                    outputFileWriterMSEDtTimesEx1.write(String.format(Locale.US, "%f\n", auxDt));
                    DampedPointOscillator.VerletAlgorithm(outputFileWriterMSEDtTimesEx1, x, v, k, gamma, auxDt, m, A, Configuration.isMseEx1Graph());
                    DampedPointOscillator.BeemanAlgorithm(outputFileWriterMSEDtTimesEx1, x, v, k, gamma, auxDt, m, A, Configuration.isMseEx1Graph());
                    DampedPointOscillator.GearPredictorCorrectorAlgorithm(outputFileWriterMSEDtTimesEx1, x, v, k, gamma, auxDt, m, A, Configuration.isMseEx1Graph());
                }

            }

        }

    }
}
