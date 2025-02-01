New Java requirement: minimum JRE-11, recommended JRE-23.

Info for Mac Users: Applications/games/rotp/ is a good place to run it smoothly!


<b><ins>Very last changes:</ins></b>

- Fixed player AI controlled not sending troops without Governor on.
- Fleet Deployment Panel: Key press to select ships based on their speed.
  - Only ships with a warp speed >= of the Pressed Key will be selected.
- Fixed several issues that lead to Sakkra- and Bulrathi-AI not Transport-rushing other empires in the early-game when they should (Xilmi)
- Governor Button Icon will be updated after Swapping Player.
- Updated Option to swap player on load.
  - Stored in game, swapped player will then reload swapped.
  - Ironman mode will not be able to swap.
  - Can be swapped from inside the game.
    - In Debug Panel (All empire available).
    - In Empire panel "Ctrl-Shift-Right Click" on the color circle.
  - Game that had the player swapped are tagged. (Mandatory info when debugging)
    - The tag is displayed on the console on load (the current player id too, if not 0)
- Fixed Restart not using the turn 1 System values when available.
- Start and restart will reinitialise Empire.PLAYER_ID modified by the swap player options.
- New Debug Option (!Alpha!) to swap player when loading or continuing a game.
  - The list of available empire is listed in the Guide.
- Fixed misevaluation of Monster strength.
- Fixed an issue that could cause the combat-AI to just run away from enemy ships instead of attacking them. (Xilmi)


<b><ins>Updates since last Reddit announcement:</ins></b>

- Galaxy setup:
  - New Side panel with all galaxy option to tune and preview.
    - New nearby stars options.
    - Loose galaxy limits.
    - The number of star per empire can now be lowered to 1.
    - Nebulae placement, global and relaive to empire. Their effects on planets.
  - Most galaxy type can now have their options set to "Random".
  - Galaxy type can be set randomly.
  - Removed the 6 buttons options, as their contents was no longer updated...
- New game options:
  - Reduced population growth: (by Practical-Incarnation)
  - Implemented AI-confidence-feature. (Xilmi)
  - Option to customize peace treaties: Peace / Armistice / ColdWar.
  - Iron man panel: a few more options.
  - Improved and persistent (optional) random generator.
  - Council options:
    - The player to vote first or Last.
    - Realms Beyond: No second chances, no half win... ==> No Alliance victory (Neither Council nor Military) & No rebellion! (Including player)
    - No Alliances: No half win... ==> No Alliance victory (Neither Council nor Military)
- Custom Species Panel:
  - New Limited Ignores Eco, a new custom race option: (by Practical-Incarnation)
  - Display malus value in Species customization panel.
  - New values for Worker Production and Factory Control.
- New Option panel to set default Nominal Species Names.
- New rules:
  - Successful execution of a sabotage-mission will now also provide a full scan of the target-system and thus allow the system to be invaded. (Xilmi)
- Old Rules:
  - Ship Combat:
    - New option to set MoO1 like astrroids distribution.
    - New option to prevent asteroids from disapearing.
    - New option to place Ships and planet in their MoO1 location.
    - New option for asteroids to impact on weapons. (As in MoO1)
  - Space Monsters can be replaced by their MoO1 equivalent.
- Governor Options:
  - Replaced the Governor "Spare Xenophobes" with a more general option: "Respect promises"
  - The last promise will be shown in the Empire Intelligence panel during the entire sensitive time.
  - New option to promote atmosphere and soil enrichment, as well as planet terraforming.
- GUI Improvement:
  - Load and Save Options Now available on the main Menu with the "CTRL" key modifier.
  - New option to select the default values: RotP-Fusion / RotP-Original / MoO1
  - Option to remove the colony notification messages requesting new allocations.
  - The state of the guide is now remembered from session to session. Default = Enabled.
  - The advisor has been restored... Easy to switch off in In-Game Options.
  - The Auto help at start has been restored... Easy to switch off in In-Game Options.
  - Game Over panel: Option to get more diversified titles.
  - Max combat turns from 10 to 1000.
  - Option to continue after game over panel! (At your own risk)
  - New "war view" mode, toggled with "W" Key (Good companion of "F7"/"F8")
  - Option to Default Forward Rally and help to select the quickest way through stargates
  - Added Last spy report age in contact panel, and their last efficiencies.
  - Empire status can show max tech level.
  - Restart will now gives the same research tech list.
  - Auto-Run now manage council and has a new Benchmark tester, and more.
  - Transport Panel: auto refill option improvement.
  - Plenty of new Hotkeys: (Shift-F1 to reveal them)
  - Plenty of minor improvements.
  - Improved Smart-Max.
  - Right-Click on planet (above spending panel), will bring the planet view. (Still under construction)
  - Missiles: New option to have their graphics based on shipset.
- AI: Stronger than ever.
- Fixed plenty of language translation bugs.
  - French translation improvement.
- Plenty of little bug fixes.
