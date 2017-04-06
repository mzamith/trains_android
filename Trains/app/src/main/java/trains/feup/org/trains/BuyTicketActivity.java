package trains.feup.org.trains;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import trains.feup.org.trains.api.ServerListCallback;
import trains.feup.org.trains.api.ServerObjectCallback;
import trains.feup.org.trains.model.SimpleTrip;
import trains.feup.org.trains.model.Ticket;
import trains.feup.org.trains.model.Travel;
import trains.feup.org.trains.service.TicketService;
import trains.feup.org.trains.util.ProgressHandler;

public class BuyTicketActivity extends AppCompatActivity {

    private ListView timeline;

    private ArrayAdapter<SimpleTrip> adapter;
    private Travel travel;

    TextView detailPrice;
    TextView detailDuration;
    TextView detailTrain;

    FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        timeline = (ListView) findViewById(R.id.timeline_list);

        detailPrice = (TextView) findViewById(R.id.detail_price);
        detailDuration = (TextView) findViewById(R.id.detail_duration);
        detailTrain = (TextView) findViewById(R.id.detail_train);

        button = (FloatingActionButton) findViewById(R.id.buy_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BuyTicketActivity.this, ChooseDayActivity.class);
                intent.putExtra(getString(R.string.extra_travel), travel);
                startActivity(intent);

            }
        });

        travel = (Travel) getIntent().getSerializableExtra(getString(R.string.detail_trip));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_results);
        setSupportActionBar(toolbar);

        TextView departure = (TextView) findViewById(R.id.departure);
        TextView arrival = (TextView) findViewById(R.id.arrival);

        departure.setText(getIntent().getStringExtra(getString(R.string.departure_station_search)));
        arrival.setText(getIntent().getStringExtra(getString(R.string.arrival_station_search)));

        //travel.getTrips().add(new SimpleTrip(travel.getEndTime(), travel.getTo()));

        detailPrice.setText(String.valueOf(travel.getPrice() / 100.0) + "€");
        detailDuration.setText(travel.getDurationString());
        detailTrain.setText(travel.getTrain().getLabel());

        adapter = new TimelineAdapter(travel.getTrips());
        timeline.setAdapter(adapter);
    }


    public class TimelineAdapter extends ArrayAdapter<SimpleTrip> {


        public TimelineAdapter(List<SimpleTrip> items) {
            super(BuyTicketActivity.this, R.layout.timeline_item, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.timeline_item, null);
            }

            SimpleTrip trip = getItem(position);

            if (trip != null) {
                TextView stop = (TextView) v.findViewById(R.id.trip_detail);
                stop.setText(trip.getTimeString() + "    " + trip.getDepartureStation().getLabel());
            }

            return v;
        }

    }



}
