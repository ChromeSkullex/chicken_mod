package chromeskullex.chicken.entity.client.renderer;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.client.model.RedChickenModel;
import chromeskullex.chicken.entity.custom.RedChickenEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RedChickenRenderer extends GeoEntityRenderer<RedChickenEntity>{


    public RedChickenRenderer(
            EntityRendererFactory.Context context){
        super(context, new RedChickenModel());
    }
    
    @Override
    public Identifier getTextureLocation(RedChickenEntity animatable){
        return new Identifier(Chicken.MOD_ID, "textures/entity/redchicken.png");
    }
        
}
