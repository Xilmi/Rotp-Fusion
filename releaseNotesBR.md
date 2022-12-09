Fixed the Player AI Changes not setting the rigth AI.

This information is stored in two places, and I was changing only one!

The AI looks at the first place for "Player" or "AI", and to the second for which AI is selected. This is why the autoplay commutation was done, but with the wrong AI. Thank to Xilmi for point ing it out.

The two places are now synchronized, but as second security the GUI will display the value the AI is looking at!

This was also affecting the "Load Game and update options"... now fixed too.

I'll check the other options for oddities of this kind. Please report any unexpected features, it'll help the bug hunting.