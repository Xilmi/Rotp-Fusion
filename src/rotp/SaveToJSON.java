package rotp;

import rotp.model.game.GameSession;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SaveToJSON {
    static {
        // highlights problems
        System.setProperty("java.awt.headless", "true");
    }

    public static void main(String arg[]) throws IOException, ClassNotFoundException {
//        int chars[] = { 0x1F66D };
//        System.out.println("SPACESHIP "+new String(chars, 0, 1));
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

        String json = RotpJSON.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(newSession);
        System.out.println("json=" + json);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(json.getBytes(StandardCharsets.UTF_8));
        }

        GameSession session = RotpJSON.objectMapper.readValue(outputFile, GameSession.class);
        System.out.println("session="+session);
    }
}
