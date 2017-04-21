package trains.feup.org.trains;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import trains.feup.org.trains.api.ServerListCallback;
import trains.feup.org.trains.api.ServerObjectCallback;
import trains.feup.org.trains.model.Departure;
import trains.feup.org.trains.model.Station;
import trains.feup.org.trains.model.Ticket;
import trains.feup.org.trains.service.TicketService;
import trains.feup.org.trains.storage.TicketsContract;
import trains.feup.org.trains.storage.TicketsDbHelper;

import static trains.feup.org.trains.TrainsApp.getContext;

public class WalletActivity extends DrawerActivity {

    private static final String TAG = WalletActivity.class.getName();

    ListView ticketList;
    ArrayList<Ticket> tickets = new ArrayList<>();

    ArrayAdapter<Ticket> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_wallet, null, false);
        drawer.addView(contentView, 0);

        ticketList = (ListView) findViewById(R.id.tickets_list);

        adapter = new TicketsAdapter(new ArrayList<Ticket>());
        ticketList.setAdapter(adapter);

        ticketList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

                Ticket item = (Ticket) adapter.getItemAtPosition(position);

                Intent intent = new Intent(WalletActivity.this, TicketDetailActivity.class);

                intent.putExtra(getString(R.string.extra_ticket), item.getId());
                startActivity(intent);
            }
        });


