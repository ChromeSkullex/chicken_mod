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
import java.util.Random;


public class RedChickenEntity extends PathAwareEntity implements GeoEntity {

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private static final Random RANDOM = new Random();

    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().then("animation.model.walk", Animation.LoopType.LOOP);
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.model.idle", Animation.LoopType.LOOP);
    protected static final RawAnimation TAIL_ANIM = RawAnimation.begin().then("animation.model.idle_tail", Animation.LoopType.LOOP);

    private boolean tail = false;
    private int tailLength = 100;
    private int randomTailInterval = RANDOM.nextInt(181) + 20;
    public RedChickenEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    private void decreaseTailLength() {
        tailLength--;
        if (tailLength <= 0) {
            tail = false;
            tailLength = 100;
        }
    }
    private void activateTail() {
        tail = true;

    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Moving", 1, this::moveController));
        controllers.add(new AnimationController<>(this, "Idle", 1, this::idleController));
        controllers.add(new AnimationController<>(this, "Tail", 0, this::tailController));
    }

    protected <E extends RedChickenEntity> PlayState moveController(final AnimationState<E> event) {
        if (event.isMoving()) {
            return event.setAndContinue(WALK_ANIM);
        }
        return PlayState.STOP;
    }

    protected <E extends RedChickenEntity> PlayState idleController(final AnimationState<E> event) {
        if (!event.isMoving()) {
            if(this.age %  randomTailInterval == 0 && !tail){
                activateTail();
            }
            return event.setAndContinue(IDLE_ANIM);
        }
        return PlayState.STOP;
    }
    protected <E extends RedChickenEntity> PlayState tailController(final AnimationState<E> event) {
        if (tail) {
            decreaseTailLength();
            return event.setAndContinue(TAIL_ANIM);
        }
        return PlayState.STOP;
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
