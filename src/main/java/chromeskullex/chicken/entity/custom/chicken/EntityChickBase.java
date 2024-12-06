package chromeskullex.chicken.entity.custom.chicken;

import chromeskullex.chicken.entity.data.EntityGender;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

import java.util.Random;

public class EntityChickBase extends BaseChickenEntity {
    public EntityChickBase(EntityType<? extends BaseChickenEntity> entityType, World world) {
        super(entityType, world, true, new Random().nextInt(2) == 0 ? EntityGender.FEMALE : EntityGender.MALE);
    }




}
