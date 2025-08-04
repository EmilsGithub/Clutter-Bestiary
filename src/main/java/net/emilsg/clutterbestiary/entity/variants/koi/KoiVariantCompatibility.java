package net.emilsg.clutterbestiary.entity.variants.koi;

public class KoiVariantCompatibility {

    public static boolean isValid(KoiBaseColorVariant base, KoiPrimaryPatternColorVariant primaryColor, KoiPrimaryPatternTypeVariant primaryType, KoiSecondaryPatternColorVariant secondaryColor, KoiSecondaryPatternTypeVariant secondaryType) {

        if (base.getID().equals(primaryColor.getID())) {
            return false;
        }

        if (primaryColor.getID().equals(secondaryColor.getID())) {
            return false;
        }

        if (base.equals(KoiBaseColorVariant.ORANGE) && (primaryColor.equals(KoiPrimaryPatternColorVariant.RED) || primaryColor.equals(KoiPrimaryPatternColorVariant.YELLOW))) {
            return false;
        }

        return true;
    }
}
