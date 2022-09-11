## What'New

2022.09.11 (Xilmi
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