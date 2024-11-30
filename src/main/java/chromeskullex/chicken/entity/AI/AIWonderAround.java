package chromeskullex.chicken.entity.AI;

import chromeskullex.chicken.entity.custom.chicken.CustomChickenEntity;
import chromeskullex.chicken.entity.custom.chicken.RedChickenEntity;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;

public class AIWonderAround extends WanderAroundFarGoal {
    private final CustomChickenEntity chicken;


    public AIWonderAround(CustomChickenEntity mob, double d) {
        super(mob, d);
        this.chicken = mob;

    }
    @Override
    public boolean canStart() {
        if (!this.chicken.isSleeping()){
            return this.mob.getRandom().nextFloat() < 0.02F;
        }
        return false;

    }
}
