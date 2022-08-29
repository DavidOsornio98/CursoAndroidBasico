package training.edu.droidbountyhunter.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import training.edu.droidbountyhunter.models.Fugitivo;

public class DatabaseBountyHunter {
    private static final String TAG = DatabaseBountyHunter.class.getSimpleName();
    /** ----------------- Nombre de Base de Datos -------------------------------------**/
    private static final String DataBaseName = "DroidBountyHunterDataBase";
    /** -------------------- Versión de Base de Datos ---------------------------------**/
    private static final int version = 1;
    /** ----------------------------- Tablas y Campos ---------------------------------**/
    private static final String TABLE_NAME = "fugitivos";
    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_STATUS = "status";
    /** ---------------------- Declaración de Tablas ----------------------------------**/
    private static final String TFugitivos = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_NAME_ID + " INTEGER PRIMARY KEY NOT NULL, " +
            COLUMN_NAME_NAME + " TEXT NOT NULL, " +
            COLUMN_NAME_STATUS + " INTEGER, " +
            "UNIQUE (" + COLUMN_NAME_NAME + ") ON CONFLICT REPLACE);";
    private final Context context;
    private DBHelper helper;
    private SQLiteDatabase database;


    private static class DBHelper extends SQLiteOpenHelper {

        private DBHelper helper;
        private SQLiteDatabase database;
        private Context context;


        public DBHelper(Context context) {
            super(context, DataBaseName, null, version);
        }

        /*@Override
        public void onCreate(SQLiteDatabase db) {}*/
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "Creación de la base de datos");
            db.execSQL(TFugitivos);
        }


        /*@Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}*/
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Actualización de la BDD de la versión " + oldVersion + "a la " +
                    + newVersion + ", de la que se destruirá la información anterior");

            // Destruir BDD anterior y crearla nuevamente las tablas actualizadas
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            // Re-creando nuevamente la BDD actualizada
            onCreate(db);
        }

    }

    public DatabaseBountyHunter(Context context) {
        this.context = context;
    }

    public DatabaseBountyHunter open() {
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        helper.close();
        database.close();
    }

    private Cursor querySQL(String sql, String[] selectionArgs) {
        Cursor regreso;
        open();
        regreso = database.rawQuery(sql, selectionArgs);
        return regreso;
    }

    public void DeleteFugitivo(int idFugitivo) {
        open();
        database.delete(TABLE_NAME, COLUMN_NAME_ID + "=?",new
                String[]{String.valueOf(idFugitivo)});
        close();
    }

    public void UpdateFugitivo(Fugitivo fugitivo) {
        open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, fugitivo.getName());
        values.put(COLUMN_NAME_STATUS, fugitivo.getStatus());
        database.update(TABLE_NAME,values,COLUMN_NAME_NAME + "=?",new
                String[]{fugitivo.getName()});
        close();
    }

    public void InsertFugitivo(Fugitivo fugitivo) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, fugitivo.getName());
        values.put(COLUMN_NAME_STATUS, fugitivo.getStatus());
        open();
        database.insert(TABLE_NAME,null,values);
        close();
    }

    @SuppressLint("Range")
    public ArrayList<Fugitivo> GetFugitivos(boolean fueCapturado) {
        ArrayList<Fugitivo> fugitivos = new ArrayList<>();
        String isCapturado = fueCapturado ? "1" : "0";
        Cursor dataCursor = querySQL("SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_NAME_STATUS + "= ? ORDER BY " + COLUMN_NAME_NAME, new
                String[]{isCapturado});
        if (dataCursor != null && dataCursor.getCount() > 0) {
            for (dataCursor.moveToFirst() ; !dataCursor.isAfterLast() ;
                 dataCursor.moveToNext()) {
                int id = dataCursor.getInt(dataCursor.getColumnIndex(COLUMN_NAME_ID));
                String name = dataCursor.getString(dataCursor
                        .getColumnIndex(COLUMN_NAME_NAME));
                String status = dataCursor
                        .getString(dataCursor.getColumnIndex(COLUMN_NAME_STATUS));
                fugitivos.add(new Fugitivo(id,name,status));
            }
        }
        close();
        return fugitivos;
    }


}
