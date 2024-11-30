package chromeskullex.chicken.entity.data;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public interface AnimalType {
    public LivingEntity getType(World world);
}
