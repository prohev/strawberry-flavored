<p align="center">
  <img src="src/main/resources/assets/icon.png" alt="Strawberry Flavored icon" width="320">
</p>

<p align="center">
  A collection of small quality-of-life features for Minecraft.
  Everything is server-side; clients do not need Strawberry Flavored installed.
</p>

## Features

<details>
<summary><strong>Armor Trim Buffs</strong></summary>

- A full four-piece set with the same trim material is required.
- The trim pattern does not matter.
- Redstone trim material grants Regeneration I.
- Lapis and quartz trim materials grant Speed I.
- Other trim materials currently have no bonus.


</details>

<details>
<summary><strong>Anti-Crop Trampling</strong></summary>

- Boots with the Feather Falling enchantment will not trample crops.
- Leather Boots have this effect inherently.

</details>

<details>
<summary><strong>Flower Crown</strong></summary>

<p align="left">
  <img src="readme-images/flower_crown_item.png" alt="Flower Crown item" width="128">
</p>

- Crafted with smaller flowers around a piece of string.

<p align="center">
  <img src="readme-images/flower_crown_README.png" alt="Flower Crown in-game screenshot" width="600">
</p>


</details>

<details>
<summary><strong>Happy Ghast Treats</strong></summary>

<p align="left">
  <img src="readme-images/happy_ghast_treat_item.png" alt="Happy Ghast Treat item" width="128">
</p>

- Shapeless recipe: Honeycomb, Honey Bottle, and Sugar.
- Treats increase the speed of Happy Ghasts for five minutes.
- Feeding a Happy Ghast again resets the timer.

</details>

<details>
<summary><strong>Dyed Brushes</strong></summary>

<p align="left">
  <img src="readme-images/red_brush_item.png" alt="Red dyed brush item" width="128">
</p>

- Shapeless recipe: a vanilla brush and any dye.
- Hold right-click to paint wool, carpet, beds, terracotta, concrete, concrete powder, glass, glass panes, candles, shulker boxes, or banners.
- Each dyed brush has 64 uses.

</details>

<details>
<summary><strong>Soul Hearts and Soul Shards</strong></summary>

<p align="left">
  <img src="readme-images/soul_heart_item.png" alt="Soul Heart item" width="128">
</p>

- Soul Hearts are crafted from a Soul Shard, Blaze Rod, Breeze Rod, and Glow Berries.
- Soul Hearts provide `keepInventory=true` when consumed.
<p align="left">
  <img src="readme-images/soul_shard_item.png" alt="Soul Shard item" width="128">
</p>

- Soul Shards can be duplicated like a template, using a Sculk Catalyst as the base block.

</details>

<details>
<summary><strong>Major Event Titles</strong></summary>

- Major events, such as summoning the Wither or entering the End, display an on-screen title.

</details>


## Versioning / GitHub

- The version in `gradle.properties` looks like `26.3-1.6`: Minecraft version, then mod version.
- A commit containing `[build]` runs CI without changing the version.
- A commit containing `[release]` runs CI and, when pushed to `main`, bumps the last number (`26.3-1.6` → `26.3-1.7`) and commits it.
- Commits without `[build]` or `[release]` do not run the workflow.
