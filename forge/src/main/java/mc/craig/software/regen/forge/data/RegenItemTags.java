package mc.craig.software.regen.forge.data;

import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.util.constants.RConstants;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

/* Created by Craig on 17/03/2021 */
public class RegenItemTags extends ItemTagsProvider {

    public RegenItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagsProvider, RConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        add(RegenUtil.TIMELORD_CURRENCY, Items.GOLD_INGOT, Items.BONE, Items.EMERALD, RItems.ZINC.get(), Items.IRON_INGOT);
        add(RegenUtil.ZINC_INGOT, RItems.ZINC.get());
    }

    public void add(TagKey<Item> branch, Item item) {
        this.tag(branch).add(item);
    }

    public void add(TagKey<Item> branch, Item... item) {
        this.tag(branch).add(item);
    }
}