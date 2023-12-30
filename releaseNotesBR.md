Info for Mac Users: Applications/games/rotp/ is a good place to run it smoothly!

<b><ins>Updates since last releases:</ins></b>
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


<b><ins> last releases:</ins></b>
- New option: Dark Galaxy Mode.
  - Only Star systems in scout range or in scanner range are shown. Out of range scouted planet are hidden.
  - Only the final replay will show the full galaxy!
  - Option to:
	- Spies also gives approximate location and info of contacted empires.
	- Spies are unable to give info on out of range Star systems, but they remember the Empires names an very approximative location.
- Shields animations tuning:
  - Added access to former 2D shields animations, as 3D glitches with mac OS.
  - Added a new 3D shield animations that should be compatible with mac OS.
  - Added the fire under the ship animation.
    - Was shown on the demo but not in combat.
  - Replaced the meaningless Animations delay by Animation Fps.
    - The real delay was dependent on the computing time.
    - The animation duration will depends on the fps and the number of frames... 
- Vanilla games can now be loaded by Rotp-Fusion.
  - Not deeply tested, please report bugs.
- Mass Transport Dialog: Set Default to Synchronized.
- All trade tech notification have the skip Button.
- Improved compact and classic Menu reactivity.
- The options opening the sub-menus are more obvious.
- Random Event Monsters:
  - New options: Triggered by tech discovery.
    - Propulsion/Hyper Drives for Space Pirates.
    - Planetology/Advanced Cloning for Space Amoeba.
    - Propulsion/Intergalactic Star Gates for Space Crystal.
    - Tech Monsters: All events + Tech triggered Monsters.
    - Only Monsters: Only Tech triggered Monsters.
  - New option to make them give loots. (Pondered by research speed and monster level)
    - Advance or complete some current research.
    - Gives some BC.
  - New option to allow concurrent monsters.
  - New option to make them easier.
  - They are shown wandering the map until they choose their new target.
- Galaxy Map: Holding "Ctrl" gives prirority on star system selection over fleet selection.
- Trade Treaties give spy view into in ranges Empires systems.
  - Traders are always good at spotting planets ready to trade, and report basic information!
  - This to prevent friendly neighbors from constantly sending armed scouts and colony!
- Monsters and Guardian are visible.
- New Option: Ungoverned colonies will have their Eco sliders set to "Growth" after Transports are sent.
- Spy: New option to stop spending the budget once the team is complete.
- New buttons on the Ship-selection screen to select or deselect all ships.
  - If you have 5 stacks or so and want to send a single ship you had to click 5 different |< buttons
- Last Xilmi improvements:
  - AI improvement against repulsors.
  - Fusion AI once again is about being selfish and opportunistic rather than trying to prevent someone else from winning.
  - Missile-boats controlled by the AI no longer retreat when their missiles are still flying and are about to kill at least one enemy-unit.
- Classic Menus: option is now selected by the mouse being inside the box.
- Updated "Advanced Game Option" Menu with sub menu.
- Enabled alternate color set for settings panels and governor panel.
- Empire statistic Panel:
  - Add second statistic option: (on the side of lin / log): Display statistic as:
    - "% Tot": Precentage relative to known total (!= Galaxy Total)
    - "% Player": Percentage relative to player value.
    - "Value": The real value
  - When not up to date, add age of data on each race into the bar graphs
- Beam Hold Frames can be set negative to lower the duration of weapons with long holding time.


<b><ins>Bug Fixes:</ins></b>
- Fixed sound echo default values.
- Fixed ship building turns estimation when ship reserve is used.
- Fixed guide appearance on compact option panels.
- Fixed some animations blocked by the temporisation of Result panel.
- Fixed Comet Event not resetting its timer! (Leading to negative delays!)
- Custom Species Menu: Improved reactivity. (It was painfully slow!)
- Fixed Symmetric Galaxies freezing the setup panel.
- Fixed Nasty long lasting crashing bug, when starting a new game. !!!YYYYeeeessss!!!
- Fixed: Clicking on the Spending "Results" will no more "scramble" the other spending...
- Fixed rare crash in Ship Combat when clicking on auto-resolve.
- Fixed: Update Planet Background when degraded or improved.
- Fixed some "Memory Low Warning" contributions...
- Fixed some menu Buttons moving up!
- Fixed Governor panel glitching when a value was over 127.
- Fixed nebula planet being both Artifact and (Ultra)Rich.
  - As compensation: Random event "Precursor Relic" add small probability (2%) of super Rich + Artifact planet generation.