package chromeskullex.chicken.entity.AI;

import chromeskullex.chicken.entity.custom.chicken.BaseChickenEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.player.PlayerEntity;

public class AILookAtEntity extends LookAtEntityGoal {

    private final BaseChickenEntity chicken;
    public AILookAtEntity(BaseChickenEntity mob, Class<? extends LivingEntity> targetType, float range) {
        super(mob, targetType, range);
        this.chicken = mob;
    }

    @Override
    public boolean canStart() {
        if (!chicken.isSleeping()){
            if (this.mob.getRandom().nextFloat() >= this.chance) {
                return false;
            } else {
                if (this.mob.getTarget() != null) {
                    this.target = this.mob.getTarget();
                }

                if (this.targetType == PlayerEntity.class) {
                    this.target = this.mob.getWorld().getClosestPlayer(this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
                } else {
                    this.target = this.mob.getWorld().getClosestEntity(this.mob.getWorld().getEntitiesByClass(this.targetType, this.mob.getBoundingBox().expand((double)this.range, 3.0, (double)this.range), (livingEntity) -> {
                        return true;
                    }), this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
                }

                return this.target != null;
            }
        }
        return false;

    }
}
