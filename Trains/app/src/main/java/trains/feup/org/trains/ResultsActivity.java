package trains.feup.org.trains;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import trains.feup.org.trains.model.Travel;

public class ResultsActivity extends AppCompatActivity {

    ListView resultsList;
    TextView departure;
    TextView arrival;

    private ArrayList<Travel> travels;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsList = (ListView) findViewById(R.id.travels_list);

        travels = (ArrayList<Travel>) getIntent().getSerializableExtra(getString(R.string.search_result));

        adapter = new TravelsAdapter(travels);
        resultsList.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_results);
        setSupportActionBar(toolbar);

        departure = (TextView) findViewById(R.id.departure);
        arrival = (TextView) findViewById(R.id.arrival);

        departure.setText(getIntent().getStringExtra(getString(R.string.departure_station_search)));
        arrival.setText(getIntent().getStringExtra(getString(R.string.arrival_station_search)));

    }

    public class TravelsAdapter extends ArrayAdapter<Travel> {


        public TravelsAdapter(List<Travel> items) {
            super(ResultsActivity.this, R.layout.result_row, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.result_row, null);
            }

            final Travel travel = getItem(position);

            if (travel != null) {
                final TextView departureTime = (TextView) v.findViewById(R.id.departure_time);
                TextView arrivalTime = (TextView) v.findViewById(R.id.arrival_time);
                TextView train = (TextView) v.findViewById(R.id.train);
                TextView price = (TextView) v.findViewById(R.id.price);
                TextView duration = (TextView) v.findViewById(R.id.duration);
                ImageButton button = (ImageButton) v.findViewById(R.id.btn_arrow);

                departureTime.setText(travel.getStartTimeString());
                arrivalTime.setText(travel.getEndTimeString());
                train.setText(travel.getTrain().getCode());
                price.setText(String.valueOf(travel.getPrice() / 100.0));
                duration.setText(travel.getDurationString());

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ResultsActivity.this, BuyTicketActivity.class);
                        intent.putExtra(getString(R.string.detail_trip), travel);
                        intent.putExtra(getString(R.string.departure_station_search), departure.getText().toString());
                        intent.putExtra(getString(R.string.arrival_station_search), arrival.getText().toString());

                        startActivity(intent);
                    }
                });


            }

            return v;
        }

    }
}
