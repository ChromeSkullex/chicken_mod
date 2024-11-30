package chromeskullex.chicken.entity.client.renderer;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.client.model.RhodeIslandHenModel;
import chromeskullex.chicken.entity.custom.chicken.RhodeIslandRed.RhodeIslandHenEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RhodeIslandHenRenderer extends GeoEntityRenderer<RhodeIslandHenEntity>{


    public RhodeIslandHenRenderer(
            EntityRendererFactory.Context context){
        super(context, new RhodeIslandHenModel());
    }
    
    @Override
    public Identifier getTextureLocation(RhodeIslandHenEntity animatable){
        return new Identifier(Chicken.MOD_ID, "textures/entity/redchicken.png");
    }
        
}
