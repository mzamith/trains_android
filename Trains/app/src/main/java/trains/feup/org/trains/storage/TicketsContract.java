package trains.feup.org.trains.storage;

import android.provider.BaseColumns;

import trains.feup.org.trains.model.Ticket;

/**
 * Created by leonardoaxe on 04/04/2017.
 */

public final class TicketsContract {

    public TicketsContract() {
    }

    public static class TicketEntry implements BaseColumns {
        public static final String TABLE_NAME = "Tickets";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_DATE = "effective_date";
        public static final String COLUMN_DEPARTURE = "from_station";
        public static final String COLUMN_TO_STATION = "to_station";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_STATE = "current_state";


    }

    public static final String TEST_CREATE = "CREATE TABLE Tickets (TicketID int);";

    public static final String SQL_CREATE_TABLE_TICKETS =
                "CREATE TABLE " + TicketEntry.TABLE_NAME + " ( " +
                        TicketEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        TicketEntry.COLUMN_DATE + " INTEGER, " +
                        TicketEntry.COLUMN_DEPARTURE + " TEXT, " +
                        TicketEntry.COLUMN_TO_STATION + " TEXT, " +
                        TicketEntry.COLUMN_PRICE + " REAL, " +
                        TicketEntry.COLUMN_STATE + " TEXT " +
                        " );";
}
