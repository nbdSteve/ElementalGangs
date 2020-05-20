package gg.steve.elemental.gangs.role;

import gg.steve.elemental.gangs.managers.Files;
import gg.steve.elemental.gangs.permission.PermissionNode;

public enum Role {
    LEADER(5, Files.CONFIG.get().getString("role-prefix.leader")),
    CO_LEADER(4, Files.CONFIG.get().getString("role-prefix.co-leader")),
    MODERATOR(3, Files.CONFIG.get().getString("role-prefix.moderator")),
    MEMBER(2, Files.CONFIG.get().getString("role-prefix.member")),
    NO_GANG(1, "");

    private final int weight;
    private final String prefix;

    Role(int weight, String prefix) {
        this.weight = weight;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getWeight() {
        return this.weight;
    }

    public static boolean higherRole(Role role1, Role role2) {
        return role1.getWeight() >= role2.getWeight();
    }

    public static Role getRoleByWeight(int weight) {
        for (Role role : Role.values()) {
            if (role.getWeight() == weight) return role;
        }
        return NO_GANG;
    }

    public static boolean noGangHasPermission(PermissionNode node) {
        return Files.CONFIG.get().getStringList("default-gang-permissions.no-gang").contains(node.get());
    }
}
