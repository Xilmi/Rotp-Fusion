## What's New

2023.09.10 (Xilmi)
- The AI can now target different stacks with different missile-racks to split their damage between them and avoid overkill.
- The AI will now shoot missiles at planets as soon as possible, missiles at ships as soon as possible when it has more than 2 remaining volleys and otherwise first come close enough to guarantee missiles will hit even if the opponent tries to dodge them.
- The reduction in score for choosing a target that already has incoming missiles will only occur if the incoming missiles will destroy at least one unit in the stack. Otherwise the aforementioned splitting by missile-rack can lead to distributing damage to several stacks but killing nothing.
- The calculation of the flight-path is skipped if only targets in range are considered.
- Player-ships in auto-combat should no longer automatically retreat when they ran out of ammunition but there's still missiles flying towards enemy-targets.
- Instead of having a weird inconsistent handling for missile-ships with still flying missiles, the incoming missiles are now taken into consideration directly in the method that determines which side is expected to win. So AI decsion-making about when to retreat should now be a lot smarter in situations that involve active missile-stacks.
