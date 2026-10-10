<p align="center">
  <img src="src/main/resources/assets/icon.png" alt="Strawberry Flavored icon" width="160">
</p>

<h1 align="center">Strawberry Flavored</h1>

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

<!-- Image placeholder: add an armor-trim screenshot here. -->
![Armor trim screenshot placeholder](https://placehold.co/800x450?text=Armor+Trim+Screenshot)

</details>

<details>
<summary><strong>Anti-Crop Trampling</strong></summary>

- Boots with the Feather Falling enchantment will not trample crops.
- Leather Boots have this effect inherently.

<!-- Image placeholder: add an anti-trampling screenshot here. -->
![Anti-trampling screenshot placeholder](https://placehold.co/800x450?text=Anti-Trampling+Screenshot)

</details>

<details>
<summary><strong>Flower Crown</strong></summary>

- Crafted with smaller flowers around a piece of string.

<!-- Image placeholder: add a flower-crown screenshot here. -->
![Flower crown screenshot placeholder](https://placehold.co/800x450?text=Flower+Crown+Screenshot)

</details>

<details>
<summary><strong>Happy Ghast Treats</strong></summary>

- Shapeless recipe: Honeycomb, Honey Bottle, and Sugar.
- Treats increase the speed of Happy Ghasts for five minutes.
- Feeding a Happy Ghast again resets the timer.

<!-- Image placeholder: add a Happy Ghast screenshot here. -->
![Happy Ghast screenshot placeholder](https://placehold.co/800x450?text=Happy+Ghast+Screenshot)

</details>

<details>
<summary><strong>Dyed Brushes</strong></summary>

- Shapeless recipe: a vanilla brush and any dye.
- Hold right-click to paint wool, carpet, beds, terracotta, concrete, concrete powder, glass, glass panes, candles, shulker boxes, or banners.
- Each dyed brush has 64 uses.

<!-- Image placeholder: add a dyed-brush screenshot here. -->
![Dyed brushes screenshot placeholder](https://placehold.co/800x450?text=Dyed+Brushes+Screenshot)

</details>

<details>
<summary><strong>Soul Hearts and Soul Shards</strong></summary>

<p>
  <img src="src/main/resources/assets/strawberry-flavored/textures/item/soul_heart.png" alt="Soul Heart" width="48">
  <img src="src/main/resources/assets/strawberry-flavored/textures/item/soul_shard.png" alt="Soul Shard" width="48">
</p>

- Soul Hearts provide `keepInventory=true` when consumed.
- Soul Hearts are crafted from a Soul Shard, Blaze Rod, Breeze Rod, and Glow Berries.
- Soul Shards can be duplicated like a template, using a Sculk Catalyst as the base block.

<!-- Image placeholder: add a Soul Hearts/Soul Shards screenshot here. -->
![Soul items screenshot placeholder](https://placehold.co/800x450?text=Soul+Hearts+%26+Soul+Shards)

</details>

<details>
<summary><strong>Major Event Titles</strong></summary>

- Major events, such as summoning the Wither or entering the End, display an on-screen title.

<!-- Image placeholder: add a major-event screenshot here. -->
![Major event title screenshot placeholder](https://placehold.co/800x450?text=Major+Event+Title)

</details>

## Versioning / GitHub

- The version in `gradle.properties` looks like `26.3-1.6`: Minecraft version, then mod version.
- A commit containing `[build]` runs CI without changing the version.
- A commit containing `[release]` runs CI and, when pushed to `main`, bumps the last number (`26.3-1.6` → `26.3-1.7`) and commits it.
- Commits without `[build]` or `[release]` do not run the workflow.
