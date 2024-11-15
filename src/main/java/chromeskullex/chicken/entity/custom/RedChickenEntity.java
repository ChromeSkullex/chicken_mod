package chromeskullex.chicken.entity.custom;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.ModEntries;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
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

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final Random RANDOM = new Random();

    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().then("animation.model.walk", Animation.LoopType.LOOP);
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().then("animation.model.idle", Animation.LoopType.LOOP);
    protected static final RawAnimation TAIL_ANIM = RawAnimation.begin().then("animation.model.idle_tail", Animation.LoopType.LOOP);
    protected static final RawAnimation FLAP_ANIM = RawAnimation.begin().then("animation.model.flapping", Animation.LoopType.LOOP);

    private boolean tail = false;
    private int tailLength = 100;
    private int randomTailInterval = RANDOM.nextInt(181) + 20;

    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0F;
    public boolean isFlapping = false;


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
    public void tickMovement(){
        super.tickMovement();

        this.fallDistance = 0.0F; // Chicken does not take Fall Damage


        this.prevFlapProgress = this.flapProgress;
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation += (this.isOnGround() ? -1.0F : 4.0F) * 0.3F;
        this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0F, 1.0F);
        if (!this.isOnGround() && this.flapSpeed < 1.0F) {
            isFlapping = true;
            this.flapSpeed = 1.0F;
        }
        else if (this.isOnGround()) {
            this.isFlapping = false;

        }

        this.flapSpeed *= 0.9F;
        Vec3d vec3d = this.getVelocity();
        if (!this.isOnGround() && vec3d.y < 0.0) {
            this.setVelocity(vec3d.multiply(1.0, 0.6, 1.0));
        }

        this.flapProgress += this.flapSpeed * 2.0F;


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
        controllers.add(new AnimationController<>(this, "Flap", 0, this::flappingController));


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
    protected <E extends RedChickenEntity> PlayState flappingController(final AnimationState<E> event) {
        if (isFlapping) {
            return event.setAndContinue(FLAP_ANIM);
        }
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static DefaultAttributeContainer.Builder createRedChickenAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 15)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_ARMOR, 0.5f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2);
    }

    @Nullable
    public RedChickenEntity createChild(ServerWorld world, PathAwareEntity entity){
        return (RedChickenEntity) ModEntries.RED_CHICKEN.create(world);
    }
}
