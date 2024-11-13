package chromeskullex.chicken.constant;

import software.bernie.geckolib.core.animation.RawAnimation;

public class DefaultAnimations {
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.model.idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.model.walk");
    

}
