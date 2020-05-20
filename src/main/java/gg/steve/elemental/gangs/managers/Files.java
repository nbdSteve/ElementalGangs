package gg.steve.elemental.gangs.managers;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public enum Files {
    CONFIG("gangs.yml"),
    PERMISSIONS("permissions.yml"),
    DEBUG("lang" + File.separator + "debug.yml"),
    MESSAGES("lang" + File.separator + "messages.yml"),
    TOP_GUI("gui" + File.separator + "top.yml"),
    PERMS_GUI("gui" + File.separator + "perms.yml"),
    PERMS_PAGE_GUI("gui" + File.separator + "perms-page.yml"),
    PENDING("gang-data" + File.separator + "pending-requests.yml");

    private final String path;

    Files(String path) {
        this.path = path;
    }

    public void load(FileManager fileManager) {
        fileManager.add(name(), this.path);
    }

    public YamlConfiguration get() {
        return FileManager.get(name());
    }

    public void save() {
        FileManager.save(name());
    }

    public static void reload() {
        FileManager.reload();
    }
}
