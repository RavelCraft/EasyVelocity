package com.github.imdabigboss.easyvelocity.utils;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.commands.interfaces.EasyCommandSender;
import com.github.imdabigboss.easyvelocity.info.PluginInfo;
import com.github.imdabigboss.easyvelocity.info.ServerInfo;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.util.Locale;

public enum PlayerMessage {
    COMMAND_MUST_BE_PLAYER(ChatColor.RED + "You must be a player to use this command!", ChatColor.RED + "Vous devez être un joueur pour utiliser cette commande!"),

    COMMAND_CONFIG_RELOADED(ChatColor.AQUA + "Config reloaded", ChatColor.AQUA + "Config rechargé"),
    COMMAND_CONFIG_HELP(ChatColor.RED + "Usage: /ravelconfig reload", ChatColor.RED + "Utilisation: /ravelconfig reload"),

    COMMAND_CRACKED_RELOADED(ChatColor.AQUA + "Cracked config reloaded!", ChatColor.AQUA + "Config de crack rechargé!"),
    COMMAND_CRACKED_HELP(ChatColor.RED + "Usage: /proxycracked reload", ChatColor.RED + "Utilisation: /proxycracked reload"),

    COMMAND_INFO(ChatColor.BOLD + "--- Server Information ---\n" + ChatColor.RESET +
            "Website: " + ChatColor.GOLD + PluginInfo.WEBSITE + ChatColor.RESET + "\n" +
            "Discord invites: " + ChatColor.GOLD + "sYWtr9E" + ChatColor.RESET + " or " + ChatColor.GOLD + "VFB6632" + ChatColor.RESET + "\n" +
            "Version: " + ChatColor.GOLD + EasyVelocity.getServer().getVersion().getName() + " - version " + EasyVelocity.getServer().getVersion().getVersion(),

            ChatColor.BOLD + "--- Informations du serveur ---\n" + ChatColor.RESET +
            "Site web: " + ChatColor.GOLD + PluginInfo.WEBSITE + ChatColor.RESET + "\n" +
            "Invitations Discord: " + ChatColor.GOLD + "sYWtr9E" + ChatColor.RESET + " ou " + ChatColor.GOLD + "VFB6632" + ChatColor.RESET + "\n" +
            "Version: " + ChatColor.GOLD + EasyVelocity.getServer().getVersion().getName() + " - version " + EasyVelocity.getServer().getVersion().getVersion()),

    COMMAND_LANGUAGE_HELP(ChatColor.RED + "Usage: /language <language>", ChatColor.RED + "Utilisation: /language <langue>"),
    COMMAND_LANGUAGE_SUCCESS(ChatColor.AQUA + "Language set to %s", ChatColor.AQUA + "Vous avez choisi %s comme langue"),
    COMMAND_LANGUAGE_INVALID(ChatColor.RED + "Invalid language!", ChatColor.RED + "Langue invalide!"),

    COMMAND_LOBBY_ALREADY_IN(ChatColor.RED + "You are already in the lobby!", ChatColor.RED + "Vous êtes déjà dans le lobby!"),
    COMMAND_LOBBY_SUCCESS(ChatColor.AQUA + "You have been sent to the lobby!", ChatColor.AQUA + "Vous avez été envoyé au lobby!"),
    COMMAND_LOBBY_ERROR(ChatColor.RED + "An error occurred while sending you to the lobby...", ChatColor.RED + "Une erreur s'est produite lors de l'envoi au lobby..."),
    COMMAND_LOBBY_NOT_FOUND(ChatColor.RED + "There is no lobby server...", ChatColor.RED + "Il n'y a pas de serveur de lobby..."),

    COMMAND_MAINTENANCE_ENABLED(ChatColor.AQUA + "Maintenance mode enabled!", ChatColor.AQUA + "Mode maintenance activé!"),
    COMMAND_MAINTENANCE_DISABLED(ChatColor.AQUA + "Maintenance mode disabled!", ChatColor.AQUA + "Mode maintenance désactivé!"),
    COMMAND_MAINTENANCE_HELP(ChatColor.RED + "Usage: /proxymaintenance <enable/disable/bypass> [player]", ChatColor.RED + "Utilisation: /proxymaintenance <enable/disable/bypass> [joueur]"),
    COMMAND_MAINTENANCE_CAN_BYPASS(ChatColor.AQUA + "%s can bypass maintenance mode!", ChatColor.AQUA + "%s peut contourner le mode maintenance!"),
    COMMAND_MAINTENANCE_CANNOT_BYPASS(ChatColor.AQUA + "%s cannot bypass maintenance mode.", ChatColor.AQUA + "%s ne peut pas contourner le mode maintenance."),
    COMMAND_MAINTENANCE_STATUS(ChatColor.AQUA + "Maintenance mode is currently %s.", ChatColor.AQUA + "Le mode maintenance est actuellement %s."),

