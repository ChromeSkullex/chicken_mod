package chromeskullex.chicken.entity.AI;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.custom.chicken.CustomChickenEntity;
import net.minecraft.entity.ai.goal.Goal;

public class AIHunger extends Goal {

    private final CustomChickenEntity chicken;

    public AIHunger(CustomChickenEntity chicken) {
        this.chicken = chicken;

    }

    @Override
    public boolean canStart() {
        this.chicken.decreaseHungerTick();

        Chicken.LOGGER.info("Chicken Started {}", this.chicken.getHungerTick());
        return this.chicken.getHungerTick() <= 0;
    }

    @Override
    public boolean shouldContinue() {
        return this.chicken.getHungerTick() <= 0;
    }

    @Override
    public void start() {
//        Chicken.LOGGER.info("Chicken Hunger {}", this.chicken.getHungerAmount());

        this.chicken.setHungerTick(this.chicken.getMaxHungerTick());
        this.chicken.decreaseHungerAmount();
    }





}
