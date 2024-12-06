package chromeskullex.chicken.entity.AI;

import chromeskullex.chicken.entity.custom.chicken.BaseChickenEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class AIWanderAround extends WanderAroundFarGoal {
    private final BaseChickenEntity chicken;


    public AIWanderAround(BaseChickenEntity mob, double d) {
        super(mob, d);
        this.chicken = mob;

    }
    @Nullable
    protected Vec3d getWanderTarget() {
        return NoPenaltyTargeting.find(this.mob, 10, 7);
    }
    @Override
    public boolean canStart() {
        if (!this.chicken.isSleeping()){
            if (this.mob.getRandom().nextInt(toGoalTicks(this.chance)) != 0) {
                return false;
            }
            Vec3d vec3d = this.getWanderTarget();
            if (vec3d == null) {
                return false;
            } else {
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
