package chromeskullex.chicken.entity.client.model;

import chromeskullex.chicken.entity.custom.chicken.RhodeIslandRed.RhodeIslandHenEntity;

public class RhodeIslandHenModel extends BaseChickenModel<RhodeIslandHenEntity> {
    public RhodeIslandHenModel() {
        super("rhode_island_hen");
    }

    @Override
    public String getTexturePath() {
        return "geo/redchicken.geo.json";
    }

    @Override
    public String getModelPath() {
        return "textures/entity/redchicken.png";
    }

    @Override
    public String getAnimationPath() {
        return "animations/redchicken.animation.json";
    }


}
