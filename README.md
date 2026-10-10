Strawberry Flavored - Everything is server-side; clients do not need Strawberry Flavored installed.

Armor Trim Buffs!
- Full 4-piece set with the same trim material is required.
- Trim pattern does not matter.
- Redstone trim material: Regeneration I.
- Lapis trim material: Speed I.
- Quartz trim material: Speed I.
- Other trim materials currently have no bonus.

Anti Crop trampling!
- Boots with the Feather Falling Enchantment will not trample crops!
- Due to being "cushioned" and having a softer blow.
- Leather Boots have this effect inheritaly

Added a flower crown!
- crafted with smaller flowers around a piece of string.

Happy Ghast Treats!
- Shapeless: Honeycomb, Honey Bottle, and Sugar.
- These treats increase the speed of Happy Ghasts for 5 mins! Feeding again will reset the timer.

Dyed Brushes!
- Shapeless: a vanilla brush + any dye.
- Right-click (you can hold) wool, carpet, beds, terracotta, concrete, concrete powder, glass, glass panes, candles, shulker boxes, or banners to paint them that color.
- Each dyed brush has 64 uses.

Soul Hearts and Soul Shards!
- Soul Hearts provide "keepInventory=true" when consumed and can be crafted from Soul Shard, Blaze Rod, Breeze Rod, and a Glow Berry.
- Soul Shards can be duplicated like a template with the base block being a Sculk Catalyst.

Major Event Titles!
- Major events, such as summoning the Wither or entering the End, display an on-screen title.

Versioning / GitHub:
- `gradle.properties` version looks like `26.3-1.6` (Minecraft version, then mod version).
- A commit containing `[build]` runs CI without changing the version.
- A commit containing `[release]` runs CI and, when pushed to `main`, bumps the last number (`26.3-1.6` -> `26.3-1.7`) and commits it.
- Commits without `[build]` or `[release]` do not run the workflow.
