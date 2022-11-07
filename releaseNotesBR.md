### !!! Beta Version !!!
This version contains many changes and does not identify itself as a full fusion mod, but more as a Beta version.
I'm quite confident it works well, but we are never too cautious. Please test it and don't hesitate to comment the new features.

### Very Last Changes:
  - Added individual opponent Ability selection (The same way the AI may be selected)
  - Added 'reworked'-option to set the ability to "raceKey.race"
  In the race editor, when selecting a "base race" (starting with * ), saving it under the proposed file name (ex RACE_HUMAN) this race will be identified as "reworked". Subsequently, selecting this option will always replace the race abilities with the reworked one.
  - Ctrl+Click on "selectable" will reset the abilities to default


### Last Changes:
  - Added some descriptions.
  - As suggested by u/Mr_Frosty_L : Added options to make race "player only". (Now you can keep weaker species for yourself!)
  - Added Personality and objectives.
  - Removed Base Data Race, it become useless as all abilities parameters can now be set!
  - The standard races can now be loaded on the right panel.

  - Added options to shuriken galaxies, some time better looking.
  - Added options to randomly load custom race file(MOD Option B - Custom Aliens: Yes, From Files)

## Here are the minor changes:

### Galaxy setup panel:
  - Fixed lonely Orion in Galaxy preview. (When opponents are maxed out)
  - Added Companions worlds in Galaxy preview.
  - Added original moo small galaxy size: named it Micro = 24 stars.
  - Added Dynamic Galaxy size, proportional to number of opponents and preferred number of stars per empire.
  - Updated Grid, Maze, and Fractal Galaxies to not crash with 4 stars...
  - Mixed Restart button with start button (ctrl+Start).

### Race setup panel:
  - Ship set: Fixed race keeping the last player preferred chip set.

### Race diplomacy panel:
  - Fixed overlaping Leader text in Race diplomacy panel. (Adaptive font)

### Other:
  - Updated max number of star with different formula for >4GB as java seems to loose efficiency! 

## Here are the major changes:

### Settings Load and save:

All the settings are now saved in the game files, and are also saved as individual files:
  - Last.options for the last GUI settings. Saved when you exit a setup panel.
  - Game.options for the last played game settings. Saved when a game is started or loaded.
  - User.options for your preffered settings. Saved Panel-by-panel on-demand.

You can load them in all setup panels. To avoid adding too many button, they are accessible thru the use of the "Shift" and "Control" keys. The buttons will display the active features.

On the Main menu, Control-key will give access to load all options from a file.
Global Mod options are also accessible thru Control-key. There you can configure how options are loaded at launch and after a game was played: By default it's set as "Vanilla" and you won't see any changes.
  - Vanilla Launch = Race, Galaxy, and Advanced options Panels are set to "Defaults" and mods panels are set to "Last".
  - Vanilla After game =  Race, Galaxy, and Advanced options Panels are set to "Game" and mods panels are set to "last".
Choosing "Last", "Game", or "User" will set all panels to "Last", "Game", or "User".

### Custom Races improvement
  - Custom races can now be saved and reloaded. You can give them a name and description which will be displayed on the races panel when "Custom Races" is enabled.
  - The Available race files are displayed on the right and can be loaded by clicking on them. The one on the top of the list is the last race selected.
  - Selecting a race on the left list no longer loads them, it selects it as the base race, defining their relationship. To load them use, Ctrl-Click.
  - On the race diplomatic panel, the show race abilities will also display the AI that controls them (Top right)


### Restart
Restart has been redone to manage new races and new options saving. While restarting, you can:

  - Swap the races of the swapped empires,
  - Keep the races of the swapped empires,
  - Use the GUI race for the player and swap the alien race.
  - Use the GUI race for the player and keep the alien race.
  - Change the player AI to the new selected one.
  - Change the aliens AI to the new selected ones.
  - Use the other GUI settings, without changing galaxy nor aliens.

... And of course every last Xilmi addition are there too!
