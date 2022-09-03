package mc.craig.software.regen.common.regeneration.fabric;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import mc.craig.software.regen.Regeneration;
import net.minecraft.resources.ResourceLocation;

public class RegenerationComponents implements EntityComponentInitializer  {

    public static final ComponentKey<RegenerationDataImpl> REGENERATION_DATA =
            ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(Regeneration.MOD_ID, "regeneration_data"), RegenerationDataImpl.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(REGENERATION_DATA, RegenerationDataImpl::new, RespawnCopyStrategy.CHARACTER);
    }
}