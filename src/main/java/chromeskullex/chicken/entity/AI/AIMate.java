package chromeskullex.chicken.entity.AI;

import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.custom.chicken.BaseChickenEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class AIMate extends Goal {

    protected final BaseChickenEntity chicken;
    @Nullable
    protected BaseChickenEntity mate;
    private int timer;
    protected final World world;
    private static final TargetPredicate VALID_MATE_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(8.0).ignoreVisibility();

    //
    public AIMate(BaseChickenEntity chicken,  World world) {
        this.chicken = chicken;
        this.world = chicken.getWorld();
    }

    @Override
    public boolean canStart() {
        if (!this.chicken.isInLove()){
            return false;
        }
        else {
            this.mate = this.findMate();
            return this.mate != null;

        }
    }
    @Override
    public boolean shouldContinue() {
        return this.mate != null
                && this.mate.isAlive()
                && this.mate.isInLove()
                && this.timer < this.getTickCount(60);
    }

    @Override
    public void stop() {
        this.mate = null;
        this.timer = 0;
    }

    @Override
    public void tick(){
        this.chicken.getLookControl().lookAt(this.mate, 10.0F, (float)this.chicken.getMaxLookPitchChange());
        this.chicken.getNavigation().startMovingTo(this.mate, 2);
        ++this.timer;

        if (this.timer >= this.getTickCount(60) && this.chicken.squaredDistanceTo(this.mate) < 9.0) {
            this.breed();
        }

    }

    @Nullable
    private BaseChickenEntity findMate() {

        Chicken.LOGGER.info("Finding mate");
        List<? extends BaseChickenEntity> list = this.world.getTargets(BaseChickenEntity.class, VALID_MATE_PREDICATE, this.chicken, this.chicken.getBoundingBox().expand(8.0));
        double d = Double.MAX_VALUE;
        BaseChickenEntity chickenMate = null;

        for (BaseChickenEntity otherMate : list) {
            if (this.chicken.canBreedWith(otherMate)) {
                chickenMate = otherMate;
                d = this.chicken.squaredDistanceTo(otherMate);

            }
        }
        return chickenMate;

    }
    protected void breed() {
        this.chicken.breed((ServerWorld)this.world, this.mate);
    }



}
