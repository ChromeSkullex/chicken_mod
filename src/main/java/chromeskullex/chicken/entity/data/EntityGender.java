package chromeskullex.chicken.entity.data;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import java.util.Random;

public enum EntityGender {
    MALE, FEMALE, CHILD, RANDOM, NONE;

    public static LivingEntity getEntity(CustomEntityType type, EntityGender gender, World world) {
        return switch (gender) {
            case FEMALE -> type.getFemale(world);
            case MALE, NONE -> type.getMale(world);
            case RANDOM -> {
                Random rand = new Random();
                yield rand.nextInt(2) == 0 ? type.getFemale(world) : type.getMale(world);
            }
            default -> null;
        };
    }
}
