package mc.craig.software.regen.util;

import com.mojang.math.Vector3f;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;

import java.util.List;
import java.util.Map;

public class AnimationUtil {

    public static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    public static ModelPart getAnyDescendantWithName(HumanoidModel<?> model, String partName) {
        return switch (partName) {
            case "RightLeg" -> model.rightLeg;
            case "LeftLeg" -> model.leftLeg;
            case "LeftArm" -> model.leftArm;
            case "RightArm" -> model.rightArm;
            case "Body" -> model.body;
            case "Head" -> model.head;
            default -> null;
        };
    }

    public static void animate(HumanoidModel<?> humanoidModel, AnimationDefinition animationDefinition, long p_232322_, float p_232323_, Vector3f vector3f) {
        float elapsedSeconds = getElapsedSeconds(animationDefinition, p_232322_);
        for (Map.Entry<String, List<AnimationChannel>> entry : animationDefinition.boneAnimations().entrySet()) {
            ModelPart optional = getAnyDescendantWithName(humanoidModel, entry.getKey());

            List<AnimationChannel> list = entry.getValue();

            if (optional != null) {
                list.forEach((p_232311_) -> {
                    Keyframe[] akeyframe = p_232311_.keyframes();
                    int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, (p_232315_) -> elapsedSeconds <= akeyframe[p_232315_].timestamp()) - 1);
                    int j = Math.min(akeyframe.length - 1, i + 1);
                    Keyframe keyframe = akeyframe[i];
                    Keyframe keyframe1 = akeyframe[j];
                    float f1 = elapsedSeconds - keyframe.timestamp();
                    float f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
                    keyframe1.interpolation().apply(vector3f, f2, akeyframe, i, j, p_232323_);
                    p_232311_.target().apply(optional, vector3f);
                });
            } else {
                System.out.println("Could not find:" + entry.getKey());
            }
        }
    }

    private static float getElapsedSeconds(AnimationDefinition animationDefinition, long p_232318_) {
        float f = (float) p_232318_ / 1000.0F;
        return animationDefinition.looping() ? f % animationDefinition.lengthInSeconds() : f;
    }

    public static void animate(HumanoidModel<?> model, AnimationState animationState, AnimationDefinition animationDefinition, float p_233388_, float p_233389_) {
        animationState.updateTime(p_233388_, p_233389_);
        animationState.ifStarted((p_233392_) -> {
            animate(model, animationDefinition, p_233392_.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
        });
    }

}