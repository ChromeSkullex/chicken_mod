package chromeskullex.chicken.entity.data;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public interface CustomEntityType {
    public String getTypeName();
    public LivingEntity getMale(World world);
    public LivingEntity getFemale(World world);

}
