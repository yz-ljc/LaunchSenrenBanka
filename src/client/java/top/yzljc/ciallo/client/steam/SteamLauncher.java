package top.yzljc.ciallo.client.steam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SteamLauncher {
    public static final Logger LOGGER = LoggerFactory.getLogger("ciallo");
    private static final String GAME_APP_ID = "1144400"; // 千恋万花游戏APP ID，可以自己换成其他游戏的ID

    private static long lastLaunchTime = 0;

    public static void launchSenrenBanka() {
        if (System.currentTimeMillis() - lastLaunchTime < 5000) {
            return;
        }
        lastLaunchTime = System.currentTimeMillis();

        LOGGER.info("Ciallo detected disconnect screen! Opening Senren * Banka...");

        String steamUrl = "steam://run/" + GAME_APP_ID;

        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + steamUrl);
            } else if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(steamUrl));
            }
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Failed to launch Steam game", e);
        }
    }
}