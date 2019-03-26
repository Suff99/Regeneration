package me.suff.regeneration.common.types;

import me.suff.regeneration.RegenConfig;
import me.suff.regeneration.client.rendering.TypeFieryRenderer;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.util.PlayerUtil;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class TypeFiery implements IRegenType<TypeFieryRenderer> {
	private long animationTicks;
	
	@Override
	public void onUpdateMidRegen(EntityPlayer player, LazyOptional<IRegeneration> capability) {
		animationTicks++;
		
		player.extinguish();
		
		if (!player.world.isRemote) {
			PlayerUtil.setPerspective((EntityPlayerMP) player, true);
		}
		
		if (player.world.isRemote)
			return;
		
		if (player.world.getBlockState(player.getPosition()).getBlock() instanceof BlockFire)
			player.world.removeBlock(player.getPosition());
		
		double x = player.posX + player.getRNG().nextGaussian() * 2;
		double y = player.posY + 0.5 + player.getRNG().nextGaussian() * 2;
		double z = player.posZ + player.getRNG().nextGaussian() * 2;
		player.world.newExplosion(player, x, y, z, 0.1F, RegenConfig.COMMON.fieryRegen.get(), false);
		
		for (BlockPos bs : BlockPos.getAllInBox(player.getPosition().north().west(), player.getPosition().south().east())) {
			if (player.world.getBlockState(bs).getBlock() instanceof BlockFire) {
				player.world.removeBlock(bs);
			}
		}
	}
	
	@Override
	public void onFinishRegeneration(EntityPlayer player, LazyOptional<IRegeneration> capability) {
		PlayerUtil.setPerspective((EntityPlayerMP) player, false);
		animationTicks = 0;
	}
	
	@Override
	public int getAnimationLength() {
		return 14 * 20; // 14 seconds of 20 ticks
	}
	
	@Override
	public double getAnimationProgress() {
		return Math.min(1, animationTicks / (double) getAnimationLength());
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = IRegenType.super.serializeNBT();
		nbt.setLong("animationTicks", animationTicks);
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		IRegenType.super.deserializeNBT(nbt);
		
		long nbtTicks = nbt.getLong("animationTicks");
		if (nbtTicks > animationTicks)
			animationTicks = nbtTicks;
	}
	
	@Override
	public TypeFieryRenderer getRenderer() {
		return TypeFieryRenderer.INSTANCE;
	}
	
}
