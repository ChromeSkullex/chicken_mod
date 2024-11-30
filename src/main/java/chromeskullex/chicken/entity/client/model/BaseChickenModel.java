package chromeskullex.chicken.entity.client.model;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.custom.chicken.BaseChickenEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.Objects;

public abstract class BaseChickenModel<T extends GeoAnimatable> extends DefaultedEntityGeoModel<T> {
    public BaseChickenModel(String modelName) {
        super(new Identifier(Chicken.MOD_ID, modelName));

    }

    public abstract String getTexturePath();
    public abstract String getModelPath();
    public abstract String getAnimationPath();




    @Override
    public Identifier getModelResource(T animatable) {
        return new Identifier(Chicken.MOD_ID, getTexturePath());
    }

    @Override
    public Identifier getTextureResource(T animatable) {
        return new Identifier(Chicken.MOD_ID, getModelPath());
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        return new Identifier(Chicken.MOD_ID, getAnimationPath());
    }
    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        if (animatable instanceof BaseChickenEntity chicken) {
            if (Objects.equals(chicken.getCurrentAnimation().getName(), "sleep")) {
                return;
            }

        }

        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
