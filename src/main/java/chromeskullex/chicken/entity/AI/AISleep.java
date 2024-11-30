package chromeskullex.chicken.entity.AI;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.custom.chicken.BaseChickenEntity;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AISleep extends Goal {

    private final BaseChickenEntity chicken;
    private final BlockPos blockPos;

    public AISleep(BaseChickenEntity chicken, BlockPos pos) {
        this.chicken = chicken;
        this.blockPos = pos;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }



    public boolean isDaytime(World world) {
        long time = world.getTimeOfDay() % 24000; // Current time in the day-night cycle
        return time >= 0 && time < 12000; // Daytime is from 0 to 12,000 ticks
    }
    public boolean isNighttime(World world) {
        long time = world.getTimeOfDay() % 24000; // Current time in the day-night cycle
        return time >= 12000 && time < 24000; // Nighttime is from 12,000 to 24,000 ticks
    }

    @Override
    public boolean canStart() {
        return isNighttime(this.chicken.world);

    }

    @Override
    public boolean shouldContinue() {
        return this.chicken.isSleeping() && isNighttime(this.chicken.world);
    }

    @Override
    public void start() {
        Chicken.LOGGER.info("AISleep starting at {}", blockPos);
        this.chicken.setSleeping(true);
    }
    @Override
    public void stop() {
        Chicken.LOGGER.info("AISleep stopping for chicken");
        this.chicken.setSleeping(false);
    }



}
