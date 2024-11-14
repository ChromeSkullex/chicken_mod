package chromeskullex.chicken;

import chromeskullex.chicken.entity.client.renderer.RedChickenRenderer;
import chromeskullex.chicken.entity.ModEntries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ChickenClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRendererRegistry.register(ModEntries.RED_CHICKEN, RedChickenRenderer::new);

	}
}