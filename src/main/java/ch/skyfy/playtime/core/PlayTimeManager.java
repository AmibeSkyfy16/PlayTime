package ch.skyfy.playtime.core;

import ch.skyfy.playtime.PlayTime;
import ch.skyfy.playtime.commands.*;
import ch.skyfy.playtime.core.times.*;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class PlayTimeManager {

    private static class PlayTimeManagerHolder {
        public static final PlayTimeManager INSTANCE = new PlayTimeManager();
    }

    public static PlayTimeManager getInstance() {
        return PlayTimeManager.PlayTimeManagerHolder.INSTANCE;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void initializeInstance() {
        getInstance();
    }

    private final File playersFolderFile;

    public final List<PlayerTime> playerTimes;

    public final List<PlayerEntity> playerEntities;

    public AFKTime afkTime = new AFKTime();
    public WalkingTime walkingTime = new WalkingTime();
    public RunningTime runningTime = new RunningTime();
    public FallingTime fallingTime = new FallingTime();
    public FlyingTime flyingTime = new FlyingTime();

    public PlayTimeManager() {
        playersFolderFile = PlayTime.MOD_CONFIG_DIR.resolve("players").toFile();
        playerTimes = new ArrayList<>();
        playerEntities = new ArrayList<>();
        loadPlayerTimes();
        startBackupTimer();
        registerCommands();
        registerEvents();
        initialize();
    }

    private void loadPlayerTimes() {
        var playerTimesFiles = playersFolderFile.listFiles();
        if (playerTimesFiles == null) return;
        for (var playerTimesFile : playerTimesFiles) {
            if (playerTimesFile.exists()) {
                var playerTime = saveOrLoadPlayerTime(null, playerTimesFile);
                if (playerTime != null) playerTimes.add(playerTime);
            }
        }
    }

    private void startBackupTimer() {
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                for (var playerTime : playerTimes)
                    saveOrLoadPlayerTime(playerTime, playersFolderFile.toPath().resolve(playerTime.UUID).toFile());
            }
        }, 500_000, 500_000);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (var playerTime : playerTimes)
                saveOrLoadPlayerTime(playerTime, playersFolderFile.toPath().resolve(playerTime.UUID).toFile());
        }));
    }

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            SneakTimeCmd.initialize(dispatcher);
            AFKTimeCmd.initialize(dispatcher);
            WalkingTimeCmd.initialize(dispatcher);
            RunningTimeCmd.initialize(dispatcher);
            FallingTimeCmd.initialize(dispatcher);
            FlyingTimeCmd.initialize(dispatcher);
        });
    }

    private void registerEvents(){
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if(!playerEntities.contains(handler.player))
                playerEntities.add(handler.player);

        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            // If a player disconnect, we save all pending time
            // Example, if he was afk and decide to disconnect without moving, we have to save the afk time here
            for (var playerTime : playerTimes) {
                if(handler.player.getUuidAsString().equalsIgnoreCase(playerTime.UUID)){
                    PlayTimeManager.getInstance().afkTime.createAndAddElapsedTime(handler.player, TimeBase.SaveOrStartCountTime.SAVE_TIME);
                    PlayTimeManager.getInstance().runningTime.createAndAddElapsedTime(handler.player, TimeBase.SaveOrStartCountTime.SAVE_TIME);
                    saveOrLoadPlayerTime(playerTime, playersFolderFile.toPath().resolve(playerTime.UUID).toFile());
                }
            }
            playerEntities.removeIf(player -> player.equals(handler.player));
        });
    }

    private void initialize() {
        MovementDetection.init();
        new SneakTime().initialize();
    }

    public static @Nullable PlayerTime saveOrLoadPlayerTime(PlayerTime playerTime, File playerTimesFile) {
        if (playerTime == null) { // We have to read
            try (var ois = new ObjectInputStream(new InflaterInputStream(new FileInputStream(playerTimesFile)))) {
                return (PlayerTime) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else { // We have to write
            try (var oos = new ObjectOutputStream(new DeflaterOutputStream(new FileOutputStream(playerTimesFile)))) {
                oos.writeObject(playerTime);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
