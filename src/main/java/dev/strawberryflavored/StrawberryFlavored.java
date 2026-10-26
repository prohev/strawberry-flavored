package dev.strawberryflavored;

import dev.strawberryflavored.armor.TrimBuffManager;
import dev.strawberryflavored.event.SoulHeartEvents;
import dev.strawberryflavored.item.DyedBrushItems;
import dev.strawberryflavored.item.FlowerCrownItems;
import dev.strawberryflavored.item.HappyGhastTreatItems;
import dev.strawberryflavored.item.SoulHeartItems;
import dev.strawberryflavored.world.MajorEventTitles;
import dev.strawberryflavored.food.FoodContent;
import dev.strawberryflavored.processing.polydex.ProcessingPolydex;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.polymer.resourcepack.extras.api.ResourcePackExtras;
import eu.pb4.polymer.resourcepack.extras.api.format.item.ItemAsset;
import eu.pb4.polymer.resourcepack.extras.api.format.item.model.BasicItemModel;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class StrawberryFlavored implements ModInitializer {
    public static final String MOD_ID = "strawberry-flavored";
    public static final String LOG_TAG = "[strawberry-flavored]";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        boolean assetsRegistered = PolymerResourcePackUtils.addModAssets(MOD_ID);
        LOGGER.info("{} Registered Polymer assets for {}: {}", LOG_TAG, MOD_ID, assetsRegistered);
        // FactoryTools renders the machine block models through item-display
        // entities. Register the block model folder so bridgeModel() emits
        // item models that resolve the original Blockbench geometry/textures.
        ResourcePackExtras.forDefault().addBridgedModelsFolder(id("block"));
        ResourcePackExtras.forDefault().addBridgedModelsFolder(id("sgui"),
                (modelId, builder) -> new ItemAsset(
                        new BasicItemModel(modelId),
                        new ItemAsset.Properties(true, true)));
        // The SGUI backgrounds, virtual models, and custom font are required
        // for the machine screens to render correctly on vanilla clients.
        PolymerResourcePackUtils.markAsRequired();

        TrimBuffManager.register();
        MajorEventTitles.register();
        FlowerCrownItems.initialize();
        HappyGhastTreatItems.initialize();
        DyedBrushItems.initialize();
        SoulHeartItems.initialize();
        FoodContent.initialize();
        ProcessingPolydex.register();
        SoulHeartEvents.register();
        LOGGER.info("{} Initialized.", LOG_TAG);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, Objects.requireNonNull(path, "path"));
    }
}
