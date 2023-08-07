package ar.edu.itba.ss;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class GenerateFiles {
    private static String STATIC_FILE = "static.txt";
    private static String DYNAMIC_FILE = "dynamic.txt";
    private static String TIMES_FILE = "times.txt";

    public static void main(String[] args) throws IOException {
        InputStream inputStream = Files.newInputStream(Paths.get("config.json"));
        InputStreamReader reader = new InputStreamReader(inputStream);
        JsonParser jsonParser = new JsonParser();
        JsonObject configObject = jsonParser.parse(reader).getAsJsonObject();

        double particleRadius = 0.25;
        double property = 1.000;
//        TODO: VER BIEN QUE TIPO DE DATO ES CADA UNO DE ESTOS
        double rc = configObject.get("rc").getAsDouble();
        double l = configObject.get("L").getAsDouble();
        double m = configObject.get("M").getAsDouble();
        int n = configObject.get("N").getAsInt();

        PrintWriter staticWriter = new PrintWriter(new FileWriter(STATIC_FILE));
        PrintWriter dynamicWriter = new PrintWriter(new FileWriter(DYNAMIC_FILE));
        staticWriter.printf("%f\n%f\n%f\n%d\n", rc, l, m, n);

        Random random = new Random();
        for(int i = 0; i < n; i++) {
            staticWriter.printf("%f\t%f\n", particleRadius, property);
            dynamicWriter.printf("%.2f\t%.2f\n", random.nextDouble() * l, random.nextDouble() * l);
        }
        staticWriter.close();
        dynamicWriter.close();
    }
}