    COMMAND_MOTD_HELP(ChatColor.RED + "Usage: /setmotd <message>", ChatColor.RED + "Utilisation: /setmotd <message>"),
    COMMAND_MOTD_UPDATED(ChatColor.AQUA + "MOTD updated!", ChatColor.AQUA + "MOTD mis à jour!"),

    COMMAND_RANKS_PLAYER_NOT_FOUND(ChatColor.RED + "Player not found!", ChatColor.RED + "Joueur introuvable!"),
    COMMAND_RANKS_PLAYER_NO_RANK(ChatColor.RED + "%s has no rank!", ChatColor.RED + "%s n'a pas de rang!"),
    COMMAND_RANKS_PLAYER_RANK(ChatColor.AQUA + "%s's rank is %s.", ChatColor.AQUA + "Le rang de %s est %s."),
    COMMAND_RANKS_PLAYER_RANK_SET(ChatColor.AQUA + "%s's rank has been set to %s.", ChatColor.AQUA + "Le rang de %s a été mis à %s."),
    COMMAND_RANKS_NO_RANK(ChatColor.RED + "Rank not found.", ChatColor.RED + "Rang introuvable."),
    COMMAND_RANKS_ADDED(ChatColor.AQUA + "Rank added!", ChatColor.AQUA + "Rang ajouté!"),
    COMMAND_RANKS_REMOVED(ChatColor.AQUA + "Rank removed!", ChatColor.AQUA + "Rang supprimé!"),
    COMMAND_RANKS_ALREADY_EXISTS(ChatColor.RED + "Rank already exists!", ChatColor.RED + "Le rang existe déjà!"),
    COMMAND_RANKS_NO_RANKS(ChatColor.RED + "There are no ranks!", ChatColor.RED + "Il n'y a pas de rangs!"),
    COMMAND_RANKS_RELOADED(ChatColor.AQUA + "Ranks reloaded!", ChatColor.AQUA + "Rangs rechargés!"),
    COMMAND_RANKS_LIST(ChatColor.BOLD + "Ranks:%s" + ChatColor.RESET, ChatColor.BOLD + "Ranks:%s" + ChatColor.RESET),
    COMMAND_RANKS_HELP(ChatColor.RED + "Usage: /ranks get <player>, set <player> <rank>, add <rank> <color>, remove <rank>, reload, list", ChatColor.RED + "Utilisation: /ranks get <joueur>, set <joueur> <rang>, add <rang> <couleur>, remove <rang>, reload, list"),

    COMMAND_PACK_GENERATED(ChatColor.AQUA + "Pack generated!", ChatColor.AQUA + "Pack généré!"),
    COMMAND_PACK_HELP(ChatColor.RED + "Usage: /proxyresourcepack <generate/send/sendall> [player]", ChatColor.RED + "Utilisation: /proxyresourcepack <generate/send/sendall> [joueur]"),
    COMMAND_PACK_SPECIFY_PLAYER(ChatColor.RED + "You must specify a player!", ChatColor.RED + "Vous devez spécifier un joueur!"),
    COMMAND_PACK_NO_PLAYER(ChatColor.RED + "Player not found!", ChatColor.RED + "Joueur introuvable!"),
    COMMAND_PACK_SENT(ChatColor.AQUA + "Pack sent!", ChatColor.AQUA + "Pack envoyé!"),
    COMMAND_PACK_SENT_ALL(ChatColor.AQUA + "Pack sent to all players!", ChatColor.AQUA + "Pack envoyé à tous les joueurs!"),

    COMMAND_SERVER_ADDED(ChatColor.AQUA + "Server added!", ChatColor.AQUA + "Serveur ajouté!"),
    COMMAND_SERVER_ADD_ERROR(ChatColor.RED + "An error occurred while adding the server...", ChatColor.RED + "Une erreur s'est produite lors de l'ajout du serveur..."),
    COMMAND_SERVER_REMOVED(ChatColor.AQUA + "Server removed!", ChatColor.AQUA + "Serveur supprimé!"),
    COMMAND_SERVER_REMOVE_ERROR(ChatColor.RED + "An error occurred while removing the server...", ChatColor.RED + "Une erreur s'est produite lors de la suppression du serveur..."),
    COMMAND_SERVER_LIST(ChatColor.BOLD + "Servers:" + ChatColor.RESET + "%s", ChatColor.BOLD + "Serveurs : " + ChatColor.RESET + "%s"),
    COMMAND_SERVER_HELP(ChatColor.RED + "Usage:\n" +
            " - /servermanage add <name> <address> <port>\n" +
            " - /servermanage remove <name>\n" +
            " - /servermanage list",

            ChatColor.RED + "Utilisation:\n" +
            " - /servermanage add <nom> <address> <port>\n" +
            " - /servermanage remove <nom>\n" +
            " - /servermanage list"),

