package chromeskullex.chicken.entity.client.model;
import chromeskullex.chicken.entity.custom.chicken.RhodeIslandRed.RhodeIslandRoosterEntity;

public class RhodeIslandRoosterModel extends BaseChickenModel<RhodeIslandRoosterEntity> {
    public RhodeIslandRoosterModel() {
        super("rhode_island_rooster");
    }

    @Override
    public String getTexturePath() {
        return "geo/redchicken_male.geo.json";
    }

    @Override
    public String getModelPath() {
        return "textures/entity/redchicken_male.png";
    }

    @Override
    public String getAnimationPath() {
        return "animations/redchicken.animation.json";
    }

}
