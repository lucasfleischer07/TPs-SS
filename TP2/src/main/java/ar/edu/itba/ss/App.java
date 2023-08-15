package ar.edu.itba.ss;

import ar.edu.itba.ss.utils.WriteFiles;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main( String[] args ) throws IOException {
        //  Lectura del archivo JSON
        InputStream inputStream = Files.newInputStream(Paths.get("config.json"));
        InputStreamReader reader = new InputStreamReader(inputStream);
        JsonParser jsonParser = new JsonParser();
        JsonObject configObject = jsonParser.parse(reader).getAsJsonObject();

        //  Geteo la informacion del config.json
        String staticFileName = configObject.get("staticFileName").getAsString();
        String outputFileName = configObject.get("outputFileName").getAsString();

        double particleRadius = configObject.get("particleRadius").getAsDouble();
        double rc = configObject.get("rc").getAsDouble();
        double l = configObject.get("L").getAsDouble();
        int n = configObject.get("N").getAsInt();
        int iterations = configObject.get("iterations").getAsInt();
        double eta = configObject.get("eta").getAsDouble();
        double velocity = configObject.get("velocity").getAsDouble();

        WriteFiles writeFiles = new WriteFiles();
        writeFiles.writeFiles(staticFileName, particleRadius, l, n, rc, velocity);




    }
}
