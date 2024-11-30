package chromeskullex.chicken.entity;
import chromeskullex.chicken.Chicken;
import chromeskullex.chicken.entity.custom.chicken.RedChickenEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;



public class ModEntries {
    public static EntityType<RedChickenEntity> RED_CHICKEN;

    static {
        RED_CHICKEN = Registry.register(
                Registries.ENTITY_TYPE,
                new Identifier(Chicken.MOD_ID, "red_chicken"),
                FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, RedChickenEntity::new).dimensions(EntityDimensions.fixed(0.4F, 0.7F)).build()
        );
    }

}
