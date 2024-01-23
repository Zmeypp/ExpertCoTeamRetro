// com.example.helper.database.DatabaseContract.java

package com.example.expertcoteamretro.database;

import android.provider.BaseColumns;

public final class DatabaseContract {

    private DatabaseContract() {
        // Constructeur privé pour éviter l'instanciation de la classe
    }

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";
    }

    public static class CharacterEntry implements BaseColumns {
        public static final String TABLE_NAME = "Characters";
        public static final String CHARACTER_ID = "character_id";
        public static final String USER_ID = "user_id";
        public static final String NAME = "name";
    }

    public static class WorksEntry implements BaseColumns {
        public static final String TABLE_NAME = "Works";
        public static final String WORK_ID = "work_id";
        public static final String CHARACTER_ID = "character_id";
        public static final String NAME = "name";
        public static final String LVL = "lvl";
    }

    public static class CoTeamsEntry implements BaseColumns {
        public static final String TABLE_NAME = "Coteams";
        public static final String COTEAM_ID = "coteam_id";
        public static final String USER_ID_1 = "user_id_1";
        public static final String USER_ID_2 = "user_id_2";
    }
}
