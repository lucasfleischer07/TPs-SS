package ar.edu.itba.ss.utils;

import java.nio.file.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class Configuration {

    private static double simulationTime, particleRadius, lineLength, mass;
    private static int outputIntervalTime, exercise, iterations, n;
    private static boolean debug, mseEx1Graph;
    private static String outputFileNameEx1, mscErrorFileNameEx1, staticFileNameEx2, outputFileNameEx2, integrationMethod;

    static {
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("config.json"));
            InputStreamReader reader = new InputStreamReader(inputStream);
            JsonParser jsonParser = new JsonParser();
            JsonObject configObject = jsonParser.parse(reader).getAsJsonObject();

            particleRadius = configObject.get("particleRadius").getAsDouble();
            lineLength = configObject.get("lineLength").getAsDouble();
            mass = configObject.get("mass").getAsDouble();
            simulationTime = configObject.get("dt").getAsDouble();
            outputIntervalTime = configObject.get("outputIntervalTime").getAsInt();
            iterations = configObject.get("iterations").getAsInt();
            exercise = configObject.get("exerciseNumber").getAsInt();
            n = configObject.get("N").getAsInt();
            debug = configObject.get("debug").getAsBoolean();
            outputFileNameEx1 = configObject.get("outputFileNameEx1").getAsString();
            mscErrorFileNameEx1 = configObject.get("mscErrorFileNameEx1").getAsString();
            staticFileNameEx2 = configObject.get("staticFileNameEx2").getAsString();
            outputFileNameEx2 = configObject.get("outputFileNameEx2").getAsString();
            integrationMethod = configObject.get("integrationMethod").getAsString();
            mseEx1Graph = configObject.get("mseEx1Graph").getAsBoolean();

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

    public static String getStaticFileNameEx2() {
        return staticFileNameEx2;
    }

    public static void setStaticFileNameEx2(String staticFileNameEx2) {
        Configuration.staticFileNameEx2 = staticFileNameEx2;
    }

    public static String getOutputFileNameEx2() {
        return outputFileNameEx2;
    }

    public static void setOutputFileNameEx2(String outputFileNameEx2) {
        Configuration.outputFileNameEx2 = outputFileNameEx2;
    }

    public static String getOutputFileNameEx1() {
        return outputFileNameEx1;
    }

    public static void setOutputFileNameEx1(String outputFileNameEx1) {
        Configuration.outputFileNameEx1 = outputFileNameEx1;
    }

    public static String getMscErrorFileNameEx1() {
        return mscErrorFileNameEx1;
    }

    public static void setMscErrorFileNameEx1(String mscErrorFileNameEx1) {
        Configuration.mscErrorFileNameEx1 = mscErrorFileNameEx1;
    }

    public static boolean isMseEx1Graph() {
        return mseEx1Graph;
    }

    public static void setMseEx1Graph(boolean mseEx1Graph) {
        Configuration.mseEx1Graph = mseEx1Graph;
    }

    public static double getParticleRadius() {
        return particleRadius;
    }

    public static void setParticleRadius(double particleRadius) {
        Configuration.particleRadius = particleRadius;
    }

    public static double getLineLength() {
        return lineLength;
    }

    public static void setLineLength(double lineLength) {
        Configuration.lineLength = lineLength;
    }

    public static double getMass() {
        return mass;
    }

    public static void setMass(double mass) {
        Configuration.mass = mass;
    }

    public static int getIterations() {
        return iterations;
    }

    public static void setIterations(int iterations) {
        Configuration.iterations = iterations;
    }

    public static int getN() {
        return n;
    }

    public static void setN(int n) {
        Configuration.n = n;
    }

    public static String getIntegrationMethod() {
        return integrationMethod;
    }

    public static void setIntegrationMethod(String integrationMethod) {
        Configuration.integrationMethod = integrationMethod;
    }
}
