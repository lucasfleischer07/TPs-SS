package ar.edu.itba.ss.utils;

import java.nio.file.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Configuration {

    private static double simulationTime;
    private static int outputIntervalTime, exercise;
    private static boolean debug;
    private static String staticFile, outputFile;

    static {
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("config.json"));
            InputStreamReader reader = new InputStreamReader(inputStream);
            JsonParser jsonParser = new JsonParser();
            JsonObject configObject = jsonParser.parse(reader).getAsJsonObject();

            simulationTime = configObject.get("dt").getAsDouble();
            outputIntervalTime = configObject.get("outputIntervalTime").getAsInt();
            exercise = configObject.get("exerciseNumber").getAsInt();
            debug = configObject.get("debug").getAsBoolean();
            staticFile = configObject.get("staticFileName").getAsString();
            outputFile = configObject.get("outputFileNameEx1").getAsString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getSimulationTime() {
        return simulationTime;
    }

    public static void setSimulationTime(double simulationTime) {
        Configuration.simulationTime = simulationTime;
    }

    public static int getOutputIntervalTime() {
        return outputIntervalTime;
    }

    public static void setOutputIntervalTime(int outputIntervalTime) {
        Configuration.outputIntervalTime = outputIntervalTime;
    }

    public static int getExercise() {
        return exercise;
    }

    public static void setExercise(int exercise) {
        Configuration.exercise = exercise;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        Configuration.debug = debug;
    }

    public static String getStaticFile() {
        return staticFile;
    }

    public static void setStaticFile(String staticFile) {
        Configuration.staticFile = staticFile;
    }

    public static String getOutputFile() {
        return outputFile;
    }

    public static void setOutputFile(String outputFile) {
        Configuration.outputFile = outputFile;
    }
}
