package net.leo.Skytools.event;

import net.leo.Skytools.reader.ScoreboardReader;
import net.leo.Skytools.reader.TablistReader;
import net.leo.Skytools.util.GameState;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class onLoginEvent {
    private static final int MAX_RETRIES = 10;
    private static int retries = 0;
    private static final ScheduledExecutorService tablistScheduler = Executors.newSingleThreadScheduledExecutor();
    public static boolean tablistSchedulerStarted = false;

    @SubscribeEvent
    public static void onServerSwitch(ClientPlayerNetworkEvent.LoggingIn event) {
        GameState.isInSkyblock = false;
        GameState.resetKeys();
        GameState.playerDisplayMap.clear();
        retries = 0;

        load();

        if (!tablistSchedulerStarted) {
            startTablistMonitor(); // Start the tablist monitor thread
            tablistSchedulerStarted = true;
        }
    }

    private static void load() {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Wait 1 second (20 ticks)
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            String title = ScoreboardReader.getTitle();
            if (title.isEmpty()) {
                if (retries < MAX_RETRIES) {
                    retries++;
                    load(); // Retry
                } else {
                    System.out.println("[Skytools] Scoreboard not found after retries.");
                }
            } else {
                if (ScoreboardReader.isInSkyblock()) {
                    GameState.isInSkyblock = true;
                }
                System.out.println("[Skytools] Scoreboard loaded: " + title);
            }
        }).start();
    }

    public static void startTablistMonitor() {
        tablistScheduler.scheduleAtFixedRate(() -> {
            if (!GameState.isInSkyblock) {
                tablistSchedulerStarted = false;
                return;
            }

            Map<String, String> map = TablistReader.getPlayerTabMap();
            if (map.isEmpty()) {
                retries++;
                load();
                return;
            }
            GameState.updatePlayerTabMap(map);

        }, 0, 1, TimeUnit.SECONDS); // every 1 second
    }
}
