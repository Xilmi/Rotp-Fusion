Changes in this release:

Added a more intuitive target range of total value in race customization.

Here's how it all works.
The limits "Setting Min" and "Setting Max" define the spread of each setting:
  - If they are equal, every randomization will return the same value.
  - As the range increase, the variety of result also increase.
  - If set to -100% 100%, all values can be expected.

"Smooth Edges" defines the randomization distribution:
  - When set to "No" the randomization is uniform between the setting limits.
  - When set to "Yes" the randomization is Gaussian, meaning that there is 1 in 6 chance that the cost will be above the limit and a 1 in 6 chance that the cost will be below the limit.

"Force Target" will activate the "Total Value" Range.
  - A second randomization pass will attempt to smoothly converge the total value to the target range.
  - The initial spread of the settings should be fairly well preserved.
  - Best results are obtained when the initial setting range overlaps the final target range.

In "MOD Options B" thes setting can be assigned to the Alien Races:
  - "Settings Window" means only the first randomization pass will be used.
  - "Target Range" means the second pass will be used to reach the target range.
  - "Player Copy" means Alien settings will match the player's, even if the player has a standard race.

Combined with a symetrical galaxy, the "player copy" will offer a very fair game. You'll see who's the boss!

Also fixed Ocean and Jungle Homeworld not being set.

And of course all the last Xilmi additions.

