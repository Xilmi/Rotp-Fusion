package rotp;

import rotp.model.galaxy.Ships;
import rotp.model.game.GameSession;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SaveToJSONShips {
    static {
        // highlights problems
        System.setProperty("java.awt.headless", "true");
    }

    public static void main(String arg[]) throws IOException, ClassNotFoundException {
        if (arg.length != 2) {
            System.out.println("SaveToJSON input.rotp output.json");
            System.exit(2);
        }
        File inputFile = new File(arg[0]);
        File outputFile = new File(arg[1]);

        RotpCommon.headlessInit();

        System.out.println("SUCCESS");

        InputStream file = new FileInputStream(inputFile);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);
        GameSession newSession = (GameSession) input.readObject();
        RotpJSON.setStaticField(GameSession.class, "instance", newSession);

        Ships ships = newSession.galaxy().ships;


        String json = RotpJSON.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ships);
        System.out.println("json=" + json);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(json.getBytes(StandardCharsets.UTF_8));
        }

        Ships ships2 = RotpJSON.objectMapper.readValue(outputFile, Ships.class);
        System.out.println("ships="+ships2);
    }
}
