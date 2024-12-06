package chromeskullex.chicken.entity.AI;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.custom.chicken.BaseChickenEntity;
import net.minecraft.entity.ai.goal.Goal;

public class AIHunger extends Goal {

    private final BaseChickenEntity chicken;

    public AIHunger(BaseChickenEntity chicken) {
        this.chicken = chicken;

    }

    @Override
    public boolean canStart() {
        this.chicken.decreaseHungerTick();

        return this.chicken.getHungerTick() <= 0;
    }

    @Override
    public boolean shouldContinue() {
        return this.chicken.getHungerTick() <= 0;
    }

    @Override
    public void start() {

        this.chicken.setHungerTick(this.chicken.getMaxHungerTick());
        this.chicken.decreaseHungerAmount();
    }





}
