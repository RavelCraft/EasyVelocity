package com.github.imdabigboss.easyvelocity.commands.interfaces;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

public abstract class EasyVelocityCommand implements SimpleCommand {
    private final String permission;

    public EasyVelocityCommand(String command) {
        this(command, null);
    }

    public EasyVelocityCommand(String command, String permission) {
        this(command, permission, new String[0]);
    }

    public EasyVelocityCommand(String command, String permission, String... aliases) {
        if (permission == null) {
            this.permission = null;
        } else if (permission.isEmpty() || permission.isBlank()) {
            this.permission = null;
        } else {
            this.permission = permission;
        }

        CommandManager commandManager = EasyVelocity.getServer().getCommandManager();

        CommandMeta.Builder metaBuilder = commandManager.metaBuilder(command);
        if (aliases.length > 0) {
            metaBuilder = metaBuilder.aliases(aliases);
        }

        CommandMeta meta = metaBuilder.build();
        commandManager.register(meta, this);
    }

    @Override
    public void execute(final Invocation invocation) {
        this.execute(new EasyCommandSender(invocation.source()), invocation.arguments());
    }

    public abstract void execute(EasyCommandSender sender, String[] args);

    @Override
    public boolean hasPermission(final Invocation invocation) {
        if (this.permission == null) {
            return true;
        } else {
            return invocation.source().hasPermission(permission);
        }
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        if (invocation.arguments().length == 0) {
            return this.commandSuggestions(new EasyCommandSender(invocation.source()), new String[1]);
        }

        List<String> commandSuggestions = this.commandSuggestions(new EasyCommandSender(invocation.source()), invocation.arguments());

        if (commandSuggestions.size() > 0) {
            if (invocation.arguments().length == 0) {
                return commandSuggestions;
            }

            String lastArg = invocation.arguments()[invocation.arguments().length - 1];

            List<String> filteredSuggestions = new ArrayList<>();
            for (String suggestion : commandSuggestions) {
                if (suggestion.toLowerCase().startsWith(lastArg.toLowerCase())) {
                    filteredSuggestions.add(0, suggestion);
                }
            }

            return filteredSuggestions;
        }

        return commandSuggestions;
    }

    public abstract List<String> commandSuggestions(EasyCommandSender sender, String[] args);
}
