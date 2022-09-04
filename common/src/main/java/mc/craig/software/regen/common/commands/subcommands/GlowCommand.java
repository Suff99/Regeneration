package mc.craig.software.regen.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mc.craig.software.regen.common.regen.RegenerationData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class GlowCommand implements Command<CommandSourceStack> {
    private static final GlowCommand CMD = new GlowCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("glow")
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        RegenerationData.get(source.getPlayerOrException()).ifPresent((cap) -> cap.stateManager().fastForwardHandGlow());
        return Command.SINGLE_SUCCESS;
    }
}
