package chromeskullex.chicken.entity.custom.chicken;

import java.util.Optional;
import java.util.Random;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.AI.*;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.stat.Stats;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

import chromeskullex.chicken.entity.MobEntries;
import chromeskullex.chicken.entity.client.helpers.ChickenAnimations;
import chromeskullex.chicken.entity.custom.chicken.RhodeIslandRed.RhodeIslandChickEntity;
import chromeskullex.chicken.entity.data.EntityGender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;


public class BaseChickenEntity extends ChickenEntity implements GeoEntity {
    // Constants for breeding and feeding ingredients
    private static final Ingredient BREEDING_INGREDIENT;
    private static final Ingredient FEEDING_INGREDIENT;

    // Constants for hunger and age management
    private static final int MAX_HUNGER = 100;
    private static final int MAX_HUNGER_TICK = 120;
    public static final int MAX_AGE_TICK = 200;
    public static final int ADULT_AGE = 2;

    // Chicken Logic
    private boolean isChild;
    public World world;
    private int loveTicks;

    // Data tracking for various chicken states
    private static final TrackedData<Boolean> SLEEPING = DataTracker.registerData(BaseChickenEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> HUNGER_AMOUNT = DataTracker.registerData(BaseChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> WATER_AMOUNT = DataTracker.registerData(BaseChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> HUNGER_TICK = DataTracker.registerData(BaseChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> GENDER = DataTracker.registerData(BaseChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> AGE = DataTracker.registerData(BaseChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> AGE_TICK = DataTracker.registerData(BaseChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> LOVE_TICK = DataTracker.registerData(BaseChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    // Animation instance cache for GeckoLib
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final Random RANDOM = new Random();

    // Current animation state
    private ChickenAnimations currentAnimation = null;
    // Animation variables for tail and wing flapping
    private boolean tail = false;
    private int tailLength = 100;
    private boolean isFlapping = false;
    private int randomTailInterval = RANDOM.nextInt(181) + 20;

    // Variables for chicken wing flapping
    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1.0F;

    /**
     * Decreases the tail length over time and resets when it reaches zero.
     * This method is called periodically to simulate the tail animation.
     */
    private void decreaseTailLength() {
        tailLength--;
        if (tailLength <= 0) {
            tail = false;
            tailLength = 100;
        }
    }

    /**
     * Activates the tail animation.
     * This method sets the tail animation flag to true.
     */
    private void activateTail() {
        tail = true;
    }

    /**
     * Updates the entity's movement logic.
     * This method is called every tick to update the chicken's movement, including wing flapping and fall damage prevention.
     * It also handles the animation states for the chicken.
     */
    @Override
    public void tickMovement(){
        super.tickMovement();
        this.fallDistance = 0.0F; // Chicken does not take Fall Damage
        if (!this.isChild){
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
        if (this.getLoveTicks() > 0){
            Chicken.LOGGER.info("Logging love ticks {}", this.getLoveTicks());
            this.setLoveTicks(this.getLoveTicks()-1);
            if (this.getLoveTicks() % 10 == 0) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.getWorld().addParticle(ParticleTypes.HEART, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), d, e, f);
            }
        }

    }

    /**
     * Returns the animation instance cache for this entity.
     * This cache is used by GeckoLib to manage animations.
     *
     * @return The animation instance cache.
     */
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    /**
     * Registers animation controllers for this entity.
     * These controllers manage different animation states such as moving, idle, flapping, and sleeping.
     *
     * @param controllers The controller registrar to add the animation controllers to.
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Moving", 1, this::moveController));
        controllers.add(new AnimationController<>(this, "Idle", 1, this::idleController));
        controllers.add(new AnimationController<>(this, "Flap", 0, this::flappingController));
        controllers.add(new AnimationController<>(this, "Sleep", 5, this::sleepController));
        if (!this.isChild) {
            controllers.add(new AnimationController<>(this, "Tail", 0, this::tailController));
        }
    }

    /**
     * Handles the movement animation state.
     * This method is called to update the animation state when the entity is moving.
     *
     * @param event The animation state event.
     * @param <E> The type of the entity.
     * @return The play state indicating whether the animation should continue or stop.
     */
    protected <E extends BaseChickenEntity> PlayState moveController(final AnimationState<E> event) {
        if (event.isMoving()) {
            currentAnimation = ChickenAnimations.WALK;
            return event.setAndContinue(currentAnimation.getAnimation());
        }
        return PlayState.STOP;
    }

    /**
     * Handles the idle animation state.
     * This method is called to update the animation state when the entity is idle.
     *
     * @param event The animation state event.
     * @param <E> The type of the entity.
     * @return The play state indicating whether the animation should continue or stop.
     */
    protected <E extends BaseChickenEntity> PlayState idleController(final AnimationState<E> event) {
        if (!event.isMoving() && !this.isSleeping()) {
            currentAnimation = ChickenAnimations.IDLE;

            if (this.age % randomTailInterval == 0 && !tail) {
                activateTail();
            }
            return event.setAndContinue(currentAnimation.getAnimation());
        }
        return PlayState.STOP;
    }

    /**
     * Handles the tail animation state.
     * This method is called to update the animation state when the tail animation is active.
     *
     * @param event The animation state event.
     * @param <E> The type of the entity.
     * @return The play state indicating whether the animation should continue or stop.
     */
    protected <E extends BaseChickenEntity> PlayState tailController(final AnimationState<E> event) {
        if (tail && !this.isSleeping()) {
            currentAnimation = ChickenAnimations.TAIL;
            decreaseTailLength();
            return event.setAndContinue(currentAnimation.getAnimation());
        }
        return PlayState.STOP;
    }

    /**
     * Handles the flapping animation state.
     * This method is called to update the animation state when the entity is flapping its wings.
     *
     * @param event The animation state event.
     * @param <E> The type of the entity.
     * @return The play state indicating whether the animation should continue or stop.
     */
    protected <E extends BaseChickenEntity> PlayState flappingController(final AnimationState<E> event) {
        if (isFlapping) {
            currentAnimation = ChickenAnimations.FLAP;
            return event.setAndContinue(currentAnimation.getAnimation());
        }
        return PlayState.STOP;
    }

    /**
     * Handles the sleeping animation state.
     * This method is called to update the animation state when the entity is sleeping.
     *
     * @param event The animation state event.
     * @param <E> The type of the entity.
     * @return The play state indicating whether the animation should continue or stop.
     */
    protected <E extends BaseChickenEntity> PlayState sleepController(final AnimationState<E> event) {
        if (this.isSleeping()) {
            currentAnimation = ChickenAnimations.SLEEP;
            return event.setAndContinue(currentAnimation.getAnimation());
        }
        return PlayState.STOP;
    }

    /**
     * Returns the current animation state of the entity.
     *
     * @return The current animation state.
     */
    public ChickenAnimations getCurrentAnimation() {
        return currentAnimation;
    }

    /**
     * Constructor for the BaseChickenEntity.
     *
     * @param entityType The type of the entity.
     * @param world The world the entity is in.
     * @param isChild Indicates if the entity is a child.
     * @param gender The gender of the entity.
     */
    public BaseChickenEntity(EntityType<? extends ChickenEntity> entityType, World world, Boolean isChild, EntityGender gender) {
        super(entityType, world);
        this.world = world;
        this.isChild = isChild;
        this.setEntityGender(gender);
        if (!this.isChild) {
            this.setAge(3);
        }
    }

    /**
     * Handles interaction with the entity.
     * This method is called when a player interacts with the entity.
     *
     * @param player The player interacting with the entity.
     * @param hand The hand used for the interaction.
     * @return The result of the interaction.
     */
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.STICK)) {
            String hunger_text = "Hunger: " + getHungerAmount();
            String gender_text = " Gender: " + getEntityGender();
            String age_text = " Age: " + getAge();
            player.sendMessage(Text.of(hunger_text + gender_text + age_text), true);

            return ActionResult.SUCCESS;

        } else if (this.isBreedingItem(itemStack)) {
            // Gets fed
            this.setHungerAmount(MAX_HUNGER);
            this.setHungerTick(MAX_HUNGER_TICK);
            if (this.getHungerAmount() == 100 && !this.isChild){
                this.eat(player, hand, itemStack);
                Chicken.LOGGER.info("Chicken Is full and can breed and an adult");
                this.setLoveTicks(600);
            }

            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    /**
     * Checks if the given item stack is a breeding item for this entity.
     *
     * @param stack The item stack to check.
     * @return True if the item stack is a breeding item, false otherwise.
     */
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return FEEDING_INGREDIENT.test(stack);
    }

    /**
     * Initializes the data tracker for this entity.
     * This method is called to set up the data tracker with initial values.
     */
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SLEEPING, false);
        this.dataTracker.startTracking(HUNGER_AMOUNT, 100);
        this.dataTracker.startTracking(HUNGER_TICK, MAX_HUNGER_TICK);
        this.dataTracker.startTracking(WATER_AMOUNT, 100);
        this.dataTracker.startTracking(GENDER, EntityGender.MALE.ordinal());
        this.dataTracker.startTracking(AGE, 0);
        this.dataTracker.startTracking(AGE_TICK, MAX_AGE_TICK);
        this.dataTracker.startTracking(LOVE_TICK, 0);
    }

    // Getters and Setters



    /**
     * Sets the Love Ticks of the Entity
     *
     * @param loveTicks  amount of love ticks
     */

    public void setLoveTicks(int loveTicks) {
        this.dataTracker.set(LOVE_TICK, loveTicks);
    }

    /**
     * Gets the Love ticks of the entity
     *
     * @return The Love Ticks of the entity.
     */
    public int getLoveTicks() {
        return this.dataTracker.get(LOVE_TICK);
    }
    /**
     * Checks to see if the entity is In Love to Breed
     *
     * @return True or False
    **/
    public boolean isInLove() {
//        Chicken.LOGGER.info("Chicken Is in love");
        return  this.getLoveTicks() > 0;
    }
    /**
     * Resets the Love Ticks
     **/
    public void resetLoveTicks() {
        this.dataTracker.set(LOVE_TICK, 0);
    }

    /**
     *  Checks to see if Animal can breed with the other
     * @param other: Other Entity
     * @return True or False
     *
     * **/
    public boolean canBreedWith(BaseChickenEntity other) {
        if (other == this) {
            return false;
        } else {
            Chicken.LOGGER.info("Can Breed, found mate!");
            return this.isInLove() && other.isInLove();
        }
    }
    /**
     * Breeds the two animals
     * @param world: server world
     * @param mate: Other Entity
     * @return True or False
     *
     * **/

    public void breed(ServerWorld world, BaseChickenEntity mate) {
        RhodeIslandChickEntity childEntity = this.createChild(world, mate);
        if (childEntity != null) {
            childEntity.setAgeTick(0);
            childEntity.setChild(true);
            childEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
            this.breed(world, mate, childEntity);
            world.spawnEntityAndPassengers(childEntity);
        }
            Chicken.LOGGER.info("Breeding {} with {}", this.getEntityGender().name(), mate.getEntityGender().name());
            this.resetLoveTicks();
            mate.resetLoveTicks();


    }


    /**
     * Returns the gender of the entity.
     *
     * @return The gender of the entity.
     */
    public EntityGender getEntityGender() {
        return EntityGender.values()[this.dataTracker.get(GENDER)];
    }

    /**
     * Sets the gender of the entity.
     *
     * @param gender The gender to set.
     */
    public void setEntityGender(EntityGender gender) {
        this.dataTracker.set(GENDER, gender.ordinal());
    }

    /**
     * Sets the age of the entity.
     *
     * @param age The age to set.
     */
    public void setAge(int age) {
        this.dataTracker.set(AGE, age);
    }

    /**
     * Returns the age of the entity.
     *
     * @return The age of the entity.
     */
    public int getAge() {
        return this.dataTracker.get(AGE);
    }

    /**
     * Returns the maximum age tick for the entity.
     *
     * @return The maximum age tick.
     */
    public int getMaxAgeTick() {
        return MAX_AGE_TICK;
    }

    /**
     * Decreases the age tick of the entity.
     * This method is called to decrement the age tick.
     */
    public void decreaseAgeTick() {
        if (this.getAgeTick() > 0) {
            this.setAgeTick(this.getAgeTick() - 1);
        }
    }

    /**
     * Sets the age tick of the entity.
     *
     * @param ageTick The age tick to set.
     */
    public void setAgeTick(int ageTick) {
        this.dataTracker.set(AGE_TICK, ageTick);
    }

    /**
     * Returns the age tick of the entity.
     *
     * @return The age tick of the entity.
     */
    public int getAgeTick() {
        return this.dataTracker.get(AGE_TICK);
    }

    /**
     * Increments the age of the entity.
     * This method is called to increase the age by one.
     */
    public void incrementAge() {
        this.setAge(this.getAge() + 1);
    }

    /**
     * Checks if the entity is a child.
     *
     * @return True if the entity is a child, false otherwise.
     */
    public boolean isChild() {
        return this.isChild;
    }

    public void setChild(boolean isChild) {
        this.isChild = isChild;
    }

    /**
     * Ages up the entity to an adult.
     * This method is called to transform the entity from a child to an adult.
     */
    public void ageUp() {
        if (this.isChild) {
            EntityType<? extends BaseChickenEntity> adultEntity =
                    this.getEntityGender() == EntityGender.MALE
                            ? MobEntries.RHODE_ISLAND_ROOSTER_CHICKEN
                            : MobEntries.RHODE_ISLAND_HEN_CHICKEN;

            BaseChickenEntity newEntity = adultEntity.create(this.world);
            if (newEntity != null && this.world instanceof ServerWorld serverWorld) {
                newEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
                newEntity.setEntityGender(this.getEntityGender());
                newEntity.setAge(this.getAge());

                serverWorld.spawnEntity(newEntity);
                this.discard();
            }
        }
    }

    // Sleeping

    /**
     * Sets the sleeping state of the entity.
     *
     * @param sleeping True to set the entity as sleeping, false otherwise.
     */
    public void setSleeping(boolean sleeping) {
        this.dataTracker.set(SLEEPING, sleeping);
    }

    /**
     * Checks if the entity is sleeping.
     *
     * @return True if the entity is sleeping, false otherwise.
     */
    public boolean isSleeping() {
        return this.dataTracker.get(SLEEPING);
    }

    /**
     * Sets the hunger amount of the entity.
     *
     * @param amount The hunger amount to set.
     */
    public void setHungerAmount(int amount) {
        this.dataTracker.set(HUNGER_AMOUNT, amount);
    }

    // Hunger (non-tick)

    /**
     * Returns the hunger amount of the entity.
     *
     * @return The hunger amount.
     */
    public int getHungerAmount() {
        return this.dataTracker.get(HUNGER_AMOUNT);
    }

    /**
     * Decreases the hunger amount of the entity.
     * This method is called to decrement the hunger amount.
     */
    public void decreaseHungerAmount() {
        if (this.getHungerAmount() > 0) {
            this.setHungerAmount(this.getHungerAmount() - 1);
        }
    }

    // Hunger (tick)

    /**
     * Returns the maximum hunger tick for the entity.
     *
     * @return The maximum hunger tick.
     */
    public int getMaxHungerTick() {
        return MAX_HUNGER_TICK;
    }

    /**
     * Sets the hunger tick of the entity.
     *
     * @param amount The hunger tick to set.
     */
    public void setHungerTick(int amount) {
        this.dataTracker.set(HUNGER_TICK, amount);
    }

    /**
     * Returns the hunger tick of the entity.
     *
     * @return The hunger tick of the entity.
     */
    public int getHungerTick() {
        return this.dataTracker.get(HUNGER_TICK);
    }

    /**
     * Decreases the hunger tick of the entity.
     * This method is called to decrement the hunger tick.
     */
    public void decreaseHungerTick() {
        if (this.getHungerTick() > 0) {
            this.setHungerTick(this.getHungerTick() - 1);
        }
    }

    // NBT

    /**
     * Writes the entity's data to an NBT compound.
     * This method is called to save the entity's state.
     *
     * @param nbt The NBT compound to write to.
     * @return The NBT compound with the entity's data.
     */
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("Hunger", this.getHungerAmount());
        nbt.putInt("HungerTick", this.getHungerTick());
        nbt.putInt("Gender", this.getEntityGender().ordinal());
        nbt.putInt("Age", this.getAge());
        nbt.putInt("AgeTick", this.getAgeTick());
        return super.writeNbt(nbt);
    }

    /**
     * Reads the entity's data from an NBT compound.
     * This method is called to load the entity's state.
     *
     * @param nbt The NBT compound to read from.
     */
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.setHungerAmount(nbt.getInt("Hunger"));
        this.setHungerTick(nbt.getInt("HungerTick"));
        this.setEntityGender(EntityGender.values()[nbt.getInt("Gender")]);
        this.setAge(nbt.getInt("Age"));
        this.setAgeTick(nbt.getInt("AgeTick"));
    }

    // Goals

    /**
     * Initializes the goals for this entity.
     * This method is called to set up the AI goals for the entity.
     */
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0, new AIHunger(this));
        this.goalSelector.add(0, new AIAging(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4));
        this.goalSelector.add(2, new AISleep(this, this.getBlockPos()));
        this.goalSelector.add(3, new AIMate(this, world));

        this.goalSelector.add(4, new AITempGoal(this, 1.0, BREEDING_INGREDIENT, false));
        this.goalSelector.add(5, new AIWanderAround(this, 1.0));
        this.goalSelector.add(6, new AILookAtEntity(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new AILookAroundGoal(this));
    }

    // Statics

    static {
        BREEDING_INGREDIENT = Ingredient.ofItems(new ItemConvertible[]{Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD});
        FEEDING_INGREDIENT = Ingredient.ofItems(new ItemConvertible[]{Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD});
    }

    /**
     * Creates a child entity.
     * This method is called to create a child entity when breeding.
     *
     * @param serverWorld The server world.
     * @param passiveEntity The other parent entity.
     * @return The created child entity, or null if creation failed.
     */
    @Override
    @Nullable
    public RhodeIslandChickEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {

        return (RhodeIslandChickEntity) MobEntries.RHODE_ISLAND_CHICK_CHICKEN.create(serverWorld);
    }
}
