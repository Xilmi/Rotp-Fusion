# Remnants of the Precursors

Remnants of the Precursors is a Java-based modernization of the original Master of Orion game from 1993. <br/>

### Mixt of of Xilmi Fusion with Modnar new races
### With BrokenRegistry Profiles Manager. <br/>
... and some more features

The decription of the additions/changes by Xilmi can be found there: <br/>
	[https://github.com/Xilmi/rotp-coder/releases/](https://github.com/Xilmi/rotp-coder/releases/) <br/>
The decription of the additions/changes by Modnar can be found there: <br/>
	[https://github.com/modnar-hajile/rotp/releases](https://github.com/modnar-hajile/rotp/releases) <br/>

The description of the additions/changes by BrokenRegistry can be found there: <br/>
	[https://brokenregistry.github.io](https://brokenregistry.github.io) <br/>
	[Also available as pdf file (User Manual)](https://brokenregistry.github.io/pdf/Profiles.pdf) <br/>

# Other Links
Official website: https://www.remnantsoftheprecursors.com/<br/>
Community subreddit: https://www.reddit.com/r/rotp/<br/>
Download build: https://rayfowler.itch.io/remnants-of-the-precursors

## What'New

2022.09.09 (Xilmi)
  - The bombardment-prompt now includes an estimate of how many factories will be destroyed.
  - AI no longer assumes that producing population to man all already existing factories is always worth it. Instead the return of investment of workers working factories is compared to the savings of letting population grow naturally.
  - AI cost-benefit-analysis for invasions now takes lost-productivity into account on the cost-side and how many factories will likely be destroyed until the invasion arrives on the benefit-side.

2022.09.08 (BR)
  - Fixed restart issue for late game when an empire as lost his home world. 

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

  
