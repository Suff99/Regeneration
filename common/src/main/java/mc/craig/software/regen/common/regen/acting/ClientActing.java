package mc.craig.software.regen.common.regen.acting;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.client.skin.CommonSkin;
import mc.craig.software.regen.client.skin.SkinHandler;
import mc.craig.software.regen.common.objects.RSounds;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.network.NetworkDispatcher;
import mc.craig.software.regen.network.messages.SkinMessage;
import mc.craig.software.regen.util.ClientUtil;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;

public class ClientActing implements Acting {

    public static final Acting INSTANCE = new ClientActing();

    @Override
    public void onRegenTick(IRegen cap) {
        // never forwarded as per the documentation
    }

    @Override
    public void onEnterGrace(IRegen cap) {
        if (cap.getLiving().getUUID().equals(Minecraft.getInstance().player.getUUID())) {
            SoundEvent ambientSound = cap.getTimelordSound().getSound();
            ClientUtil.playSound(cap.getLiving(), ForgeRegistries.SOUND_EVENTS.getKey(RSounds.HEART_BEAT.get()), SoundSource.PLAYERS, true, () -> !cap.regenState().isGraceful(), 0.2F, cap.getLiving().getRandom());
            ClientUtil.playSound(cap.getLiving(), ForgeRegistries.SOUND_EVENTS.getKey(ambientSound), SoundSource.AMBIENT, true, () -> cap.regenState() != RegenStates.GRACE, 1.5F, cap.getLiving().getRandom());
        }
    }

    @Override
    public void onHandsStartGlowing(IRegen cap) {
        if (cap.getLiving().getType() == EntityType.PLAYER) {
            ClientUtil.playSound(cap.getLiving(), ForgeRegistries.SOUND_EVENTS.getKey(RSounds.HAND_GLOW.get()), SoundSource.PLAYERS, true, () -> !cap.glowing(), 1.0F, cap.getLiving().getRandom());
        }
    }

    @Override
    public void onRegenFinish(IRegen cap) {

    }

    @Override
    public void onPerformingPost(IRegen cap) {

    }

    @Override
    public void onRegenTrigger(IRegen cap) {
        if (Minecraft.getInstance().player.getUUID().equals(cap.getLiving().getUUID())) {

            if (RegenConfig.CLIENT.changeMySkin.get()) {
                if (cap.isNextSkinValid()) {
                    NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new SkinMessage(cap.nextSkin(), cap.isNextSkinTypeAlex()));
                    return;
                }
                Minecraft.getInstance().submitAsync(() -> {
                    if (!cap.isNextSkinValid()) {
                        File file = CommonSkin.chooseRandomSkin(cap.getLiving().getRandom(), cap.preferredModel().isAlex(), false);
                        boolean isAlex = file.getAbsolutePath().contains("\\skins\\alex");
                        Regeneration.LOG.info("Chosen Skin: " + file);
                        NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new SkinMessage(RegenUtil.fileToBytes(file), isAlex));
                    }
                });
            } else {
                SkinHandler.sendResetMessage();
            }
        }
    }

    @Override
    public void onGoCritical(IRegen cap) {
        if (Minecraft.getInstance().player.getUUID().equals(cap.getLiving().getUUID())) {
            if (cap.getLiving().getType() == EntityType.PLAYER) {
                ClientUtil.createToast(Component.translatable("regen.toast.enter_critical"), Component.translatable("regen.toast.enter_critical.sub", RegenConfig.COMMON.criticalPhaseLength.get() / 60));
                ClientUtil.playSound(cap.getLiving(), ForgeRegistries.SOUND_EVENTS.getKey(RSounds.CRITICAL_STAGE.get()), SoundSource.PLAYERS, true, () -> cap.regenState() != RegenStates.GRACE_CRIT, 1.0F, RandomSource.create());
            }
        }
    }

}
