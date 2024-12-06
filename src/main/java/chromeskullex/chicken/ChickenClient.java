package chromeskullex.chicken;

import chromeskullex.chicken.entity.MobEntries;
import chromeskullex.chicken.entity.client.renderer.RhodeIslandChickRenderer;
import chromeskullex.chicken.entity.client.renderer.RhodeIslandHenRenderer;
import chromeskullex.chicken.entity.client.renderer.RhodeIslandRoosterRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ChickenClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRendererRegistry.register(MobEntries.RHODE_ISLAND_HEN_CHICKEN, RhodeIslandHenRenderer::new);
		EntityRendererRegistry.register(MobEntries.RHODE_ISLAND_ROOSTER_CHICKEN, RhodeIslandRoosterRenderer::new);
		EntityRendererRegistry.register(MobEntries.RHODE_ISLAND_CHICK_CHICKEN, RhodeIslandChickRenderer::new);

	}
}