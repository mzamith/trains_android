package trains.feup.org.tickets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import trains.feup.org.tickets.model.DepartureDTO;
import trains.feup.org.tickets.model.TicketInspectorDTO;
import trains.feup.org.tickets.storage.TicketContract;
import trains.feup.org.tickets.storage.TicketDbHelper;
import trains.feup.org.tickets.util.Decrypter;

public class ValidateTicketActivity extends DrawerActivity {

    private static final String TAG = ValidateTicketActivity.class.getName();

    private Spinner departureSpinner;
    private ArrayAdapter<DepartureDTO> departuresAdapter;
    private ArrayList<DepartureDTO> departures;
    private Long selectedDeparture;

    private Button validateButton;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putLong(getString(R.string.selected_departure), selectedDeparture);
        savedInstanceState.putSerializable(getString(R.string.saved_departures), departures);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        //ALWAYS DO THIS IN ACTIVITIES WITH DRAWER
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_validate_ticket, null, true);
        drawer.addView(contentView, 0);

        if (savedInstanceState != null) {
            selectedDeparture = savedInstanceState.getLong(getString(R.string.selected_departure));
            departures = (ArrayList<DepartureDTO>) savedInstanceState.getSerializable(getString(R.string.saved_departures));
        }

        departureSpinner = (Spinner) findViewById(R.id.select_departure_spinner);
        manageDepartureSpinner();

        if (departures == null || departures.isEmpty()) {
            List<DepartureDTO> temps = getDeparturesFromDatabase();
            if (temps == null || temps.isEmpty()) {
                Intent intent = new Intent(this, DownloadTicketsActivity.class);
                startActivity(intent);
                return;
            }
            departures = new ArrayList<>(temps);
            resetDepartures();
        }

        validateButton = (Button) findViewById(R.id.validate_ticket_button);

        if (selectedDeparture == null || selectedDeparture < 1) {
            validateButton.setClickable(false);
        }
    }

    public void validateTicket(View view) {
        final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                act.startActivity(intent);
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i(TAG, "onActivityResult");
        TicketInspectorDTO content = null;
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String rawContents = intent.getStringExtra("SCAN_RESULT");
                if (rawContents == null || rawContents.isEmpty()) {
                    return;
                }
//                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.i(TAG, "Raw Contents: " + rawContents);

                try {
                    content = Decrypter.decrypt(rawContents);
                } catch (JsonSyntaxException e) {
                    Toast toast = Toast.makeText(this, "Invalid!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                Log.i(TAG, "Content: " + content);
            }
        }

        if (content == null) {
            Toast toast = Toast.makeText(this, "Invalid!", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        verifyTicket(content);
    }

    private void verifyTicket(TicketInspectorDTO content) {
        Log.i(TAG, "verifyTicket");
        try {
            TicketDbHelper helper = new TicketDbHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();

            Log.i(TAG, "got helper and db");

            String[] projection = {
                    TicketContract.TicketEntry.COLUMN_DEPARTURE_ID,
                    TicketContract.TicketEntry.COLUMN_USERNAME
            };

            String selection = TicketContract.TicketEntry._ID + " = ?";

            String[] selectionsArgs = {String.valueOf(content.getTicket())};

            Cursor cursor = db.query(
                    TicketContract.TicketEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionsArgs,
                    null,
                    null,
                    null);

            Log.i(TAG, "got cursor");

            int count = cursor.getCount();

            if (count == 0) {
                Log.i(TAG, "cursor count is zero");
                Toast toast = Toast.makeText(this, "Invalid!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            Log.i(TAG, "cursor row count: " + count);

            Log.i(TAG, "creating ticket");

            cursor.moveToNext();

            TicketInspectorDTO temp = new TicketInspectorDTO();

            temp.setDeparture(cursor.getLong(cursor.getColumnIndexOrThrow(TicketContract.TicketEntry.COLUMN_DEPARTURE_ID)));
            temp.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(TicketContract.TicketEntry.COLUMN_USERNAME)));

            cursor.close();
            Log.i(TAG, "departure: " + temp.getDeparture());
            Log.i(TAG, "username: " + temp.getUsername());

            db.close();

            helper.close();

            if (!temp.getDeparture().equals(selectedDeparture)
                    || !content.getDeparture().equals(selectedDeparture)
                    || !temp.getUsername().equals(content.getUsername())) {
                Toast toast = Toast.makeText(this, "Invalid!", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            Toast toast = Toast.makeText(this, "Invalid!", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        Toast toast = Toast.makeText(this, "Valid!", Toast.LENGTH_LONG);
        toast.show();
    }

    private List<DepartureDTO> getDeparturesFromDatabase() {
        Log.i(TAG, "getDeparturesFromDatabase");

        List<DepartureDTO> departures = new ArrayList<>();

        String[] projection = {
                TicketContract.TicketEntry.COLUMN_DEPARTURE_ID,
                TicketContract.TicketEntry.COLUMN_DEPARTURE
        };


        try {
            TicketDbHelper helper = new TicketDbHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();

            Cursor cursor = db.query(
                    true,
                    TicketContract.TicketEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);

            while (cursor.moveToNext()) {
                DepartureDTO temp = new DepartureDTO();
                temp.setId(cursor.getLong(cursor.getColumnIndexOrThrow(TicketContract.TicketEntry.COLUMN_DEPARTURE_ID)));
                temp.setLabel(cursor.getString(cursor.getColumnIndexOrThrow(TicketContract.TicketEntry.COLUMN_DEPARTURE)));
                departures.add(temp);
            }
            cursor.close();

            db.close();

            helper.close();

            if (departures.isEmpty()) {
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return departures;
    }

    private void manageDepartureSpinner() {
        Log.i(TAG, "manageDepartureSpinner");

        departuresAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<DepartureDTO>());
        departuresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        departureSpinner.setAdapter(departuresAdapter);
        departureSpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
    }

    private void resetDepartures() {
        Log.i(TAG, "resetDepartures");

        departuresAdapter.clear();
        departuresAdapter.addAll(departures);
        departuresAdapter.notifyDataSetChanged();
    }

    private class SpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView parent, View view, int position, long id) {
            Log.i(TAG, "onItemSelected");
            DepartureDTO departure = (DepartureDTO) parent.getItemAtPosition(position);
            selectedDeparture = departure.getId();
            validateButton.setClickable(true);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

}
