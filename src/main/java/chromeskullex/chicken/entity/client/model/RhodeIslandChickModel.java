package chromeskullex.chicken.entity.client.model;

import chromeskullex.chicken.entity.custom.chicken.RhodeIslandRed.RhodeIslandChickEntity;

public class RhodeIslandChickModel extends BaseChickenModel<RhodeIslandChickEntity> {
    public RhodeIslandChickModel() {
        super("rhode_island_chick");
    }

    @Override
    public String getTexturePath() {
        return "geo/redchicken_chick.geo.json";
    }

    @Override
    public String getModelPath() {
        return "textures/entity/redchicken_chick.png";
    }

    @Override
    public String getAnimationPath() {
        return "animations/redchicken_chick.animation.json";
    }
}
