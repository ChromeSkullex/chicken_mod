package chromeskullex.chicken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chromeskullex.chicken.entity.MobEntries;
import chromeskullex.chicken.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.passive.ChickenEntity;

public class Chicken implements ModInitializer {
	public static final String MOD_ID = "chicken";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing " + MOD_ID);

		ModItems.registerModItems();
		FabricDefaultAttributeRegistry.register(MobEntries.RHODE_ISLAND_HEN_CHICKEN, ChickenEntity.createChickenAttributes());
		FabricDefaultAttributeRegistry.register(MobEntries.RHODE_ISLAND_ROOSTER_CHICKEN, ChickenEntity.createChickenAttributes());
		

	}
}