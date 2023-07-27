package com.example.prueba3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.res.AssetManager;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.Nullable;

public class AdminBD extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "my_database.s3db";
    static final int DATABASE_VERSION = 1;
    private final Context context;

    public AdminBD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void copyDatabaseFromAssets() throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(DATABASE_NAME);
        String outFileName = context.getDatabasePath(DATABASE_NAME).getPath();
        OutputStream outputStream = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public SQLiteDatabase openDatabase() {
        try {
            copyDatabaseFromAssets();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SQLiteDatabase.openDatabase(
                context.getDatabasePath(DATABASE_NAME).getPath(),
                null,
                SQLiteDatabase.OPEN_READWRITE
        );
    }

    public boolean grabar(String cod, String des, int stock, String ubi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cod", cod);
        values.put("des", des);
        values.put("stock", stock);
        values.put("ubi", ubi);

        int rowsAffected = db.update("productos", values, "cod=?", new String[]{cod});
        if (rowsAffected == 0) {
            db.insert("productos", null, values);
        }
        db.close();
        return false;
    }

    public boolean deleteProductByCod(String cod) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define the WHERE clause and the arguments for the delete query
        String whereClause = "cod=?";
        String[] whereArgs = new String[]{cod};

        // Execute the delete query
        int rowsAffected = db.delete("productos", whereClause, whereArgs);

        db.close();

        // Return true if the delete operation was successful (at least one row affected)
        return rowsAffected > 0;
    }
}

