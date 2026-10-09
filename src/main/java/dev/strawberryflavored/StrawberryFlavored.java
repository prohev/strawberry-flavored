package dev.strawberryflavored;

import dev.strawberryflavored.armor.TrimBuffManager;
import dev.strawberryflavored.item.DyedBrushItems;
import dev.strawberryflavored.item.FlowerCrownItems;
import dev.strawberryflavored.item.HappyGhastTreatItems;
import dev.strawberryflavored.world.MajorEventTitles;
import dev.strawberryflavored.world.ReadableClocks;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrawberryFlavored implements ModInitializer {
    public static final String MOD_ID = "strawberry-flavored";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        PolymerResourcePackUtils.addModAssets(MOD_ID);

        TrimBuffManager.register();
        MajorEventTitles.register();
        ReadableClocks.register();
        FlowerCrownItems.initialize();
        HappyGhastTreatItems.initialize();
        DyedBrushItems.initialize();
        LOGGER.info("Strawberry Flavored initialized.");
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
