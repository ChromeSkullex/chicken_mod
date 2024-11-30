package chromeskullex.chicken.entity.client.renderer;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.client.model.RhodeIslandChickModel;
import chromeskullex.chicken.entity.client.model.RhodeIslandHenModel;
import chromeskullex.chicken.entity.custom.chicken.RhodeIslandRed.RhodeIslandChickEntity;
import chromeskullex.chicken.entity.custom.chicken.RhodeIslandRed.RhodeIslandHenEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RhodeIslandChickRenderer extends GeoEntityRenderer<RhodeIslandChickEntity>{


    public RhodeIslandChickRenderer(
            EntityRendererFactory.Context context){
        super(context, new RhodeIslandChickModel());
    }
    
    @Override
    public Identifier getTextureLocation(RhodeIslandChickEntity animatable){
        return new Identifier(Chicken.MOD_ID, "textures/entity/redchicken_chick.png");
    }
        
}
