## What's New

2022.10.03 (Xilmi)
  - Reduced the amount of animation-frames for shooting weapons and displaying weapon-effects when using auto-combat to speed it up significantly.
  - Reduced the amount of animation-frames for bio-weapons to the same number as for regular bombs.
  - The expansion-tab on the systems-screen now also provides information on fertility, mineral-richness and artifacts.
  - The exploit-tab on the systems-screen now also provides information on planet-type and size.
  - In autoplay-mode the personality and objective of your AI's leader is now visible in the diplomacy-tab of the races-screen.
  - Fixed that "Hybrid" was missing from the autoplay-options.
  - The order of "Fun" and "Character" in the menu have been swapped to indicate that "Character" is likely the easier of both options.
  - "Character" is the new default-AI-mode for now as I want to gather more feedback on it's changes and I think this increases the chance of people using that mode.
  - Fusion-AI will now ask others if they would start a joint war on their preferred target even if they are not already at war with their preferred target.
  - Fusion-AI will now agree to starting a joint war, if someone asks them to attack their preferred target together even if they wouldn't have done so on their own.
  - The AI-Mode "Character" has been reworked tremendously. The personality and objective of each leader should now have significant impact on how they behave.
    This impacts tech-slider-allocation-preferences, the choice who, if anyone is preferred as war target and how likely a war-declaration on their target is.
    Each objective type prefers a certain tech-category in the way, that they act as if all techs in that category cost only half of the regular price.
    Technologist => Computers, Industrialist => Construction, Diplomat => Force-Fields, Ecologist => Planetology, Expansionist => Propulsion, Militarist => Weapons
    Choice of favorite war-target now works as follows:
    Militarist => Their military-industrial-complex needs to justify itself and infuences the decisions of their leader. And the best justification is to have a strong enemy. They will always pick the opponent with the strongest military.
    Industrialist => Protecting their existing industry takes precedent over anything else. They will pick whoever is the owner of the colony that is closest to their core-worlds.
    Ecologist => Their main-interest is to keep the ecology of the galaxy as balanced as possible. They want to regulate species that have grown out of control and will always target whoever has the highest population. If that is themselves, they see no reason for war and will not attack anyone as war is not particularly great for the eco-systems of the planets either.
    Technologist => They want the techs. All of them. If you have techs, that they don't have you move onto their target-list. The higher the levels of these techs the higher you move up on their "to take techs from by force"-list. If they have everything everyone else has, they are completely content and won't attack anyone.
    Expansionist => They want more planets. Regardless of how many they already have. They want them quick and they don't want to make this goal hard for themselves. They attack in a similar pattern as the Industrialist. Except they look at what planet is closest to their core-fleet and they also take into account how much resistance they expect from their target.
    Diplomat => They are the most cunning of the bunch. They use a complex algorithm that takes a lot of things into account. In particular what the relationship of other races with each other looks like. They are very likely to pick on the ones who are already struggling.
    Personality impacts whether and when wars are declared and whether and when peace is considered. They impact the behavior as follows:
    Important note: None of the following is true, when there's only 2 factions left. In this case, all of them will always immediately attack.
    Ruthless => They will definitely attack their preferred target and chase it down until there's nothing left to attack. They don't care about their own losses and will only ever agree to peace when they are very close to extinction.
    Aggressive => They will attack even if they are at somewhat of a disadvantage. But they are not suicidal. They will make peace if things are looking badly or when they are eying another target.
    Xenophobic => They are cautious and opportunistic. They only attack when they see themselves at an advantage. They will make peace if things are looking badly or when they are eying another target.
    Erratic => On average they are just like the Xenophobes... But sometimes they are as aggressive as an aggressive one... other times they are quite timid and skittish. But the average is of little help, when they still declare war eventually. They will make peace if things are looking badly or when they are eying another target.
    Pacifist => They are actually real pacifists and don't want to have anyhting to do with war. They never start wars on their own. They will want to make peace as soon as the situation has changes. Regardless in what direction. Even if they are winning. Except when they still have transports en-route, that is!
    Honorable => They won't start a war unless someone asks them too. If they are asked to attack the target they are eyeing, they will declare war. They won't do surprise-attacks and always warn their target in advance. However, once they have commited to a war, they will not back down from it. There is no honour in backing down!
    Note, that the competitive strength of these personalities varies dramatically as I deliberately chose not to care about balancing them at all. All that was important to me for this AI-type is to make them play their roles in an immersive way!
    So while a Xenophobic Diplomat will naturally make smart decisions suitable for winning, whereas a Honorable Militarist might let themselves be dragged into a war with an opponent that will eventually just kill them.
