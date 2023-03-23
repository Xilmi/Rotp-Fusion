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

# Other Links
Official website: https://www.remnantsoftheprecursors.com/<br/>
Community subreddit: https://www.reddit.com/r/rotp/<br/>
Download build: https://rayfowler.itch.io/remnants-of-the-precursors

## What's New

2023.03.19 (BR)
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

  
