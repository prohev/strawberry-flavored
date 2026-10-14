package dev.strawberryflavored.food.ui;

import dev.strawberryflavored.food.block.FoodMachineBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import eu.pb4.sgui.api.gui.SimpleGui;

/** Server-side custom station GUI backed by the machine's real inventory. */
public final class FoodMachineGui extends SimpleGui {
    private final FoodMachineBlockEntity machine;

    public FoodMachineGui(ServerPlayer player, FoodMachineBlockEntity machine) {
        // Use a fully explicit four-row chest. SGUI's automatically included
        // player range is protected by the wrapper; explicit slots remain
        // normal interactive inventory slots.
        super(MenuType.GENERIC_9x3, player, false);
        this.machine = machine;
        // Keep the player's normal inventory visible and usable below the
        // station slots. Only the machine's status slot is locked separately.
        setLockPlayerInventory(false);
        setTitle(machine.getDisplayName());
        ItemStack filler = new ItemStack(dev.strawberryflavored.food.item.FoodItems.GUI_FILLER);
        filler.set(DataComponents.ITEM_NAME, Component.empty());
        for (int slot = 0; slot < 27; slot++) setSlot(slot, filler);

        // Centered working area: inputs, status paper, preview, output.
        for (int slot = 0; slot < FoodMachineBlockEntity.INPUT_SLOT_COUNT; slot++) {
            final int machineSlot = slot;
            setSlot(10 + slot, new Slot(machine, machineSlot, 98 + slot * 18, 38) {
                @Override public boolean mayPlace(ItemStack stack) {
                    return machine.canPlaceItem(machineSlot, stack);
                }
            });
        }
        setSlot(14, machine.getItem(FoodMachineBlockEntity.STATUS_SLOT));
        setSlot(15, machine.previewStack());
        setSlot(16, new Slot(machine, FoodMachineBlockEntity.OUTPUT_SLOT, 152, 38) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
        });
        updateStatus();
    }

    private Slot machineSlot(int index, int x, int y) {
        return new Slot(machine, index, x, y);
    }

    private void updateStatus() {
        setSlot(14, machine.getItem(FoodMachineBlockEntity.STATUS_SLOT));
        setSlot(15, machine.previewStack());
    }

    @Override
    public void onTick() {
        super.onTick();
        if (!machine.stillValid(getPlayer())) {
            close(true);
            return;
        }
        updateStatus();
    }
}
