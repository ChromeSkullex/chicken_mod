package chromeskullex.chicken.entity.AI;

import chromeskullex.chicken.entity.custom.chicken.BaseChickenEntity;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;

public class AIWanderClose extends WanderAroundGoal {
    private final BaseChickenEntity chicken;


    public AIWanderClose(BaseChickenEntity mob, double speed) {
        super(mob, speed);
        this.chicken = mob;
    }

    @Override
    public boolean canStart() {
        if (!this.chicken.isSleeping()){
            Vec3d vec3d = this.getWanderTarget();
            if (vec3d == null) {
                return false;
            } else {
                if (!this.ignoringChance) {

                    if (this.mob.getRandom().nextInt(toGoalTicks(this.chance)) != 0) {
                        return false;
                    }
                }
                this.targetX = vec3d.x;
                this.targetY = vec3d.y;
                this.targetZ = vec3d.z;
                this.ignoringChance = false;
                return true;
            }
        }
        return false;

    }
}
