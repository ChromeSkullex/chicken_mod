package chromeskullex.chicken.entity.data;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public enum EntityGender {
    MALE, FEMALE, CHILD, RANDOM, NONE;

    public static LivingEntity get(CustomEntityType type, EntityGender gender, World world) {
        return switch (gender) {
            case FEMALE -> type.getFemale(world);
            case MALE, NONE -> type.getMale(world);
            default -> null;
        };

    }
}
