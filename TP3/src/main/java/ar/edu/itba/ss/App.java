package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.utils.ParticleGeneration;
import ar.edu.itba.ss.utils.WriteFiles;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        double enclosure1X = configObject.get("enclosure1X").getAsDouble();
        double enclosure1Y = configObject.get("enclosure1Y").getAsDouble();
        double enclosure2X = configObject.get("enclosure2X").getAsDouble();
        double enclosure2Y = configObject.get("enclosure2Y").getAsDouble();

        int n = configObject.get("N").getAsInt();
        double particleRadius = configObject.get("particleRadius").getAsDouble();
        double velocity = configObject.get("velocity").getAsDouble();
        int mass = configObject.get("mass").getAsInt();

        // Escribo el archivo static.txt
        WriteFiles writeFiles = new WriteFiles();
        writeFiles.generateStaticFile(staticFileName, particleRadius, n, mass, velocity, enclosure1X, enclosure1Y);

        // En base a la info del archivo static.txt genero las particulas
        Parameters parameters = ParticleGeneration.generateParticles(staticFileName, enclosure1X, enclosure2Y);
        System.out.println(parameters.getParticles());
    }
}
