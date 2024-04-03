Info for Mac Users: Applications/games/rotp/ is a good place to run it smoothly!


<b><ins>Very last changes:</ins></b>
- Fixed possible crash with random Galaxies

<b><ins>Updates since last Reddit announcement:</ins></b>

<b><ins>New Features:</ins></b>
- New Random Galaxy selection. (Pairs well with the dark galaxy!)
  - Most galaxy type can now have their options set to "Random".
  - Galaxy type can be also be randomly selected from any (except Text and Bitmap):
    - Or Galaxy type can be randomly selected from a reduced set:
      - Star Field, Ellipse, Spiral, Spiral Arm, Shuriken, Cluster.
    - Random Galaxy type will also have their options set to "Random".
- New option: Reduced population growth: (by Practical-Incarnation)
  - The goal of this new setting is to make pops a more valuable resource. Losing pops and mass transporting them will have higher costs to your economy. It will slow down conquests from transports and development of newly conquered worlds. There may be a different meta.
  - The pop purchase cap in particular mainly affects highly developed colonies in the mid to late game. It means colonies cannot instantly regrow their pops after sending transports.
    - Reduces natural pop growth by 0.5x
    - Increases the cost to purchase pops with the eco slider by 2x
    - Adds a maximum cap on how much pops you can purchase per turn, proportionally to the size of the planet. The spending cap is set initially to 2.5% of the planet size, and increases with cloning techs.
- New Limited Ignores Eco, a new custom race option: (by Practical-Incarnation)
  - In addition to No, which is the default for all races, and All, which is what Silicoids have, there's a new setting Limited. Limited ignores Waste and Hostile environments like All, but only Barren, Tundra, and Dead hostile planets can be immediately colonized in addition to the normal environments. Inferno, Toxic, and Radiated environments still needs to be researched first.
  - The goal of this new option is to provide a new option to create Silicoid-like races, that are not too overpowered on certain galaxy settings such as larger Hellish galaxies.
- New Ironman option:
  - In the Empire Diplomacy panel, allows or prohibits calling for more detailed species information.
- New option for the player to vote first or Last.
- Successfull execution of a sabotage-mission will now also provide a full scan of the target-system and thus allow the system to be invaded. (Xilmi)
- Replaced the Governor "Spare Xenophobes" with a more general option: "Respect promises"
  - Once framed by an alien empire, the Governor will follow the player's choice for the time necessary for the empire to calm down.
  - Players who had checked "Spare Xenophobes" will now have to also give the right answers when warned!
  - Players who had not checked "Spare Xenophobes" will not be impacted!
  - Defaut value will be "On"
- The last promise will be shown in the Empire Intelligence panel during the entire sensitive time.
  - Named "Governor instructions", this value can be toggled by clicking on it.
- Added option to remove the colony notification messages requesting new allocations.
- Changed how the memory is monitored.
  - Now based on Garbage collection.
  - Based on min of 10 poll if GC does not work.
  - Changed Low Memory tracker!
- The state of the guide is now remembered from session to session. Default = Enabled.
- The advisor has been restored... Easy to switch off in In-Game Options.
- The Auto help at start has been restored... Easy to switch off in In-Game Options.
- Species Customization:
  - Added malus value in Species customization panel.
  - New values for Worker Production and Factory Control.
- Auto-Run manage council
  - New Benchmark tester
- Game Over panel: Option to get more diversified titles.
- Added a new option in the council setting:
  - Realms Beyond: No second chances, no half win... ==> No Alliance victory (Neither Council nor Military) & No rebellion! (Including player)
  - No Alliances: No half win... ==> No Alliance victory (Neither Council nor Military)
- New sub panel to customize Artifact and ressources probabilities.


<b><ins>Optimisation:</ins></b>
- Added a little transparency to the cuttlefish to reduce its brightness.
- Added Base Mod Options SubMenu. (now Empty)
- Restored vanilla galaxy setup panel advanced options. 
- Restart will now gives the same research tech list.
  - This accidentally removed the "new dice roll" when reloading a game! To fix this:
    - New option: Research Moo1 style vs vanilla RotP. (Ironman Panel)
    - YES: Reload won't change research probability (Moo1 style)
    - NO: You may try to reload to get a new dice roll! (RotP default)
- Added Persistent Random Generator option to to Ironman Panel.
- Right click on spending result will maximize the spending (even if not needed).
- Missile base settings: Improved controls
  - Shift = +/- 5;
  - Control = +/- 20;
  - Shift Control = +/- 100;
  - Max value = 9999; (default max still limited to 999)
  - Loop at Max and Min
- The AI will now try to get a spy-network up and running as quickly as possible with empires that it never had infiltrated before. This also applies to the governor, when "Let AI handle spies" is enabled. (Xilmi)
- Increased Governor Max Bases value to 1000.
- Ship Display position will be rounded in fraction of total travel time.
- Auto-Run:
  - Manage council
  - New Benchmark tester
  - Benchmark "Continue" option to Stop the current game, log results, then Continue the tests.
  - Notice will be shown at the bottom of the screen.
  - Option to show the full galaxy when Benchmarking.
  - New option to choose if the autorun should continue after the player lost!
- Moved "Show all AI" in Remnant.cfg
  - Added it to "Main Menu -> Settings"
  - Added it to Pre Game Options (above autoplay setting)
- Little reorganisation in "In-Game Options" Panel for more clarity... I hope!
- Renamed some remaining Nazloks, Fiershan, Ssslauran, and Cryslonoids.


<b><ins>Bug Fixes:</ins></b>
- Fixed multiple star systems sharing the same name.
- Fixed Tech Monsters triggering on reload!
- Fixed occasional crash when fighting Monster.
- Fixed some low occurences crash vulnerabilities. (Thanks for reporting them)
- Fixed wrong button display after Ctrl-option Pop-up panel.
- Fixed "Continue" on startup will suggest a name based on the loaded game.
- Fixed some display options not being saved!
- Fixed some Fullscreen crash on startup
- Fixed an issue that prevented the AI from attacking systems guarded by monsters such as the Orion-guardian when they had previously explored the system with scanners. (Xilmi)
- Fixed Orion Guardian auto-repair not working.
- Fixed Silicoid + Cloning = Never + Atmospheric Terraforming = Never leading to Crash!
- Fixed a bug where AI:Rookie was listed twice in AI:Random
- Fixed an old bug when loosing as "non leader ally" will pop up the lost as rebel screen! Changed to military loss instead!
- Fixed some null pointer exception in for loops when list content may not be up to date.
- Removed autoBombard and autocolonize from "Main Menu -> Settings", as they are no more stored in Remnant.cfg, and it could be confusing!
- Fixed relocating ship pathSprite bug
- Fixed crash due to monster checking to colonize planet!
- Spending Pane: Fixed wrong ship building time estimation when interrupting stargate build.
- Fixed possible crash with random Galaxies
- minor fixes...