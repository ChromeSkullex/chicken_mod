package chromeskullex.chicken.entity.custom.chicken;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.world.World;

public class EntityHenBase extends CustomChickenEntity{
    public EntityHenBase(EntityType<? extends CustomChickenEntity> entityType, World world) {
        super(entityType, world);
    }
}
