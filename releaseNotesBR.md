The easy additions first:

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

Text shaped Galagies:
  - Fixed text not being memorized.
  - Added a preview for popup selection tools.

And Some Settings configuration options:
  - There seems to be some confusion about the local to panel Loading and saving. Then:
    - Added option to chose if the Settings should be loaded Locally to the panel, or Globally.
    - Added option to chose if the Settings should be saved Locally to the panel, or Globally.
  - Added an option to let you choose which settings have to be loaded with agame

Some explanaitions about the settings:

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
    - Base Settings: In the game file and in files.options
    - Mod Settings: In the game file and in files.options
  - GUI Settings at launch:
    - Base Settings: Your choice; OldWay = Default.
    - Mod Settings: Your choice; OldWay = Last values.
  - GUI Settings after a game was played:
    - Base Settings: Your choice; OldWay = Game Settings.
    - Mod Settings: Your choice; OldWay = Last values.
  - Settings Used by the game:
    - Base Settings: Your choice; OldWay = Game Settings.
    - Mod Settings: Your choice; OldWay = GUI values. (Can be quickly Changed before loading with the "Ctrl"-options)

About the Settings Used by the game:
  - This will change the Original Settings memorized in the Game Files.
  - Not all settings are taken in account!
    - Trying to change the galaxy parameters is useless.
    - Trying to change the races is useless.
    - Etc...
  - There are to many possibilities to test them all!
  - Everything should be safe...
  - Please report any issue, so I could add some limits.

The main reasons for these change:
  - Uniformity in the settings usage.
  - Game files compatibility after adding new settings.
  - Improving the "Restart" fidelity.

I hope this clarify a bit the new saving options.
