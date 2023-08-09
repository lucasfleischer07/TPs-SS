package ar.edu.itba.ss;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class GenerateFiles {

    public static void main(String[] args) throws IOException {
//        Lectura del archivo JSON
        InputStream inputStream = Files.newInputStream(Paths.get("config.json"));
        InputStreamReader reader = new InputStreamReader(inputStream);
        JsonParser jsonParser = new JsonParser();
        JsonObject configObject = jsonParser.parse(reader).getAsJsonObject();

//        Geteo la informacion del config.json
        double particleRadiusMin = configObject.get("particleRadiusMin").getAsDouble();
        double particleRadiusMax = configObject.get("particleRadiusMax").getAsDouble();
        double property = configObject.get("property").getAsDouble();

        double rc = configObject.get("rc").getAsDouble();
        double l = configObject.get("L").getAsDouble();
        int m = configObject.get("M").getAsInt();
        int n = configObject.get("N").getAsInt();
        int times = configObject.get("times").getAsInt();
        String staticFileName = configObject.get("staticFileName").getAsString();
        String dynamicFileName = configObject.get("dynamicFileName").getAsString();
        String statisticsFileName = configObject.get("statisticsFileName").getAsString();
        boolean isStatistics = configObject.get("statistics").getAsBoolean();

//        Creo los archivos para poder escribirlos
        PrintWriter staticWriter = new PrintWriter(new FileWriter(staticFileName));
        PrintWriter dynamicWriter = new PrintWriter(new FileWriter(dynamicFileName));
        if(isStatistics) {
            PrintWriter statisticsWriter = new PrintWriter(new FileWriter(statisticsFileName));
            statisticsWriter.close();
        }

        staticWriter.printf("%d\n%f\n", n, l);

        Random random = new Random();
//        Para mas adelante vamos a tener que generar movimiento de las particulas, entonces ya lo dejamos asi listo
        for(int i = 0; i < times; i++) {
            dynamicWriter.printf("%d\n", i);
            for(int j = 0; j < n; j++) {
                if(i == 0) {
                    staticWriter.printf("%f\t%f\n", particleRadiusMin + Math.random() * (particleRadiusMax - particleRadiusMin), property);
                }
                dynamicWriter.printf("%.2f\t%.2f\n", random.nextDouble() * l, random.nextDouble() * l);
            }
        }

        staticWriter.close();
        dynamicWriter.close();
    }
}
