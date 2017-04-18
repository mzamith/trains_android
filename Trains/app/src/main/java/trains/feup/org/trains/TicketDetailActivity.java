package trains.feup.org.trains;

import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import trains.feup.org.trains.storage.TicketsContract;
import trains.feup.org.trains.storage.TicketsDbHelper;

import static trains.feup.org.trains.TrainsApp.getContext;

public class TicketDetailActivity extends DrawerActivity {
    //ImageView qrCodeImageview;

    private String id;
    private String departure;
    private String destination;
    private String date;
    private long price;
    private TextView departureView;
    private TextView destinationView;
    private TextView dateView;
    private TextView priceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TicketsDbHelper ticketsDbHelper = new TicketsDbHelper(getContext());

        try (SQLiteDatabase db = ticketsDbHelper.getReadableDatabase()) {
            Cursor cursor;

            String[] projection = {
                    TicketsContract.TicketEntry.COLUMN_ID,
                    TicketsContract.TicketEntry.COLUMN_DATE,
                    TicketsContract.TicketEntry.COLUMN_DEPARTURE,
                    TicketsContract.TicketEntry.COLUMN_TO_STATION,
                    TicketsContract.TicketEntry.COLUMN_PRICE,
                    TicketsContract.TicketEntry.COLUMN_STATE
            };

            String selection = TicketsContract.TicketEntry.COLUMN_ID + " = ?";

            long TicketID = getIntent().getExtras().getLong("Ticket_ID");
            id = String.valueOf(TicketID);

            String[] selectionArgs = {id};

            String sortOrder = TicketsContract.TicketEntry.COLUMN_ID + " DESC";

            cursor = db.query(
                    TicketsContract.TicketEntry.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            cursor.moveToNext();
            departure = cursor.getString(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_DEPARTURE));
            destination = cursor.getString(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_TO_STATION));
            date = cursor.getString(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_DATE));
            price = cursor.getLong(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_PRICE));
            cursor.close();
        } catch (SQLiteAbortException e){
            Log.e("SQLLite:", e.getStackTrace().toString());
        }


        departureView = (TextView) findViewById(R.id.ticket_departure);
        destinationView = (TextView) findViewById(R.id.ticket_destination);
        dateView = (TextView) findViewById(R.id.ticket_date);
        priceView = (TextView) findViewById(R.id.ticket_price);

        departureView.setText(departure);
        destinationView.setText(destination);
        dateView.setText(date);
        priceView.setText(String.valueOf(price));

        //setContentView(R.layout.activity_ticket_detail);

        //qrCodeImageview=(ImageView) findViewById(R.id.img_qr_code_image);
    }
}
