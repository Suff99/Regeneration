package me.swirtzly.regen.data;

import me.swirtzly.regen.common.objects.RItems;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class EnglishLang extends LanguageProvider {

    public EnglishLang(DataGenerator gen) {
        super(gen, RConstants.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // === Damages Sources ===
        add("source.regen.regen_energy", "%s was blasted by Regeneration Energy!");
        add("source.regen.regen_heal", "%s died by donating too much Regeneration energy...");
        add("source.regen.regen_crit", "%s died from holding in their regeneration for too long");
        add("source.regen.theft", "%s had their body stolen!");
        add("source.regen.lindos", "%s consumed lindos hormones! They are reborn!");
        add("source.regen.regen_killed", "%s was killed mid-regeneration...");

        //Sounds
        for (int i = 0; i < 7; i++) {
            add("regen.sound.regeneration_" + i, "Regeneration");
        }
        add("regen.subtitles.regen_bubble", "Jar bubbles");
        add("regen.subtitles.hum", "Calming hums");
        add("regen.subtitles.fob_watch_dialogue", "Fob Watch Speaks");
        add("regen.subtitles.critical_stage", "Critical Condition");
        add("regen.subtitles.heart_beat", "Timelord Heartbeat");
        add("regen.subtitles.hand_glow", "Glowing Hands");
        add("regen.subtitles.fob_watch", "Ticking");
        add("regen.subtitles.regen_breath", "Regeneration exhale");
        add("regen.subtitles.alarm", "Regeneration Alarm");

        //Items
        add(RItems.FOB.get(), "Fob watch");

        //Messages
        add("regeneration.messages.regen_death_msg", "%s is regenerating...");
        add("regeneration.messages.now_timelord", "You are now a Time lord!");
        add("regeneration.messages.gained_regens", "Recieved Regenerations!");
        add("regeneration.messages.new_skin", "Your skin will change next Regeneration!");
        add("regeneration.messages.transfer.success", "You've transferred one of your regenerations into the watch");
        add("regeneration.messages.transfer.full_watch", "You can't store anymore regenerations in this watch!");
        add("regeneration.messages.transfer.empty_watch", "This watch is empty!");
        add("regeneration.messages.transfer.max_regens", "You already have the maximum number of regenerations");
        add("regeneration.messages.transfer.no_regens", "You don't have any regenerations left to transfer");
        add("regeneration.messages.jar_amount", "This container currently contains %s Lindos!");
        add("regeneration.messages.jar_inuse", "This is in use!");
        add("regeneration.messages.regen_chat_message", "%s is regenerating...");
        add("regeneration.messages.warning.grace", "You are in a state of grace, press %s to regenerate!");
        add("regeneration.messages.warning.grace_critical", "You are near death, press %s to regenerate!");
        add("regeneration.messages.regen_delayed", "Regeneration delayed!");
        add("regeneration.messages.regen_warning", "Punch a block to delay your regeneration further");
        add("regeneration.messages.jar", "You have gained a Regeneration from harvested lindos!");
        add("regeneration.messages.jar_not_enough", "There is not enough Lindos in this jar! [100 Lindos = 1 free Regeneration]");
        add("regeneration.messages.jar_no_break", "You cannot break this Jar while it has %s Lindos Energy!");
        add("regeneration.messages.not_alive", "You cannot transfer regens in this state!");
        add("regeneration.messages.item_taken_regens", "You have gained %s regenerations from %s");
        add("regeneration.messages.cannot_use", "You cannot use this right now!");
        add("regeneration.messages.healed", "You have given %s some of your Regeneration Energy!");
        add("regeneration.messages.reduced_dmg", "Reduced Damage due to Post Regen State!");
        add("regeneration.messages.fall_dmg", "Fall damage reduced due to post regen state! Although you're gonna feel sick..");
        add("regeneration.messages.post_ended", "Your Regeneration process is now entirely completed!");
        add("regeneration.messages.fast_forward_cmd_fail", "Nothing to fast forward to.");
        add("regeneration.messages.cant_glow", "You cannot activate a glowing hand without being in a grace period");
    }
}
