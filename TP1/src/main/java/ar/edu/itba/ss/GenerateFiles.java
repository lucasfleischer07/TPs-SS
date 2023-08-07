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
        double particleRadius = configObject.get("particleRadius").getAsDouble();
        double property = configObject.get("property").getAsDouble();

        double rc = configObject.get("rc").getAsDouble();
        double l = configObject.get("L").getAsDouble();
        int m = configObject.get("M").getAsInt();
        int n = configObject.get("N").getAsInt();
        String staticFileName = configObject.get("staticFileName").getAsString();
        String dynamicFileName = configObject.get("dynamicFileName").getAsString();

//        Creo los archivos para poder escribirlos
        PrintWriter staticWriter = new PrintWriter(new FileWriter(staticFileName));
        PrintWriter dynamicWriter = new PrintWriter(new FileWriter(dynamicFileName));
        staticWriter.printf("%d\n%f\n", n, l);

//        TODO: Este 0 esta hardcodeado porque mas adelante se va a tener que cambiar, pero para este TP seirve que este el 0
        dynamicWriter.printf("%d\n", 0);

        Random random = new Random();
        for(int i = 0; i < n; i++) {
            staticWriter.printf("%f\t%f\n", particleRadius, property);
            dynamicWriter.printf("%.2f\t%.2f\n", random.nextDouble() * l, random.nextDouble() * l);
        }
        staticWriter.close();
        dynamicWriter.close();
    }
}
