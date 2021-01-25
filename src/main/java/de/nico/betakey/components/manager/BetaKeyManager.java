package de.nico.betakey.components.manager;

import cn.nukkit.Player;
import de.nico.betakey.components.language.Language;
import lombok.Getter;
import de.nico.betakey.BetaKey;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class BetaKeyManager {
    private final BetaKey plugin;

    @Getter
    private List<String> keys = new ArrayList<>();

    public void save(boolean async) {
        this.keys.forEach((key) -> this.plugin.getKeyDB().set(key, null));
        this.plugin.getKeyDB().save(async);
    }

    public void addBetaPlayer(String name, String key) {
        if(plugin.getBetaMode()) {
            if (!plugin.getBetaPlayers().containsKey(name)) {
                if (!plugin.getBetaPlayers().containsValue(key)) {
                    if (!plugin.getWhitelistedPlayers().contains(name)) {
                        plugin.getBetaPlayers().put(name, key);
                        this.removeKey(key);
                    }
                }
            }
        }
    }

    public void removeBetaPlayer(String name) {
        if(plugin.getBetaMode()) {
            if (plugin.getBetaPlayers().containsKey(name)) {
                if (!plugin.getWhitelistedPlayers().contains(name)) {
                    plugin.getBetaPlayers().remove(name);
                }
            }
        }
    }

    public void generateKey(String key, boolean random, Player player) {
        if(!random) {
            if(!plugin.getGenerateKeys().contains(key) && !this.keys.contains(key)) {
                this.saveKey(key);
                player.sendMessage(Language.get("key-created", key));
            }
        } else {
            final String chars = "1234567890";
            final StringBuilder builder = new StringBuilder();
            final Random r = new Random();

            while(builder.length() < 10) {
                int i = (int)(r.nextFloat() * chars.length());
                builder.append(chars.charAt(i));
            }

            if(!keys.contains(builder.toString())) {
                if(!plugin.getGenerateKeys().contains(builder.toString())) {
                    this.saveKey(builder.toString());
                    player.sendMessage(Language.get("key-created", builder.toString()));
                }
            }
        }
    }

    public void deleteKey(String key, Player player) {
        if (plugin.getGenerateKeys().contains(key)) {
            if (keys.contains(key)) {
                this.removeKey(key);
                player.sendMessage(Language.get("key-deleted", key));
            }
        }
    }

    public void addWhitelistPlayer(String name) {
        if(plugin.getBetaMode()) {
            if (!plugin.getBetaPlayers().containsKey(name) && !plugin.getWhitelistedPlayers().contains(name)) {
                plugin.getWhitelistedPlayers().add(name);
            }
        }
    }

    public void removeWhitelistPlayer(String name) {
        if(plugin.getBetaMode()) {
            if(!plugin.getBetaPlayers().containsKey(name) && plugin.getWhitelistedPlayers().contains(name)) {
                plugin.getWhitelistedPlayers().remove(name);
            }
        }
    }

    public void saveKey(String key) {
        keys.add(key);
        plugin.getGenerateKeys().add(key);
        this.save(true);
    }

    public void removeKey(String key) {
        keys.remove(key);
        plugin.getGenerateKeys().remove(key);
        this.save(true);
    }
}
