package chromeskullex.chicken.entity.custom;

import chromeskullex.chicken.Chicken;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RedChickenEntity extends PathAwareEntity implements GeoEntity{

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public RedChickenEntity(EntityType<? extends PathAwareEntity> entityType, World world){
        super(entityType, world);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller",0,this::predicate));

    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4));
//        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
//        this.goalSelector.add(3, new TemptGoal(this, 1.0, BREEDING_INGREDIENT, false));
//        this.goalSelector.add(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState){

        if(tAnimationState.isMoving()){
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.model.walk", Animation.LoopType.LOOP));
        }
        else {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.model.idle", Animation.LoopType.LOOP));
        }

        return PlayState.CONTINUE;

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    public static DefaultAttributeContainer.Builder createRedChickenAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 15)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_ARMOR, 0.5f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2);
    }



    
}
