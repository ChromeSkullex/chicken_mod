package chromeskullex.chicken.item;


import chromeskullex.chicken.Chicken;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    // Food Settings
    public static final FoodComponent RAW_CHICKEN_COMPONENT = new FoodComponent.Builder()
        .alwaysEdible()
        .snack()
		.statusEffect(new StatusEffectInstance(StatusEffects.POISON, 6 * 20, 1), .3f)
        .build();

    
    // Food Items
    public static final Item CHICKEN_LEG = registerItem("chicken_leg", new Item(new FabricItemSettings().food(RAW_CHICKEN_COMPONENT)));   
    public static final Item RED_CHICKEN_SPAWN_EGG = registerItem("red_chicken_spawn_egg", SpawnEgg.RED_CHICKEN_SPAWN_EGG);
    

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Chicken.MOD_ID, name), item);

    }

    private static void addItemsToIngredients(FabricItemGroupEntries entries) {
        entries.add(CHICKEN_LEG);
        entries.add(RED_CHICKEN_SPAWN_EGG);
    }
    public static void registerModItems() {
        Chicken.LOGGER.info("Registering items " + Chicken.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredients);
    }
    

}
