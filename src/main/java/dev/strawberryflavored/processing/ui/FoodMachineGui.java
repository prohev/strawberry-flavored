package dev.strawberryflavored.processing.ui;

import dev.strawberryflavored.processing.block.FoodMachineBlockEntity;
import dev.strawberryflavored.processing.polydex.ProcessingPolydex;
import eu.pb4.sgui.api.elements.GuiElement;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** Common server-side SGUI screen for the oven, millstone, and cooking_pot. */
public final class FoodMachineGui extends SimpleGui {
    private static final int INPUT_ROW = 1;
    private static final int INPUT_FIRST_COLUMN = 1;
    private static final int STATUS_COLUMN = 5;
    private static final int PREVIEW_COLUMN = 6;
    private static final int OUTPUT_COLUMN = 7;

    private final FoodMachineBlockEntity machine;

    public FoodMachineGui(ServerPlayer player, FoodMachineBlockEntity machine) {
        super(MenuType.GENERIC_9x3, player, false);
        this.machine = machine;
        setLockPlayerInventory(false);
        setTitle(machine.isCookingPot()
                ? FoodMachineGuiTextures.cookingPotTitle(machine.getDisplayName())
                : FoodMachineGuiTextures.machineTitle(machine.getDisplayName(), machine.isOven() ? "d" : "e"));
        buildLayout();
        updateStatus();
    }

    private void buildLayout() {
        if (machine.isCookingPot() || machine.isOven()) {
            // Both the cooking pot and oven use a 2x3 ingredient grid, a result
            // preview, and a single output slot. Only the cooking pot needs heat.
            for (int machineSlot = 0; machineSlot < FoodMachineBlockEntity.INPUT_SLOT_COUNT; machineSlot++) {
                int row = machineSlot / 3;
                int column = machineSlot % 3;
                setSlot(screenSlot(1 + column, row), inputSlot(machineSlot));
            }
            // These positions mirror the reference CookingPotUi exactly.
            setSlot(screenSlot(4, 0), progressElement());
            setSlot(screenSlot(6, 0), FoodMachineGuiElements.display(machine.previewStack()));
            if (machine.isCookingPot()) {
                setSlot(screenSlot(2, 2), FoodMachineGuiElements.heat(machine.isHeated()));
                setSlot(screenSlot(0, 2), ProcessingPolydex.cookingPotButton(getPlayer(), this::open));
            } else {
                setSlot(screenSlot(0, 2), ProcessingPolydex.ovenButton(getPlayer(), this::open));
            }
            setSlot(screenSlot(7, 2), outputSlot());
            return;
        }
        if (machine.isMillstone()) {
            // The millstone uses a single-input flow with a two-cell progress arrow.
            setSlot(screenSlot(2, INPUT_ROW), inputSlot(0));
            setSlot(screenSlot(3, INPUT_ROW), progressElement());
            setSlot(screenSlot(5, INPUT_ROW), FoodMachineGuiElements.display(machine.previewStack()));
            setSlot(screenSlot(0, 2), ProcessingPolydex.millstoneButton(getPlayer(), this::open));
            setSlot(screenSlot(6, INPUT_ROW), outputSlot());
            return;
        }
    }

    private int screenSlot(int column, int row) {
        return row * 9 + column;
    }

    private Slot inputSlot(int machineSlot) {
        return new Slot(machine, machineSlot, 0, 0) {
            @Override public boolean mayPlace(ItemStack stack) {
                return machine.canPlaceItem(machineSlot, stack);
            }
        };
    }

    private Slot outputSlot() {
        return new Slot(machine, FoodMachineBlockEntity.OUTPUT_SLOT, 0, 0) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
        };
    }

    private void updateStatus() {
        if (machine.isCookingPot() || machine.isOven()) {
            setSlot(screenSlot(4, 0), progressElement());
            setSlot(screenSlot(6, 0), FoodMachineGuiElements.display(machine.previewStack()));
            if (machine.isCookingPot()) {
                setSlot(screenSlot(2, 2), FoodMachineGuiElements.heat(machine.isHeated()));
            }
        } else if (machine.isMillstone()) {
            setSlot(screenSlot(3, INPUT_ROW), progressElement());
            setSlot(screenSlot(5, INPUT_ROW), FoodMachineGuiElements.display(machine.previewStack()));
        }
    }

    private GuiElement progressElement() {
        return FoodMachineGuiElements.progress(machine.duration() == 0
                ? 0.0f : (float) machine.progress() / machine.duration());
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

