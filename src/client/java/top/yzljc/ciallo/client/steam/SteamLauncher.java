package top.yzljc.ciallo.client.steam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.yzljc.ciallo.client.config.ConfigManager;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SteamLauncher {
    public static final Logger LOGGER = LoggerFactory.getLogger("ciallo");
    private static long lastLaunchTime = 0;

    public static void launch() {
        if (System.currentTimeMillis() - lastLaunchTime < 5000) {
            return;
        }
        lastLaunchTime = System.currentTimeMillis();

        List<String> games = ConfigManager.get().gameList;

        if (games == null || games.isEmpty()) {
            LOGGER.warn("No game configured in Ciallo settings!");
            return;
        }

        Random random = new Random(System.nanoTime());
        String target = games.get(random.nextInt(games.size()));

        LOGGER.info("Ciallo detected disconnect! Randomly selected: " + target);

        try {
            boolean fullscreen = ConfigManager.get().isFullScreen;
            if (isSteamAppId(target)) {
                launchSteamGame(target, fullscreen);
            } else {
                launchLocalGame(target, fullscreen);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to launch game: " + target, e);
        }
    }

    private static boolean isSteamAppId(String str) {
        return str.matches("\\d+");
    }

    private static void launchSteamGame(String appId, boolean fullscreen) throws IOException {
        String steamUrl = "steam://run/" + appId;
        if (fullscreen) {
            steamUrl += "//-fullscreen";
        }

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(steamUrl));
                return;
            } catch (Exception e) {
                LOGGER.warn("Failed to launch Steam game via Desktop API, falling back to command line: {}", e.getMessage());
            }
        }

        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", steamUrl)
                    .start();
        } else {
            String command = os.contains("mac") ? "open" : "xdg-open";
            new ProcessBuilder(command, steamUrl)
                    .start();
        }
    }

    private static void launchLocalGame(String path, boolean fullscreen) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            LOGGER.error("Game file not found: " + path);
            return;
        }

        List<String> command = new ArrayList<>();
        command.add(file.getAbsolutePath());

        if (fullscreen) {
            command.add("-fullscreen");
        }

        ProcessBuilder pb = new ProcessBuilder(command);

        pb.directory(file.getParentFile());
        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        pb.redirectError(ProcessBuilder.Redirect.DISCARD);

        Process process = pb.start();

        process.getOutputStream().close();

        LOGGER.info("Launched local game: " + file.getName());
    }
}