package trains.feup.org.trains;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

import trains.feup.org.trains.api.ServerListCallback;
import trains.feup.org.trains.model.Station;
import trains.feup.org.trains.model.Travel;
import trains.feup.org.trains.service.StationService;
import trains.feup.org.trains.service.TripService;
import trains.feup.org.trains.util.ProgressHandler;

public class SearchTripsActivity extends DrawerActivity {

    private Spinner fromSpinner;
    private Spinner toSpinner;
    private FloatingActionButton searchButton;
    private ImageView imageView;

    private ArrayAdapter adapter;
    private ArrayList<Station> stations;

    private View mProgressView;

    private View linearLayout;
    private ProgressHandler progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) stations = (ArrayList) savedInstanceState.getSerializable(getString(R.string.saved_stations));

        if(stations == null || stations.isEmpty()) {

            StationService service = new StationService();
            service.getStations(this, new ServerListCallback() {
                @Override
                public void OnSuccess(JSONArray result) {
                    Log.i("STATIONS", result.toString());

                    Gson gson = new Gson();
                    Station[] stationsArray = gson.fromJson(result.toString(), Station[].class);
                    stations = new ArrayList<>(Arrays.asList(stationsArray));

                    adapter.addAll(stations);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void OnError(int errorCode) {
                    Log.i("STATIONS", String.valueOf(errorCode));
                }
            });
        }

        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (fromSpinner.getSelectedItemId() == toSpinner.getSelectedItemId()){
                    searchButton.setEnabled(false);
                    searchButton.setAlpha((float) 0.5);
                } else {
                    searchButton.setEnabled(true);
                    searchButton.setAlpha((float) 1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        //ALWAYS DO THIS IN ACTIVITIES WITH DRAWER
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawer.addView(contentView, 0);

        fromSpinner = (Spinner) findViewById(R.id.spinner_from);
        toSpinner = (Spinner) findViewById(R.id.spinner_to);
        searchButton = (FloatingActionButton) findViewById(R.id.search_button);
        imageView = (ImageView) findViewById(R.id.jumbotron);
        linearLayout = findViewById(R.id.inner_linear);
        mProgressView = findViewById(R.id.search_progress);


        //Hide image when on Landscape
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageView.setVisibility(View.GONE);
        } else{
            imageView.setVisibility(View.VISIBLE);
        }

        searchButton.setEnabled(false);

        adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, new ArrayList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        fromSpinner.setOnItemSelectedListener(spinnerListener);
        toSpinner.setOnItemSelectedListener(spinnerListener);

        //if we got stations from savedInstance
        if (stations != null){
            adapter.addAll(stations);
            adapter.notifyDataSetChanged();
        }

        progress = new ProgressHandler(mProgressView, linearLayout, this);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.showProgress();

                TripService tripService = new TripService();
                final Station originStation = (Station) fromSpinner.getSelectedItem();
                final Station destinationStation = (Station) toSpinner.getSelectedItem();

                tripService.getTravels(SearchTripsActivity.this, originStation.getId(), destinationStation.getId(), new ServerListCallback() {
                    @Override
                    public void OnSuccess(JSONArray result) {

                        Gson gson = new Gson();
                        Travel[] travelsArray = gson.fromJson(result.toString(), Travel[].class);
                        ArrayList<Travel> travels = new ArrayList<>(Arrays.asList(travelsArray));

                        String a =  travels.get(0).getDurationString();

                        Intent intent = new Intent(SearchTripsActivity.this, ResultsActivity.class);
                        intent.putExtra(getString(R.string.search_result), travels);
                        intent.putExtra(getString(R.string.departure_station_search), originStation.getLabel());
                        intent.putExtra(getString(R.string.arrival_station_search), destinationStation.getLabel());

                        progress.hideProgress();

                        startActivity(intent);

                    }

                    @Override
                    public void OnError(int errorCode) {

                        progress.hideProgress();

                    }
                });
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putSerializable(getString(R.string.saved_stations), stations);
    }
}
