package chromeskullex.chicken.entity.custom.chicken;

import chromeskullex.chicken.entity.data.EntityGender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.world.World;

public class EntityRoosterBase extends BaseChickenEntity {

    public EntityRoosterBase(EntityType<? extends BaseChickenEntity> entityType, World world) {
        super(entityType, world,false, EntityGender.MALE);
    }

}
