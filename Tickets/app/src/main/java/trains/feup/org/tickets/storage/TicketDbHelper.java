package trains.feup.org.tickets.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Renato on 4/19/2017.
 */

public class TicketDbHelper extends SQLiteOpenHelper {

    private static final String TAG = TicketDbHelper.class.getName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Ticket.db";

    public TicketDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TicketContract.SQL_CREATE_TABLE_TICKET);

        Log.i(TAG, "TABLE HAS BEEN CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
