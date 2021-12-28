package ch.skyfy.playtime;

import ch.skyfy.playtime.core.PlayTimeManager;
import ch.skyfy.playtime.database.Database;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

// TODO Make a fucking DATABASE
public class PlayTime implements ModInitializer {

    public static Path MOD_CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("PlayTime");

    @Override
    public void onInitialize() {
        if (!createConfig()) {
            System.out.println("PlayTime mod could not create configurations files\nThe mod is now disabled");
            return;
        }
        Database.initializeInstance();
        PlayTimeManager.initializeInstance();
    }

    private boolean createConfig() {
        var configDirectoryFile = MOD_CONFIG_DIR.toFile();
        var playersFolderFile = MOD_CONFIG_DIR.resolve("players").toFile();
        if (!configDirectoryFile.exists())
            if (!configDirectoryFile.mkdir()) return false;

        if (!playersFolderFile.exists())
            return playersFolderFile.mkdir();

        return true;
    }

}
