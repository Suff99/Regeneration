package mc.craig.software.regen.util;

import net.minecraft.world.damagesource.DamageSource;

public class RegenSources extends DamageSource {

    public static DamageSource REGEN_DMG_ENERGY_EXPLOSION = new RegenSources("regeneration_blast").bypassArmor(),
            REGEN_DMG_CRITICAL = new RegenSources("critical_period").bypassInvul().bypassArmor(),
            REGEN_DMG_KILLED = new RegenSources("mid_regeneration").bypassInvul().bypassArmor(),
            REGEN_DMG_FORCED = new RegenSources("forced_regeneration").bypassInvul().bypassArmor(),
            REGEN_DMG_RIFLE = new RegenSources("rifle_shot").bypassArmor(),
            REGEN_DMG_HAND = new RegenSources("severed_arm").bypassInvul().bypassArmor(),
            REGEN_DMG_STASER = new RegenSources("staser_shot");


    @Override
    public DamageSource bypassArmor() {
        return super.bypassArmor();
    }

    public RegenSources(String damageTypeIn) {
        super(damageTypeIn);
    }
}
