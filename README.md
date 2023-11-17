# Remnants of the Precursors

Remnants of the Precursors is a Java-based modernization of the original Master of Orion game from 1993. <br/>

### Mixt of of Xilmi Fusion with Modnar new races
### With BrokenRegistry Profiles Manager. <br/>
... and some more features

Summary of the differences of Fusion-Mod to the base-game:
        [https://www.reddit.com/r/rotp/comments/x2ia8x/differences_between_fusionmod_and_vanillarotp/](https://www.reddit.com/r/rotp/comments/x2ia8x/differences_between_fusionmod_and_vanillarotp/) <br/>

Description of the different AI-options in Fusion-Mod:
        [https://www.reddit.com/r/rotp/comments/xhsjdr/some_more_details_about_the_different_aioptions/](https://www.reddit.com/r/rotp/comments/xhsjdr/some_more_details_about_the_different_aioptions/) <br/>

The decription of the additions/changes by Modnar can be found there: <br/>
	[https://github.com/modnar-hajile/rotp/releases](https://github.com/modnar-hajile/rotp/releases) <br/>

The description of the additions/changes by BrokenRegistry can be found there: <br/>
	[https://brokenregistry.github.io](https://brokenregistry.github.io) <br/>
	[Also available as pdf file (User Manual)](https://brokenregistry.github.io/pdf/Profiles.pdf) <br/>

### To build and run locally:

```
sudo apt install vorbis-tools
sudo apt install webp
mvn clean package -Dmaven.javadoc.skip=true
java -jar target/rotp-<timestamp>-mini.jar
```

# Other Links
Official website: https://www.remnantsoftheprecursors.com/<br/>
Community subreddit: https://www.reddit.com/r/rotp/<br/>
Download build: https://rayfowler.itch.io/remnants-of-the-precursors

## To-Do list
Bug to fix:

Very soon:
- Species Easy-Normal-Hard
  - https://www.reddit.com/r/rotp/comments/17qul3g/nudder_nutty_notion_simple_startup_settings/
- Monsters:
  - Option to change their travel time.
  - https://www.reddit.com/r/rotp/comments/16ov0ln/monstrous_ends_rotp_fusion_mod_tweaks/
  - https://www.reddit.com/r/rotp/comments/mc3ux3/different_take_on_space_monsters/
- Spy: option to not over spend with the spy budget!
- Add second statistic option: (on the side of lin / log): Display statistic as:
    - "% Tot": Precentage relative to known total (!= Galaxy Total)
    - "% Player": Percentage relative to player value.
    - "Value": The real value
    - ("Auto"?): May be! a dynamic, parameter dependent choice.
    - ("Individual choice"?): Probably not! individual choice for each parameter
- statistic: improve help
- Some Achievements options.
- change the color of a weapon button on the combat screen if the target is so well shielded that damaging it is impossible. Currently all these buttons are green with white/yellow highlights, perhaps swapping the green for a grey would do it? And maybe change the highlight from yellow to black as well.
  - Hit chance like in bombard panel!
- To Fix: Planet which was converted from hostile to minimal via atmospheric terraforming, the background matches the original planet type instead of the current minimal environment.

Later:
- Investigate: William482 request: Age of your data on each race into the bar graphs on the status page.
- Tech trading extended list.
- Option to be used to force war declaration upon "Smart Resolve".
  - Check if at war and add a button if not!
- Governor: Auto-invasion ?!
- Governor: Individual disable receive troops
  - The AI is bombing my world, killing 7 or so colonists per turn. Every turn, my healthy planets send colonists to 'help', which ALL get killed by the orbiting enemy fleet. I would like to be able to prevent this while still allowing transport between my other planets.
- Are there any plans to give the player an option whether to drop Death Spores or not, when they are present? (Or to only use Death Spores, and not bombs)?


## What's New

2023.11.17 (BR)
- Random Event Monsters:
  - Constant monster size.
  - little tuning and comments cleaning!

2023.11.16 (BR)
- Fixed crash due to concurrent monsters.

2023.11.15 (BR)
- Random Event Monsters: Random wandering path to monsters.
- Loots adjusted to research speed and monster level.

2023.11.14 (BR)
- Random Event Monsters: Fixed last backward compatibility issues.

2023.11.13 (BR)
- New Option for multiple concurrent monsters.
- Ready for monster extensions.
- New Option for easier monsters.

2023.11.12 (BR)
- Removed Monsters redundancies.
- Fixed backward compatibility by reseting the monster to 3 turns to the target. 

2023.11.10 (BR)
- Monsters and Guardian are visible.

2023.11.08 (BR)
- Random Event Monsters: new option to make them give loots.
  - Complete some current researh. And BC.
- F7/F8 will loop thru planet with incoming monsters too.
- Fixed Shields not showing on monsters.
- Guide on "Difficulty"-option has same content as "Help"

2023.11.07 (BR)
- Galaxy Map: Holding "Ctrl" gives prirority on star system selection over fleet selection.
- Tech triggered Monsters:
  - Don't follow of other start turn limitations.
  - Follow the repeatability settings.
  - Follow the max stars system settings.
- Trade Treaties give spy view into in ranges Empires systems.
  - Traders are always good at spotting planets ready to trade, and report basic information!
  - This to prevent friendly neighbors from constantly sending armed scouts and colony!

2023.11.06 (BR)
- New Event Monster options: Triggered by tech discovery.
  - Tech Monsters: All events + Tech triggered Monsters.
  - Only Monsters: Only Tech triggered Monsters.

2023.11.05 (BR)
- Fixed occasionally missing options in menu panels.
  - Fixed interfaces directly accessing static class parameters!
  - Direct accesses are replaced by static method calls, making initialization sequences more predictable.
  - Or static constants are moved to the interface.
  - Added static initialization call in Rotp.main to force a static initialization sequence.

2023.11.04 (BR)
- Improved classic Menu reactivity.
- Classic menu: sub-menu have a lighter border color.
- Dark mode: Fixed nebulae showing in turn-transition.

2023.11.02 (BR)
- Improved compact Menu reactivity.
- The options opening the sub-menus are more obvious.

2023.11.01 (BR)
- All trade tech notification have the skip Button
- Dark Galaxy: Fixed sabotage mission showing to much.

2023.10.31 (BR)
- Dark Galaxy:
  - Names are not clipped anymore
  - Fixed Hidden stars responding to clicking and hovering.
  - Stars around Out of range ships are shown correctly.
- Mass Transport Dialog: Set Default to Synchronized.

2023.10.30 (BR)
- Dark Galaxy:
  - Fixed ship display on borders.
  - Fixed ship scanner range (Scale was missing).
- Vanilla games can now be loaded by Rotp-Fusion.
- No shield shown in nebulae.

2023.10.29 (BR)
- Fixed PrecursorRelic, GauntletRelic, AncientDerelic that were deleting tech from the library.
- Updated Final Messages with New Species Names.
  -  Some customized species names will also be displayed.

2023.10.28 (BR)
- Custom Species Menu: Fixed intermitent 3/4 line hight Yellow higlight!
- Fixed Symmetric Galaxies freezing the setup panel.
  - Fixed adaptive growth!

2023.10.27 (BR)
- Dark Galaxy: Default values not linked to last value.
- Dark Galaxy: Removed old clugging test code.
- Custom Species Menu: Improved reactivity.

2023.10.26 (BR)
- Dark Galaxy: Found New cleaner way to hide all out of range elements.
  - Stars background and nebuleas are no more visible.
  - "Shrink" replaced by "Dark"
  - "Limited spying" replaced by "Names": Dark, but you remember the names of out of range Empires.

2023.10.25 (BR)
- Fixed some animations blocked by the temporisation of Result panel.
- Dark Galaxy: Only the final replay will show the full galaxy!
- Shields animations tuning:
  - Added a new 3D shield animations that should be compatible with mac OS.
  - Added the fire under the ship animation.
    - Was shown on the demo but not in combat.
  - Replaced the meaningless Animations delay by Animation Fps.
    - The real delay was dependent on the computing time.
- Fixed Comet Event not resetting its timer!

2023.10.24 (BR)
- Add access to former 2D shields animations, as 3D glitches with mac OS.
- Dark Galaxy Mode new options:
  - Spies also gives approximate location and info of contacted empires.
  - Spies are unable to give info on out of range Star systems.
  - If your empire shrink, out of range scouted planet are hidden.

2023.10.23 (BR)
- Extended ship building turns estimation Fix to full reserve consumption.
- Fixed guide appearance on compact option panel
- Fixed Pom.xml giving errors on eclipse IDE.
- New option: Dark Galaxy Mode.

2023.10.22 (BR)
- Fixed sound echo default values.
- Fixed ship building turns estimation when ship reserve is used.

2023.10.21 (BR)
- Made pom.xml and release.yml able to manage all configurations.
  - Repository name and owner are automatically identified.
  - Selected branch will be relased. (Selected under the button "Run workflow")
  - Added releaseAsDraft.yml to add the "Draft"-Tag to the release, and partially hide it.
  - Removed old pom and actions.

2023.10.20 (BR)
- New 3D Shield animations.
- New sound echo for when player is the target.
- Sound on Hit (not fire) when player is the target.
- Result panel will wait for the end of animation before poping up.

2023.10.08 (BR)
- Fixed and re-enabled governor animations.
- Added new options to allow/disallow Technologies research:
  - Atmospheric Terraforming,
  - Cloning,
  - Advanced Soil Enrichment.

2023.10.07 (BR)
- Tools for new 3d combat shield animations.

2023.10.06 (Peyre)
- Fixes typos in tech file.

2023.10.01 (Xilmi)
- Fixed an issue where the AI-ship-captain would shoot beam-weapons at the planet instead of ships even when the planet couldn't be damaged by them.

2023.09.29 (BR)
- Fixed some display options not always being saved.

2023.09.27 (BR)
- Fixed Manual about production formula

2023.09.20 (BR)
- Cleaned useless console log.
- Fixed disconnected "Deterministic Artifact"-option.

2023.09.18 (BR)
- Moved Options Setup Panels backImage control to BaseModPanel. 
- Custom Species Menu Buttons display optimization...
  - Still a lot of text optimization to-do

2023.09.17 (BR)
- Moved background paint to BaseModPanel.
- Compact Menu options Optimizations.
  - Buttons display back Image.
- Original Menu options Optimizations.
  - Buttons display back Image.

2023.09.16 (BR)
- Moved button background managment to BaseModPanel.
- SetupGalaxyUI optimization:
  - Buttons display back Image.
  - Opponents Mugs back Images:
  - 4K, 49 opponents, Global AI and abilities: <10 ms
  - 4K, 49 opponents, Custom AI and abilities: ~30 ms; Text display :-(
  - 4K, 5 opponents, Global AI and abilities: ~3 ms
  - 4K, 5 opponents, Custom AI and abilities: ~3 ms;

2023.09.15 (Xilmi)
- Fixed a recently introduced bug that could prevent the AI from attacking with more than one weapon-bank.

2023.09.14 (BR)
- Moved "No Fog on Icon to Main Options Settings"
  - Removed CheckBox from SetupRaceUI.
  - SetupRaceUI Toggle FOG KEY = "F"
  - SetupGalaxyUI Follow No Fog on Icon choice.
  - SetupGalaxyUI Toggle FOG KEY = "F"

2023.09.13 (BR)
- Character setting panel:
  - Improved Big image display: Refresh time < 5 ms !!!
  - Maximized rendering quality: Refresh time < 10 ms !!!
  - Can be set to display only original species.
    - Still available, but hidden.
    - Added option setting in the main setting panel.
- Cleaned the code from debug lines

2023.09.12 (BR)
- Character setting panel: 4K Refresh = 150 ms
  - Improved Ship drawing efficiency: Time improvement: 16 ms (~10%)
  - Improved Buttons drawing efficiency: Time improvement: 45 ms (~30%)

2023.09.11 (BR)
- Fixed shied effects showing when no shield.
- Fixed mysterious systemView out of bound error.
- Tuned Shield opacity.

2023.09.10 (Xilmi)
- The AI can now target different stacks with different missile-racks to split their damage between them and avoid overkill.
- The AI will now shoot missiles at planets as soon as possible, missiles at ships as soon as possible when it has more than 2 remaining volleys and otherwise first come close enough to guarantee missiles will hit even if the opponent tries to dodge them.
- The reduction in score for choosing a target that already has incoming missiles will only occur if the incoming missiles will destroy at least one unit in the stack. Otherwise the aforementioned splitting by missile-rack can lead to distributing damage to several stacks but killing nothing.
- The calculation of the flight-path is skipped if only targets in range are considered.
- Player-ships in auto-combat should no longer automatically retreat when they ran out of ammunition but there's still missiles flying towards enemy-targets.
- Instead of having a weird inconsistent handling for missile-ships with still flying missiles, the incoming missiles are now taken into consideration directly in the method that determines which side is expected to win. So AI decsion-making about when to retreat should now be a lot smarter in situations that involve active missile-stacks.

2023.09.09 (BR)
- Added shielding animation.
- Added Monster Shield color.

2023.09.08 (BR)
- Added shielding animation.

2023.09.04 (BR)
- Added new weapon sounds.

2023.09.03 (BR)
- Ready for distinct weapon sounds.

2023.09.02 (BR)
- added Guide info (No relationship bar, no alliances) to Fusion, Fun, and Character.

2023.09.01 (BR)
- Galaxy Map: "ALT -" = Full view of the galaxy.
- Replay History Panel:
  - New option to start the final replay with a Full view of the galaxy.
  - New option to start the empire replay with a Full view of the galaxy.
  - New option to set the turn pace.
    - Orignal (and default) pace: 100 ms per turn.
    - Can be set up to 10 s per turn.
    - In History panel press "1" to "0" to set 1 x 100 ms to 10 x 100 ms.
    - In History panel press "SHIFT+1" to "SHIFT+0" to set 1 s to 10 s.

2023.08.31 (BR)
- Minor cleaning.
- Release description.

2023.08.29 (BR)
- Made selection pop-up bigger to accommodate smaller screen resolutions.
- Changed "err("Unable to deploy.." to "log("Unable to deploy.."
- Set default value of "Track ships across turns" to "disabled".

2023.08.28 (BR)
- Auto-Run new option do disable console log output.
- Auto-Run can be paused (Esc or click)
- Auto-Run ask for AI.
- Auto-Run available from in game option panel.

2023.08.26 (BR)
- No galaxy map shading when auto-run.
- More Lock info on next turn button.
- Update done with Ironman default values (=None)
- Minor Auto-run display tuning.
- Safer Ramdom event changes.
- Removed useless files.

2023.08.24 (BR)
- added Warning for Auto-Run.
- added Ironman locked load.
- added Ironman Artifact same on reload.
- added Ironman subpanel.
- cleaned random events classes and methods.

2023.08.23 (BR)
- Fixed bug in "Fixed random events": Event selection is more repeatable.
  - A different event could have been chosen if a previous Monster event was lasting too long.

2023.08.22 (BR)
- New Random Event Sub Panel.
- All Random Events are now customizable.
- Random Events can be set to not favor the weak.
- New Random Events Pacing adjustment.
- Fixed bug in "Fixed random events": Pending events are now saved!
- Moved autoplay to pregame option; Was confusing! in game, it should be changed from "Empire panel".

2023.08.13 (BR)
- Rearranged the research slow names: don't affect previous settings
- Removed the inaccurate "Race"-word from menus and common dialogues.

2023.08.13 (BR)
- Added 2 new very slow research rate (renamed the former "Slowest")
- Commented "warpDisturbances" not ready for GNN background.

2023.08.10 (BR)
- Moved some debug log tools to Base.
- Cleaned TODO list
- No forbidden Tech plundering.
- No Tech stealing = no Tech plundering.

2023.08.09 (BR)
- Homeworlds can be rich and have artefacts
  - Added to Custom Race.
- Homeworlds artefact can be Orion Ruin like.
  - Added to Custom Race.
- Updated auto-flag to resources and ruins combo.
- Updated planet view to resources and ruins combo.

2023.08.08 (BR)
- Add new max security tax option.
  - Security steps: 1%, shift = 5%, ctrl = 20%
- Fixed ironman inverted logic.

2023.08.07 (BR)
- ironmanMode (Once set, options can't be changed)
- Optimization:
  - Distance to empire are no more recomputed after each colony gain/loss, but once after all these events.
- Fixed "Total Power"-status saturation.
- Added logarithmic scale for race status.

2023.08.06 (BR)
- Random Event: When set off in game, immediately stop events advancement.
  - They will continue if set on later.
- Fixed Events Mode
  - Updated random events to follow Fixed Events Rules
    - Same Events on Reload.
    - Same time Same event same target on diferent play.
    - If event still actif, put on waiting list.
    - If target is dead, Monsters are direct to their former home world.

2023.08.05 (BR)
- Auto Run log:
  - Fixed Time separation.
  - Fixed first contact double log.
  - Added turn duration.
  - Added num empire left.
  - Added num system colonized.
  - Removed 500ms double click security
  - Reset log files on first turn.
- ironmanMode preparation
- fixedEventsMode preparation
- refreshViews - recalcDistances optimization.

2023.08.04 (BR)
- Debug autoPlay to autoRun:
  - Renamed debug autoPlay to autoRun for less confusion.
  - Added first contact notification.
  - Added to Log: Num colonies and Num contacts.

2023.08.02 (BR)
- Fixed Readme year.
- Added Debug options sub-menu.
- Moved Memory monitoring to Debug options sub-menu.
- Added Memory monitoring options. (console and file)
- Sub-menu return id is now the parent GUI_ID.
- Added fractional turn to logfiles.
- Fixed .cfg sub-UI load/save.
- Added Debug AutoPlay: Don't stop at end of turns
  - Stop for council.
  - Don't stop if player lost! Run till one Empire only!

2023.08.01 (BR)
- Forbidden tech can not be stolen anymore.
- Cleaned pom files
- Removed obsolete tools
- New option to disable tech stealing.

2023.07.31 (BR)
- Show Memory options:
  - More descriptive names.
  - Added Max allocated.
  - Also written down to console when activated.
- Fixed and "normalized" random event delay.
- Normalized council requirement.
- Fixed "reduced range"-option.

2023.07.24 (BR)
- Fixed Old save game compatibility with "Track ships across turns" (Uninitialized parameter)

2023.07.24 (dHannasch)
- Fixed Retreating ship bug.

2023.07.21 (BR)
- Added category " ~~~ NEW OPTIONS (BETA) ~~~"

2023.07.21 (Xilmi)
- When invading, AI's of the Xilmi-Family will now keep at least one third of the source-planet's maximum population back at home in order to stay in the window of decent natural population-growth and not to ruin their economy.

2023.07.16 (BR)
- Fixed Mrrshan victory text: (Thanks to Keilah Martin)
  - Before: And when this galaxy has given to you all that has,
  - After: And when this galaxy has given to you all that it has,

2023.07.16 (dHannasch)
- "Track ships across turns"
  - Building block for next generation.
  - Improved display, no need to select the ship to see the probable destination.

2023.07.16 (Xilmi)
- For all AIs of the Xilmi-family: A technologically superior AI that currently has no enemies will no longer try to get further ahead in tech and instead focus on increasing fleet-size. This change was inspired by Keilah Martin's Let's play.

2023.07.15 (BR)
- Improved the "Reworked"-Abilities description.
- Improved the "Randomize AI" description.
- Fixed "Randomize AI"-abilities not working.

2023.07.14 (BR)
- Updated release notes.

2023.07.12 (BR)
- Remaining years to next council will be shown on the yellow alert sprite too.
- Fixed council percentage not being saved.
- Added option to skip after council tech sharing notifications.
- Removed "Governor full refresh on load"-Option.
  - Useless since the governor panel is closed when leaving the game panel.


2023.07.10 (BR)
- Added Option to remove the fog hiding the race icons.

2023.07.09 (BR)
- Added HotKey list to compact and classic options Panels.

2023.07.08 (BR)
- Fixed "Track ships across turns" crash over ship design duplicate.
- Reactivated "Track ships across turns".
- HotKey Uniformization.
- Added HotKey list to Race and Galaxy Setup Menu.

2023.07.05 (BR)
- Track ships across turns:
  - Added a test to better disable the background...
  - Temporary disabled the option.

2023.07.02 (BR)
- Made "Rudimentary Scanner" Optional.
- Labels allows now cross referencing with key between →←
  - Updated pom files to include Apache commons-lang3
  - Updated Xilmi pom file to last settings

2023.07.01 (dHannasch)
- Added "Rudimentary Scanner".

2023.06.29 (BR)
- Set "Empires Spreading" limits  to 10% ~ 1000%.
- Linearized empire spreading.
- Changed "Alt Animation" to "Alt Diplomat".
- Added Galaxy size on the preview.
- Multiple Bombard: Get the focus without small mouse move.

2023.06.28 (BR)
- Changed the Fixed value of "Minimum Empires Spacing" to a relative one "Empires Spreading" between 20% and 1000%. 100% being the vanilla value.
  - Absolute minimum distance = 3.8 ly.
  - Allowed for all galaxies.
- Restored Modnar empire spreading to Modnar galaxies.
- Improved the galaxies' Empire positioning time.
- Show info on offered Tech trade (Press "T")

2023.06.27 (Xilmi)
- A fleet that exclusively consists of designs that are flagged as automated for either scouting, colonizing or attacking will no longer be considered as idle in the context of cycling through idle fleets via space-bar or page-up/down.

2023.06.26 (BR)
- Colony Panel:
  - added "B" Hotkey to change "Max Bases".
  - added Arrows Right/Left for Next/Previous Colony.
  - added Move selector by clicking on the rail.
- Added Hotkey list to Ship Design Panel.
- Minor changes to Map panel Help.

2023.06.25 (dHannasch)
- Added functions to track ships.

2023.06.25 (BR)
- Added button in galaxy map to access the "In-game Options" Panel.
- Updated the galaxy map help.
- Fixed secondary galaxy shape option being ignored!
- Updated some "e.getModifiersEx() == xx" to more readable code.
- Changed option call to "O" to be more consistent with other panel call.
- Added Hotkey list in Galaxy Map Help (second panel).
  - Available alone by right-clicking the Help button.
  - Available alone with SHIFT+F1.
- Added Hotkey list in System Panel.
- Added Hotkey list in Race Panel.
- Added Hotkey list in Colony Panel.
- Added Hotkey list in Fleet Panel.
- Added Hotkey list in Tech Panel.

2023.06.24 (dHannasch)
- Extract functions from ShipFleet and Transport to Ship.
- Don't send transports that will be shot down.

2023.06.24 (BR)
- Added distance between empires on the map.
- Fixed wrong Integer to Integer equal test.
- Restored the missing race backstory.

2023.06.23 (Xilmi)
- Restored Moo1 Race names.

2023.06.23 (BR)
- Added a 10 ly grid to the galaxy preview.
- Added a guide to the galaxy preview.
- Added "Empire Spacing" to easily customise the distance between home world.
  - Default value = "Auto"
  - Scrolling on the galaxy preview will change it's value.
  - Middle Click on the galaxy preview will reset it's value to "Auto"
- Removed "Max Spacing Multiplier" and "Maximize Spacing".
  - Too complex, and replaced by "Empire Spacing".
- Made galaxy hovering borders more discreet.
- Restored Vanilla Human diplomat.
  - Added option to select the younger one (default value).

2023.06.22 (BR)
- Fixed defaultAI not correctly set everywhere.
- Fixed occasional wrong player icon on Setup Galaxy panel.
- Restored former Galaxy factory.
- Added missing options to "compact"-Options menu.
- Updated "Rand" to be more tuneable.
- Split randX and randY for all Galaxy.
- Removed Overrider methods identical to overriden! (For better readability).
- Restored vanilla parameters for vanilla galaxies.

2023.06.22 (Xilmi)
- The move-rate of missiles is now being taken into consideration when it comes to the AI determining whether they should be able to outrun missiles or not. So the decision of whether to kite a missile or retreat immediately shall be more accurate.

2023.06.21 (BR)
- Fixed  "Max Spacing Multiplier" not working.
  - Improved guide for "Max Spacing Multiplier"
- Fixed eco reallocation when all other slides are locked.
- Fixed Governor initialisation issue on new options & new game.
- Fixed Circle ranges default value.

2023.06.19 (BR)
- Fixed crash hovering uncolonized planets.

2023.06.19 (dHannasch)
- New alien's factories (to be refurbished) display.
- Colony growth methods restructuration.

2023.06.18 (dHannasch)
- Fixed ungoverned colony sending transport without following "transport Max Turn"
- Fixed inaccuracies of expected colony growth.

2023.06.18 (BR)
- Sending transport helper will work even if a transport is already set.

2023.06.17 (BR)
- Setup options will now follow the current game options.
  - Any changes in the game panels will be ported to the setup options.
  - But the current game options won't follow the changes made in the setup panels!
- Fixed bad option copy.

2023.06.16 (BR)
- Single colony sending transport panel: Dynamic change of transport destination.
- Fixed Random Event Fertile to not target hostile planets, because enrichSoil() has no effect on them.
- Updated Help with New transport features.
- Fixed Screen selection: maxScreen() will return the max index instead of the number of screen.
  - And renamed it to  maxScreenIndex() for better code lisibility.
  - Screen selection values are now looping.
- Fixed Fullscreen setting: The selected screen will be tested for fullscreen ability before forcing this setting.
  - Boderless will be used if fullscreen is not available.

2023.06.15 (BR)
- Planet Map now display incoming enemy transports.
- Planet Map now display incoming player invasion transports.
- Fixed Transport panel missing reinitialization when reopening!
- All new colonies will be set with the current default governor option.
- Single colony sending transport panel:
  - Middle click on slider = "Stable"-value selection.
  - Right click on slider = "Fill"-value selection.
- Fixed enemy transport count: only if their destination is known! (was shown)
- Planet Map now display incoming transports as:
  - Pop(num) for Population.
  - Troop(num) for invasion troops.
  - Green for Player transports.
  - Red for opponent transports.

2023.06.14 (BR)
- Planet Map now display incoming transports.
- Restored light-years display when grid is on.
- Fixed some "Remnant.cfg"-options not fully initialized.
- Changed default AI to Roleplay.
- Fixed too short label name when loading Some options.
- Swapped "Ctrl"-selection on Game, Last loading button.
  - Last Game" is on the top!

2023.06.13 (BR)
- Send transport panel has 2 new columns:
  - Stable = Max population to send to keep a stable population on Max ECO.
  - Fill = Max Population to send to have the planet filled on Max ECO.
  - Left click on slider = normal % selection.
  - Middle click on slider = "Stable"-value selection.
  - Right click on slider = "Fill"-value selection.
  - It's up to the player or the governor to set the ECO to max.
- Probably fixed shifted Range Area!

2023.06.12 (BR)
- Made Governor options available through Base Interface.
- Captured planet governor is now set to the Governor default value.

2023.06.11 (BR)
- Governor panel mixed fixes.
- Secured Governor a little more.
- Removed both folder paths from the file save.

2023.06.10 (BR)
- New option to allow full refresh on governor panel when loading a new game.
  - "Off" by default to avoid glitches.
- Missile size Modifiers split for Bases and Ship weapons.
- Fixed buttons not showing race display panel first call.
- Added another security in Governor panel.

2023.06.09 (BR)
- Converted some Remnant.cfg settings to options:
  - displayYear, showAlliancesGNN, showNextCouncil, showLimitedWarnings, techExchangeAutoRefuse, autoColonize_, autoBombard_, divertExcessToResearch, defaultMaxBases,
- Moved all other Remnant.cfg settings in the setting panel from the main menu.
- "Menu Options" received new options from in-game Galaxy Map panel, and allow saving them in User's prefered options.
  - A guide has been writen for these options too.
- Partial refresh of governor panel when loading a game.
  - A full refresh may generate display glicth on some computers.
  - The full refresh will be done when the focus is given to the panel.
  - Partial refresh = only update values
  - Full refresh = update panel display format (position, size, color) and update values.
- Allow Missile size modifier to go up to 200% (Reddit request)

2023.06.08 (BR)
- Fixed crash on opponent AI selection with guide on and selecting "AI:Selectable"
	- The guide is now availble for this selection
	- No more crach for a missing guide!

2023.06.07 (BR)
- New Main Settings UI with Guide and all options' tool features.

2023.06.06 (BR)
- Fixed AI-Guide not immediately updated when changing "Show all AIs"
- Fixed button missing description.
- Fix tentative: Governor panel won't update it's components if the windows is not focused.

2023.06.05 (Xilmi)
- Showing which ship is currently being built on each colony on the main-map right below the colony.
- Techs which you could steal but are obsolete to your empire are now displayed in grey on the intelligence-tab in the races-screen.
- When you select your own empire on the intelligence-tab in the races-screen the techs you are lacking now also are color-coded in grey, yellow or orange depending on whether they are obsolete, stealable or not stealable.

2023.06.05 (BR)
- Fixed Manual.pdf Table 10.5: Heavy Fusion Beam & Heavy Phasor.
- "Graviton Beam" description: rendering -> rending
- Fixed Two "Kekule" in Psilon star name list!
  - Added Fresnel and Lemaître (Published 2 years before Hubble... But in French in a little Belgium paper!)
- Galaxy Map Display Parameters are now memorized!
- Fixed Limited Bombard missing labels.


2023.06.04 (BR)
- On launch; Continue will now load the last saved game.

2023.06.03 (BR)
- Fixed Governor double production with stargates.
- Fixed minor Glitches.

2023.06.02 (BR)
- Lowered Governor useless refresh.
- Terraforming events will update autoflag.

2023.06.01 (BR)
- Custom Race Panel: "Save" button is now on top of "load" Button.

2023.05.31 (BR)
- Options Tools: Better names and uniformization.
- Fixed the planet view not updated on terraforming

2023.05.30 (BR)
- Remade Main settings panel, now Guide compatible.
  - Not yet activated.
- Fixed Buttons appearing before the panels.
- Completed the Main settings panel Guide.
- Removed useless parameters.

2023.05.29 (BR)
- Player initial race will be randomized (as it was on vanilla Rotp)
  - If the race was a custom race, id wont'be randomized.
  - If you always want to play with the same race: Standard races can be selected in custom race menu.
  - "R" in Race setup panel will randomize your race.

2023.05.28 (BR)
- Cleaned Governor from obsolete methods.
- All Governor off will now also disable:
  - Transport from ungoverned colony.
  - Auto-Spy.
  - Auto-Infiltrate.

2023.05.26 (BR)
- Greater integration of Governor in option tools.
- Renamed options interface for easier code reading.

2023.05.25 (BR)
- Cleaned Option tools initialisation.
  - Cleaned options call and initialisation.
  - Renamed some method more meaningfully.
- Removed some newly useless method parameters.
- Fixed Mac Crash on Governor new spinners Fix (Blocking useless constraint)

2023.05.24 (BR)
- Mac Crash on Governor new spinners Fix (Smooth Tentative)

2023.05.23 (BR)
- "param" Mod option tools auto update options.
- Cleaned Advanced options duplicate "Override".
- Fixed potential null pointer bug.

-2023.05.22 (BR)
- Removed "global" from sub-menus.
- Removed some buttons label redundancies.

2023.05.21 (BR)
- Converted more "static" methods from MOO1GameOptions.
- Validated the right option is given to the GUI panel.
- New buttons names: "pregame" and "in-game".

2023.05.20 (BR)
- Fixed Governor panel having wrong parameters when starting a new game.
- Setup options are now only loaded once at startup...
  - No more updated when going through the main menu.
  - The player decide when setup options needs to be reloaded.
- Loaded games will always load their own options. (no mix with current setup option)
  - The player decide if when to change some options.
- Removed "static" from MOO1GameOptions method that should be called through "options()".
- GUI are initialized with the option they have to works with.

2023.05.19 (BR)
- Moved games options from UserPreferences to IGameOptions to be available through options() call.
  - The dynamic options were wrongly located in UserPreferences, a guess had to be done about which option was calling! With a little change in the interface these options can now be called by standard option interface.
  - They are distributed on several files for a better lisibility. (No need to flood IGameOptions)
- Added "noOptions" in Rotp to validate the end of options initialisation... Early call to options are fatal!
- Fixed Missiles not reaching planet.
  - It was a bug that has probably always been there! The distance to unlock the fire button was calculated according to ship movement rules (Diagonal movement = 1). But missiles follow another rule! (Diagonal movement = sqrt(2)). When ships are on the side of the screen, their distance to the planet is 6, but for missiles, this distance is ~7.2. Since the merculite missile range is 6 (+0.7 attack range)... You were allowed to fire, but the missiles run out of fuel before reaching the target and are destroyed!
  - The fix only unlocks the button when the missiles are really in range.
- Commented the main options' change to allow a release of the fix.
  - The options are managed like before, but through the options() call.

2023.05.16 (BR)
- Governor panel:Tuned button colors.
- AI:Base and AI:Modnar are now optional.
- Centralized the AI options control.
- Moved Custom Spinner out of Governor to remove excessive "Static" param.
- Moved and grouped all new AI definitions to IGameOptions.

2023.05.14 (BR)
- Governor panel: local variable for display settings.

2023.05.13 (BR)
- Mac OS: Fixed Governor Buttons?
- New Spinner Rotp Style

2023.05.12 (BR)
- Fixed base setting auto colonize and auto bombard not being saved.
- Governor panel: Fixed ugly html text on Mac.
- If no flags, only show one black flag.
- Set AI:Hybrid as default AI.

2023.05.11 (BR)
- Fixed the Spying Labels.
- Fixed auto-transport.

2023.05.11 (BR)
- Improved Governor options continuity
- Unlocked Screen Size up to 200%
- Fixed the JSpinner Size.
- Fixed Reserve limits.
- New option to select the screen on which you will play.

2023.05.10 (BR)
- Fixed Bombing auto firing the turn following a "Fire all weapon"
- Governor new options Initialized withRemnant.cfg
- Different color for Gaia and Fertile text.
- Governor Position Memorized.

2023.05.09 (BR)
- Governor Panel update:
  - Options are now saved to the game file.
  - New compact panel available from setup Menu.
  - minor fixes.
  - If Fullscreen and OS Allows "Always on top": Governor panel will be "Always on top".

2023.05.08 (BR)
- Governor Panel update:
  - Option to choose between the old and new design.
  - Option to adjust the size.
  - Option to adjust the brightness.
  - Methods reorganization, cleaning and documenting.
  - Added Animation to Image.
  - Fixed possible crash when swicthing view while animated.
  - Fixed image size on original view.


2023.05.06 (BR)
- Governor Panel:
  - New Colors.
  - New race image.
  - New CheckBox and RadioButtons.
  - Replaced "Auto Apply" by check box.
    - The Toggle button color is to problematic!

2023.05.04 (BR)
- Cleanup and preparation for governor panel colorization.
- Governor new fonts and colors.

2023.05.02 (BR)
- Made a specific ParamFlagColor class parameter.
- More guide content.
- Restored vanilla Planet Quality options
  - Modnar options are still available.
  - Added Hell and Heaven quality.
- ECO bar:
  - Right Click on the text on the right = Enough to terraform, no growing.
  - Middle Click on the text on the right = Enough to Clean.
- New option: Terraform End; The terraformation will stop either:
  - When fully populated.
  - When fully terraformed. (Don't bother populating)
  - When fully cleaned. (Don't bother terraforming)

2023.05.01 (BR)
- Ships Scrapping refund:
  - Option to change the refund percentage.
  - Option to limit the ships location for the refund.
- Renamed "recentStart.rotp" to "!!! To Replay Last Turn !!!".
  - This should clarify its purpose and keep it on the top of the list.
- Governor Spare Xenophobes: The spying will be stopped only when framed.
- Fixed Governor not updating options when reloading a file.

2023.04.30 (BR)
- Custom Race Menu:
  - AI Box has Guide.
  - Moved the buttons below the description box and removed the "showTooltips"-option.
- PopUp selection list Fix: "Cancel" will work again!

2023.04.29 (BR)
- Custom Race Menu:
  - Some Guide addition.
  - Removed most redundancies.
- Guide: short list are now diplayed with all their options.

2023.04.28 (BR)
- Fixed Flag not showing correctly.

2023.04.27 (BR)
- Fixed Classic option menu hovering color not showing (Since Guide introduction)
- Optimized the hover loop for most setting menus.
- Updated Dynamic option guide content.
- Custom Race Menu Tuning.

2023.04.24 (BR)
- Fixed guide messing with option menu selection box display.
- Made a better use of Enum for button guide.
- Moved buttons param to their right place.
- Made BaseModPanel ready for custom Race Panel full integration.
- Optimized the mouse events management in compact option menus.
- Removed redundancies and useless code in compact option menus.
- Fixed little guide glitch in Classic option menus.

2023.04.22 (BR)
- Completed the Guide for Static Options.
- Guide switchable by the key "G".
- Minor changes to the Guide appearance.
- Custom Race Menu:
  - Activated the guide.
  - Fixed the priority conflict with the Guide.
  - Auto load the parameters to the Guide.

2023.04.21 (BR)
- Fixed Fractal Galaxy option2 guide... The only one being dependent on option1.
- Completed Modnar Grid Galaxy, "Some Clusters" are no more "All Clusters".
- Cleaned and clarified some abilities conflict.
- Almost completed the Guide for Static Options.

2023.04.20 (BR)
- Completed the Guides for the options in the Setup Galaxy Panel.

2023.04.19 (BR)
- Removed the starting beep on missing configuration files.
- Removed the empty abilities in the selection list.

2023.04.18 (BR)
- Fixed middle click not working on opponents abilities.
- Guide available on select Opponent.
- Guide Abailable on select specific abilities.
- Guide Abailable on select global abilities.


2023.04.17 (BR)
- Fixed the arrows not working...
- Added some guide descrition in galaxy panel.
- Fixed the boolean option guide display.
- Improved code readability. (Naming and location)
- More guide descriptions in label.txt.

2023.04.16 (BR)
- Updated guide and help to Html.

2023.04.14 (BR)
- Guide and Contextual help use Html in JEditorPane.

2023.04.12 (BR)
- Integrated Auto Contextual Help in Merged Options Panel.
- Formated the Guide display.
- Integrated Auto Contextual Help in Standard Options Panel.

2023.04.11 (BR)
- Finished Auto Contextual help tools for:
  - Galaxy Panel. ("GUIDE" button)
  - Race Panel. ("GUIDE" button)
  - List selection pop-up. ("?" button)

2023.04.10 (BR)
- Long range ship scans, long range planetary scans, and allies sharing scans will now also update "Auto-Flag".
- Removed the lag in the first list selection pop-up.
- List selection pop-up updates Galaxy preview.
- Contextual help is now removed when moving out of the option.
- Fixed Individual opponent race; ai and ability box overlap.

2023.04.09 (BR)
- Improved  pop-up list help.
- Added AI selection Help.

2023.04.08 (BR)
- Moved contextual help tools to HelpUI.
- Improved contextual help auto location.
- Added Help in pop-up list selection.
- pop-up list selection isn't static anymore.

2023.04.07 (BR)
- Added contextual help to the race and galaxy menu. "F1" will show the help for the option under the cursor. If no specific help is available, the global help will be shown.
- New Limited Annoying Warnings for Expansion, Genocide, and use of Bio weapons.
  - You can set a maximum number of empires warning you.
- Fixed Tooltip box not scaling well.

2023.04.05 (BR)
- Fixed Tech Never, Auto, Always labels.
- Fixed AI and Race selection Arrows bringing up the selection list.
- Added option to force new lines in multi-lines text, using "\n"
  - Used in help and descriptions.
- Preparation for contextual Help.

2023.04.04 (BR)
- Ctrl + Shift on scrolling = Ctrl Increment * Shift Increment / Base Increment.
- Removed useless initialization, Removed old Rand class which has now its file.
- New WYSIWYG option: The final galaxy will be the preview one. With controlled randomness.
  - Change the source number to change the galaxy.
  - 0 => random. (as before)
  - Applied WYSIWYG to Rectangular Galaxy.
  - Improved big source number randomness.
  - Applied WYSIWYG to Ellipse and spiral Galaxies.
  - Applied WYSIWYG to all other Galaxies.
  - the new random function extends now the Random class.


2023.04.03 (BR)
- Some code cleaning. (Ghost, imports, comments)
  - Removed some ghost of profiles.
  - Removed some old commented test.
  - Cleaned import list.
  - Made private some public methods.
  - Added new random function for galaxy factory.
  - Fixed little bug in pop-up lists.

2023.04.02 (BR)
- Right click on alert to center the map on the system.
- Totally removed Profiles Manager (Was no more maintained -> Danger)

2023.04.01 (BR)
- Cleaned the GUI pop-up call:
  - The parameters don't need to memorize the frame anymore.
- Renamed "Target Bombing" to "Limited Bombing" and adjusted the GUI texts.
  - in Option GUI and on the buttons: "Drop All" "Spare x pop" "No".
- Governor: Added an option to not infiltrate nor spy the xenophobic races.

2023.03.31 (BR)
- Custom Races:
  - Added Preferred Chip Set.
  - Added Preferred Chip Size.
  - Added pop up list managment.

2023.03.30 (BR)
- Added Auto-Flag option: Clear Flag Color.
- Fixed Rotp not starting in an empty folder!

2023.03.29 (BR)
- Partially Disabled Profiles Manager:
  - It was no more updated and may already be broken!
  - Almost everything is now available from the GUI.
- Up to 4 flags are now available!

2023.03.28 (BR)
- Auto-Flag:
  - Added option for colony ship Technologies.
  - Fixed multiple initialisation.
  - Fixed mose mouvement required to activate main menu on return.
  - Adjusted the panel width.
  - "Home" to set the selected planet flag following auto-flag rules.
  - "Alt"+"Home" to set all planets flag following auto-flag rules.

2023.03.27 (BR)
- Added auto-Flag on system when scouted.
- Fixed Red and white flags being swapped! (in 2023.03.25 changes)
- Fixed text on buttons no more always following the modifier keys.
- Fixed some Header errors.
- Auto-Flag: Fixed Artifact planets not setting the right flag.
- Auto-Flag: Resources and Environment normal or none = no flag by default.

2023.03.25 (BR)
- Fixed second Map Flag showing black instead of not showing.
- Made Flag list ready for more colors.

2023.03.24 (BR)
- "Precursor Relic" event:
  - Fixed SystemView.planetType() not always being up to date.
  - AIs and Player have now the same view of the new created planets.
  - Player is always notified of the planet creation.

2023.03.23 (BR)
  - Fixed crash due to unknown new options with some very old saves.

2023.03.21 (BR)
  - Fixed crash due to unknown new options with some old save.

2023.03.20 (BR)
  - Added Option for Dual Flag on planets.
  - Changed Mouse control on flags

2023.03.19 (BR)
  - Galaxy Setup Menu.
    - Fixed "Menu Options" sometime opening "Compact Static Options"
    - Added Help button.
    - "F1" = Help too.
  - Completed Galaxy Setup Menu Help.


2023.03.18 (BR)
  - Split Menu to Static and Dynamic Option.
    - Only Dynamic options are shown in Game.
  - Split standard looking menu, they were full.
  - Added Options to Space Pirates (Same as other Monster).
  - Added duplicate setting in Compact Dynamic Menu:
    - Setting Auto Colony.
    - Setting Auto Bombard.
  - Simplified the Standard menu declaration:
    - Add "null" in the list as column separator.
    - rows array is no more required.
  - Added MOD info on Galaxy map Help screen.
  - Completed Race Setup Menu Help.

2023.03.17 (Xilmi)
  - Integrate Terget Bombing to AI.

2023.03.17 (BR)
  - Option to Stop Bombing:
    - Added option to toggle this function.
    - This option will be available for AI.
  - Race Setup Menu.
    - Added Help button.
    - "F1" = Help too.

2023.03.15 (BR)
  - Add option to Stop Bombing when reaching a population target.

2023.03.15 (BR)
  - Add display of Turn and Year on the save panel.
  - Save Turn Start on it's Own File
  - Add option to auto refuse tech exchange

2023.03.14 (BR)
  - Fixed some AI not proposing tech deals.

2023.03.11 (BR)
  - Fixed System Scouted popup when AutoPlay.
  - Add option to avoid GNN Aliances info.

2023.03.09 (BR)
  - Fixed GUI malfunction after game played.
  - Added Colors to Compact Merged Advanced Options.

2023.03.07 (BR)
  - Added option to show Compact Merged Advanced Options only.
  - Space Monsters will be cancelled if you turn of the option.
  - Crystal and Amoeba Monster:
    - Added selectable delay after standard random events.
    - Added selectable Number of visited systems before the monster vanish.
    - Added option for reoccuring Monster.
  - Polish language fix.

2023.02.06 (Xilmi)
  - AI now identifies scouts in the ship-sending-method by exclusion rather than the isScout-flag.
This prevents an issue that after toggling on autoplay in an ongoing match ships designed by the player would falsely be identified as scouts and thus not be moved like other fleets.

2023.02.04 (Xilmi)
  - AI no longer counting already en-route transports towards what is needed to overwhelm a defending fleet. Doing so has lead to an issue where the AI would keep trickling in transports that would all be shot down.

2023.02.04 (BR)
  - Galaxy setup Panel: Fixed bug on Individual AI selection acting on the wrong opponent.

2023.01.02 (BR)
  - Added Map zooming Factor to merged options.

2023.01.01 (BR)
  - Split dynamic stars per empire from preferred Stars per empire.

2022.12.31 (BR)
  - Merged Advanced options and mod options in a single compact GUI.
  - Made the merged GUI available with "Shift-Click"-Advanced options.
  - Made the merged GUI available while playing a game, with "Ctrl"-o on galaxy map.

2022.12.27 (BR)
  - Fixed some new GUI interface issues.

2022.12.26 (BR)
  - Advanced option updated to new GUI interface.

2022.12.24 (BR)
  - BitmapGalaxy filename Box:
    - Increased width.
    - Uses scalable font to fit.
  - Fixed Fusion Font size being wrong sometime.
  - Generalized Base Options duplicate in Abstract Param.
  - Galaxy menu:
    - Number of opponents: added Full Control.
    - Selection Panel for AI selection

2022.12.23 (BR)
  - Option List Selection Panel: Fixed Bug with redundant values (Galaxy size).
  - BitmapGalaxy not selected: Fixed FileName box not totally hidden.
  - Option List Selection Panel Activation with "Ctrl-Click"
    - On galaxy shape options selection.

2022.12.22 (BR)
  - Option List Selection Panel Activation with "Ctrl-Click"
    - On mod menu options list.
    - On Ship set selection.
    - On galaxy shape selection.
    - On Galaxy size selection.
    - On Difficulty selection.

2022.12.21 (BR)
  - BitmapGalaxy: The last valid image folder is now saved and will be used if the current choice is not valid.
  - Show User AI: Fixed the missng "AI: Off".

2022.12.21 (Xilmi)
  - When using the "Use AI to handle transports"-governor-option the "Don't send from rich/artifact planets"-option will now be considered.
  - Fixed an issue where colonies would reduce eco-spending to clean after having manually set it to something higher with governor disabled.
  - Right-clicking the text right of the eco-slider with governor disabled will now only put the maximum amount required to it instead of building reserve.

2022.12.20 (BR)
  - GalaxyShape: "usingRegion"
    - Fixed overflow with some odd "textGalaxy" and some "BitmapGalaxy".
    - The regions' array are now dynamic.
    - Reduced the security reserve by a factor 10.
  - Fixed galaxyEdgeBuffer not set for Dynamic sizes.

2022.12.19 (BR)
  - Galaxy Bitmap: Fix and improvements.
  - Fixed Orion misplacement on big map (was always Top left!).
  - Galaxy Preview:
    - Added new "Mod Global Option" for Orion and Empires in color with bigger size.

2022.12.16 (BR)
  - Added Galaxy Bitmap Advanced.

2022.12.15 (BR)
  - Added Galaxy Bitmap grey level.
  - Added Galaxy Bitmap inverted grey level.
  - Added Galaxy Bitmap color.

2022.12.09 (BR)
  - Fixed "Change AI" setting the wrong Player AI.
  - Loading File with new options: Fixed "Autoplay"-option.
  - Fixed Player "Use New Race" Random selection being inverted

2022.12.08 (BR)
  - Fixed "Change AI" setting the wrong Opponent AI.
  - Replaced AI list duplicate by a dynamic one.

2022.12.07 (BR)
  - Fixed Restart compatibility issues.
  - Improved general compatibility with old game files.

2022.12.05 (BR)
  - Fixed loaded Game not saving Games.options correctly. (mixed with Gui parameters)

2022.12.04 (BR)
  - Fixed Local User settings not saving correctly.
  - Forced the creation of newGameSettings before loading a game.
  - Fixed some "initialOptions"-issues.

2022.12.03 (BR)
  - Text Shaped Galaxy: Fixed text not being saved.
  - Text Shaped Galaxy: Added Preview for popup selection window
  - Loading Game: Added Option Selection

2022.12.01 (BR)
  - Map Expand Panel: Purple crosses are visible on planetless stars wathever the year's configuration.
  - Map Expand Panel: Player's colonized planets won't have a green cross anymore.

2022.11.30 (BR)
  - Added option to avoid Artifact planets being too close to Home Worlds.

2022.11.29 (BR)
  - Added option to change AI in game, On the "species'abilities"-panel.

2022.11.28 (BR)
  - Fixed Fusion Font not loading outside the IDE.
  - Improved the Text Shaped Galaxy preview generation speed.
  - Removed "Beta" tag.

2022.11.27 (BR)
  - Fixed international font problems.
  - Added option to choose percentage needed to win council vote.
  - Added option to Display years left until next council.

2022.11.22 (BR)
  - Text shaped Galaxy: Added selection from file Galaxy.txt

2022.11.15 (BR)
  - Fixed possible crash while changing the number of companion world.
  - Added base galaxy size for Dynamic Galaxy with a general formula, valid for all galaxy size.
  - Text shaped Galaxy: Added option for "Fusion" font.

2022.11.14 (BR)
  - Linked the cost of Research revenue and cost multiplier.
  - Adjusted the combined cost formula of Research cost multiplier.
  - Adjusted the combined cost formula of Research discovery probability.

2022.11.13 (BR)
  - Replaced tech discovery probability product by sum.
  - Linked global and individual discovery probability.
  - Added cost management to tech discovery probability.

2022.11.12 (BR)
  - Fixed custom race edit / show panel display Glitch.
  - Restored the responsivity of race selection Panel.
  - Restored the shipset display.
  - Added Individual tech discovery probability.

2022.11.11 (BR)
  - Added Prefix and suffix option to custom race name, leader names and worlds name.
  - Selection popup available for global ability selection.
  - Fixed Missing Worlds suffix

2022.11.10 (BR)
  - Fixed game options not always being saved.
  - Fixed overlapping text in race ability selection.
  - Set Minimum Tech Discovery to 0.

2022.11.09 (BR)
  - Fixed some responsivity issues.
  - Fixed "Reworked" not being reloaded correctly.

2022.11.08 (BR)
  - Made Race customization panel descriptions more responsive.
  - Made Race customization panel Buttons more responsive to key modifiers.
  - Made Galaxy startup Panel Buttons more responsive to key modifiers.
  - Made Races startup Panel Buttons more responsive to key modifiers.
  - Made Mod startup Panel Buttons more responsive to key modifiers.

2022.11.07 (BR)
  - Added ability files to the opponents ability list.
  - Added PopUp to select opponent ability.

2022.11.06 (BR)
  - Fixed Missing initialization in "vanilla"-options.
  - Ctrl+Click on "selectable" will reset the abilities to default.
  - Fixed offset in opponent Abilities.
  - Fixed opponent list being deleted.

2022.11.05 (BR)
  - Added individual opponent Ability selection.
  - Added "reworked" to set the ability to "raceKey.race"

2022.11.03 (BR)
  - Fixed custom Race file list refresh.
  - Reduced selectable AI font size to fit the box.
  - Added custom race selection to opponent selection panel. (Not yet functionnal)
  - Fixed Crashing bug due to null pointer! (in Empire.Java)

2022.11.02 (BR)
  - Added options to make race player only.
  - Added Personality and objectives.
  - Removed Base Data Race.
  - Added some descriptions.
  - Moved Standard races on the right panel.

2022.10.30 (BR)
  - Added options to randomly load custom race file(MOD Option B - Custom Aliens: Yes, From Files)

2022.10.26 (BR)
  - Added options to shuriken galaxies, some time better looking.

2022.10.24 (BR)
  - Fixed lonely Orion in Galaxy preview. (When opponents are maxed out)
  - Added Companions worlds in Galaxy preview.
  - Added original moo small galaxy size: named it Micro = 24 stars.
  - Added Dynamic Galaxy size, proportional to number of opponents and preferred number of stars per empire.
  - Mixed Restart button with start button (ctrl+Start).
  - Ship set: Fixed race keeping the last player preferred chip set.
  - Fixed overlaping Leader text in Race diplomacy panel. (Adaptive font)
  - Updated max number of star with different formula for >4GB as java seems to loose efficiency!
  - All the settings are now saved in the game files, and are also saved as individual files:
    - Last.options for the last GUI settings. Saved when you exit a setup panel.
    - Game.options for the last played game settings. Saved when a game is started or loaded.
    - User.options for your preffered settings. Saved Panel-by-panel on-demand.
  - On the Main menu, Control-key will give access to load all options from a file.
  - Global Mod options are also accessible thru Control-key. There you can configure how options are loaded at launch and after a game was played: By default it's set as "Vanilla" and you won't see any changes.
    - Vanilla Launch = Race, Galaxy, and Advanced options Panels are set to "Defaults" and mods panels are set to "Last".
    - Vanilla After game =  Race, Galaxy, and Advanced options Panels are set to "Game" and mods panels are set to "last".
      - Choosing "Last", "Game", or "User" will set all panels to "Last", "Game", or "User".
  - Custom races can now be saved and reloaded. You can give them a name and description which will be displayed on the races panel when "Custom Races" is enabled.
  - The Available race files are displayed on the right and can be loaded by clicking on them. The one on the top of the list is the last race selected.
  - Selecting a race on the left list no longer loads them, it selects it as the base race, defining their relationship. To load them use, Ctrl-Click.
  - On the race diplomatic panel, the show race abilities will also display the AI that controls them (Top right)
  - Restart has been redone to manage new races and new options saving. While restarting, you can:
    - Swap the races of the swapped empires,
    - Keep the races of the swapped empires,
    - Use the GUI race for the player and swap the alien race.
    - Use the GUI race for the player and keep the alien race.
    - Change the player AI to the new selected one.
    - Change the aliens AI to the new selected ones.
    - Use the other GUI settings, without changing galaxy nor aliens.

2022.10.19 (BR)
  - Fixed Randomized alien races not reaching target values.

2022.10.14 (Xilmi)
  - Fixed an issue where the report of how many transports were destroyed it would show the number of transports that the defending fleet potentially could have destroyed instead of the actual number.
  - Fixed an issue whith Autoplay being enacted at the start of the game before the views were updated which led to not taking distances into consideration.
  - When sending a colony-ship to unexplored systems the AI will now double the value of yellow stars and multiply the value of orange and red stars by 1.5.
  - AI will no longer send scouts to systems it has already send a colony-ship to.
  - AI will be more efficient with its scouts by giving a higher priority to targets closer to existing scouts.
  - When loading a save-game there's now a validity check for fleets that eliminates fleets with negative amounts of ships. This is a work-around for an issue reported by /u/Thor1noak. Unfortunately the cause of of the underlying bug could not be determined from the save-game.

2022.10.08 (Xilmi)
  - Fixed a bug that caused scatter-pack-missiles doing way less damage than expected during orbital-bombardment. Preview was correct already but actual execution wasn't!
  - Fixed a crash that could occur when autoresolving combats that included missile-bases.
  - During combat AI stacks will now avoid triggering automatic reactive fire of enemy stacks with higher initiative when they can outrange them.
  - During combat target selection of AI will now double the preference of attacking an enemy stack, when it should be able to kill at least one ship in the stack.

2022.10.06 (Xilmi)
  - Fixed an issue that prevented you from switching off ship-building when the governor was enabled on planets that were captured while building ships. Thanks again to /u/Mjoelnir77 for reporting.

2022.10.04 (Xilmi)
  - Halfed the numner of systems for the biggest possible map-size. I wanted to do more trial&error to see what the biggest is that I could go but I couldn't be bothered to sit another several minutes through galaxy-creation-time.
  - Fixed polish translation of Deuterium Fuel Cells to be the same as the translation of Iridium Fuel Cells.
  - Colonies now always produce at least 0.1 income in order to prevent weird effects resulting from zero or negative income. This fixes an exploit reported by /u/Mjoelnir77

2022.10.03 (Xilmi)
  - Reduced the amount of animation-frames for shooting weapons and displaying weapon-effects when using auto-combat to speed it up significantly.
  - Reduced the amount of animation-frames for bio-weapons to the same number as for regular bombs.
  - The expansion-tab on the systems-screen now also provides information on fertility, mineral-richness and artifacts.
  - The exploit-tab on the systems-screen now also provides information on planet-type and size.
  - In autoplay-mode the personality and objective of your AI's leader is now visible in the diplomacy-tab of the races-screen.
  - Fixed that "Hybrid" was missing from the autoplay-options.
  - The order of "Fun" and "Character" in the menu have been swapped to indicate that "Character" is likely the easier of both options.
  - "Character" is the new default-AI-mode for now as I want to gather more feedback on it's changes and I think this increases the chance of people using that mode.
  - Fusion-AI will now ask others if they would start a joint war on their preferred target even if they are not already at war with their preferred target.
  - Fusion-AI will now agree to starting a joint war, if someone asks them to attack their preferred target together even if they wouldn't have done so on their own.
  - The AI-Mode "Character" has been reworked tremendously. The personality and objective of each leader should now have significant impact on how they behave.
    This impacts tech-slider-allocation-preferences, the choice who, if anyone is preferred as war target and how likely a war-declaration on their target is.
    Each objective type prefers a certain tech-category in the way, that they act as if all techs in that category cost only half of the regular price.
    Technologist => Computers, Industrialist => Construction, Diplomat => Force-Fields, Ecologist => Planetology, Expansionist => Propulsion, Militarist => Weapons
    Choice of favorite war-target now works as follows:
    Militarist => Their military-industrial-complex needs to justify itself and infuences the decisions of their leader. And the best justification is to have a strong enemy. They will always pick the opponent with the strongest military.
    Industrialist => Protecting their existing industry takes precedent over anything else. They will pick whoever is the owner of the colony that is closest to their core-worlds.
    Ecologist => Their main-interest is to keep the ecology of the galaxy as balanced as possible. They want to regulate species that have grown out of control and will always target whoever has the highest population. If that is themselves, they see no reason for war and will not attack anyone as war is not particularly great for the eco-systems of the planets either.
    Technologist => They want the techs. All of them. If you have techs, that they don't have you move onto their target-list. The higher the levels of these techs the higher you move up on their "to take techs from by force"-list. If they have everything everyone else has, they are completely content and won't attack anyone.
    Expansionist => They want more planets. Regardless of how many they already have. They want them quick and they don't want to make this goal hard for themselves. They attack in a similar pattern as the Industrialist. Except they look at what planet is closest to their core-fleet and they also take into account how much resistance they expect from their target.
    Diplomat => They are the most cunning of the bunch. They use a complex algorithm that takes a lot of things into account. In particular what the relationship of other races with each other looks like. They are very likely to pick on the ones who are already struggling.
    Personality impacts whether and when wars are declared and whether and when peace is considered. They impact the behavior as follows:
    Important note: None of the following is true, when there's only 2 factions left. In this case, all of them will always immediately attack.
    Ruthless => They will definitely attack their preferred target and chase it down until there's nothing left to attack. They don't care about their own losses and will only ever agree to peace when they are very close to extinction.
    Aggressive => They will attack even if they are at somewhat of a disadvantage. But they are not suicidal. They will make peace if things are looking badly or when they are eying another target.
    Xenophobic => They are cautious and opportunistic. They only attack when they see themselves at an advantage. They will make peace if things are looking badly or when they are eying another target.
    Erratic => On average they are just like the Xenophobes... But sometimes they are as aggressive as an aggressive one... other times they are quite timid and skittish. But the average is of little help, when they still declare war eventually. They will make peace if things are looking badly or when they are eying another target.
    Pacifist => They are actually real pacifists and don't want to have anyhting to do with war. They never start wars on their own. They will want to make peace as soon as the situation has changes. Regardless in what direction. Even if they are winning. Except when they still have transports en-route, that is!
    Honorable => They won't start a war unless someone asks them too. If they are asked to attack the target they are eyeing, they will declare war. They won't do surprise-attacks and always warn their target in advance. However, once they have commited to a war, they will not back down from it. There is no honour in backing down!
    Note, that the competitive strength of these personalities varies dramatically as I deliberately chose not to care about balancing them at all. All that was important to me for this AI-type is to make them play their roles in an immersive way!
    So while a Xenophobic Diplomat will naturally make smart decisions suitable for winning, whereas a Honorable Militarist might let themselves be dragged into a war with an opponent that will eventually just kill them.

2022.09.30 (Xilmi)
  - Shooting beam-weapons should now look much more like how it looked in Master of Orion.

2022.09.28 (Xilmi)
  - Fixed a bug reported by /u/Thor1Noak where the robotics-control-bonus of the Meklonar was not being taken into consideration for calculating how many alien factories could be converted within one turn and thus lead to neither building nor converting any factories at all.

2022.09.27 (Xilmi)
  - AI follows a more adaptive research-strategy in the expansion phase.
  - Fixed an issue where in certain cases the AI accidentally knew whether a system was colonized or not when it shouldn't be able to know it.
  - Fixed an issue where the governor wouldn't realize that planets became colonizable after the event that turns uncolonizable planets into colonizable ones.
  - Reworked diplomatic decision-making of Fusion-AI. It should be a lot more aggressive and also try to stop empires that are getting too strong.

2022.09.21 (Xilmi)
  - Fixed a crash reportet by /u/Thor1Noak that could occur when the AI wanted to check the range of their current colonizer during diplomatic negotiations, while only having a huge colonizer-design but now no longer wanting a huge design for colonizers but hasn't had the chance to design a new non-huge colony-ship yet because diplomacy is processed before ship-designing!
  - Changed e-mail-address for the bug-report-prompt to my own.

2022.09.20 (Xilmi)
  - Fixed a rare issue that could cause the governor to forget it was supposed to be building ships.
  - Fixed that the bombardment-preview would display too high of a kill-count when the attack-level exceeded 10.
  - AI no longer tries to reinvade rebelling colonies when they are under siege by their enemies.
  - AI now uses actual travel-turns for the scoring of where to send their ships. Previously this was extrapolated by distance:speed with a modifier for nebulae. This change likely will increase AI-processing-time on big maps but should help the AI to make better decisions when nebulae are involved.

2022.09.18 (Xilmi)
  - Removed AI: Unfair and added two new AI's instead: AI: Fun and AI: Character. The diplomatic behaviour of AI: Fusion has also been reworked.
  - AI: Fusion: While the race still has some impact on aggression-level, it is now significantly less aggressive for all races and more about playing safe.
  - AI: Character is roughly similar to how Fusion-AI used to work a while back and takes leader-personality into account for how aggressive it should be. Pick this option for getting the experience clostest to how it played before this update.
  - AI: Fun is meant to provide a fun challenge with comparable difficulty across different playthroughs rather than having extreme variance from one game to another. I don't want to spoil yet how exactly it works as I want some unbiased feedback. But I promise that it neither cheats nor is aware of the player to do so. This is now also the default for new games.
  - Random-options for AI have been changed accordingly. Instead of "Random" and "Random+" there now are:
  - AI: Random - Chooses a random AI out of all other AIs.
  - AI: R.Basic - Randomly chooses one of Base, Modnar or Rookie.
  - AI: R.Xilmi - Randomly chooses one of Roleplay, Hybrid, Fun, Character or Fusion.
  - AI: R.no relat - Randomly chooses on of the AIs that don't use relationship for decision-making, which are: Fun, Character and Fusion.
  - The impact of tech-level to both the power-graph and AI's consideration of other's power is now linked to how miniaturization works. This means getting new techs in early-game will have a much lower impact on the power-graph.
  - Fixed an issue that didn't take how far the mouse's scrolling wheel was spun into consideration of how far to scroll in load-game-screen. This issue exists at many places throughout the game that all need fixing. But fixing it for loading-screen was most important due to how much of a nuisance it was to scroll down to a turn-1 save if you have a lot of auto-saves.
  - Fixed that altering build-limit would only call governor when altered from colonies-screen.
  - Fixed governor overspending in ecology under certain circumstances.
  - AI now takes initiative into account when considering whether a speed-disadvantge will lead to dealing less damage.
  - Fixed an issue that prevented AI from attacking the Orion-guardian.

2022.09.13 (Xilmi)
  - Changed that after finishing a predefined amount of ships or a stargate the allocations weren't changed back automatically from ships to something else.
  - When you change the build-limit the governor is called and will immediately start building the ordered ships given it doesn't need all of it's allocation to build something else.
  - Having a build-limit set now will also tell the governor that it's colony is supposed to be building ships. In this case it doesn't need a tick in ship-allocation to remember that.
  - Governor will now spend the exact amount of money required into building the ships requested via build-limit instead of keeping the bar at a maximum.
  - Fixed an issue with tech-tree-generation when "Always" was chosen in "MOD Options B" for certain techs that could cause the available techs to be empty for certain tiers and thus made the category unresearchable.
  - When the opponent has a warp-dissipator, AI will not try to retreat their last ship at the last moment anymore and instead at the first moment it thinks it'll lose.
  - AI now will want to upgrade trade-treaties when the trade-volume is more than 1.5 than that of the last treaty or the treaty is maxed out instead of requiring both of these conditions to be met.

2022.09.12 (Xilmi)
  - Fixed issue introduced in last version that prevented the governor from building defensive structures.

2022.09.11 (Xilmi)
  - The player is now informed when a stargate is finished even if it was built using governor.
  - When the governor is active and the player is informed about being able to change allocations the part that readjused allocations on it's own differently from how the governor would have behaved is no longer executed.
  - The "Equalize Allocations"-button in the Tech-screen now uses a more equal algorithm for it's equalization.
  - Instead of using a cost-based fleet-power analysis the AI now uses one based on firepower and defensive stats. This should lead to more accurate assumptions about whether a fleet will be able to deal with their enemies. This will help the Fiershan and Altairi in particular as their bonuses are now correctly taken into consideration during this analyis.
  - Ship-design-screen and Military-tab in the race-screen now include racial-bonuses for attack and defense for the displayed ship-design-stats.
  - Fixed many bugs in AI-fleet behavior. Most of those caused by the revamp of using a fleet-power-based analysis but certainly also some that existed before. Keep an eye open for remaining unintentional behaviors, though!
  - Fixed issue causing AI not attacking space-monsters like the Guardian in space-combat.
  - Fixed issue where finishing a research-category would not automatically redistribute the allocation-points that were freed up by this.
  - Fixed issue that prevent you from using the governor to build ships in the same turn a stargate got finished.
  - Fixed a nested call of the governor that could lead to weird, unintentional slider-allocations.
  - Fixed an issue in the governor that could lead to not putting remaining allocation into either ships or research.
  - Fixed an issue in the governor that caused it to put an additional click into ship-building instead of only when it was meant to memorize that it was building ships before. This sometimes could lead to pollution.
  - Fixed an issue in the governor that could lead it to put more allocation into ecology, industry or ship than was needed to finish the current project.
  - Fixed an issue in the governor that could lead to allocating more production than it actually had available when building a stargate.
  - Fixed that you could change the allocation of the eco-slider when it was locked by using the max-slider-button.
  - Fixed that after maxing the eco-slider the function that makes sure that it's clean was called as that could lead to the slider not actually being maxed.
  - Fixed that when using the max-slider-buttons from inside of the Colonies-screen the sliders wouldn't properly update.

2022.09.09 (BR)
  - Moved restart button to the left side, to be visible in windowed mode too.

2022.09.09 (Xilmi)
  - The bombardment-prompt now includes an estimate of how many factories will be destroyed.
  - AI no longer assumes that producing population to man all already existing factories is always worth it. Instead the return of investment of workers working factories is compared to the savings of letting population grow naturally.
  - AI cost-benefit-analysis for invasions now takes lost-productivity into account on the cost-side and how many factories will likely be destroyed until the invasion arrives on the benefit-side.

2022.09.08 (BR)
  - Fixed restart issue for late game when an empire as lost his home world.

2022.09.06 (BR)
  - Added tools to load and save options.
  - Added some load/save buttons.
  - Regrouped "selectedxxx" and added new settings

2022.09.05 (BR)
  - Profile Manager: Fixed Number of opponents being limited by previous smaller galaxies.
  - Renamed Mod View menu To Mod Global
  - Added menuStartup and MenuLoadGame to Mod Global Menu
  - Cleaned Unused Class and methods

2022.09.05 (Xilmi)
  - Fixed width-issue in races-screen and replaced short text describing racial abilities with a more prominent hint to click the text to get full information about a species' abilities.
  - Autodesign as well as AI now try to fill up the design better when there's still some space left.
  - Some refactoring considering the Roleplay-AI. Roleplay-AI now uses the same AI-General as Fusion-AI. This shall bring some lacking aspects of it's play to Fusion-AI's level. All important distinctions are in it's AI-Diplomat.

2022.09.04 (Xilmi)
  - Introduced new AI-type: Hybrid. Hybrid-AI is based on Fusion-AI but uses the same Diplomacy- and Espionage-logic as Rookie-AI.
  - Planets initially spawned as fertile were lacking the associated size-increase. This is fixed now.
  - Fixed that confirming a design after using Auto would change the icon of the design.
  - Fixed that Incidents meant exclusively for the Roleplay-AI were also taken into consideration by other AI-types.
  - Fusion-AI will now retreat from enemy colonies it can't do damage to even if their fleet would survive aswell. This is to prevent useless losses if one stack has auto-repair but the colony would target another other.

2022.09.03 (BR)
  - Restart: Fixed nearby system order; Not important to the player, but the AI was fooled.
  - Restart: Fixed Swapped opponent randomly generated.

2022.09.02 (BR)
  - Restart: You can now choose the empire.
  - Custom Races Menu: Fixed ground attack cost.
  - Custom Races Menu: Swapped colors.
  - Fixed some Aliens races badly set.
  - Fixed The value in the Restart menu.

2022.09.01 (BR)
  - Addition of the Xilmi help file in pdf format, on each release, alongside the jar files.
  - Fixed Show abilities.

2022.09.01 (Xilmi)
  - Both trade-partners now gain faster trade-route-growth if one of them has a diplomacy-bonus.
  - Auto-ship-design now recognizes the role of the previous design in that slot and will create a design fulfilling that same role.
  - Fixed an issue that caused AI being able to research Future-Tech before it had all other techs in the respective category.
  - Fusion-AI less willing to resort to designs with almost no bombs when fighting multi-frontier-wars.
  - Removed a condition that could lead to Fusion-AI not to build colony-ships.
  - A bunch of improvements of how Fusion-AI reacts to their opponents building many missile-bases.

2022.08.31 (BR)
  - Diplomacy panel: Added show all abilities by clicking on "Ability".
  - Removed the values of the planet type.

2022.08.30 (BR)
  - Added Target Range for race customization.
  - Alien Races can be set to copy the player race.
  - Fixed Ocean and Jungle not being set.
  - Ranamed CustomRace to CustomRaceFactory to match the naming philosophy.

2022.08.28 (Xilmi)
  - The growth-speed of trade routes has been normalized and is no longer affected by how much one empire likes the other.
  - The racial diplomacy-bonus now impacts trade-route-growth-speed.
  - Removed an unintentional way of Fusion-AI rejecting peace-treaties.
  - Fixed an issue where the AI considered bio-weapons to always do no damage when there was at least one missile base on the strategical map. This makes them way stronger in mid-game-bio-weapon-elimination-wars.
  - Fixed an issue where the AI would always try and sometimes fail to find an alternative target for a fleet too small take on an enemy siege-fleet. This issue was especially problematic when they were outranged. And this fix should make the AI much better at defending.
  - During war AI will only use their own colonies as gatherpoints.

2022.08.27 (Xilmi)
  - Fixed an issue causing Modnar- and Rookie-AI to be replaced by Roleplay-AI.
  - The starting ship-designs for Fighter, Bomber and Destroyer are now procedurally generated to make sure they work correctly with Neo-Humans and custom-races that change the space on their ships.
  - There now is an Auto-button on the ship-design-screen that will automatically generate a design for the selected size.
  - Fixed an issue that could cause missile-boats to be designed with sizes that weren't intended for missile-boats.
  - Further improved invasion-logic of Fusion-AI.
  - Fusion-AI will now adapt the usage of shields depending on how many of the systems they can reach are in nebulae.
  - Fusion-AI now uses a more optimistic logic to determine whether its fleets should be able to handle enemy missile-bases during decision-making where to send their fleets.
  - Fusion-AI will no longer retreat defending ships when they are the last stack other than their planet and the missile-bases would beat the opponent all alone.
  - Fusion-AI will no longer consider personality to determine how aggressive it should be. However, there is now a bigger variance in how the race impacts aggressivness. This also means the extremes of extremely aggressive and extremely passive have been removed and everyone is within a range of 67-150% of the default. Note that for now all custom-races played by the AI will use default-aggressivenes instead of something appropriate to their modifiers.

2022.08.27 (BR)
  - Adjusted Race Customization setting cost.
  - Changed the randomizetion limits with Min, Max and Smooth Edges
  - Added "boolean Empire.isCustomRace()"
  - Added to Empire methods for every modnar feature.
  - Replaced the direct call to dataRace by the Empires ones.
  - Set description4 for the customized races.
  - Changed the font size for Modnar Races with too long description.
  - Fixed list randomization in custom Race.
  - Cleaned UserPreferences from removed parameters.
  - Fixed Hostile, Poor and UltraPoor not bein set.
  - Balanced the cost according to Xilmi's recommendations.
  - Preselected opponent won't be set as customized race.

2022.08.26 (BR)
  - Added Player Race Customization.
  - Added Alien Race Randomization.

2022.08.24 (BR)
  - Fixed an old bug where "race" and "dataRace" were mixed, affecting the "randomized race abilities"-option.
  - Better integration of Modnar races abilities thru Race and RaceFactory classes (will be needed later).
  - Corrected Misnamed folders starting with a capital letter.
  - Corrected a syntax error in a property name.
  - Some more "race" to "dataRace" correction.
  - Added personal github building tools allowing easier release Notes.

2022.08.23 (Xilmi)
  - Fixed a bug that would break tech-trees when using the "Always" option for techs in the "MOD Options B"-menu.

2022.08.21 (Xilmi)
  - Fixed an issue that could cause AI to bomb when it shouldn't and not bomb when it should.
  - The amount of ships that cover for an incoming invasion of an enemy planet will be reduced in some cases.
  - You can now create rally-points by right-clicking a system while having another system selected. This will also shift all rallies going towards the selected system to the right-clicked system. This is a fast and convenient way to quickly reorganize rally-points.

2022.08.17 (Xilmi)
  - The scout-fix from yesterdays version could cause AI to spam lots of colony-ships. This has been fixed.

2022.08.16 (Xilmi)
  - You can now review each races' ability in the diplomacy-tab in the races-screen
  - AI will now continue to use already existing scout-designs when it doesn't want scouts anymore rather than having them idle until they are scrapped (primarily impacts NeoHumans)
  - When the AI designs ships where the primary weapon is 2-shot-missiles it will not use shields or defensive specials at the same time in favor of more fire-power

2022.08.15 (Xilmi)
  - Fixed an issue where AI would consider potential enemy missile-bases as more dangerous than they actually are during invasion-calculations

2022.08.14 (Xilmi)
  - Fixed an issue where AI would continue to build huge colony-ships when it shouldn't
  - Improved selection of systems where Fusion-AI builds colony-ships to allow faster expansion
  - Fusion-AI will now avoid attacking undefended planets in tactical-combat when it would destroy a colony it still wants to invade
  - Fixed issue where Fusion-AI would sometimes bombard planets it shouldn't bombard
  - Fusion-AI invasion-logic is now completely driven by cost-benefit analysis including the troops expected to be shot down
  - Fusion-AI no longer refitting factories when there's an urgent threat like an invasion or a siege

2022.08.09 (BR)
  - Technology Always/Never: Never affect Artifacts Planets Too.
  - Technology Always/Never: Removed Terraform120 as it's already Always, and selecting Never crash the game!

2022.08.09 (BR)
  - Restart: Debuged the random placement of alien races.
  - Restart: If opponents were set to random, the initial realization will be taken. For race, AI, personality and objective
  - Removed old "Race change"-option
  - Added Restart options to Remnants.cfg
  - Added "Random Events Starting Turn" to "MOD Options B". This setting was already available in Profiles.cfg

2022.08.09 (Xilmi)
  - Healthbar of currently selected stack now matches border-color to make it easier to tell which stack is currently selected

2022.08.08 (Xilmi)
  - Hostily from Advanced Options now impacts Fusion-AI
  - Added "Smart-Resolve" (like Auto-Resolve but allowed to retreat) to the ship-combat-prompt-options

2022.08.07 (Xilmi)
  - Fixed issues with AI tech-selection
  - AI will not focus on computer techs before it has the techs it needs for a rush
  - Fixed an issue present in both governor and AI that would send more people from rich and fewer people from poor systems than intended
  - Fixed an issue where AI started building too many military-ships too early in the game

2022.08.07 (BR)
  - Removed every calls to Java 9 and Java 11. Now only Java 8.
  - Initial Profiles.cfg generation: removed call to the updated "Always Stargates"

2022.08.06 (BR)
  - Removed the useless double call to options() and newOptions().
  - Rewrote "Restart with a new race". It's now a "Start" allowing to load an old save and copy galaxy and opponents.

2022.08.04 (BR)
  - Made tools to simplify setting addition to user preferences: Only two lines are needed.
  - Made tools to simplify setting addition to MOD Options GUI: Only two lines are needed (Using above tools). Compatible with traditionnal settings.
  - On these new settings the middle button will now reset the setting to its default value.
  - The GUI auto resize up to 4 columns and 6 rows.
  - Added two GUI: MOD Options B and Display Options.
  - Display option GUI: Added all the galaxy Map zooming settings already added to Remnant.cfg.
  - MOD Options B GUI: Added the galaxy spacing options
  - Maximum Spacing Limit is now under your control.
  - MOD Options B GUI: Added 8 key Technology control with Always/Never/Auto options; For Player or AI.
  - Technologies: Irradiated Cloaking Stargate Hyperspace Industry2 Thorium Transport Terraforming120.
  - MOD Options A GUI: Added option to give Artifact, Fertile, Rich or Ultra-Rich Home World to Player/AI.
  - Added all these new things to Profiles Manager.

2022.07.25 (Xilmi)
  - Changed version-string from to "Rotp-C-M-X-BR" to "Fusion-Mod"
  - Renamed AI's Legacy => Fusion, Advanced => Roleplay
  - Restored default- and maximum empire-count from Fusion-Mod
  - Improved AIs ability to deal with starting with a rich or artifact-homeworld
  - AI with superior technology more likely to build bigger fleets
  - Fusion AI considers backstab-potential of other races before declaring war
  - Fusion AI now uses an aggressiveness model to determine whether to go to war. Aggressiveness also depends on leader-personality.
  - Fusion AI war-weariness no longer depends on aggressiveness in order to reduce hit&run-exploits against less aggressive races

2022.07.24 (BR)
  - Replaced Modnar Ships colors settings with last Ray's settings.

2022.07.24 (BR)
  - Restored default language management.

2022.07.24 (BR)
  - Added GridCircularDisplay to Remnant.cfg.

2022.07.24 (BR)
  - Added full mouse control to Galaxy Options GUI.
  - Added new races on/off on Galaxy Options GUI.

2022.07.23 (BR)
  - Added easy control to switch ON/OFF the new races.
  - Xilmi improvement for the new races with Ultra-Rich Home World.

2022.07.22 (BR)
  - Retreat Restriction Current value now displayed as text instead of integer.
  - The other languages are available again.

2022.07.22 (BR)
  - Minor bugs correction.
  - Auto generated Profiles.cfg: Removed the "surprise"-action from "MyRandom" for a better feeling in initial random testing!

2022.07.21 (BR)
  - Made Rookie AI independent of Base and Modnar AI.
  - Restored original Base and Modnar AI.
  - Added Low fuel range. (to compensate companion bonus!)
  - Allow Fuel Range edition.
  - Allow Warp Speed edition.
  - Allow Terraforming Hostile edition.
  - Allow AI Hostility edition.
  - Allow Research Rate edition.
  - Debug Random Events edition.
  - Added Random Events Starting Year.
  - Added Option to always have the Control Irradiated in Tech tree
  - Added Always Irradiated to Modnar GUI

2022.07.15 (BR)
  - Changing Player Race: Fixed Technology tree re-generation.
  - Minor bugs Fix
  - Added total mouse control to Modnar and Advanced GUI

2022.07.13 (BR)
  - Restored Fuel Range.
  - Added randomness in the distribution of companion worlds.
    - Up to 6 companions worlds are now available.
    - Negative value of companion worlds for the original distribution.
  - Added tools for symmetric Galaxies generation.
    - Added symmetric option to Elliptical Galaxies.
    - Added symmetric option to Spiral Galaxies.
    - Added symmetric option to Spiral Arms Galaxies.
      - Added Straigth and Very Loose options to Spiral Arms Galaxies.
  - Updated to Xilmi Fusion 2022-07-13.

2022.07.05 (BR)
  - Added AI filters for individual filling option.
  - Added secondary options for Galaxy shapes.
  - Added possibility to hide parameters in Profiles.cfg

2022.07.01 (BR)
  - Junit test integration to github
  - Pom file normalization
  - Minor bugs solved
  - Updated to ROTP Fusion-MOD v2022.06.29

2022.06.29 (BR)
  - Copy of Fusion-MOD v2022.06.25
  - Merged with 1.02a_modnar_MOD39_newRaces
  - Merged with Profiles Manager
  - Added some on the side feature...
