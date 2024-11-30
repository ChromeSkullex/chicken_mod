package chromeskullex.chicken.entity.AI;

import chromeskullex.chicken.entity.custom.chicken.BaseChickenEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.Nullable;

public class AITempGoal extends TemptGoal {
    private final BaseChickenEntity chicken;
    private static final TargetPredicate TEMPTING_ENTITY_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(10.0).ignoreVisibility();
    private final TargetPredicate predicate;

    private final Ingredient food;

    public AITempGoal(BaseChickenEntity chicken, double speed, Ingredient food, boolean canBeScared) {
        super(chicken, speed, food, canBeScared);
        this.chicken = chicken;
        this.predicate = TEMPTING_ENTITY_PREDICATE.copy().setPredicate(this::isTemptedBy);
        this.food = food;

    }

    private boolean isTemptedBy(LivingEntity entity) {
        return this.food.test(entity.getMainHandStack()) || this.food.test(entity.getOffHandStack());
    }

    @Override
    public boolean canStart() {
        if (!this.chicken.isSleeping()){
                this.closestPlayer = this.mob.getWorld().getClosestPlayer(this.predicate, this.mob);
                return this.closestPlayer != null;

        }
        return false;

    }




}
