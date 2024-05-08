package dev.simukraft.data.group;

import dev.simukraft.SimUKraft;
import dev.simukraft.entities.folk.EntityFolk;
import dev.simukraft.init.ModEntities;
import dev.simukraft.net.ModPackets;
import dev.simukraft.net.packet.GroupUpdateS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public class SimGroup {

    private UUID uuid;
    private String name;
    private double money;
    private int numSims;
    private List<UUID> simIDS;
    private List<UUID> playerIDS;
    private UUID owner;

    private long lastSecond = 0L;
    private long lastMinute = 0L;

    public SimGroup() {
        this.simIDS = new ArrayList<>();
        this.playerIDS = new ArrayList<>();
    }

    public SimGroup(String name, UUID owner) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.money = 10.00d;
        this.numSims = 0;
        this.simIDS = new ArrayList<>();
        this.playerIDS = new ArrayList<>();
        this.playerIDS.add(owner);
        this.owner = owner;
    }

    public void tick(TickEvent.ServerTickEvent event) {
        // 20x a second

        Long now = System.currentTimeMillis();


        if ((now - lastSecond) > 1000) {
            // Once a second

            lastSecond = now;
        }

        if ((now - lastMinute) > 60000) {
            // Once a minute

            if (shouldSpawnNewFolk() && lastMinute > 0L) {
                spawnNewFolk(event.getServer().overworld());
            }

            lastMinute = now;
        }
    }

    public boolean shouldSpawnNewFolk() {
        if (getPlayerIDS().isEmpty()) return false;
        return simIDS.size() <= 0;
        // FIXME calculate
    }

    public void spawnNewFolk(ServerLevel level) {
        BlockPos location = getRandomPlayerLocation(level);
        Random random = new Random();

        int x = (random.nextInt(2) == 1) ? location.getX() + random.nextInt(50) : location.getX() - random.nextInt(25);
        int z = (random.nextInt(2) == 1) ? location.getZ() + random.nextInt(50) : location.getZ() - random.nextInt(25);
        BlockPos pos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, new BlockPos(x, 0, z));

        EntityFolk folk = (EntityFolk) ModEntities.ENTITY_FOLK.get().spawn(level, null, null, pos, MobSpawnType.NATURAL, false, false);

        if (folk == null) {
            // failed to spawn, ret
            SimUKraft.LOGGER.error("SimGroup - Failed to spawn a folk.");
            return;
        }

        getSimIDS().add(folk.getUUID());

        Predicate<ServerPlayer> predicate = (player) -> getPlayerIDS().contains(player.getUUID());

        for (ServerPlayer player : level.getPlayers(predicate)) {
            player.sendSystemMessage(Component.translatable("simukraft.message.folk_spawned", Component.literal(folk.getFullname())));
            ModPackets.sendToPlayer(new GroupUpdateS2CPacket(getName(), getMoney(), getNumSims(), getUuid()), player);
        }
    }

    // TODO: This will not work forever, for obvs reasons, lets use some of the folk data, like where some buildings are in the future
    private BlockPos getRandomPlayerLocation(ServerLevel level) {
        Random random = new Random();
        int player = random.nextInt(playerIDS.size());
        return level.getEntity(playerIDS.get(player)).getOnPos();
    }

    public static SimGroup load(CompoundTag tag) {
        SimUKraft.LOGGER.debug("SimGroup Load() - Loading a group... " + tag.getAsString());
        SimGroup group = new SimGroup();

        group.setName(tag.getString("name"));
        group.setUuid(tag.getUUID("uuid"));
        group.setMoney(tag.getDouble("money"));
        group.setOwner(tag.getUUID("owner"));
        group.setNumSims(tag.getInt("numSims"));

        for (int i = 1; i <= group.getNumSims(); i++) {
            group.getSimIDS().add(tag.getUUID("sim." + i));
        }

        for (int i = 1; i <= tag.getInt("numPlayers"); i++) {
            group.getPlayerIDS().add(tag.getUUID("player." + i));
        }

        return group;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putUUID("uuid", getUuid());
        tag.putString("name", getName());
        tag.putDouble("money", getMoney());
        tag.putUUID("owner", getOwner());

        tag.putInt("numSims", simIDS.size());
        tag.putInt("numPlayers", playerIDS.size());

        int id = 1;

        for (UUID sim : simIDS) {
            tag.putUUID("sim." + id, sim);
            id++;
        }

        id = 1;

        for (UUID player : playerIDS) {
            tag.putUUID("player." + id, player);
            id++;
        }

        return tag;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getNumSims() {
        return numSims;
    }

    public void setNumSims(int numSims) {
        this.numSims = numSims;
    }

    public List<UUID> getSimIDS() {
        return simIDS;
    }

    public void setSimIDS(List<UUID> simIDS) {
        this.simIDS = simIDS;
    }

    public List<UUID> getPlayerIDS() {
        return playerIDS;
    }

    public void setPlayerIDS(List<UUID> playerIDS) {
        this.playerIDS = playerIDS;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }
}
