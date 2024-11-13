package chromeskullex.chicken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chromeskullex.chicken.entity.ModEntries;
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
		FabricDefaultAttributeRegistry.register(ModEntries.RED_CHICKEN, ChickenEntity.createChickenAttributes());
		

	}
}