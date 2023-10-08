Info for Mac Users: Applications/games/rotp/ is a good place to run it smoothly!

<b><ins>Updates since last announcement:</ins></b>
- Last Xilmi's AI missile update.
- Fixed disconnected "Deterministic Artifact"-option.
- Fixed some display options not always being saved.
- Fixed and re-enabled governor's animations.
- Added new options to allow/disallow Technologies Research:
  - Atmospheric Terraforming,
  - Cloning,
  - Advanced Soil Enrichment.

<b><ins>Last announcement:</ins></b>
- Last Xilmi's AI missile update.
- Character setting panel improved responsivity
  - On my 4K display it went from 150 ms to < 10 ms !!!
  - Even been able to maximize image rendering quality.
- Character setting panel can be set to display only original species.
    - They are still available, but hidden.
      - This setting is located in the main Option Settings Panel
- Moved "No Fog on Diplomat Icons" to Main Option Settings Panel
  - Character setting panel Toggle FOG KEY = "F"
- Galaxy setting Panel:
  - Improved responsivity (Buttons and Icons)
  - Follow the "No Fog on Diplomat Icons" choice.
  - Toggle FOG KEY = "F"
- Text Options settings Panels (Classic, Compact, and Custom Species)
  - Improved responsivity (Buttons only)
    - Still a lot of text optimization to-do.

- Added distinct weapon sounds.
- Added new beam weapon animated shielding effects.
  - Base shield color = Empire color.
    - Monsters have their own color.
  - Impact color = Weapon color.
  - Impact Radius is function of beam power.
  - Impact Intensity is function of beam absorption ratio.
  - Spreading Radius is function of beam power and shield level.
  - Spreading Intensity is function of beam absorption ratio and and shield level.

<em>Xilmi last AI improvements:</em>
- For all AIs of the Xilmi-family: A technologically superior AI that currently has no enemies will no longer try to get further ahead in tech and instead focus on increasing fleet-size. This change was inspired by Keilah Martin's Let's play.
- When invading, AI's of the Xilmi-Family will now keep at least one third of the source-planet's maximum population back at home in order to stay in the window of decent natural population-growth and not to ruin their economy.

<em>Minor changes</em>
- Added category " ~~~ NEW OPTIONS (BETA) ~~~"
- Forbidden tech can not be stolen anymore, nor plundered.
- New option to disable tech stealing (and plundering).
  - If disabled, half of the infiltration bonus will be given to Research bonus.
  - Infiltration penalty won't impact Research bonus.
- Random Event: When set off in game, immediately stop events evolution.
  - They will continue if set on later.
- Added logarithmic scale for race status.
- Security tax can now go up to 90%.
- Added 2 new very slow research rates (renamed the former "Slowest")
  - Useful with Massives and Insane sized galaxies, to avoid terminating all research.
- Removed the inaccurate "Race"-word from menus and common dialogues.
- Full view of the galaxy:
  - Galaxy Map: "ALT -" = Full view of the galaxy.
- Replay History Panel:
  - New option to start the final replay with a Full view of the galaxy.
  - New option to start the empire replay with a Full view of the galaxy.
  - New option to set the turn pace.
    - Orignal (and default) pace: 100 ms per turn.
    - Can be set up to 10 s per turn.
    - In History panel press "1" to "0" to set 1 x 100 ms to 10 x 100 ms.
    - In History panel press "SHIFT+1" to "SHIFT+0" to set 1 s to 10 s.
  - These option are common to all game and are merorized in Remnants.cfg
    - Can be set in the Main setting panel, in the In-game Options Panel, and In-Game B classic panel.
      - Look for "Zoom Factors" new Sub-panel.
- Added Guide info (No relationship bar, no alliances) to Fusion, Fun, and Character.

<em>Minor Fixes</em>
- Fixed AI scouts bugging with "Reduced range"-option.
- Fixed random event delay not updating on reload.
- Fixed "Total Power"-status saturation.

<em>New additions</em>
- Added New "Deterministic Events"-Option.
  - Same time, same event, same target on different play.
  - Same Events on Reload.
  - If event still actif, put on waiting list till the previous occurence ends.
  - If target is extinct, Monsters are directed to their former home world.
- Ironman Mode (Once set, options can't be changed)
  - Planet artifact won't change on reload.
  - Limited reload option (only every n Turns)
    - "Next Turn"-Button will warn you of this option.
- Optimization:
  - Stars systems distances to empire are no more recomputed after each colony gain/loss, but once after all these events.
    - This was very time cousuming on big galaxy.
- Custom Races can now have Artefacts + Rich or Poor homeworld.
  - Updated auto-flag for resoures and artefacts combo.
  - Updated planet display for resoures and artefacts combo.
- Random Events:
  - New Random Event Sub Panel.
  - All Random Events are now customizable.
  - Random Events can be set to not favor the weak.
  - New Random Events Pacing adjustment.
- Beta Auto-Run Options.
  - Mainly thought for debug!
  - Full log settings in the Main setting option panel.
  - Can be Toggled from the in-game option panel.
  - Can be paused/stopped.
  - Won't start without AI selection (available in empire panel)
  - "Next Turn"-Button will warn you of this choice.
  

