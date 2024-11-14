package chromeskullex.chicken.item;

import chromeskullex.chicken.entity.ModEntries;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;

public class SpawnEgg {
    public static final Item RED_CHICKEN_SPAWN_EGG = new SpawnEggItem(
            ModEntries.RED_CHICKEN,
            0xc97e3e,
            0x993e28,
            new FabricItemSettings()
    );
}
