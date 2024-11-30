package chromeskullex.chicken.entity.AI;

import chromeskullex.chicken.entity.custom.chicken.BaseChickenEntity;
import net.minecraft.entity.ai.goal.LookAroundGoal;

public class AILookAroundGoal extends LookAroundGoal {

    private final BaseChickenEntity chicken;

    public AILookAroundGoal(BaseChickenEntity mob) {
        super(mob);
        this.chicken = mob;

    }

    public boolean canStart() {
        if (chicken.isSleeping()){
            return false;
        }

        return this.chicken.getRandom().nextFloat() < 0.02F;
    }



}
