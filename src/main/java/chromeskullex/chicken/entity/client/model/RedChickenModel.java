package chromeskullex.chicken.entity.client.model;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.custom.RedChickenEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class RedChickenModel extends GeoModel<RedChickenEntity>{

    @Override
    public Identifier getModelResource(RedChickenEntity animatable) {
        return new Identifier(Chicken.MOD_ID, "geo/redchicken.geo.json");
    }

    @Override
    public Identifier getTextureResource(RedChickenEntity animatable) {
        return new Identifier(Chicken.MOD_ID, "textures/entity/redchicken.png");
    }

    @Override
    public Identifier getAnimationResource(RedChickenEntity animatable) {
        return new Identifier(Chicken.MOD_ID, "animations/redchicken.animation.json");
    }

}
