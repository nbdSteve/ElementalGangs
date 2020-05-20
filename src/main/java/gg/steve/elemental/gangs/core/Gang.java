package gg.steve.elemental.gangs.core;

import gg.steve.elemental.gangs.managers.Files;
import gg.steve.elemental.gangs.permission.PermissionGui;
import gg.steve.elemental.gangs.permission.PermissionNode;
import gg.steve.elemental.gangs.permission.PermissionPageGui;
import gg.steve.elemental.gangs.player.GangPlayerManager;
import gg.steve.elemental.gangs.role.Role;
import gg.steve.elemental.gangs.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class Gang {
    private UUID gangId, ownerId;
    private String name, founded;
    private double bank;
    private GangFileUtil data;
    private Map<UUID, Role> playerMap;
    private Map<Role, List<String>> permissionMap;
    private Map<Role, PermissionPageGui> permsGuis;
    private PermissionGui permissionGui;

    public Gang(UUID gangId, UUID ownerId, String name) {
        this.gangId = gangId;
        this.ownerId = ownerId;
        this.name = name;
        this.bank = 0;
        this.data = new GangFileUtil(String.valueOf(gangId), name, String.valueOf(ownerId));
        this.founded = this.data.get().getString("founded");
        this.playerMap = new HashMap<>();
        this.playerMap.put(this.ownerId, Role.LEADER);
        loadPermissionMap();
    }

    public Gang(UUID gangId) {
        this.gangId = gangId;
        this.data = new GangFileUtil(String.valueOf(gangId));
        this.ownerId = UUID.fromString(this.data.get().getString("owner-id"));
        this.name = this.data.get().getString("name");
        this.founded = this.data.get().getString("founded");
        this.bank = this.data.get().getDouble("bank");
        this.playerMap = new HashMap<>();
        for (String entry : this.data.get().getStringList("gang-members")) {
            String[] player = entry.split(":");
            this.playerMap.put(UUID.fromString(player[0]), Role.valueOf(player[1]));
        }
        loadPermissionMap();
    }

    public void disband() {
        LogUtil.info("Disbanded the gang, " + this.name + ", with the id: " + this.gangId + ".");
        this.data.delete();
    }

    public void loadPermissionMap() {
        if (permissionMap == null) permissionMap = new HashMap<>();
        List<String> member = new ArrayList<>();
        for (String node : this.data.get().getStringList("permissions.member")) {
            member.add(node);
        }
        permissionMap.put(Role.MEMBER, member);
        List<String> moderator = new ArrayList<>();
        for (String node : this.data.get().getStringList("permissions.moderator")) {
            moderator.add(node);
        }
        permissionMap.put(Role.MODERATOR, moderator);
        List<String> coOwner = new ArrayList<>();
        for (String node : this.data.get().getStringList("permissions.co_leader")) {
            coOwner.add(node);
        }
        permissionMap.put(Role.CO_LEADER, coOwner);
        List<String> owner = new ArrayList<>();
        for (String node : this.data.get().getStringList("permissions.leader")) {
            owner.add(node);
        }
        permissionMap.put(Role.LEADER, owner);
    }

    public void removePermission(Role role, PermissionNode node) {
        this.permissionMap.get(role).remove(node.get());
    }

    public void addPermission(Role role, PermissionNode node) {
        this.permissionMap.get(role).add(node.get());
    }

    public boolean promote(UUID playerId) {
        Role role = playerMap.get(playerId);
        switch (role) {
            case CO_LEADER:
                UUID leader = this.ownerId;
                playerMap.put(leader, Role.CO_LEADER);
                this.ownerId = playerId;
                playerMap.put(playerId, Role.LEADER);
                GangPlayerManager.updateGangPlayer(leader);
                break;
            case MODERATOR:
                playerMap.put(playerId, Role.CO_LEADER);
                break;
            case MEMBER:
                playerMap.put(playerId, Role.MODERATOR);
                break;
            default:
                return false;
        }
        GangPlayerManager.updateGangPlayer(playerId);
        return true;
    }

    public boolean demote(UUID playerId) {
        Role role = playerMap.get(playerId);
        switch (role) {
            case CO_LEADER:
                playerMap.put(playerId, Role.MODERATOR);
                break;
            case MODERATOR:
                playerMap.put(playerId, Role.MEMBER);
                break;
            default:
                return false;
        }
        GangPlayerManager.updateGangPlayer(playerId);
        return true;
    }

    public boolean isMember(UUID playerId) {
        if (this.playerMap == null) return false;
        return this.playerMap.containsKey(playerId);
    }

    public List<UUID> getPlayers() {
        return new ArrayList<>(this.playerMap.keySet());
    }

    public boolean addPlayer(UUID playerId, Role role) {
        if (this.playerMap == null) this.playerMap = new HashMap<>();
        if (this.playerMap.containsKey(playerId)) return false;
        this.playerMap.put(playerId, role);
        return true;
    }

    public boolean removePlayer(UUID playerId) {
        if (this.playerMap == null) return false;
        if (!this.playerMap.containsKey(playerId)) return false;
        this.playerMap.remove(playerId);
        return true;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(this.ownerId);
    }

    public List<UUID> getOnlinePlayers() {
        List<UUID> online = new ArrayList<>();
        for (UUID uuid : this.getPlayers()) {
            if (Bukkit.getPlayer(uuid) != null) online.add(uuid);
        }
        return online;
    }

    public List<UUID> getOfflinePlayers() {
        List<UUID> offline = new ArrayList<>();
        for (UUID uuid : this.getPlayers()) {
            if (Bukkit.getPlayer(uuid) == null) offline.add(uuid);
        }
        return offline;
    }

    public String getNumberOnline() {
        return String.valueOf(getOnlinePlayers().size());
    }

    public String getOnlinePlayersAsString() {
        List<UUID> players = getOnlinePlayers();
        String online = "";
        int count = 0;
        for (UUID uuid : players) {
            online += (Files.CONFIG.get().getString("who-cmd.online-format.name-color") + getRoleForPlayer(uuid).getPrefix() + Bukkit.getPlayer(uuid).getName());
            if (count != players.size() - 1) {
                online += Files.CONFIG.get().getString("who-cmd.online-format.separator");
            }
            count++;
        }
        return online;
    }

    public String getNumberOffline() {
        return String.valueOf(getOfflinePlayers().size());
    }

    public String getOfflinePlayersAsString() {
        List<UUID> players = getOfflinePlayers();
        String online = "";
        int count = 0;
        for (UUID uuid : players) {
            online += (Files.CONFIG.get().getString("who-cmd.offline-format.name-color") + getRoleForPlayer(uuid).getPrefix() + Bukkit.getOfflinePlayer(uuid).getName());
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

    public List<String> getPlayersAsStringList() {
        List<String> members = new ArrayList<>();
        for (UUID memberId : this.getPlayers()) {
            members.add(memberId + ":" + this.playerMap.get(memberId).name());
        }
        return members;
    }

    public void messageOnlinePlayers(String message) {
        for (UUID memberId : getOnlinePlayers()) {
            Player member = Bukkit.getPlayer(memberId);
            if (member == null) return;
            member.sendMessage(message);
        }
    }

    public List<String> getPermissionsForRole(Role role) {
        return permissionMap.get(role);
    }

    public boolean roleHasPermission(Role role, PermissionNode node) {
        return this.permissionMap.get(role).contains(node.get());
    }

    public void saveToFile() {
        YamlConfiguration conf = this.data.get();
        conf.set("owner-id", String.valueOf(this.ownerId));
        conf.set("name", this.name);
        conf.set("bank", this.bank);
        conf.set("gang-members", getPlayersAsStringList());
        conf.set("permissions.leader", permissionMap.get(Role.LEADER));
        conf.set("permissions.co_leader", permissionMap.get(Role.CO_LEADER));
        conf.set("permissions.moderator", permissionMap.get(Role.MODERATOR));
        conf.set("permissions.member", permissionMap.get(Role.MEMBER));
        this.data.save();
    }

    public Role getRoleForPlayer(UUID playerId) {
        if (!this.playerMap.containsKey(playerId)) return Role.NO_GANG;
        return this.playerMap.get(playerId);
    }

    public void openPermissionMenu(Player player) {
        if (this.permissionGui == null) {
            this.permissionGui = new PermissionGui(Files.PERMS_GUI.get(), this);
        } else {
            this.permissionGui.refresh();
        }
        this.permissionGui.open(player);
    }

    public void openPermissionPageGui(Role role, Player player) {
        if (this.permsGuis == null) this.permsGuis = new HashMap<>();
        if (!this.permsGuis.containsKey(role)) {
            this.permsGuis.put(role, new PermissionPageGui(Files.PERMS_PAGE_GUI.get(), role, this));
        } else {
            this.permsGuis.get(role).refresh();
        }
        this.permsGuis.get(role).open(player);
    }

    public void refreshPermissionGui(Role role) {
        if (this.permsGuis == null) this.permsGuis = new HashMap<>();
        if (!this.permsGuis.containsKey(role)) {
            this.permsGuis.put(role, new PermissionPageGui(Files.PERMS_PAGE_GUI.get(), role, this));
        } else {
            this.permsGuis.get(role).refresh();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalPlayers() {
        return String.valueOf(this.getPlayers().size());
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