//        checkConnectionErrors();

        Log.i(TAG, "verifying internet connection");

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            Log.i(TAG, "internet connection is available");
            Log.i(TAG, "populating from server");
            populateFromServer();
        } else {
            Log.i(TAG, "internet connection is not available");
            Log.i(TAG, "populating from local storage");
            populateFromLocalStorage();
        }

    }

    private void populateFromLocalStorage() {
        Log.i(TAG, "populateFromLocalStorage");

        tickets = new ArrayList<>();

        adapter.clear();

        TicketsDbHelper ticketsDbHelper = new TicketsDbHelper(getContext());
        try (SQLiteDatabase db = ticketsDbHelper.getReadableDatabase()) {

            db.beginTransaction();

            String[] projection = {
                    TicketsContract.TicketEntry.COLUMN_ID,
                    TicketsContract.TicketEntry.COLUMN_DATE,
                    TicketsContract.TicketEntry.COLUMN_DEPARTURE,
                    TicketsContract.TicketEntry.COLUMN_DEPARTURE_TIME,
                    TicketsContract.TicketEntry.COLUMN_DESTINATION,
                    TicketsContract.TicketEntry.COLUMN_PRICE,
                    TicketsContract.TicketEntry.COLUMN_STATE,
                    TicketsContract.TicketEntry.COLUMN_CODE_DTO
            };
            String sortOrder = TicketsContract.TicketEntry.COLUMN_ID + " DESC";

            Cursor cursor = db.query(
                    TicketsContract.TicketEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );

            while (cursor.moveToNext()) {
                Station from = new Station();
                from.setLabel(cursor.getString(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_DEPARTURE)));
                Departure departure = new Departure();
                departure.setFrom(from);
                departure.setTime(cursor.getLong(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_DEPARTURE_TIME)));

                Station to = new Station();
                to.setLabel(cursor.getString(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_DESTINATION)));

                Ticket temp = new Ticket();
                temp.setId(cursor.getLong(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_ID)));
                temp.setDay(cursor.getLong(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_DATE)));
                temp.setDeparture(departure);
                temp.setTo(to);
                temp.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_PRICE)));
                temp.setState(cursor.getString(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_STATE)));
                temp.setCodeDTO(cursor.getString(cursor.getColumnIndexOrThrow(TicketsContract.TicketEntry.COLUMN_CODE_DTO)));

                tickets.add(temp);
            }

            cursor.close();

            db.setTransactionSuccessful();
            db.endTransaction();

            db.close();

        } catch (SQLiteAbortException e) {
            Log.e("ERROR", e.getStackTrace().toString());
        }
        ticketsDbHelper.close();

        adapter.addAll(tickets);
        adapter.notifyDataSetChanged();
    }

    private void populateFromServer() {
        Log.i(TAG, "populateFromServer");

        TicketService service = TicketService.getInstance();

        service.getTickets(this, new ServerListCallback() {

            @Override
            public void OnSuccess(JSONArray result) {

                // continue here implementing the sync from external database to local 


                adapter.clear();

                Gson gson = new Gson();
                Ticket[] ticketsArray = gson.fromJson(result.toString(), Ticket[].class);
                tickets = new ArrayList<>(Arrays.asList(ticketsArray));

                TicketsDbHelper ticketsDbHelper = new TicketsDbHelper(getContext());
                try (SQLiteDatabase db = ticketsDbHelper.getWritableDatabase()) {
                    ticketsDbHelper.restart(db);
                    Log.i(TAG, "restarted database");
                    db.beginTransaction();
                    for (Ticket ticket : tickets) {
                        //Add Ticket to Local Storage
                        ContentValues values = new ContentValues();

                        values.put(TicketsContract.TicketEntry.COLUMN_ID, ticket.getId());
                        values.put(TicketsContract.TicketEntry.COLUMN_DATE, ticket.getDay());
                        values.put(TicketsContract.TicketEntry.COLUMN_DEPARTURE, ticket.getDeparture().getFrom().toString());
                        values.put(TicketsContract.TicketEntry.COLUMN_DEPARTURE_TIME, ticket.getDeparture().getTime());
                        values.put(TicketsContract.TicketEntry.COLUMN_DESTINATION, ticket.getTo().toString());
                        values.put(TicketsContract.TicketEntry.COLUMN_PRICE, ticket.getPrice());
                        values.put(TicketsContract.TicketEntry.COLUMN_STATE, ticket.getState());
                        values.put(TicketsContract.TicketEntry.COLUMN_CODE_DTO, ticket.getCodeDTO());


                        db.insert(TicketsContract.TicketEntry.TABLE_NAME, null, values);
                        Log.i("SQLite Tickets", "Ticket inserted into local SQLite database");

                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    db.close();

                } catch (SQLiteAbortException e) {
                    Log.e("ERROR", e.getStackTrace().toString());
                }
                ticketsDbHelper.close();

                adapter.addAll(tickets);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void OnError(int errorCode) {

            }
        });
    }

    private void checkConnectionErrors() {

        int errorCode = getIntent().getIntExtra(getString(R.string.error_connection), 0);

        switch (errorCode) {
            case ServerObjectCallback.NOT_FOUND:
                Toast.makeText(this, "Make sure you have Wifi Connection", Toast.LENGTH_LONG).show();
                break;
        }
    }


    public class TicketsAdapter extends ArrayAdapter<Ticket> {

        public TicketsAdapter(List<Ticket> items) {
            super(WalletActivity.this, R.layout.ticket_row, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.ticket_row, null);
            }

            final Ticket ticket = getItem(position);

            if (ticket != null) {
                View colorBar = v.findViewById(R.id.color_ticket);
                TextView departure = (TextView) v.findViewById(R.id.ticket_departure);
                TextView arrival = (TextView) v.findViewById(R.id.ticket_arrival);
                TextView date = (TextView) v.findViewById(R.id.ticket_day);

                departure.setText(ticket.getFrom().getLabel());
                arrival.setText(ticket.getTo().getLabel());

                DateFormat df = new SimpleDateFormat("dd/MM/yy");
                date.setText(df.format(new Date(ticket.getDay())));

                switch (ticket.getState()) {
                    case "RESERVED":
                        colorBar.setBackgroundColor(getColor(R.color.amber_700));
                        break;
                    case "DENIED":
                        colorBar.setBackgroundColor(getColor(R.color.red_700));
                        break;
                    case "VALIDATED":
                        colorBar.setBackgroundColor(getColor(R.color.green_700));
                        break;
                }

            }

            return v;
        }

    }
}
