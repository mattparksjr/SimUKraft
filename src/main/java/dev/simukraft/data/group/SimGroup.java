package dev.simukraft.data.group;

import dev.simukraft.SimUKraft;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimGroup {

    private UUID uuid;
    private String name;

    private double money;

    private int numSims;

    private List<UUID> simIDS;

    private List<UUID> playerIDS;

    private UUID owner;


    public SimGroup(String name, UUID owner) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.money = 10.00d;
        this.numSims = 0;
        this.simIDS = new ArrayList<>();
        this.playerIDS = new ArrayList<>();
        this.owner = owner;
    }

    public SimGroup() {

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
            tag.putUUID("c." + id, player);
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
