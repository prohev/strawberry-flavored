Strawberry Flavored - Everything is server-side; clients do not need Strawberry Flavored installed.

Armor Trim Behavior:
- Full 4-piece set with the same trim material is required.
- Trim pattern does not matter.
- Redstone trim material: Regeneration I.
- Lapis trim material: Speed I.
- Quartz trim material: Speed I.
- Other trim materials currently have no bonus.

Feather Falling/Leather Boots!
- Prevent crops from being Trampled.

Added a flower crown!
- crafted with smaller flowers around a piece of string.

Happy Ghast Treats!
- Added a recipe of Honey Comb, Honey bottle, and sugar gives you a Happy Ghast Treats!

Dyed Brushes!
- Shapeless: a vanilla brush + any dye.
- Right-click wool, carpet, beds, terracotta, concrete, concrete powder, glass, glass panes, candles, shulker boxes, or banners to paint them that color.
- Each dyed brush has 64 uses.

Readable Clocks!
- Holding a vanilla clock in either hand displays the Minecraft day and translated 12-hour time above the hotbar.
- The display updates once per translated Minecraft minute and clears when the clock is no longer held.

Versioning / GitHub:
- `gradle.properties` version looks like `26.3-1.6` (Minecraft version, then mod version).
- A commit containing `[build]` runs CI without changing the version.
- A commit containing `[release]` runs CI and, when pushed to `main`, bumps the last number (`26.3-1.6` -> `26.3-1.7`) and commits it.
- Commits without `[build]` or `[release]` do not run the workflow.
