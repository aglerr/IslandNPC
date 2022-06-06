package me.aglerr.islandnpc.config;

import me.aglerr.mclibs.libs.CustomConfig;

public class ConfigManager {

    public static CustomConfig CONFIG;
    public static CustomConfig DATA;

    public static void initialize(){
        CONFIG = new CustomConfig("config.yml", null);
        DATA = new CustomConfig("data.yml", null);
    }

    public static void reloadAllConfigs(){
        CONFIG.reloadConfig();
        DATA.reloadConfig();
    }

}
