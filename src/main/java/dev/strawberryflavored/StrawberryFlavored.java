package dev.strawberryflavored;

import dev.strawberryflavored.armor.TrimBuffManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrawberryFlavored implements ModInitializer {
    public static final String MOD_ID = "strawberry-flavored";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        TrimBuffManager.register();
        LOGGER.info("Strawberry Flavored initialized.");
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
