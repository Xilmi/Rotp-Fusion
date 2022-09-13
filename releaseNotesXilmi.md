## What'New

2022.09.13 (Xilmi)
  - Changed that after finishing a predefined amount of ships or a stargate the allocations weren't changed back automatically from ships to something else.
  - When you change the build-limit the governor is called and will immediately start building the ordered ships given it doesn't need all of it's allocation to build something else.
  - Having a build-limit set now will also tell the governor that it's colony is supposed to be building ships. In this case it doesn't need a tick in ship-allocation to remember that.
  - Governor will now spend the exact amount of money required into building the ships requested via build-limit instead of keeping the bar at a maximum.
  - Fixed an issue with tech-tree-generation when "Always" was chosen in "MOD Options B" for certain techs that could cause the available techs to be empty for certain tiers and thus made the category unresearchable.
  - When the opponent has a warp-dissipator, AI will not try to retreat their last ship at the last moment anymore and instead at the first moment it thinks it'll lose.
  - AI now will want to upgrade trade-treaties when the trade-volume is more than 1.5 than that of the last treaty or the treaty is maxed out instead of requiring both of these conditions to be met.
