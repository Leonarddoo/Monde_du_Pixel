package fr.leonarddoo.pixel.games;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Pendu extends ListenerAdapter {

    private static String j1 = null;
    private static String j2 = null;
    private static String mot1 = null;
    private static String mot2 = null;
    private static String msg1 = null;
    private static String msg2 = null;
    private static char[] cache1 = null;
    private static char[] cache2 = null;
    private static List<Character> use1 = new ArrayList<>();
    private static List<Character> use2 = new ArrayList<>();
    private static boolean winner1 = false;
    private static boolean winner2 = false;
    private static int tour1 = 0;
    private static int tour2 = 0;

    private static final String[] affichage = {
            "https://cdn.discordapp.com/attachments/958700629201666069/959902463421468725/IMG_7199.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902479837986836/IMG_7200.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902489459687544/IMG_7201.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902498938822707/IMG_7202.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902505255464991/IMG_7203.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902516504584272/IMG_7204.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902528059895908/IMG_7205.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902538570809414/IMG_7206.jpg",
            "https://cdn.discordapp.com/attachments/958700629201666069/959902547588550746/IMG_7208.jpg"
    };




}
