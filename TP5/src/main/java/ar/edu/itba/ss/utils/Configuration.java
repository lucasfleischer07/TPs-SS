package ar.edu.itba.ss.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Configuration {

    private static double particleMinRadius, particleMaxRadius, w, l, d, mass, dt, a;
    private static int iterations, n;
    private static String outputFile, staticFile, exercise;

    static {
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("config.json"));
            InputStreamReader reader = new InputStreamReader(inputStream);
            JsonParser jsonParser = new JsonParser();
            JsonObject configObject = jsonParser.parse(reader).getAsJsonObject();

            particleMinRadius = configObject.get("particleMinRadius").getAsDouble();
            particleMaxRadius = configObject.get("particleMaxRadius").getAsDouble();
            w = configObject.get("W").getAsDouble();
            l = configObject.get("L").getAsDouble();
            d = configObject.get("D").getAsDouble();
            n = configObject.get("N").getAsInt();
            mass = configObject.get("mass").getAsDouble();
            dt = configObject.get("dt").getAsDouble();
            iterations = configObject.get("iterations").getAsInt();
            a = configObject.get("A").getAsDouble();
            outputFile = configObject.get("outputFile").getAsString();
            staticFile = configObject.get("staticFile").getAsString();
            exercise = configObject.get("exercise").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getParticleMinRadius() {
        return particleMinRadius;
    }

    public static double getParticleMaxRadius() {
        return particleMaxRadius;
    }

    public static double getW() {
        return w;
    }

    public static double getL() {
        return l;
    }

    public static double getD() {
        return d;
    }

    public static double getMass() {
        return mass;
    }

    public static double getDt() {
        return dt;
    }

    public static int getIterations() {
        return iterations;
    }

    public static int getN() {
        return n;
    }

    public static double getA() {
        return a;
    }

    public static String getOutputFile() {
        return outputFile;
    }

    public static String getStaticFile() {
        return staticFile;
    }

    public static String getExercise() {
        return exercise;
    }
}
