package chromeskullex.chicken.entity.client.model;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.custom.chicken.RedChickenEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class RedChickenModel extends DefaultedEntityGeoModel<RedChickenEntity> {

    public RedChickenModel() {
        super(new Identifier(Chicken.MOD_ID, "red_chicken"), true);
    }

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
