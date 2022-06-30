package rotp;

import rotp.model.game.GameSession;
import rotp.model.planet.PlanetFactory;
import rotp.model.ships.ShipLibrary;
import rotp.model.tech.TechLibrary;
import rotp.util.LanguageManager;
import rotp.util.sound.SoundManager;

public class RotpCommon {
    public static void headlessInit() {
        // Headless init
        TechLibrary.current();

        LanguageManager.current().selectedLanguageName();

        SoundManager.current();

        // ImageManager.current();

        // AnimationManager.current();

        // DialogueManager.current();

        ShipLibrary.current();

        // PlanetImager.current();

        PlanetFactory.current();

        GameSession session = GameSession.instance();
    }
}
