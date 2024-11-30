package chromeskullex.chicken.entity.custom.chicken;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.AI.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;



public class CustomChickenEntity extends ChickenEntity {
    private static final Ingredient BREEDING_INGREDIENT;


    public World world;
    private static final TrackedData<Boolean> SLEEPING = DataTracker.registerData(RedChickenEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> HUNGER_AMOUNT = DataTracker.registerData(RedChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> WATER_AMOUNT = DataTracker.registerData(RedChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> HUNGER_TICK = DataTracker.registerData(RedChickenEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final int MAX_HUNGER = 100;
    // .0 - 1
    public CustomChickenEntity(EntityType<? extends ChickenEntity> entityType, World world) {
        super(entityType, world);
        this.world = world;
    }

    @Override
    protected void mobTick(){
        // Reduce water content

    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.STICK)) {
            String hunger = "Hunger: " + getHungerAmount();
            player.sendMessage(Text.of(hunger), true);

            return ActionResult.SUCCESS;

        }
        return ActionResult.PASS;
    }

    // Data Tracker
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SLEEPING, false);
        this.dataTracker.startTracking(HUNGER_AMOUNT, 100);
        this.dataTracker.startTracking(HUNGER_TICK, 120);
        this.dataTracker.startTracking(WATER_AMOUNT, 100);
    }

    public int getMaxHunger(){
        return MAX_HUNGER;
    }


    public void setSleeping(boolean sleeping) {
        this.dataTracker.set(SLEEPING, sleeping);
    }

    public boolean isSleeping() {
        return this.dataTracker.get(SLEEPING);
    }

    public void setHungerAmount(int amount) {
        this.dataTracker.set(HUNGER_AMOUNT, amount);

    }


    public int getHungerAmount() {
        return this.dataTracker.get(HUNGER_AMOUNT);
    }

    public void decreaseHungerAmount() {
        if (this.getHungerAmount() > 0) {
            this.setHungerAmount(this.getHungerAmount() - 1);
        }

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
// nbt

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(0, new AIHunger(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.4));
        this.goalSelector.add(2, new AISleep(this, this.getBlockPos()));
        this.goalSelector.add(3, new TemptGoal(this, 1.0, BREEDING_INGREDIENT, false));
        this.goalSelector.add(5, new AIWonderAround(this, 1.0));
        this.goalSelector.add(6, new AILookAtEntity(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new AILookAroundGoal(this));

    }

    static {
        BREEDING_INGREDIENT = Ingredient.ofItems(new ItemConvertible[]{Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD});
    }




}
