package chromeskullex.chicken.entity.client.model;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.custom.chicken.RhodeIslandRed.RhodeIslandHenEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.Objects;

public class RhodeIslandHenModel extends DefaultedEntityGeoModel<RhodeIslandHenEntity> {

    public RhodeIslandHenModel() {
        super(new Identifier(Chicken.MOD_ID, "red_chicken"), true);
    }

    @Override
    public Identifier getModelResource(RhodeIslandHenEntity animatable) {
        return new Identifier(Chicken.MOD_ID, "geo/redchicken.geo.json");
    }

    @Override
    public Identifier getTextureResource(RhodeIslandHenEntity animatable) {
        return new Identifier(Chicken.MOD_ID, "textures/entity/redchicken.png");
    }

    @Override
    public Identifier getAnimationResource(RhodeIslandHenEntity animatable) {
        return new Identifier(Chicken.MOD_ID, "animations/redchicken.animation.json");
    }

    @Override
    public void setCustomAnimations(RhodeIslandHenEntity animatable, long instanceId, AnimationState<RhodeIslandHenEntity> animationState) {
        if (Objects.equals(animatable.getCurrentAnimation().getName(), "sleep"))
            return;
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }

}
