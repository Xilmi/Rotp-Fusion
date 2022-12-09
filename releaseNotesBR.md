### The easy additions first:

Added option to change AI in game, (on the Races/species'abilities-panel):
  - Click on the AI Button and select the new AI.
  - Available for the player and the aliens.
  - The changes are persistent.
  - Tested on short game only, I guess that u/pizza-knight who requested this feature and u/dweller_below who seconded it may want to try it more intensively...
    - It change the option value and reinitialize the AI variables, those that are already reinitialized when reloading a game; then it should be safe.

Added option to avoid Artifact planets being too close to Home Worlds.
  - This should help u/paablo in his ultimate symmetrical restart challenge!

Changed some Map Expand Panel display features:
  - Player's colonized planets won't display a green cross anymore.
  - Purple crosses are now visible on planetless stars wathever the year's configuration.
  - Year's  configuration is toggled by pressing "Y"

Text shaped Galaxies:
  - Fixed text not being memorized.
  - Added a preview for popup selection tools.

And Some Settings configuration options:
  - There seems to be some confusion about the local to panel Loading and saving. Then:
    - Added option to chose if the Settings should be loaded Locally to the panel, or Globally.
    - Added option to chose if the Settings should be saved Locally to the panel, or Globally.
  - Added an option to let you choose which settings have to be loaded with agame

Minor Fixes and improved general compatibility with older game files. (should not crash!)
  - Fixed "Change AI" setting the wrong AI.

### Some explanations about the new settings management:

First: How to use it:

To follow the unmoded / moo1 philosophy:
  - GUI at Startup:
    - Default Settings.
  - GUI after Game:
    - Game Settings.
  - Loading Game:
    - Game Settings.

To follow the previous moded philosophy: (Old way)
  - GUI at Startup:
    - Old Way.
  - GUI after Game:
    - Old Way.
  - Loading Game:
    - Old Way.

Recommended Setting:
  - GUI at Startup:
    - According to your preferences. (No impact on the game play)
  - GUI after Game:
    - Game Settings. (This way you leave the game, go to the GUI to changes some Mod Settings, and continue the game)
  - Loading Game:
    - Game Settings. (Whatever the GUI is, you always reload the same settings)

To recover the settings from the first saved game, as is:
  1.  Ctrl + "Go To global Mod settings"
  2.  Set: Loading Game = Last GUI Settings.
  3.  Escape
  4.  Load the first saved game. (Its settings are saved to Game.options)
  5.  Escape
  6.  Ctrl + "Load last game settings". (The current GUI is set to Game.options)
  7.  Load your game. (The current settings are applied to the game)
  8.  Save the Game. (The new settings are memorized)

To recover and modify the settings from the first saved game:
  1.  Ctrl + "Go To global Mod settings"
  2.  Set: Loading Game = Last GUI Settings. (for the second game loading)
  3.  Set: GUI after Game = Game Settings.
  4.  Escape
  4.  Load the first saved game. (Its settings are saved to Game.options)
  5.  Escape
  6.  New Game  (The current GUI is set to Game.options)
  7.  verify, adjust the settings, and go back to the main menu
  8.  Load your game. (The current settings are applied to the game)
  9.  Save the Game. (The new settings are memorized)

### How the settings are/were managed:

Originally, Base Settings only:
  - Save location:
    - In the game file.
  - GUI Settings at launch:
    - Default.
  - GUI Settings after a game was played:
    - Game Settings.
  - Settings Used by the game:
    - Game Settings.

After the modders added some settings: (Old Way)
  - Save locations:
    - Base Settings: In the game file.
    - Mod Settings: In Remnant.cfg.
  - GUI Settings at launch:
    - Base Settings: Default.
    - Mod Settings: Last values.
  - GUI Settings after a game was played:
    - Base Settings: Game Settings.
    - Mod Settings: Last values.
  - Settings Used by the game:
    - Base Settings: Game Settings.
    - Mod Settings: GUI values.
  
With the new configurations files:
  - Save locations:
    - Base Settings:
      - In the game file and in files.options
    - Mod Settings:
      - In the game file and in files.options
  - GUI Settings at launch:
    - Base Settings:
      - Your choice; Old Way = Default.
    - Mod Settings:
      - Your choice; Old Way = Last values.
  - GUI Settings after a game was played:
    - Base Settings:
      - Your choice; Old Way = Game Settings.
    - Mod Settings:
      - Your choice; Old Way = Last values.
  - Loaded games:
    - Base Settings: Your choice;
      - Old Way = Game Settings.
      - Game Settings = Game Settings.
      - Last GUI Settings = Current GUI - Base Settings.
      - User's Settings = User's Preferred Settings - Base Settings.      
    - Mod Settings: Your choice;
      - Old Way = Current GUI Mod Settings. (Can be quickly Changed before loading with the "Ctrl"-options)
      - Game Settings = Current GUI is set to this Game Mod Settings.
      - Last GUI Settings = Current GUI - Mod Settings.
      - User's Settings = User's Preferred Settings - Mod Settings.      

### More info about the Settings Used by the game:

- This will change the Original Settings memorized in the Game Files.
- Not all settings are taken in account!
  - Trying to change the galaxy parameters is useless.
  - Trying to change the races is useless.
  - Etc...
- Everything should be safe... But there are too many possibilities to test them all! Please report any issue, so I could forbid some settings to be changed.

The main reasons for these changes:
  - Uniformity in the Base/Mod settings usage.
  - Game files compatibility after adding new settings.
  - Improving the "Restart" fidelity.

I hope this clarify a bit the new settings management.
