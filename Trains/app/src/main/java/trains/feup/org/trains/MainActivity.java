package trains.feup.org.trains;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;

import trains.feup.org.trains.api.ServerListCallback;
import trains.feup.org.trains.model.Station;
import trains.feup.org.trains.service.StationService;

public class MainActivity extends DrawerActivity {

    private Spinner fromSpinner;
    private Spinner toSpinner;
    private FloatingActionButton searchButton;

    private ArrayAdapter adapter;
    private Station[] stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        StationService service = new StationService();
        service.getStations(this, new ServerListCallback() {
            @Override
            public void OnSuccess(JSONArray result) {
                Log.i("STATIONS", result.toString());

                Gson gson = new Gson();
                stations = gson.fromJson(result.toString(), Station[].class);

                Date date = stations[0].getCreatedAtDate();

                adapter.addAll(stations);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void OnError(int errorCode) {
                Log.i("STATIONS", String.valueOf(errorCode));
            }
        });


        //ALWAYS DO THIS IN ACTIVITIES WITH DRAWER
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawer.addView(contentView, 0);

        fromSpinner = (Spinner) findViewById(R.id.spinner_from);
        toSpinner = (Spinner) findViewById(R.id.spinner_to);
        searchButton = (FloatingActionButton) findViewById(R.id.search_button);

        adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, new ArrayList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
