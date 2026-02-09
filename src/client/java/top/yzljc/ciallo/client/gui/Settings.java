package top.yzljc.ciallo.client.gui;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import top.yzljc.ciallo.client.config.ConfigManager;

import java.util.ArrayList;

public class Settings {

    public static Screen createScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("编辑在被服务器踢出时的启动程序/文件"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Text.of("General"));

        general.addEntry(entryBuilder.startStrList(Text.of("在此处输入你的文件路径或Steam APP ID"), ConfigManager.get().gameList)
                .setDefaultValue(new ArrayList<>())
                .setTooltip(Text.of("输入Steam App ID或文件路径，输入多个预选程序将会随机抽取一个程序启动"))
                .setSaveConsumer(newList -> {
                    ConfigManager.get().gameList = newList;
                })
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.of("尝试全屏启动"), ConfigManager.get().isFullScreen)
                .setDefaultValue(false)
                .setTooltip(Text.of("尝试添加 -fullscreen 参数启动程序 (对于非应用程序可能无效)"))
                .setSaveConsumer(newValue -> {
                    ConfigManager.get().isFullScreen = newValue;
                })
                .build());

        builder.setSavingRunnable(ConfigManager::save);

        return builder.build();
    }
}