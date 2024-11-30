package chromeskullex.chicken.entity.client.helpers;

import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.RawAnimation;

public enum ChickenAnimations {
    WALK("walk", RawAnimation.begin().then("animation.model.walk", Animation.LoopType.LOOP)),
    IDLE("idle", RawAnimation.begin().then("animation.model.idle", Animation.LoopType.LOOP)),
    TAIL("tail", RawAnimation.begin().then("animation.model.idle_tail", Animation.LoopType.LOOP)),
    FLAP("flap", RawAnimation.begin().then("animation.model.flapping", Animation.LoopType.LOOP)),
    SLEEP("sleep", RawAnimation.begin().then("animation.model.sleep", Animation.LoopType.LOOP));

    private final String name;
    private final RawAnimation animation;

    ChickenAnimations(String name, RawAnimation animation) {
        this.name = name;
        this.animation = animation;
    }
    public String getName() {
        return name;
    }
    public RawAnimation getAnimation() {
        return animation;
    }

}