    COMMAND_TEMPBAN_NAN(ChatColor.RED + "Invalid time: %s. That is not a number. Go back to preschool.", ChatColor.RED + "Temps invalide: %s. Ce n'est pas un nombre. Retournez à la maternelle."),
    COMMAND_TEMPBAN_PLAYER_NOT_FOUND(ChatColor.RED + "That player doesn't exist. Let me go back to sleep.", ChatColor.RED + "Ce joueur n'existe pas. Laissez-moi retourner dormir."),
    COMMAND_TEMPBAN_BANNED(ChatColor.AQUA + "%s has been banned for %s days.", ChatColor.AQUA + "%s a été banni pour %s jours."),
    COMMAND_TEMPBAN_UNBANNED(ChatColor.AQUA + "%s has been unbanned.", ChatColor.AQUA + "%s a été débanni."),
    COMMAND_TEMPBAN_NOT_BANNED(ChatColor.RED + "%s is not banned.", ChatColor.RED + "%s n'est pas banni."),
    COMMAND_TEMPBAN_LIST(ChatColor.BOLD + "Temporarily banned players:" + ChatColor.RESET + "%s" + ChatColor.RESET, ChatColor.BOLD + "Joueurs bannis temporairement : " + ChatColor.RESET + "%s"),
    COMMAND_TEMPBAN_HELP(ChatColor.RED + "Usage:\n" +
            " - /tempban ban <player> <days> [reason]\n" +
            " - /tempban unban <player>\n" +
            " - /tempban list",

            ChatColor.RED + "Utilisation:\n" +
            " - /tempban ban <joueur> <jours> [raison]\n" +
            " - /tempban unban <joueur>\n" +
            " - /tempban list"),

    COMMAND_KICK_HELP(ChatColor.RED + "Usage: /proxykick <player>", ChatColor.RED + "Utilisation: /proxykick <joueur>"),
    COMMAND_KICK_PLAYER_NOT_FOUND(ChatColor.RED + "Player not found!", ChatColor.RED + "Joueur introuvable!"),
    COMMAND_KICK_MESSAGE(ChatColor.RED + "You have been kicked from the network.", ChatColor.RED + "Vous avez été expulsé du réseau."),

    COMMAND_NICK_HELP(ChatColor.RED + "Usage: /proxynick <player> <player>/stop", ChatColor.RED + "Utilisation: /proxynick <joueur> <joueur>/stop"),
    COMMAND_NICK_PLAYER_NOT_FOUND(ChatColor.RED + "Player not found!", ChatColor.RED + "Joueur introuvable!"),
    COMMAND_NICK_REJOIN(ChatColor.RED + "Please rejoin the server.", ChatColor.RED + "Veuillez rejoindre le serveur."),
    COMMAND_NICK_STOP(ChatColor.AQUA + "Your nickname has been removed.", ChatColor.AQUA + "Votre surnom a été supprimé."),

    COMMAND_WEBSITE_RELOADED(ChatColor.AQUA + "Website reloaded!", ChatColor.AQUA + "Site web rechargé!"),
    COMMAND_WEBSITE_HELP(ChatColor.RED + "Usage: /ravelwebsite reload", ChatColor.RED + "Utilisation: /ravelwebsite reload"),

    COMMAND_WHITELIST_HELP(ChatColor.RED + "Usage: /proxywhitelist <add/remove/enable> <player/true/false> [server]", ChatColor.RED + "Utilisation: /proxywhitelist <add/remove/enable> <joueur/true/false> [serveur]"),
    COMMAND_WHITELIST_ADDED(ChatColor.AQUA + "Added %s to the whitelist!", ChatColor.AQUA + "%s a été ajouté à la whitelist!"),
    COMMAND_WHITELIST_ADDED_SERVER(ChatColor.AQUA + "Added %s to the whitelist on %s!", ChatColor.AQUA + "%s a été ajouté à la whitelist sur %s!"),
    COMMAND_WHITELIST_ADD_FAILED(ChatColor.RED + "Failed to add %s to the whitelist!", ChatColor.RED + "Impossible d'ajouter %s à la whitelist!"),
    COMMAND_WHITELIST_REMOVED(ChatColor.AQUA + "Removed %s from the whitelist!", ChatColor.AQUA + "%s a été supprimé de la whitelist!"),
    COMMAND_WHITELIST_REMOVED_SERVER(ChatColor.AQUA + "Removed %s from the whitelist on %s!", ChatColor.AQUA + "%s a été supprimé de la whitelist sur %s!"),
    COMMAND_WHITELIST_REMOVE_FAILED(ChatColor.RED + "Failed to remove %s from the whitelist!", ChatColor.RED + "Impossible de supprimer %s de la whitelist!"),
    COMMAND_WHITELIST_ENABLED(ChatColor.AQUA + "Whitelist enabled!", ChatColor.AQUA + "Whitelist activée!"),

