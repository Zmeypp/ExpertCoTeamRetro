package com.example.expertcoteamretro.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.database.Cursor;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


// FONCTIONS DE CREATION DES TABLES

    private static final String DATABASE_NAME = "HelperDatabase";

    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + DatabaseContract.UserEntry.TABLE_NAME + " (" +
                    DatabaseContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DatabaseContract.UserEntry.COLUMN_USERNAME + " TEXT, " +
                    DatabaseContract.UserEntry.COLUMN_PASSWORD + " TEXT);";

    private static final String CREATE_TABLE_CHARACTER =
            "CREATE TABLE " + DatabaseContract.CharacterEntry.TABLE_NAME + " (" +
                    DatabaseContract.CharacterEntry.CHARACTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DatabaseContract.CharacterEntry.USER_ID + " INTEGER, " +
                    DatabaseContract.CharacterEntry.NAME + " TEXT, " +
                    "FOREIGN KEY(" + DatabaseContract.CharacterEntry.USER_ID + ") REFERENCES " +
                    DatabaseContract.UserEntry.TABLE_NAME + "(" + DatabaseContract.UserEntry._ID + "));";

    private static final String CREATE_TABLE_WORKS =
            "CREATE TABLE " + DatabaseContract.WorksEntry.TABLE_NAME + " (" +
                    DatabaseContract.WorksEntry.WORK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DatabaseContract.WorksEntry.CHARACTER_ID + " INTEGER, " +
                    DatabaseContract.WorksEntry.NAME + " TEXT, " +
                    DatabaseContract.WorksEntry.LVL + " INTEGER, " +
                    "FOREIGN KEY(" + DatabaseContract.WorksEntry.CHARACTER_ID + ") REFERENCES " +
                    DatabaseContract.CharacterEntry.TABLE_NAME + "(" + DatabaseContract.CharacterEntry.CHARACTER_ID + "));";

    private static final String CREATE_TABLE_COTEAMS =
            "CREATE TABLE " + DatabaseContract.CoTeamsEntry.TABLE_NAME + " (" +
                    DatabaseContract.CoTeamsEntry.COTEAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DatabaseContract.CoTeamsEntry.USER_ID_1 + " INTEGER, " +
                    DatabaseContract.CoTeamsEntry.USER_ID_2 + " INTEGER, " +
                    "FOREIGN KEY(" + DatabaseContract.CoTeamsEntry.USER_ID_1 + ") REFERENCES " +
                    DatabaseContract.UserEntry.TABLE_NAME + "(" + DatabaseContract.UserEntry._ID + "), " +
                    "FOREIGN KEY(" + DatabaseContract.CoTeamsEntry.USER_ID_2 + ") REFERENCES " +
                    DatabaseContract.UserEntry.TABLE_NAME + "(" + DatabaseContract.UserEntry._ID + "));";

// FONCTION DE HASH

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Gérer l'erreur selon vos besoins
        }
    }

// FONCTIONS DE CREATIONS DES LIGNES
    public long createUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserEntry.COLUMN_USERNAME, username);

        // Hacher le mot de passe avec SHA-256
        String hashedPassword = hashPassword(password);
        values.put(DatabaseContract.UserEntry.COLUMN_PASSWORD, hashedPassword);

        long userId = -1;

        try {
            userId = db.insertOrThrow(DatabaseContract.UserEntry.TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return userId;
    }

// FONCTION DE VERIFICATION DE CONNEXION

    public long checkUserLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                DatabaseContract.UserEntry._ID,
                DatabaseContract.UserEntry.COLUMN_PASSWORD
        };
        String selection = DatabaseContract.UserEntry.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(
                DatabaseContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        long userId = -1;

        if (cursor != null && cursor.moveToFirst()) {
            // Récupérer l'ID de l'utilisateur et le mot de passe haché de la base de données
            long storedUserId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry._ID));
            String hashedPasswordFromDB = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_PASSWORD));

            // Vérifier si le mot de passe fourni correspond au mot de passe haché
            if (hashedPasswordFromDB.equals(hashPassword(password))) {
                userId = storedUserId; // Connexion réussie, retourner l'ID de l'utilisateur
            }

            cursor.close();
        }

        db.close();

        return userId;
    }


// GET USERNAME WITH ID

    public String getConnectedUsername(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {DatabaseContract.UserEntry.COLUMN_USERNAME};
        String selection = DatabaseContract.UserEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        String connectedUsername = null;

        Cursor cursor = null;
        try {
            cursor = db.query(
                    DatabaseContract.UserEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                connectedUsername = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_USERNAME));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return connectedUsername;
    }


