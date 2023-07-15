Info for Mac Users: Applications/games/rotp/ is a good place to run it smoothly!

<i><u>New features since last announcement:</u></i>

<i>HotKey list</i> are available on game panels and setup panels.
- Available by right-clicking the Help button. 
- Available with SHIFT+F1.
- Colony Panel:
  - Added "B" Hotkey to change "Max Bases".
  - Added Arrows Right/Left for Next/Previous Colony.
    - Arrows Up/Down were already taken for scrolling. 
  - Clicking on the rail will move the selector.

<i>Improved panels</i>
- Multiple colony send transport panel has 2 new columns:
  - Stable = Max population to send to keep a stable population on Max ECO.
  - Fill = Max Population to send to have the planet filled on Max ECO.
  - Left click on slider = normal % selection.
  - Middle click on slider = "Stable"-value selection.
  - Right click on slider = "Fill"-value selection.
  - It's up to the player or the governor to set the ECO to max.
- Single colony send transport panel:
  - Middle click on slider = "Stable"-value selection.
  - Right click on slider = "Fill"-value selection.
- Planet Map now display incoming transports.
  - Pop(num) for population.
  - Troop(num) for invasion troops.
  - Green for Player transports.
  - Red for opponents transports.

<i>Options managements</i>
- Remnant.cfg options file:
  - Moved all parameter not computer related to the game files.
  - All the remaining parameters are available in the settings panel under the main menu, where:
    - The Guide is available.
    - Middle click will get the default value, etc...
    - Can be saved and retreived from User's preference...
      - But will never be copied from outside of this panel. (Loading parameters from Race or galaxy panel will not change Remnant.cfg)
- Simplified options' management.
  - Setup options follow Game options, but game options don't follow setup option. (Exept when starting a new game)
  - Setup options are loaded once at startup, according to your selection: Last Game, Last Session, or User's
    - Then will follow the game options and setup panel settings; last change win.
  - Loaded games will always load their own options.
  - Removed "global" load and save from sub-menus.
    - Only Race setup panel and Galaxy setup panel will allow Global reload.
    - Sub panels are available in-game, and it wouldn't be a good idea to reload all parameters there!
  - New buttons names: "Static" -> "pregame" and "Dynamic" -> "in-game",
    - Thanks to William482 for the name proposal.
  - "Menu Options" received new options from in-game Galaxy Map panel, and allow saving them in User's prefered options.
    - A guide has been writen for these options too.
- Custom Race Panel: "Save" button is now on top of "load" Button
- On launch: "Continue" will now load the last saved game.

<i>Restored some vanilla default values</i>
- Player initial race will be randomized (as it was on vanilla Rotp)
  - If the race was a custom race, id wont'be randomized.
  - If you always want to play with the same race: Standard races can be selected in custom race menu.
  - "R" in Race setup panel will randomize your race.
- Restored vanilla empire spacing for vanilla galaxies. (Modnar galaxies keep their own values.)
- Restored Moo1 Race names. (Xilmi)
- Restored Vanilla Human diplomat.
  - Added option to select the younger one. (default value)

<i>Governor</i>
- All Governor off will now also disable: 
  - Transport from ungoverned colony.
  - Auto-Spy.
  - Auto-Infiltrate.
- Governor won't send transports that will be shot down by attacking fleet. (dHannasch)

<i>Galaxy preview and Empire spreading</i>
- Added a 10 ly grid to the galaxy preview.
  - Added a guide to the galaxy preview.
- Added a relative"Empire Spreading", between 20% and 500%, to easily customise the distance between home worlds.
  - Absolute minimum distance = 3.8 ly.  - Removed "Max Spacing Multiplier" and "Maximize Spacing".
    - Too complex, and replaced by "Empire Spacing".
  - Scrolling on the galaxy preview will change it's value.
  - Middle Click on the galaxy preview will reset it's value to "Auto"
  - Added Galaxy size and distance between empires on the map. (When hovering, or grid on)

<i>Various improvements</i>
- Added Option to remove the fog hiding the nice race icons.
- Showing which ship is currently being built on each colony on the main-map right below the colony. (Xilmi)
- Techs which you could steal but are obsolete to your empire are now displayed in grey on the intelligence-tab in the races-screen. (Xilmi)
- When you select your own empire on the intelligence-tab in the races-screen the techs you are lacking now also are color-coded in grey, yellow or orange depending on whether they are obsolete, stealable or not stealable. (Xilmi)
- Added button in galaxy map to access the "In-game Options" Panel.
  - Changed option call to "O" to be more consistent with other panel call. (Ctrl isn't required anymore)
- Added option to skip the "after council" tech sharing notifications.
  - A button is temporary added in the tech trade notification screen.
- Remaining years to next council will be shown on the yellow alert sprite too.
- Separate missile size Modifiers for Bases and Ship weapons.
  - They are initialized with the former common modifier.
- Changed default AI to Roleplay.


<i><u>Last Fixes:</u></i>
- New Governor Panel don't crash any more on Apple OS.
- Fixed some missing help text.
- Fixed Buttons appearing before the panels.
- Fixed Governor double production with stargates.
- Fixed Manual.pdf Table 10.5: Heavy Fusion Beam & Heavy Phasor.
- "Graviton Beam" description: rendering -> rending 
- Two "Kekule" in Psilon star name list!
- Fixed Limited Bombard missing labels.
- Fixed AI-Guide not immediately updated when changing "Show all AIs".
  - Fixed crash on opponent AI selection with guide on and selecting "AI:Selectable".
- Fixed Governor Panel glitching (badly) on some computers.
- Fixed buttons not showing race display panel on first call.
- Captured planet governor is now set to the Governor default value.
- Fixed some "Remnant.cfg"-options not fully initialized.
- Fixed Transport panel missing reinitialization when reopening!
- All new colonies will be set with the current default governor option.
- Fixed Random Event Fertile to not target hostile planets, this because enrichSoil() has no effect on them.
- Fixed Screen selection: maxScreen() will return the max index instead of the number of screen!
  - Screen selection values are now looping.
- Fixed Fullscreen setting: The selected screen will be tested for fullscreen ability before forcing this setting.
  - Boderless will be used if fullscreen is not available.
- Fixed ungoverned colony sending transport without following "transport Max Turn". (dHannasch)
- Fixed inaccuracies of expected colony growth. (dHannasch)
- Fixed eco reallocation when all other slides are locked.
- Fixed occasional wrong player icon on Setup Galaxy panel.
- Fixed council percentage not being saved.
- Multiple Bombard: Get the focus without small mouse move.
