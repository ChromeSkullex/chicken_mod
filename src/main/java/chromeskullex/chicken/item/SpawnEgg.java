package chromeskullex.chicken.item;

import chromeskullex.chicken.entity.MobEntries;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;

public class SpawnEgg {
    public static final Item RHODE_ISLAND_HEN_CHICKEN_SPAWN_EGG_FACTORY = new SpawnEggItem(
            MobEntries.RHODE_ISLAND_HEN_CHICKEN,
            0xc97e3e,
            0x993e28,
            new FabricItemSettings()
    );
    public static final Item RHODE_ISLAND_RED_ROOSTER_SPAWN_EGG_FACTORY = new SpawnEggItem(
            MobEntries.RHODE_ISLAND_ROOSTER_CHICKEN,
            0x6e3820,
            0x0b120e,
            new FabricItemSettings()
    );
}
