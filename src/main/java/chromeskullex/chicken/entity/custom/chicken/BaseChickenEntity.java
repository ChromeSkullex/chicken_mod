package chromeskullex.chicken.entity.custom.chicken;

import chromeskullex.chicken.entity.AI.*;
import chromeskullex.chicken.entity.client.helpers.ChickenAnimations;
import chromeskullex.chicken.entity.data.EntityGender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Random;


public class BaseChickenEntity extends ChickenEntity implements GeoEntity {
    private static final Ingredient BREEDING_INGREDIENT;
    private static final Ingredient FEEDING_INGREDIENT;
    private static final int MAX_HUNGER = 100;
    private static final int MAX_HUNGER_TICK = 120;

    public EntityGender gender;

    public World world;
    private static final TrackedData<Boolean> SLEEPING = DataTracker.registerData(ChickenEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> HUNGER_AMOUNT = DataTracker.registerData(ChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> WATER_AMOUNT = DataTracker.registerData(ChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> HUNGER_TICK = DataTracker.registerData(ChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);

    // Animation
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final Random RANDOM = new Random();

    private ChickenAnimations currentAnimation = null;
    // Animation Variables
    private boolean tail = false;
    private int tailLength = 100;
    private boolean isFlapping = false;
    private int randomTailInterval = RANDOM.nextInt(181) + 20;

    // Chicken Velocity for Falling
    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0F;

    // Animation for Tail (Maybe changed to a goal)
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

    // Taking from ChickenEntity, may need revision
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

    // Geo Animations

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Moving", 1, this::moveController));
        controllers.add(new AnimationController<>(this, "Idle", 1, this::idleController));
        controllers.add(new AnimationController<>(this, "Tail", 0, this::tailController));
        controllers.add(new AnimationController<>(this, "Flap", 0, this::flappingController));
        controllers.add(new AnimationController<>(this, "Sleep", 5, this::sleepController));

    }

    protected <E extends BaseChickenEntity> PlayState moveController(final AnimationState<E> event) {
        if (event.isMoving()) {
            currentAnimation = ChickenAnimations.WALK;
            return event.setAndContinue(currentAnimation.getAnimation());
        }
        return PlayState.STOP;
    }
    protected <E extends BaseChickenEntity> PlayState idleController(final AnimationState<E> event) {
        if (!event.isMoving() && !this.isSleeping()) {
            currentAnimation = ChickenAnimations.IDLE;

            if(this.age %  randomTailInterval == 0 && !tail){
                activateTail();
            }
            return event.setAndContinue(currentAnimation.getAnimation());
        }
        return PlayState.STOP;
    }
    protected <E extends BaseChickenEntity> PlayState tailController(final AnimationState<E> event) {
        if (tail && !this.isSleeping()) {
            currentAnimation = ChickenAnimations.TAIL;
            decreaseTailLength();
            return event.setAndContinue(currentAnimation.getAnimation());
        }
        return PlayState.STOP;
    }
    protected <E extends BaseChickenEntity> PlayState flappingController(final AnimationState<E> event) {
        if (isFlapping) {
            currentAnimation = ChickenAnimations.FLAP;
            return event.setAndContinue(currentAnimation.getAnimation());
        }
        return PlayState.STOP;
    }
    protected <E extends BaseChickenEntity> PlayState sleepController(final AnimationState<E> event) {
        if (this.isSleeping()) {

            currentAnimation = ChickenAnimations.SLEEP;
            return event.setAndContinue(currentAnimation.getAnimation());
        }
        return PlayState.STOP;
    }
    public void setCurrentAnimation(ChickenAnimations animation) {
        if (currentAnimation != animation) {
            currentAnimation = animation;
            // Additional logic can be added here if needed
        }
    }

    public ChickenAnimations getCurrentAnimation() {
        return currentAnimation;
    }

    // Constructor
    public BaseChickenEntity(EntityType<? extends ChickenEntity> entityType, World world) {
        super(entityType, world);
        this.world = world;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.STICK)) {
            String hunger = "Hunger: " + getHungerAmount();
            player.sendMessage(Text.of(hunger), true);

            return ActionResult.SUCCESS;

        } else if (this.isBreedingItem(itemStack)) {
            // Gets fed
            this.setHungerAmount(MAX_HUNGER);
            this.setHungerTick(MAX_HUNGER_TICK);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return FEEDING_INGREDIENT.test(stack);
    }

    // Data Tracker
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SLEEPING, false);
        this.dataTracker.startTracking(HUNGER_AMOUNT, 100);
        this.dataTracker.startTracking(HUNGER_TICK, MAX_HUNGER_TICK);
        this.dataTracker.startTracking(WATER_AMOUNT, 100);
    }

    // Getters and Setters

    // Gender
    public EntityGender getEntityGender()
    {
        return this.gender;
    }

    // Sleeping
    public void setSleeping(boolean sleeping) {
        this.dataTracker.set(SLEEPING, sleeping);
    }

    public boolean isSleeping() {
        return this.dataTracker.get(SLEEPING);
    }

    public void setHungerAmount(int amount) {
        this.dataTracker.set(HUNGER_AMOUNT, amount);

    }

    // Hunger (non-tick)
    public int getHungerAmount() {
        return this.dataTracker.get(HUNGER_AMOUNT);
    }

    public void decreaseHungerAmount() {
        if (this.getHungerAmount() > 0) {
            this.setHungerAmount(this.getHungerAmount() - 1);
        }

    }
    // Hunger (tick)
    public int getMaxHungerTick() {
        return MAX_HUNGER_TICK;
    }

    public void setHungerTick(int amount) {
        this.dataTracker.set(HUNGER_TICK, amount);

    }
    public int getHungerTick() {
        return this.dataTracker.get(HUNGER_TICK);
    }

    public void decreaseHungerTick() {
        if (this.getHungerTick() > 0) {
            this.setHungerTick(this.getHungerTick() - 1);
        }

    }


    // NBT

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("Hunger", this.getHungerAmount());
        nbt.putInt("HungerTick", this.getHungerTick());
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.setHungerAmount(nbt.getInt("Hunger"));
        this.setHungerTick(nbt.getInt("HungerTick"));

    }

    // Goals
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0, new AIHunger(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4));
        this.goalSelector.add(2, new AISleep(this, this.getBlockPos()));
        this.goalSelector.add(3, new AITempGoal(this, 1.0, BREEDING_INGREDIENT, false));
        this.goalSelector.add(5, new AIWonderAround(this, 1.0));
        this.goalSelector.add(6, new AILookAtEntity(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new AILookAroundGoal(this));

    }

    // Statics

    static {
        BREEDING_INGREDIENT = Ingredient.ofItems(new ItemConvertible[]{Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD});
        FEEDING_INGREDIENT = Ingredient.ofItems(new ItemConvertible[]{Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD});

    }


}
