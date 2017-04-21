package trains.feup.org.trains.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by leonardoaxe on 27/03/2017.
 */

public class TicketsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Tickets.db";

    public TicketsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TicketsContract.SQL_CREATE_TABLE_TICKETS);

        Log.i("SQLite", "TABLE HAS BEEN CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void restart(SQLiteDatabase db) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(TicketsContract.SQL_DELETE_TABLE_TICKETS);
        db.execSQL(TicketsContract.SQL_CREATE_TABLE_TICKETS);
    }
}
