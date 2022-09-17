package mc.craig.software.regen.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public abstract class NetworkManager {

    protected final ResourceLocation channelName;
    protected final Map<String, MessageType> toServer = new HashMap<>();
    protected final Map<String, MessageType> toClient = new HashMap<>();

    public NetworkManager(ResourceLocation channelName) {
        this.channelName = channelName;
    }

    @ExpectPlatform
    public static NetworkManager create(ResourceLocation channelName) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Packet<?> spawnPacket(Entity livingEntity) {
        throw new RuntimeException("This isn't where you get the packet! tut tut!");
    }

    public MessageType registerS2C(String id, MessageDecoder<MessageS2C> decoder) {
        var msgType = new MessageType(id, this, decoder, false);
        this.toClient.put(id, msgType);
        return msgType;
    }

    public MessageType registerC2S(String id, MessageDecoder<MessageC2S> decoder) {
        var msgType = new MessageType(id, this, decoder, true);
        this.toServer.put(id, msgType);
        return msgType;
    }

    public abstract void sendToServer(MessageC2S message);

    public abstract void sendToPlayer(ServerPlayer player, MessageS2C message);

    public void sendToDimension(Level level, MessageS2C message) {
        if (!level.isClientSide) {
            for (Player player : level.players()) {
                this.sendToPlayer((ServerPlayer) player, message);
            }
        }
    }


    @ExpectPlatform
    public static Packet<?> spawnPacket(Entity livingEntity) {
        throw new RuntimeException("This isn't where you get the packet! tut tut!");
    }

    @FunctionalInterface
    public interface MessageDecoder<T extends Message> {

        T decode(FriendlyByteBuf buf);

    }

}