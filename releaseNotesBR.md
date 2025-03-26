[Official website](https://www.remnantsoftheprecursors.org) <br/>

New Java requirement: minimum JRE-17, recommended JRE-23.

Info for Mac Users: Applications/games/rotp/ is a good place to run it smoothly!


<b><ins>Very last changes:</ins></b>

2025.03.26 (Xilmi)
- Bugfixes in retreat-logic
  - Fixed a bug where the AI counted the damage of their own missiles towards the damage they would receive when it came to whether they should retreat from incoming missiles. This caused them to flee from fights they were winning.
  - Range- and speed-advantages no longer arbitrarily impact the combat-outcome calculations of the retreat-logic. This caused faster ships to sometimes not retreat when they should in order to prevent from being killed.
- Value colonizable planets in wars.
  - AI fleets without colony-ships would previously completely ignore all systems without colonies on them.
  - This caused several problems during wars:
    - Uninvolved parties could take over a lot of planets bombed out during a war.
    - Great staging-points for causing further havoc deep in the enemies' empire were given up because they weren't colonized yet.
  - The AI now values uncolonized systems with 1/10th the score of colonized ones for fleets without colony-ship. This will make them stay at such systems and wait for a colony-ship instead of travelling very long distances to another front. The AI will also leave some ships behind on these systems to cover up a colonization while advancing further.
- Avoid premature peace-treaties
  - The Xilmi-AIs will no longer automatically make peace once it's ships can no longer reach colonies of their enemies.
  - Instead the AI will check whether they are currently hovering colonizable systems with their fleets and only consider war when there are none.

[Historic](https://github.com/BrokenRegistry/Rotp-Fusion/blob/main/DetailedChanges)
