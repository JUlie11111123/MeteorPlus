package nekiplay.meteorplus.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
	public static final Item LOGO_ITEM = new Item(new FabricItemSettings());
	public static final Item LOGO_MODS_ITEM = new Item(new FabricItemSettings());
	public static void Initialize() {
		Registry.register(Registries.ITEM, new Identifier("meteorplus", "logo"), LOGO_ITEM);
		Registry.register(Registries.ITEM, new Identifier("meteorplus", "logo_mods"), LOGO_MODS_ITEM);
	}
}
