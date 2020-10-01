package me.swirtzly.regen.common.objects;

import me.swirtzly.regen.common.entities.OverrideEntity;
import me.swirtzly.regen.common.item.FobWatchItem;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RItems {

    private static final Item.Properties PROP = new Item.Properties().group(RItems.MAIN);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RConstants.MODID);

    public static RegistryObject<Item> FOB = ITEMS.register("fobwatch", FobWatchItem::new);



    //Item group
    public static ItemGroup MAIN = new ItemGroup("regen") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.PUMPKIN);
        }
    };

}
