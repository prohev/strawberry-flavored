package dev.strawberryflavored.processing.block;

import dev.strawberryflavored.processing.recipe.FoodProcessingRecipes;
import dev.strawberryflavored.processing.recipe.ProcessingRecipe;
import dev.strawberryflavored.processing.ui.FoodMachineMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import java.util.ArrayList;
import java.util.List;

/** Persistent machine inventory: input in slot 0 and output in slot 4. */
public final class FoodMachineBlockEntity extends BaseContainerBlockEntity {
    public static final int INPUT_SLOT = 0;
    public static final int INPUT_SLOT_COUNT = 6;
    public static final int STATUS_SLOT = 6;
    public static final int OUTPUT_SLOT = 7;
    private static final int SIZE = 8;
    private final FoodMachineBlock.Station station;
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    private int progress;
    private int burnRemaining;
    private int burnTotal;

    public FoodMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, FoodMachineBlock.Station station) {
        super(type, pos, state);
        this.station = station;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, FoodMachineBlockEntity machine) {
        if (level.isClientSide()) return;
        machine.updateIndicator();
        ProcessingRecipe recipe = machine.recipe();
        boolean working = recipe != null
                && machine.canProduce(recipe.output())
                && (machine.isOven() || machine.isMillstone() || machine.isHeatedByOven());
        if (working) {
            machine.progress++;
            if (machine.progress >= recipe.duration()) {
                machine.finish(recipe.output());
            }
            machine.setChanged();
        } else if (recipe == null) {
            machine.progress = 0;
        }
        if (machine.burnRemaining > 0) {
            machine.burnRemaining--;
            machine.setChanged();
        }
    }

    /** Slot 2 is a server-controlled, non-interactive status icon. */
    private void updateIndicator() {
        ItemStack indicator;
        if (isOven() && burnRemaining > 0) {
            int count = Math.max(1, Math.min(4, (burnRemaining * 4 + Math.max(1, burnTotal) - 1)
                    / Math.max(1, burnTotal)));
            indicator = new ItemStack(Items.CAMPFIRE, count);
            indicator.set(DataComponents.ITEM_NAME, Component.translatable(
                    "container.strawberry-flavored.fuel_remaining", burnRemaining));
        } else if (progress > 0 && duration() > 0) {
            int count = Math.max(1, Math.min(4, (progress * 4 + duration() - 1) / duration()));
            // The GUI renders the virtual SGUI progress element; this server
            // slot only retains progress/tooltip state for synchronization.
            indicator = new ItemStack(Items.CLOCK);
            indicator.set(DataComponents.ITEM_NAME, Component.translatable(
                    "container.strawberry-flavored.progress", progress, duration()));
        } else {
            indicator = new ItemStack(Items.PAPER);
            indicator.set(DataComponents.ITEM_NAME, Component.translatable(
                    "container.strawberry-flavored.waiting"));
        }
        if (!ItemStack.matches(getItem(STATUS_SLOT), indicator)) setItem(STATUS_SLOT, indicator);
    }

    private ProcessingRecipe recipe() {
        return FoodProcessingRecipes.forStation(station).stream()
                .filter(recipe -> recipe.inputs().stream().allMatch(required -> hasInput(required)))
                .findFirst().orElse(null);
    }

    private boolean hasInput(net.minecraft.world.item.Item item) {
        for (int slot = 0; slot < INPUT_SLOT_COUNT; slot++) {
            if (getItem(slot).is(item)) return true;
        }
        return false;
    }

    private boolean canProduce(net.minecraft.world.item.Item result) {
        ItemStack output = getItem(OUTPUT_SLOT);
        if (!(output.isEmpty() || (output.is(result) && output.getCount() < output.getMaxStackSize()))) return false;
        ProcessingRecipe recipe = recipe();
        return true;
    }

    private void finish(net.minecraft.world.item.Item result) {
        ProcessingRecipe recipe = recipe();
        if (recipe != null) for (net.minecraft.world.item.Item required : recipe.inputs()) {
            for (int slot = 0; slot < INPUT_SLOT_COUNT; slot++) {
                if (getItem(slot).is(required)) { getItem(slot).shrink(1); break; }
            }
        }
        ItemStack output = getItem(OUTPUT_SLOT);
        if (output.isEmpty()) setItem(OUTPUT_SLOT, new ItemStack(result));
        else output.grow(1);
        progress = 0;
    }

    @Override
    public int getContainerSize() { return SIZE; }

    @Override
    protected NonNullList<ItemStack> getItems() { return items; }

    @Override
    protected void setItems(NonNullList<ItemStack> items) { this.items = items; }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot < 0 || slot >= INPUT_SLOT_COUNT) return false;
        return FoodProcessingRecipes.forStation(station).stream()
                .anyMatch(recipe -> recipe.inputs().contains(stack.getItem()));
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.strawberry-flavored." + station.name().toLowerCase());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, items);
        progress = input.getIntOr("Progress", 0);
        burnRemaining = input.getIntOr("BurnRemaining", 0);
        burnTotal = input.getIntOr("BurnTotal", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        ContainerHelper.saveAllItems(output, items);
        output.putInt("Progress", progress);
        output.putInt("BurnRemaining", burnRemaining);
        output.putInt("BurnTotal", burnTotal);
        super.saveAdditional(output);
    }

    public int progress() { return progress; }
    public int duration() { ProcessingRecipe recipe = recipe(); return recipe == null ? 0 : recipe.duration(); }

    public boolean isOven() { return station == FoodMachineBlock.Station.OVEN; }
    public boolean isMillstone() { return station == FoodMachineBlock.Station.MILLSTONE; }
    public boolean isPot() { return station == FoodMachineBlock.Station.POT; }
    public boolean isHeated() { return isHeatedByOven(); }
    public int burnRemaining() { return burnRemaining; }
    public int burnTotal() { return burnTotal; }

    public ItemStack previewStack() {
        ProcessingRecipe recipe = recipe();
        return recipe == null ? ItemStack.EMPTY : new ItemStack(recipe.output());
    }

    /** Required by BaseContainerBlockEntity; normal station interaction uses SGUI. */
    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new FoodMachineMenu(id, inventory, this);
    }

    public boolean stillValid(Player player) {
        return level != null && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5) <= 64.0;
    }

    private boolean isHeatedByOven() {
        if (level == null) return false;
        BlockState below = level.getBlockState(worldPosition.below());
        if (below.is(dev.strawberryflavored.food.registry.FoodBlocks.OVEN)) return true;
        if (below.is(Blocks.CAMPFIRE) || below.is(Blocks.SOUL_CAMPFIRE)) {
            return below.getValue(BlockStateProperties.LIT);
        }
        return below.is(Blocks.FIRE);
    }

    private static int fuelDuration(ItemStack stack) {
        if (stack.is(net.minecraft.world.item.Items.COAL) || stack.is(net.minecraft.world.item.Items.CHARCOAL)) return 1600;
        if (stack.is(net.minecraft.world.item.Items.BLAZE_ROD)) return 2400;
        if (stack.is(net.minecraft.world.item.Items.LAVA_BUCKET)) return 20000;
        return 0;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        // The status icon is only a GUI indicator and must never become a world drop.
        setItem(STATUS_SLOT, ItemStack.EMPTY);
        if (level != null) Containers.dropContents(level, pos, this);
        super.preRemoveSideEffects(pos, state);
    }
}