// FONCTION POUR RECUPERER LE NOMBRE DE PERSONNAGES DE L'UTILISATEUR CONNECTE

    public long getConnectedUserNumberCharacter(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numberOfCharacters = 0;

        // Requête SQL avec une jointure et un comptage
        String query = "SELECT COUNT(*)" +
                " FROM " + DatabaseContract.CharacterEntry.TABLE_NAME +
                " INNER JOIN " + DatabaseContract.UserEntry.TABLE_NAME +
                " ON " + DatabaseContract.CharacterEntry.TABLE_NAME + "." + DatabaseContract.CharacterEntry.USER_ID +
                " = " + DatabaseContract.UserEntry.TABLE_NAME + "." + DatabaseContract.UserEntry._ID +
                " WHERE " + DatabaseContract.UserEntry.TABLE_NAME + "." + DatabaseContract.UserEntry._ID +
                " = " + userId;

        Cursor cursor = db.rawQuery(query, null);

        // Récupérer le nombre de personnages à partir du curseur
        if (cursor.moveToFirst()) {
            numberOfCharacters = cursor.getInt(0);
        }

        // Fermer le curseur et la base de données
        cursor.close();
        db.close();

        // Retourner le nombre de personnages
        return numberOfCharacters;
    }




// FONCTION POUR RECUPERER TOUS LES NOMS DES PERSONNAGES DE L'UTILISATEUR CONNECTE

    public String[] getConnectedUserCharacters(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> characterNames = new ArrayList<>();

        // Requête SQL avec une jointure
        String query = "SELECT " + DatabaseContract.CharacterEntry.NAME +
                " FROM " + DatabaseContract.CharacterEntry.TABLE_NAME +
                " INNER JOIN " + DatabaseContract.UserEntry.TABLE_NAME +
                " ON " + DatabaseContract.CharacterEntry.TABLE_NAME + "." + DatabaseContract.CharacterEntry.USER_ID +
                " = " + DatabaseContract.UserEntry.TABLE_NAME + "." + DatabaseContract.UserEntry._ID +
                " WHERE " + DatabaseContract.UserEntry.TABLE_NAME + "." + DatabaseContract.UserEntry._ID +
                " = " + userId;

        Cursor cursor = db.rawQuery(query, null);

        // Parcourir le curseur et ajouter les noms des personnages à la liste
        if (cursor.moveToFirst()) {
            do {
                String characterName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CharacterEntry.NAME));
                characterNames.add(characterName);
            } while (cursor.moveToNext());
        }

        // Fermer le curseur et la base de données
        cursor.close();
        db.close();

        // Convertir la liste en tableau de chaînes et le retourner
        return characterNames.toArray(new String[0]);
    }



// FONCTION POUR CREER UN NOUVEAU PERSONNAGE POUR L'UTILISATEUR CONNECTE
    public long createNewCharacter(long userId, String characterName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CharacterEntry.USER_ID, userId);
        values.put(DatabaseContract.CharacterEntry.NAME, characterName);

        long characterId = -1;

        try {
            characterId = db.insertOrThrow(DatabaseContract.CharacterEntry.TABLE_NAME, null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return characterId;
    }



// FONCTION POUR RECUPERER LES INFORMATIONS DU PERSONNAGE CLIQUE
    public String[] getCharacterInformations(long userId, String characterName) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> characterInformations = new ArrayList<>();

        // Requête SQL avec une jointure
        String query = "SELECT " + DatabaseContract.CharacterEntry.CHARACTER_ID + ", " + DatabaseContract.CharacterEntry.NAME +
                " FROM " + DatabaseContract.CharacterEntry.TABLE_NAME +
                " INNER JOIN " + DatabaseContract.UserEntry.TABLE_NAME +
                " ON " + DatabaseContract.CharacterEntry.TABLE_NAME + "." + DatabaseContract.CharacterEntry.USER_ID +
                " = " + DatabaseContract.UserEntry.TABLE_NAME + "." + DatabaseContract.UserEntry._ID +
                " WHERE " + DatabaseContract.UserEntry.TABLE_NAME + "." + DatabaseContract.UserEntry._ID +
                " = " + userId +
                " AND " + DatabaseContract.CharacterEntry.NAME +
                " = '" + characterName + "'";

        Cursor cursor = db.rawQuery(query, null);

        // Parcourir le curseur et ajouter les informations du personnage à la liste
        if (cursor.moveToFirst()) {
            do {
                String characterId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CharacterEntry.CHARACTER_ID));
                String characterNameResult = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CharacterEntry.NAME));

                // Ajouter les informations du personnage à la liste
                characterInformations.add(characterId);
                characterInformations.add(characterNameResult);
            } while (cursor.moveToNext());
        }

        // Fermer le curseur et la base de données
        cursor.close();
        db.close();

        // Convertir la liste en tableau de chaînes et le retourner
        return characterInformations.toArray(new String[0]);
    }







// AUTRES FONCTIONS

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_CHARACTER);
        db.execSQL(CREATE_TABLE_WORKS);
        db.execSQL(CREATE_TABLE_COTEAMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Logique de mise à niveau de la base de données
    }

}
