package chromeskullex.chicken.entity.AI;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.custom.chicken.BaseChickenEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.world.World;

public class AIAging extends Goal {

    private final BaseChickenEntity chicken;
    public AIAging(BaseChickenEntity chicken) {
        this.chicken = chicken;
    }

    @Override
    public boolean canStart() {

        // if the chicken is 24000 ticks old, then add 1
        this.chicken.decreaseAgeTick();
        Chicken.LOGGER.info("Aging " + this.chicken.getAgeTick() + " ticks");


        return this.chicken.getAgeTick() <= 0;
    }

    @Override
    public void start() {
        // If a chick is more 2 or more days, age up
        // Add 1 to the age
        if (this.chicken.getAge() >= BaseChickenEntity.ADULT_AGE && this.chicken.isChild()) {
            this.chicken.ageUp();

        }
        this.chicken.setAgeTick(BaseChickenEntity.MAX_AGE_TICK);
        this.chicken.incrementAge();
    }


}
