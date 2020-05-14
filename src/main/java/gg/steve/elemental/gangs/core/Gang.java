package gg.steve.elemental.gangs.core;

import gg.steve.elemental.gangs.managers.Files;
import gg.steve.elemental.gangs.player.GangPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Gang {
    private UUID gangId, ownerId;
    private String name, founded;
    private double bank;
    private GangFileUtil data;
    private List<UUID> members;

    public Gang(UUID gangId, UUID ownerId, String name) {
        this.gangId = gangId;
        this.ownerId = ownerId;
        this.name = name;
        this.bank = 0;
        this.data = new GangFileUtil(String.valueOf(gangId), name, String.valueOf(ownerId));
        this.founded = this.data.get().getString("founded");
        this.members = new ArrayList<>();
        this.members.add(ownerId);
    }

    public Gang(UUID gangId) {
        this.gangId = gangId;
        this.data = new GangFileUtil(String.valueOf(gangId));
        this.ownerId = UUID.fromString(this.data.get().getString("ownerId"));
        this.name = this.data.get().getString("name");
        this.founded = this.data.get().getString("founded");
        this.bank = this.data.get().getDouble("bank");
        this.members = new ArrayList<>();
        for (String memberId : this.data.get().getStringList("gang-members")) {
            this.members.add(UUID.fromString(memberId));
        }
    }

    public boolean isMember(GangPlayer player) {
        return false;
    }

    public List<UUID> getMembers() {
        return this.members;
    }

    public boolean addMember(GangPlayer player) {
        return false;
    }

    public boolean removeMember(GangPlayer player) {
        return false;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(this.ownerId);
    }

    public List<UUID> getOnlineMembers() {
        List<UUID> online = new ArrayList<>();
        for (UUID uuid : this.members) {
            if (Bukkit.getPlayer(uuid) != null) online.add(uuid);
        }
        return online;
    }

    public List<UUID> getOfflineMembers() {
        List<UUID> offline = new ArrayList<>();
        for (UUID uuid : this.members) {
            if (Bukkit.getPlayer(uuid) == null) offline.add(uuid);
        }
        return offline;
    }

    public String getNumberOnline() {
        return String.valueOf(getOnlineMembers().size());
    }

    public String getOnlineMembersAsString() {
        List<UUID> players = getOnlineMembers();
        String online = "";
        int count = 0;
        for (UUID uuid : players) {
            online += (Files.CONFIG.get().getString("who-cmd.online-format.name-color") + Bukkit.getPlayer(uuid).getName());
            if (count != players.size() - 1) {
                online += Files.CONFIG.get().getString("who-cmd.online-format.separator");
            }
            count++;
        }
        return online;
    }

    public String getNumberOffline() {
        return String.valueOf(getOfflineMembers().size());
    }

    public String getOfflineMembersAsString() {
        List<UUID> players = getOfflineMembers();
        String online = "";
        int count = 0;
        for (UUID uuid : players) {
            online += (Files.CONFIG.get().getString("who-cmd.offline-format.name-color") + Bukkit.getOfflinePlayer(uuid).getName());
            if (count != players.size() - 1) {
                online += Files.CONFIG.get().getString("who-cmd.offline-format.separator");
            }
            count++;
        }
        return online;
    }

    public void incrementBank(double increase) {
        this.bank += increase;
        this.data.get().set("bank", this.bank);
        this.data.save();
    }

    public void messageOnlineMembers(String message) {
        for (UUID memberId : getOnlineMembers()) {
            Player member = Bukkit.getPlayer(memberId);
            if (member == null) return;
            member.sendMessage(message);
        }
    }

    public String getTotalMembers() {
        return String.valueOf(this.members.size());
    }

    public GangFileUtil getData() {
        return data;
    }

    public UUID getGangId() {
        return gangId;
    }

    public String getName() {
        return name;
    }

    public String getFounded() {
        return founded;
    }

    public double getBank() {
        return bank;
    }
}
