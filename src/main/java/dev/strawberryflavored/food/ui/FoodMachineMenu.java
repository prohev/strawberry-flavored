package dev.strawberryflavored.food.ui;

import dev.strawberryflavored.food.block.FoodMachineBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/** Native vanilla-compatible menu; no client-side library or mixin is needed. */
public final class FoodMachineMenu extends AbstractContainerMenu {
    private final FoodMachineBlockEntity machine;

    public FoodMachineMenu(int id, Inventory inventory, FoodMachineBlockEntity machine) {
        // HOPPER is the vanilla menu type with exactly five machine slots.
        // This keeps the client-created menu identical without a client mod or
        // custom data packets (which vanilla clients cannot bind safely).
        super(MenuType.HOPPER, id);
        this.machine = machine;
        addSlot(new Slot(machine, FoodMachineBlockEntity.INPUT_SLOT, 26, 20));
        addSlot(new Slot(machine, 1, 62, 20));
        addSlot(new Slot(machine, 2, 98, 20));
        addSlot(new Slot(machine, FoodMachineBlockEntity.STATUS_SLOT, 134, 20) {
            @Override public boolean isActive() { return false; }
            @Override public boolean mayPlace(ItemStack stack) { return false; }
            @Override public boolean mayPickup(Player player) { return false; }
        });
        addSlot(new Slot(machine, FoodMachineBlockEntity.OUTPUT_SLOT, 170, 20) {
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
        if (index < 0 || index >= slots.size()) return ItemStack.EMPTY;
        Slot source = slots.get(index);
        if (!source.hasItem()) return ItemStack.EMPTY;
        ItemStack original = source.getItem().copy();
        ItemStack moving = source.getItem();
        if (index < 5) {
            if (!moveItemStackTo(moving, 5, slots.size(), true)) return ItemStack.EMPTY;
        } else {
            boolean moved = false;
            if (machine.canPlaceItem(FoodMachineBlockEntity.INPUT_SLOT, moving)) {
                moved = moveItemStackTo(moving, 0, 1, false);
            }
            if (!moved) return ItemStack.EMPTY;
        }
        if (moving.isEmpty()) source.set(ItemStack.EMPTY);
        else source.setChanged();
        return original;
    }

    @Override public boolean stillValid(Player player) {
        return machine.getLevel() != null && machine.getLevel().getBlockEntity(machine.getBlockPos()) == machine
                && player.distanceToSqr(Vec3.atCenterOf(machine.getBlockPos())) <= 64.0;
    }
}
