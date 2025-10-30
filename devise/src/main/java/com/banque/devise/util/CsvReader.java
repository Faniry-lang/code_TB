package com.banque.devise.util;

import com.banque.devise.entity.Devise;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    private static final String CSV_FILE_PATH = "devises.csv";

    public static List<Devise> lireToutesDevises() {
        List<Devise> devises = new ArrayList<>();

        try (InputStream inputStream = CsvReader.class
                .getClassLoader()
                .getResourceAsStream(CSV_FILE_PATH)) {

            if (inputStream == null) {
                throw new RuntimeException("Fichier " + CSV_FILE_PATH + " non trouvé dans les ressources");
            }

            devises = lireDevisesDepuisStream(inputStream);

        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture du fichier CSV", e);
        }

        return devises;
    }

    public static List<Devise> lireDevisesActives() {
        List<Devise> toutesDevises = lireToutesDevises();
        List<Devise> devisesActives = new ArrayList<>();
        LocalDate aujourdhui = LocalDate.now();

        for (Devise devise : toutesDevises) {
            // Une devise est active si aujourd'hui est entre dateDebut et dateFin
            if (!aujourdhui.isBefore(devise.getDateDebut()) &&
                    !aujourdhui.isAfter(devise.getDateFin())) {
                devisesActives.add(devise);
            }
        }

        return devisesActives;
    }

    private static List<Devise> lireDevisesDepuisStream(InputStream inputStream) throws IOException {
        List<Devise> devises = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                // Ignorer l'en-tête et les lignes vides
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }

                Devise devise = parserLigneCsv(line);
                if (devise != null) {
                    devises.add(devise);
                }
            }
        }

        return devises;
    }

    private static Devise parserLigneCsv(String ligne) {
        try {
            String[] colonnes = ligne.split(",");

            if (colonnes.length < 4) {
                System.err.println("Ligne CSV ignorée - format invalide: " + ligne);
                return null;
            }

            Devise devise = new Devise();
            devise.setLibelle(colonnes[0].trim());
            devise.setDateDebut(LocalDate.parse(colonnes[1].trim()));
            devise.setDateFin(LocalDate.parse(colonnes[2].trim()));
            devise.setValeur(new BigDecimal(colonnes[3].trim()));

            return devise;

        } catch (Exception e) {
            System.err.println("Erreur parsing ligne CSV: " + ligne + " - " + e.getMessage());
            return null;
        }
    }
}