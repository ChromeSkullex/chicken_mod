package chromeskullex.chicken.entity.client.renderer;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.client.model.RhodeIslandRoosterModel;
import chromeskullex.chicken.entity.custom.chicken.RhodeIslandRed.RhodeIslandRoosterEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RhodeIslandRoosterRenderer extends GeoEntityRenderer<RhodeIslandRoosterEntity>{


    public RhodeIslandRoosterRenderer(
            EntityRendererFactory.Context context){
        super(context, new RhodeIslandRoosterModel());
    }
    
    @Override
    public Identifier getTextureLocation(RhodeIslandRoosterEntity animatable){
        return new Identifier(Chicken.MOD_ID, "textures/entity/redchicken_male.png");
    }
        
}
