package de.nico.betakey;

import cn.nukkit.Player;
import cn.nukkit.command.CommandMap;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import de.nico.betakey.commands.BetaUICommand;
import de.nico.betakey.components.forms.FormWindows;
import de.nico.betakey.components.language.Language;
import de.nico.betakey.components.listener.EventListener;
import de.nico.betakey.components.manager.BetaKeyManager;
import de.nico.betakey.components.forms.FormListener;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nico, Yannick
 * @website https://cloudburstmc.org
 * @project BetaKeySystem
 */

public class BetaKey extends PluginBase {

    @Getter
    private FormWindows windows;

    @Getter
    private List<String> generateKeys;
    
    @Getter
    private BetaKeyManager manager;

    @Getter
    private Config keyDB;

    @Getter
    private List<String> beta;

    @Getter
    private ArrayList<String> whitelistedPlayers = new ArrayList<>();

    @Getter   //player //key
    private Map<String, String> betaPlayers = new HashMap<>();

    @Getter
    @Setter
    private Boolean betaMode;

    @Override
    public void onLoad() {
        this.saveDefaultConfig();
        this.manager = new BetaKeyManager(this);
        this.keyDB = new Config(this.getDataFolder() + "/keys.yml", Config.YAML);
        this.generateKeys = this.keyDB.getStringList("generateKeys");
        this.beta = this.keyDB.getStringList("beta");
        this.keyDB.getAll().forEach((key, value) -> {
            this.getManager().getKeys().add(key);
        });
        this.windows = new FormWindows(this);
        this.setBetaMode(this.getConfig().getBoolean("beta"));
    }

    @Override
    public void onEnable() {
        Language.init(this);
        this.registerCommands(this.getServer().getCommandMap());
        this.registerListener(this.getServer().getPluginManager());
    }

    @Override
    public void onDisable() {
        this.manager.save(false);
    }
    
    private void registerCommands(CommandMap map) {
        map.register("betaui", new BetaUICommand(this));
    }

    private void registerListener(PluginManager man) {
        man.registerEvents(new FormListener(), this);
        man.registerEvents(new EventListener(this), this);
    }
}
