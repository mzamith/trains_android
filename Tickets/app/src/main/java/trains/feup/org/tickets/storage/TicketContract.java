package trains.feup.org.tickets.storage;

import android.provider.BaseColumns;

/**
 * Created by Renato on 4/19/2017.
 */

public class TicketContract {

    private TicketContract() {
    }

    public static class TicketEntry implements BaseColumns {
        public static final String TABLE_NAME = "Ticket";
        public static final String COLUMN_DEPARTURE_ID = "departure_id";
        public static final String COLUMN_DEPARTURE = "departure";
        public static final String COLUMN_USERNAME = "username";
    }

    public static final String SQL_CREATE_TABLE_TICKET =
            "CREATE TABLE " + TicketEntry.TABLE_NAME + " (" +
                    TicketEntry._ID + " INTEGER PRIMARY KEY," +
                    TicketEntry.COLUMN_DEPARTURE_ID + " INTEGER," +
                    TicketEntry.COLUMN_DEPARTURE + " TEXT," +
                    TicketEntry.COLUMN_USERNAME + " TEXT" +
                    " );";

}
