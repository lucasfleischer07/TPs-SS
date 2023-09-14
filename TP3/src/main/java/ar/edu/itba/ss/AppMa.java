package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Parameters;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.utils.CollisionsMa;
import ar.edu.itba.ss.utils.ParticleGeneration;
import ar.edu.itba.ss.utils.WriteFiles;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AppMa {
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

        Parameters parameters = ParticleGeneration.generateParticles(staticFileName, enclosure1X, enclosure2Y);

//        for(int rep = 0; rep < 10; rep++) {
            CollisionsMa gd = new CollisionsMa(parameters.getParticles(), enclosure1X, enclosure1Y, enclosure2X, enclosure2Y, 10, 0);
            gd.start();

            try {
                BufferedWriter writerPy = new BufferedWriter(new FileWriter("src/main/resources/outputFinal.txt", true));

                writerPy.write("N " + n + "\n");
                writerPy.write("MAX_STEP " + 1 + "\n");
                writerPy.write("RADIUS " + particleRadius + "\n");
                writerPy.write("MASS " + mass + "\n");
                writerPy.write("INIT_VELOCITY " + velocity + "\n");
                writerPy.write("MAIN_WIDTH " + enclosure1Y + "\n");
                writerPy.write("MAIN_HEIGHT " + enclosure1X + "\n");
                writerPy.write("MINOR_WIDTH " + enclosure2Y + "\n");
                writerPy.write("MINOR_HEIGHT " + enclosure2X + "\n\n");

                writerPy.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }

    }
}
