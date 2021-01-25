package de.nico.betakey.components.forms;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import de.nico.betakey.BetaKey;
import de.nico.betakey.components.forms.custom.CustomForm;
import de.nico.betakey.components.forms.simple.SimpleForm;
import de.nico.betakey.components.language.Language;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@RequiredArgsConstructor
public class FormWindows {
    private final BetaKey plugin;

    public void openEnterKeyForm(Player player) {
        PlayerMoveEvent event = new PlayerMoveEvent(player, player.getLocation(), player.getLocation());
        CustomForm customForm = new CustomForm.Builder(Language.getNP("beta-title"))
                .addElement(new ElementInput(Language.getNP("join-name"), Language.getNP("join-placeholder")))
                .onSubmit((e, r) -> {
                    if(r.getInputResponse(0).isEmpty()) {
                        if(plugin.getBetaPlayers().containsKey(e.getName())) {
                            return;
                        }
                        e.kick(Language.get("kick-message"), false);
                        event.setCancelled(false);
                    } else {
                        if(plugin.getBetaPlayers().containsKey(e.getName())) {
                            return;
                        }

                        if(plugin.getManager().getKeys().contains(r.getInputResponse(0))) {
                            this.plugin.getManager().addBetaPlayer(player.getName(), r.getInputResponse(0));
                            e.sendMessage(Language.get("code-right-message"));
                            event.setCancelled(false);
                        } else {
                            if(plugin.getBetaPlayers().containsKey(e.getName())) {
                                return;
                            }

                            player.kick(Language.get("kick-message"), false);
                            event.setCancelled(false);
                        }
                    }
                }).build();
        customForm.send(player);
    }

    public void openBetaForm(Player player) {
        SimpleForm simpleForm = new SimpleForm.Builder(Language.getNP("beta-title"), Language.getNP("beta-content"))
                .addButton(new ElementButton(Language.getNP("beta-create-custom-form")), e -> openCreateForm(player))
                .addButton(new ElementButton(Language.getNP("beta-delete-form")), e -> openDeleteForm(player))
                .addButton(new ElementButton(Language.getNP("beta-manager-form")), e -> openManagerForm(player))
                .build();
        simpleForm.send(player);
    }

    private void openCreateForm(Player player) {
        CustomForm customForm = new CustomForm.Builder(Language.getNP("beta-title"))
                .addElement(new ElementInput(Language.getNP("beta-create-name"), Language.getNP("beta-create-placeholder")))
                .onSubmit((e, r) -> {
                    if(r.getInputResponse(0).isEmpty()) {
                        this.plugin.getManager().generateKey(null, true, e);
                    } else {
                        this.plugin.getManager().generateKey(r.getInputResponse(0), false, e);
                    }
                }).build();
        customForm.send(player);
    }

    private void openDeleteForm(Player player) {
        if(plugin.getManager().getKeys().size() <= 0) {
            player.sendMessage(Language.get("no-keys-found"));
            return;
        }

        CustomForm customForm = new CustomForm.Builder(Language.getNP("beta-title"))
                .addElement(new ElementDropdown(Language.getNP("beta-title"), new ArrayList<>(plugin.getManager().getKeys())))
                .onSubmit((e, r) -> {
                    if(r.getDropdownResponse(0).getElementContent().isEmpty()) {
                        return;
                    }

                    String key = r.getDropdownResponse(0).getElementContent();

                    this.plugin.getManager().deleteKey(key, e);
                })
                .build();
        customForm.send(player);
    }

    private void openManagerForm(Player player) {
        SimpleForm simpleForm = new SimpleForm.Builder(Language.getNP("beta-title"), Language.getNP("beta-content"))
                .addButton(new ElementButton(Language.getNP("beta-settings")), e -> openBetaSettingsForm(player))
                .addButton(new ElementButton(Language.getNP("beta-keys")), e -> openKeys(player))
                .addButton(new ElementButton(Language.getNP("beta-statistic")), e -> openBetaStatistic(player))
                .addButton(new ElementButton(Language.getNP("beta-remove-betaplayer")), e -> openRemoveBetaPlayer(player))
                .addButton(new ElementButton(Language.getNP("beta-add-whitelist")), e -> openAddWhitelistPlayer(player))
                .addButton(new ElementButton(Language.getNP("beta-remove-whitelist")), e -> openRemoveWhitelistPlayer(player)).build();
        simpleForm.send(player);
    }

    private void openBetaSettingsForm(Player player) {
        CustomForm customForm = new CustomForm.Builder(Language.getNP("beta-title"))
                .addElement(new ElementToggle(Language.getNP("settings-name"), plugin.getBetaMode()))
                .onSubmit((e, r) -> {
                    if(!r.getToggleResponse(0)) {
                        this.plugin.setBetaMode(false);
                        this.plugin.getConfig().set("beta", false);
                        this.plugin.getConfig().save();
                    } else {
                        this.plugin.setBetaMode(true);
                        this.plugin.getConfig().set("beta", true);
                        this.plugin.getConfig().save();
                    }
                }).build();

        customForm.send(player);
    }

    private void openKeys(Player player) {
        SimpleForm simpleForm = new SimpleForm.Builder(Language.getNP("beta-title"), String.join("\n", plugin.getManager().getKeys())).build();
        simpleForm.send(player);
    }

    private void openBetaStatistic(Player player) {
        SimpleForm simpleForm = new SimpleForm.Builder(Language.getNP("beta-title"),
                plugin.getBetaPlayers().size() + " - §aTester§6!" +
                        "\n" + plugin.getWhitelistedPlayers().size() + " - §cWhitelisted§6!").build();
        simpleForm.send(player);
    }

    public void openRemoveBetaPlayer(Player player) {
        CustomForm customForm = new CustomForm.Builder(Language.getNP("beta-title"))
                .addElement(new ElementInput(Language.getNP("remove-betaplayer-name"), Language.getNP("remove-betaplayer-placeholder")))
                .onSubmit((e, r) -> {
                    if(!r.getInputResponse(0).isEmpty()) {
                        this.plugin.getManager().removeBetaPlayer(r.getInputResponse(0));
                    }
                })
                .build();
        customForm.send(player);
    }

    private void openAddWhitelistPlayer(Player player) {
        CustomForm customForm = new CustomForm.Builder(Language.getNP("beta-title"))
                .addElement(new ElementInput(Language.getNP("add-whitelist-name"), Language.getNP("add-whitelist-placeholder")))
                .onSubmit((e, r) -> {
                    if(!r.getInputResponse(0).isEmpty()) {
                        this.plugin.getManager().addWhitelistPlayer(r.getInputResponse(0));
                    }
                })
                .build();
        customForm.send(player);
    }

    private void openRemoveWhitelistPlayer(Player player) {
        CustomForm customForm = new CustomForm.Builder(Language.getNP("beta-title"))
                .addElement(new ElementInput(Language.getNP("remove-whitelist-name"), Language.getNP("remove-whitelist-placeholder")))
                .onSubmit((e, r) -> {
                    if(!r.getInputResponse(0).isEmpty()) {
                        this.plugin.getManager().removeWhitelistPlayer(r.getInputResponse(0));
                    }
                })
                .build();
        customForm.send(player);
    }
}
