package net.emilsg.clutterbestiary.network;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.emilsg.clutterbestiary.ClutterBestiary;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Makes sure a joining client runs the same Clutter: Bestiary version as the server.
 * The server sends its version, the client answers with its own, and the server disconnects the player on a mismatch.
 * Clients without the handshake channel (no mod, or a version from before the handshake existed) are disconnected right away.
 * Messages are literal text, since an older client would not have the translation keys.
 */
public final class VersionHandshake {
    private static final String MOD_NAME = "Clutter: Bestiary";
    private static final Logger LOGGER = LogManager.getLogger("Clutter: Bestiary Handshake");

    private VersionHandshake() {
    }

    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, ClientVersionPayload.ID, ClientVersionPayload.CODEC, (payload, context) ->
                context.queue(() -> {
                    if (context.getPlayer() instanceof ServerPlayer player) checkClientVersion(player, payload.version());
                })
        );

        if (Platform.getEnvironment() == Env.CLIENT) {
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, ServerVersionPayload.ID, ServerVersionPayload.CODEC, (payload, context) ->
                    context.queue(() -> NetworkManager.sendToServer(new ClientVersionPayload(getModVersion())))
            );
        } else {
            NetworkManager.registerS2CPayloadType(ServerVersionPayload.ID, ServerVersionPayload.CODEC);
        }

        PlayerEvent.PLAYER_JOIN.register(VersionHandshake::onPlayerJoin);
    }

    public static String getModVersion() {
        String version = Platform.getMod(ClutterBestiary.MOD_ID).getVersion();
        int loaderSuffix = version.indexOf('-');
        return loaderSuffix == -1 ? version : version.substring(0, loaderSuffix);
    }

    private static void onPlayerJoin(ServerPlayer player) {
        if (!NetworkManager.canPlayerReceive(player, ServerVersionPayload.ID)) {
            disconnect(player, "This server requires " + MOD_NAME + " " + getModVersion() + ".\nYou are missing the mod or running an older version.");
            return;
        }

        NetworkManager.sendToPlayer(player, new ServerVersionPayload(getModVersion()));
    }

    private static void checkClientVersion(ServerPlayer player, String clientVersion) {
        String serverVersion = getModVersion();
        if (serverVersion.equals(clientVersion)) return;

        LOGGER.info("Disconnecting {}: {} version mismatch (server {}, client {})", player.getName().getString(), MOD_NAME, serverVersion, clientVersion);
        disconnect(player, MOD_NAME + " version mismatch.\nServer: " + serverVersion + "\nYou: " + clientVersion);
    }

    private static void disconnect(ServerPlayer player, String message) {
        player.connection.disconnect(Component.literal(message).withStyle(ChatFormatting.RED));
    }

    public record ServerVersionPayload(String version) implements CustomPacketPayload {
        public static final Type<ServerVersionPayload> ID = new Type<>(Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "server_version"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ServerVersionPayload> CODEC = ByteBufCodecs.STRING_UTF8.map(ServerVersionPayload::new, ServerVersionPayload::version).cast();

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }

    public record ClientVersionPayload(String version) implements CustomPacketPayload {
        public static final Type<ClientVersionPayload> ID = new Type<>(Identifier.fromNamespaceAndPath(ClutterBestiary.MOD_ID, "client_version"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ClientVersionPayload> CODEC = ByteBufCodecs.STRING_UTF8.map(ClientVersionPayload::new, ClientVersionPayload::version).cast();

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return ID;
        }
    }
}
