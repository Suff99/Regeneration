package me.suff.regeneration.util;

import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import me.suff.regeneration.network.MessageUpdateSkin;
import me.suff.regeneration.network.NetworkHandler;
import me.suff.regeneration.util.client.MovingSoundBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public class ClientUtil {
	
	public static String keyBind = "???";
	
	public static void createToast(TextComponentTranslation title, TextComponentTranslation subtitle) {
		Minecraft.getInstance().getToastGui().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, title, subtitle));
	}
	
	public static void playPositionedSoundRecord(SoundEvent sound, float pitch, float volume) {
		Minecraft.getInstance().getSoundHandler().play(SimpleSound.getRecord(sound, pitch, volume));
	}
	
	
	public static void sendSkinResetPacket() {
		NetworkHandler.sendToServer(new MessageUpdateSkin("none", SkinChangingHandler.wasAlex(Minecraft.getInstance().player)));
	}
	
	@OnlyIn(Dist.CLIENT)
	public static void playSound(Entity entity, ResourceLocation soundName, SoundCategory category, boolean repeat, Supplier<Boolean> stopCondition, float volume) {
		if (entity.world.isRemote) {
			Minecraft.getInstance().getSoundHandler().play(new MovingSoundBase(entity, new SoundEvent(soundName), category, repeat, stopCondition, volume));
		}
	}
	
	
}
