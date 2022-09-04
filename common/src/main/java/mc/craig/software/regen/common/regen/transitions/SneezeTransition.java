package mc.craig.software.regen.common.regen.transitions;

import mc.craig.software.regen.common.objects.RSounds;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.network.NetworkDispatcher;
import mc.craig.software.regen.network.messages.POVMessage;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class SneezeTransition extends TransitionType {

    @Override
    public int getAnimationLength() {
        return 50;
    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{RSounds.HAND_GLOW.get()};
    }


    @Override
    public Vec3 getDefaultPrimaryColor() {
        return new Vec3(0.93F, 0.61F, 0F);
    }

    @Override
    public Vec3 getDefaultSecondaryColor() {
        return new Vec3(1F, 0.5F, 0.18F);
    }

    @Override
    public void onFinishRegeneration(IRegen cap) {
        LivingEntity living = cap.getLiving();
        if (living instanceof ServerPlayer) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) cap.getLiving()), new POVMessage(RConstants.FIRST_PERSON));
        }
    }

    @Override
    public void onStartRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayer) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) cap.getLiving()), new POVMessage(RConstants.THIRD_PERSON_FRONT));
        }
    }

}