    JOIN_NETWORK(ChatColor.YELLOW + "%s has joined the network!", ChatColor.YELLOW + "%s a rejoint le réseau!"),
    LEAVE_NETWORK(ChatColor.YELLOW + "%s has left the network!", ChatColor.YELLOW + "%s a quitté le réseau!"),
    LOBBY_REDIRECT(ChatColor.AQUA + "You have been redirected to the lobby.", ChatColor.AQUA + "Vous avez été redirigé vers le lobby."),

    LOGIN_EXPIRED(ChatColor.RED + "Your login has expired. Please authenticate again.", ChatColor.RED + "Votre connexion a expiré. Veuillez vous authentifier à nouveau."),
    NOT_WHITELISTED(ChatColor.RED + "You are not whitelisted on this server.", ChatColor.RED + "Vous n'êtes pas sur liste blanche sur ce serveur."),
    NICK_LOGIN(ChatColor.RED + "You have logged in as a nick! Beware!", ChatColor.RED + "Attention! Vous vous êtes connecté avec un surnom!"),

    RULES_WARNING(ChatColor.LIGHT_PURPLE + "Hey there %s! Did you read the rules??? Click this message to see them.", ChatColor.LIGHT_PURPLE + "Salut %s! As-tu lu les règles??? Clique sur ce message pour les voir."),
    ANNOUNCEMENTS_WARNING(ChatColor.LIGHT_PURPLE + "Oh hello %s! Have you been keeping up to date with the announcements? Click this message to see them.", ChatColor.LIGHT_PURPLE + "Oh salut %s! As-tu suivi les annonces? Clique sur ce message pour les voir."),

    PLAYER_LIST_HEADER(ChatColor.YELLOW +
            "---- " + ServerInfo.SERVER_NAME + " ----\n" +
            "There are %s/" + ServerInfo.MAX_PLAYERS + " players online!\n" +
            "With %s on %s.",

            ChatColor.YELLOW +
            "---- " + ServerInfo.SERVER_NAME + " ----\n" +
            "Il y a %s/" + ServerInfo.MAX_PLAYERS + " joueurs en ligne!\n" +
            "Avec %s sur %s."),
    PLAYER_LIST_FOOTER(ChatColor.YELLOW + "-".repeat(ServerInfo.SERVER_NAME.length() + 8), ChatColor.YELLOW + "-".repeat(ServerInfo.SERVER_NAME.length() + 8)),

    BEDROCK_TEXTUREPACK_WARNING(ChatColor.YELLOW + "You are playing on Minecraft Bedrock edition. We are warning you that this server uses a texture pack that is not supported by Bedrock at this point.", ChatColor.YELLOW + "Vous jouez sur Minecraft Bedrock edition. Nous vous avertissons que ce serveur utilise un pack de textures qui n'est pas pris en charge par Bedrock à ce stade.");

    private final String english;
    private final String french;

    PlayerMessage(String english, String french) {
        this.english = english;
        this.french = french;
    }

    public String getEnglish() {
        return english;
    }

    public String getFrench() {
        return french;
    }

    public static Component formatMessage(PlayerMessage message, EasyCommandSender commandSender, String... args) {
        if (commandSender.isConsole()) {
            return formatMessage(message, MessageLanguage.ENGLISH, args);
        } else {
            return formatMessage(message, commandSender.getPlayer(), args);
        }
    }

    public static Component formatMessage(PlayerMessage message, Player player, String... args) {
        Locale locale = player.getEffectiveLocale();
        MessageLanguage language = EasyVelocity.getLanguageManager().getPlayerLanguage(player);

        if (language == null) {
            if (locale != null && locale.getLanguage().equals(MessageLanguage.FRENCH.getCode())) {
                language = MessageLanguage.FRENCH;
            } else {
                language = MessageLanguage.ENGLISH;
            }
        }

        return formatMessage(message, language, args);
    }

    private static Component formatMessage(PlayerMessage message, MessageLanguage language, String... args) {
        String tmp;
        if (language == MessageLanguage.FRENCH) {
            tmp = message.getFrench();
        } else {
            tmp = message.getEnglish();
        }

        if (args != null) {
            tmp = String.format(tmp, args);
        }

        return Component.text(tmp);
    }

    public enum MessageLanguage {
        ENGLISH("en"),
        FRENCH("fr");

        private final String code;

        MessageLanguage(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static MessageLanguage getLanguage(String code) {
            for (MessageLanguage language : values()) {
                if (language.getCode().equals(code)) {
                    return language;
                }
            }
            return null;
        }
    }
}
