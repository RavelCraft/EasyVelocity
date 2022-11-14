package com.github.imdabigboss.easyvelocity.commands;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyVelocityCommand;
import com.github.imdabigboss.easyvelocity.utils.PlayerMessage;
import com.velocitypowered.api.proxy.Player;

import java.util.ArrayList;
import java.util.List;

public class LanguageCommand extends EasyVelocityCommand {
    public LanguageCommand() {
        super("language");
    }

    @Override
    public void execute(EasyCommandSender sender, String[] args) {
        if (sender.isConsole()) {
            sender.sendMessage(PlayerMessage.COMMAND_MUST_BE_PLAYER);
            return;
        }

        Player player = sender.getPlayer();
        if (args.length != 1) {
            sender.sendMessage(PlayerMessage.COMMAND_LANGUAGE_HELP);
            return;
        }

        PlayerMessage.MessageLanguage language = PlayerMessage.MessageLanguage.getLanguage(args[0]);
        if (language != null) {
            EasyVelocity.getLanguageManager().setPlayerLanguage(player, language);
            sender.sendMessage(PlayerMessage.COMMAND_LANGUAGE_SUCCESS, language.getCode());
        } else {
            sender.sendMessage(PlayerMessage.COMMAND_LANGUAGE_INVALID);
        }
    }

    @Override
    public List<String> commandSuggestions(EasyCommandSender sender, String[] args) {
        List<String> cmds = new ArrayList<>();
        if (args.length == 1) {
            for (PlayerMessage.MessageLanguage language : PlayerMessage.MessageLanguage.values()) {
                cmds.add(language.getCode());
            }
        }
        return cmds;
    }
}
