package me.suff.mc.regen.client.rendering.tiles;

import com.mojang.blaze3d.platform.GlStateManager;
import me.suff.mc.regen.client.rendering.model.HandModel;
import me.suff.mc.regen.client.skinhandling.SkinManipulation;
import me.suff.mc.regen.common.item.HandItem;
import me.suff.mc.regen.common.tiles.HandInJarTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

/**
 * Created by Craig on 22/08/2019 @ 16:30
 */
public class HandTileRenderer extends TileEntityRenderer<HandInJarTile> {

    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
    public static HashMap<HandInJarTile, ResourceLocation> TEXTURES = new HashMap<>();
    public static EntityModel STEVE_ARM = new HandModel(false);
    public static EntityModel ALEX_ARM = new HandModel(true);

    @Override
    public void render(HandInJarTile tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);

        if (tileEntityIn.hasHand()) {
            String handType = HandItem.getSkinType(tileEntityIn.getHand());
            Minecraft.getInstance().getTextureManager().bind(getOrCreateTexture(tileEntityIn));
            GlStateManager.rotatef(180, 1, 0, 0);
            GlStateManager.translated(0.5, -1.5, -0.5);

            if (handType.equals("ALEX")) {
                ALEX_ARM.render(null, 0, 0, 0, 0, 0, 0.0625F);
            } else {
                STEVE_ARM.render(null, 0, 0, 0, 0, 0, 0.0625F);
            }
        } else {
            TEXTURES.remove(tileEntityIn);
        }

        GlStateManager.popMatrix();
    }

    public ResourceLocation getOrCreateTexture(HandInJarTile tileEntityHandInJar) {

        if (HandItem.getTextureString(tileEntityHandInJar.getHand()).equalsIgnoreCase("NONE")) {
            boolean isAlex = HandItem.getSkinType(tileEntityHandInJar.getHand()).equalsIgnoreCase("ALEX");
            return isAlex ? TEXTURE_ALEX : TEXTURE_STEVE;
        }

        if (!TEXTURES.containsKey(tileEntityHandInJar)) {
            NativeImage image = SkinManipulation.decodeToImage(HandItem.getTextureString(tileEntityHandInJar.getHand()));
            ResourceLocation res = Minecraft.getInstance().getTextureManager().register("hand_", new DynamicTexture(image));
            TEXTURES.put(tileEntityHandInJar, res);
            return res;
        }
        return TEXTURES.get(tileEntityHandInJar);
    }

}