package trains.feup.org.tickets;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import trains.feup.org.tickets.api.ServerCallback;
import trains.feup.org.tickets.model.DepartureDTO;
import trains.feup.org.tickets.model.LineDTO;
import trains.feup.org.tickets.service.LineService;
import trains.feup.org.tickets.util.ProgressHandler;

public class DownloadTicketsActivity extends DrawerActivity {

    private static final String TAG = DownloadTicketsActivity.class.getName();

    private LineService service;

    private Spinner listSpinner;
    private ArrayAdapter<LineDTO> linesAdapter;
    private ArrayList<LineDTO> lines;
    private Long selectedLine;

    private RecyclerView departuresRecyclerView;
    private DepartureAdapter departuresAdapter;
    private HashMap<Long, ArrayList<DepartureDTO>> allDepartures;
    private ArrayList<DepartureDTO> selectedDepartures;

    private ProgressHandler progress;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putLong(getString(R.string.selected_line), selectedLine);
        savedInstanceState.putSerializable(getString(R.string.saved_lines), lines);
        savedInstanceState.putSerializable(getString(R.string.saved_departures), allDepartures);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);

        //ALWAYS DO THIS IN ACTIVITIES WITH DRAWER
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_download_tickets, null, true);
        drawer.addView(contentView, 0);

        if (savedInstanceState != null) {
            lines = (ArrayList<LineDTO>) savedInstanceState.getSerializable(getString(R.string.saved_lines));
            selectedLine = savedInstanceState.getLong(getString(R.string.selected_line));
            allDepartures = (HashMap<Long, ArrayList<DepartureDTO>>) savedInstanceState.getSerializable(getString(R.string.saved_departures));
        }

        service = new LineService(getApplicationContext());

        progress = new ProgressHandler(findViewById(R.id.progress_bar), findViewById(R.id.activity_download_tickets), this);

        listSpinner = (Spinner) findViewById(R.id.select_line_spinner);
        manageLineSpinner();

        allDepartures = new HashMap<>();

        departuresRecyclerView = (RecyclerView) findViewById(R.id.departure_list);
        manageDepartures();

        if (lines == null || lines.isEmpty()) {
            downloadLines();
        } else {
            resetLines();
        }

        if (selectedLine != null && selectedLine > 0L) {
            listSpinner.setSelection(selectedLine.intValue());
            List<DepartureDTO> tempSelectedDepartures = allDepartures.get(selectedLine);
            if (tempSelectedDepartures == null || tempSelectedDepartures.isEmpty()) {
                downloadDepartures();
            } else {
                resetDepartures();
            }
        }

    }

    private void downloadLines() {
        Log.i(TAG, "downloadLines");

        progress.showProgress();

        service.getLines(new ServerCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray result) {
                Log.i(TAG, "onSuccess");


                Gson gson = new Gson();
                LineDTO[] linesArray = gson.fromJson(result.toString(), LineDTO[].class);
                lines = new ArrayList<>(Arrays.asList(linesArray));

//                lines.sort(new Comparator<LineDTO>() {
//                    @Override
//                    public int compare(LineDTO first, LineDTO second) {
//                        return (int) (first.getId() - second.getId());
//                    }
//                });

                resetLines();
            }

            @Override
            public void onError(int errorCode) {
                Log.e(TAG, "onError");
                Log.e(TAG, String.valueOf(errorCode));

                if (errorCode == ServerCallback.UNAUTHORIZED) {
                } else if (errorCode == ServerCallback.NOT_FOUND) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Could not find requested line", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        progress.hideProgress();

    }

    private void manageLineSpinner() {
        Log.i(TAG, "manageLineSpinner");

        linesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<LineDTO>());
        linesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        listSpinner.setAdapter(linesAdapter);
        listSpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
    }

    private void resetLines() {
        Log.i(TAG, "resetLines");

        linesAdapter.clear();
        linesAdapter.addAll(lines);
        linesAdapter.notifyDataSetChanged();
    }

    private void resetDepartures() {
        Log.i(TAG, "resetLines");

        selectedDepartures.clear();
        selectedDepartures.addAll(allDepartures.get(selectedLine));
        departuresAdapter.notifyDataSetChanged();
    }

    private void downloadDepartures() {
        Log.i(TAG, "downloadDepartures");

        progress.showProgress();

        service.getLineDepartures(selectedLine, new ServerCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray result) {
                Log.i(TAG, "onSuccess");

                Gson gson = new Gson();
                DepartureDTO[] departuresArray = gson.fromJson(result.toString(), DepartureDTO[].class);
                ArrayList<DepartureDTO> selectedDepartures = new ArrayList<>(Arrays.asList(departuresArray));

                allDepartures.put(selectedLine, selectedDepartures);

//                allDepartures.get(selectedLine).sort(new Comparator<DepartureDTO>() {
//                    @Override
//                    public int compare(DepartureDTO first, DepartureDTO second) {
//                        return (int) (first.getId() - second.getId());
//                    }
//                });

                resetDepartures();
            }

            @Override
            public void onError(int errorCode) {
                Log.e(TAG, "onError");
                Log.e(TAG, String.valueOf(errorCode));

                if (errorCode == ServerCallback.UNAUTHORIZED) {
                } else if (errorCode == ServerCallback.NOT_FOUND) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Could not find requested line", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        progress.hideProgress();
    }

    private void manageDepartures() {
        Log.i(TAG, "manageDepartures");

        selectedDepartures = new ArrayList<>();

        departuresAdapter = new DepartureAdapter(this, selectedDepartures);
        departuresRecyclerView.setAdapter(departuresAdapter);
    }

    private class SpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView parent, View view, int position, long id) {
            Log.i(TAG, "onItemSelected");
            LineDTO line = (LineDTO) parent.getItemAtPosition(position);
            selectedLine = line.getId();
            downloadDepartures();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}
