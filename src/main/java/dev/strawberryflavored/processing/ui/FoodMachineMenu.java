package dev.strawberryflavored.processing.ui;

import dev.strawberryflavored.processing.block.FoodMachineBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * Required vanilla fallback for BaseContainerBlockEntity.
 * FoodMachineBlock opens the richer SGUI screen; this menu is not opened by the
 * station interaction path and contains no separate machine behavior.
 */
public final class FoodMachineMenu extends AbstractContainerMenu {
    private final FoodMachineBlockEntity machine;

    public FoodMachineMenu(int id, Inventory inventory, FoodMachineBlockEntity machine) {
        super(MenuType.HOPPER, id);
        this.machine = machine;
        for (int slot = 0; slot < FoodMachineBlockEntity.INPUT_SLOT_COUNT; slot++) {
            final int machineSlot = slot;
            addSlot(new Slot(machine, machineSlot, 26 + slot * 18, 20) {
                @Override public boolean mayPlace(ItemStack stack) {
                    return machine.canPlaceItem(machineSlot, stack);
                }
            });
        }
        addSlot(new Slot(machine, FoodMachineBlockEntity.STATUS_SLOT, 116, 20) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
            @Override public boolean mayPickup(Player player) { return false; }
        });
        addSlot(new Slot(machine, FoodMachineBlockEntity.OUTPUT_SLOT, 134, 20) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
        });
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 51 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 8 + column * 18, 109));
        }
    }

    @Override public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override public boolean stillValid(Player player) {
        return machine.getLevel() != null
                && machine.getLevel().getBlockEntity(machine.getBlockPos()) == machine
                && player.distanceToSqr(Vec3.atCenterOf(machine.getBlockPos())) <= 64.0;
    }
}
