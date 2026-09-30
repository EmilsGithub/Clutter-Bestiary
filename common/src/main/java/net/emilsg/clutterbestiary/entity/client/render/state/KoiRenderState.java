package net.emilsg.clutterbestiary.entity.client.render.state;

import net.emilsg.clutterbestiary.entity.custom.KoiEntity;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class KoiRenderState extends BestiaryRenderState<KoiEntity> {
    /** RGB tint of the base colour layer, or {@code 0} when the variant has its own texture. */
    public int baseColor;
    @Nullable
    public Identifier primaryPatternTexture;
    public int primaryPatternColor;
    @Nullable
    public Identifier secondaryPatternTexture;
    public int secondaryPatternColor;
}
