Info for Mac Users: Applications/games/rotp/ is a good place to run it smoothly!
<b><ins>Very last changes:</ins></b>
- New option for the player to vote first or Last.


<b><ins>Updates since last releases:</ins></b>

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

<b><ins>Last Release:</ins></b>

<b><ins>New Features:</ins></b>
- New hotkeys to loop thru flagged star systems: ("SHIFT" for reverse)
  - Num * : Loop thru systems with top left flag ("CTRL-/" Same Flag Color only)
  - Num - : Loop thru systems with top right flag ("CTRL-* " Same Flag Color only)
  - Num + : Loop thru systems with bottom right flag ("CTRL-9" Same Flag Color only)
  - Num 9 or / : Loop thru systems with bottom left flag ("CTRL-8" Same Flag Color only)
    - When "Num" is off: "9" = "PgUp", then "/" can be used instead.
- New Space Monsters Guardian
  - Good planets may now be Guarded (Space Monster)
    - Option to select the ressource to protect.
    - Option to tune the probability of guardian.
    - Option to tune the Monster level.
  - Added Monster Guardian: Giant Space Cyaneidae.
  - Added Monster Guardian: Giant Space Sepiidae.
- New nebulae from real photography option.
  - Nebulae shapes can be different than rectangular.
  - Adjustable real nebulae opacity.


<b><ins>Optimisation:</ins></b>
- Fixed Apple Retina Screen compatibility.
  - Previous reactivity optimisation was not compatible with some retina screen specificity!
    - Optimized for image quality with reactivity cost.
- New Mass Transport Auto-refill Set independently for each colony.
  - Replace the previous global system.
  - Option to initialyze at the panel opening to "On", "Off", or No changes.
  - Auto-refill colony are set by Ctrl-Click the slider.
  - Auto-refill colony sliders are shown in a different color.


<b><ins>Bug Fixes:</ins></b>
- Fixed an issue that caused the fleet-commander of the AI to underestimate the damage-output of missile-bases equipped with scatter-packs. (Xilmi)
- Fixed reset to Default in Galaxy setting panel badly initialyzing the player homeworld.
- Fixed crash starting a new game after playing vanilla savegame.
- Fixed species stat being sometime hidden under the scroll bar.
- Fixed rally from alien planets. (Xilmi)
- Fixed language font generating errors.
- Fixed modnar races text files missing in most languages.
- Fixed "No Spying" protection disabling council alliance alliance tech sharing call.
- Fixed "Continue" will now initialise empty game name.
