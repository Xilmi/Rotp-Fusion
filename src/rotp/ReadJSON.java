package rotp;

import rotp.model.game.GameSession;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ReadJSON {
    static {
        // highlights problems
        System.setProperty("java.awt.headless", "true");
    }

    public static void main(String arg[]) throws IOException, ClassNotFoundException {
        int chars[] = { 0x1F66D };
        System.out.println("SPACESHIP "+new String(chars, 0, 1));
        if (arg.length != 2) {
            System.out.println("ReadJSON input.rotp output.json");
            System.exit(2);
        }
        File inputFile = new File(arg[0]);
        File outputFile = new File(arg[1]);

        RotpCommon.headlessInit();

        System.out.println("SUCCESS");


        GameSession session = RotpJSON.objectMapper.readValue(inputFile, GameSession.class);
        System.out.println("session="+session);
    }
}
