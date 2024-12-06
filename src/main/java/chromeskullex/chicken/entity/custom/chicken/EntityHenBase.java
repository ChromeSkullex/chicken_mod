package chromeskullex.chicken.entity.custom.chicken;

import chromeskullex.chicken.entity.data.EntityGender;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class EntityHenBase extends BaseChickenEntity {
    public EntityHenBase(EntityType<? extends BaseChickenEntity> entityType, World world) {
        super(entityType, world, false, EntityGender.FEMALE);
    }
}
