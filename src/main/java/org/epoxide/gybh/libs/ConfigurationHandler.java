package org.epoxide.gybh.libs;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {
    
    private static Configuration config = null;
    

    public static void initConfig (File file) {
        
        config = new Configuration(file);
        syncConfig();
    }
    
    public static void syncConfig () {
        

        if (config.hasChanged())
            config.save();
    }
}