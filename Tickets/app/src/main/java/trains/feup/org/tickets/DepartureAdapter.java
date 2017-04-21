package trains.feup.org.tickets;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.List;

import trains.feup.org.tickets.api.ServerCallback;
import trains.feup.org.tickets.model.DepartureDTO;
import trains.feup.org.tickets.model.TicketInspectorDTO;
import trains.feup.org.tickets.service.DepartureService;
import trains.feup.org.tickets.storage.TicketContract;
import trains.feup.org.tickets.storage.TicketDbHelper;

/**
 * Created by Renato on 4/11/2017.
 */

public class DepartureAdapter extends RecyclerView.Adapter<DepartureHolder> {

    private static final String TAG = DepartureAdapter.class.getName();

    private Context context;

    private final List<DepartureDTO> departures;

    public DepartureAdapter(Context context, List<DepartureDTO> departures) {
        this.context = context;
        this.departures = departures;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public DepartureHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View departureView = inflater.inflate(R.layout.departure_list_item, parent, false);

        return new DepartureHolder(departureView);
    }

    @Override
    public void onBindViewHolder(DepartureHolder holder, int position) {

        final DepartureDTO departure = departures.get(position);

        TextView departureName = holder.getDepartureName();
        departureName.setText(departure.makeName());

        TextView departureFrom = holder.getDepartureFrom();
        departureFrom.setText(departure.getFrom().makeName());

        TextView departureHour = holder.getDepartureHour();
        departureHour.setText(departure.getTime().toString());

        Button button = holder.getDownloadTicketsButton();
        button.setClickable(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                String message = "Downloading tickets from departure " + departure.makeName();
                progressDialog.setMessage(message);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();

                DepartureService service = new DepartureService(context);

                service.getDepartureTickets(departure.getId(), new ServerCallback<JSONArray>() {
                    @Override
                    public void onSuccess(JSONArray result) {

                        Gson gson = new Gson();
                        TicketInspectorDTO[] tickets = gson.fromJson(result.toString(), TicketInspectorDTO[].class);

                        try {
                            TicketDbHelper helper = new TicketDbHelper(context);
                            SQLiteDatabase db = helper.getWritableDatabase();

                            db.beginTransaction();

                            for (TicketInspectorDTO ticket : tickets) {

                                Log.i(TAG, "Ticket: " + ticket.getTicket());

                                ContentValues values = new ContentValues();

                                values.put(TicketContract.TicketEntry._ID, ticket.getTicket());
                                values.put(TicketContract.TicketEntry.COLUMN_DEPARTURE_ID, ticket.getDeparture());
                                values.put(TicketContract.TicketEntry.COLUMN_DEPARTURE, ticket.getDepartureLabel());
                                values.put(TicketContract.TicketEntry.COLUMN_USERNAME, ticket.getUsername());

                                db.insert(TicketContract.TicketEntry.TABLE_NAME, null, values);
                            }

                            db.setTransactionSuccessful();
                            db.endTransaction();

                            db.close();

                            helper.close();
                        } catch (Exception e) {
                            Log.e(TAG, e.getLocalizedMessage());
                        }

                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(int errorCode) {

                        progressDialog.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return departures.size();
    }
}
