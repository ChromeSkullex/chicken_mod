package chromeskullex.chicken.entity.AI;

import chromeskullex.chicken.entity.custom.chicken.CustomChickenEntity;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

public class AILookAroundGoal extends LookAroundGoal {

    private final CustomChickenEntity chicken;

    public AILookAroundGoal(CustomChickenEntity mob) {
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
