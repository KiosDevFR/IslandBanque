package org.cubitech.islandbanque.managers;

import org.bukkit.ChatColor;

import java.util.TreeMap;

public class MessagesManager {
    private static MessagesManager instance;
    private TreeMap<String, String> placeholders;

    private MessagesManager() {
        placeholders = new TreeMap<>();

        String[] placeholders = {
                "%interets%",

                "%interet_max_argent%",
                "%formatte_interet_max_argent%",
                "%espace_interet_max_argent%",

                "%interet_max_exp%",
                "%formatte_interet_max_exp%",
                "%espace_interet_max_exp%",

                "%interet_max_farmpoints%",
                "%formatte_interet_max_farmpoints%",
                "%espace_interet_max_farmpoints%",

                "%interet_argent%",
                "%formatte_interet_argent%",
                "%espace_interet_argent%",

                "%interet_exp%",
                "%formatte_interet_exp%",
                "%espace_interet_exp%",

                "%interet_farmpoints%",
                "%formatte_interet_farmpoints%",
                "%espace_interet_farmpoints%",

                "%banque_argent%",
                "%formatte_banque_argent%",
                "%espace_banque_argent%",

                "%banque_exp%",
                "%formatte_banque_exp%",
                "%espace_banque_exp%",

                "%banque_farmpoints%",
                "%formatte_banque_farmpoints%",
                "%espace_banque_farmpoints%",

                "%joueur_argent%",
                "%formatte_joueur_argent%",
                "%espace_joueur_argent%",

                "%joueur_exp%",
                "%formatte_joueur_exp%",
                "%espace_joueur_exp%",

                "%joueur_farmpoints%",
                "%formatte_joueur_farmpoints%",
                "%espace_joueur_farmpoints%",

                "%quantite%",
                "%formatte_quantite%",
                "%espace_quantite%",

                "%quantite_peut_retirer%",
                "%formatte_quantite_peut_retirer%",
                "%espace_quantite_peut_retirer%",

                "%banque_log_date%",
                "%banque_joueur%",
                "%banque_log_montant%",
                "%formatte_banque_log_montant%",
                "%espace_banque_log_montant%",

                "%banque_log_solde_total%",
                "%formatte_banque_log_solde_total%",
                "%espace_banque_log_solde_total%",

                "%temps_interet%"
        };

        //Initializes the placeholders
        for (String placeholder : placeholders) {
            this.placeholders.put(placeholder, null);
        }
    }

    public String processText(String text) {
        for (String placeholder : placeholders.keySet()) {
            if (text.contains(placeholder)) {
                text = text.replaceAll(placeholder, placeholders.get(placeholder));
            }
        }

        return ChatColor.translateAlternateColorCodes('&',text);
    }


    public void setPlaceholder(String placeholder, String value) {
        placeholders.replace(placeholder, value);
    }

    public void restartPlaceholders() {
        for (String ignored : placeholders.keySet()) {
            placeholders.replaceAll((a, b) -> null);
        }
    }

    public static MessagesManager getInstance() {
        if (instance == null) {
            instance = new MessagesManager();
        }
        return instance;
    }

}
