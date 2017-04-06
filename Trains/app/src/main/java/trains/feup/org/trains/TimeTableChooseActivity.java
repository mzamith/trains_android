package trains.feup.org.trains;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import org.json.JSONObject;

import trains.feup.org.trains.api.ServerObjectCallback;
import trains.feup.org.trains.model.Timetable;
import trains.feup.org.trains.service.TripService;

public class TimeTableChooseActivity extends DrawerActivity {

    Button forwardButton;
    Button backwardsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_time_table_choose, null, false);
        drawer.addView(contentView, 0);

        forwardButton = (Button) findViewById(R.id.forward_button);
        backwardsButton = (Button) findViewById(R.id.backwards_button);

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TripService service = new TripService();
                service.getTimetable(TimeTableChooseActivity.this, Timetable.FORWARD, new ServerObjectCallback() {
                    @Override
                    public void OnSuccess(JSONObject result) {

                        Gson gson = new Gson();
                        Timetable timetable = gson.fromJson(result.toString(), Timetable.class);

                        Intent intent = new Intent(TimeTableChooseActivity.this, TimetableActivity.class);
                        intent.putExtra(getString(R.string.timetable_extra), timetable);
                        intent.putExtra(getString(R.string.timetable_extra_way), Timetable.FORWARD);
                        startActivity(intent);

                    }

                    @Override
                    public void OnError(int errorCode) {

                    }
                });
            }
        });

        backwardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TripService service = new TripService();
                service.getTimetable(TimeTableChooseActivity.this, Timetable.BACKWARDS, new ServerObjectCallback() {
                    @Override
                    public void OnSuccess(JSONObject result) {

                        Gson gson = new Gson();
                        Timetable timetable = gson.fromJson(result.toString(), Timetable.class);

                        Intent intent = new Intent(TimeTableChooseActivity.this, TimetableActivity.class);
                        intent.putExtra(getString(R.string.timetable_extra), timetable);
                        intent.putExtra(getString(R.string.timetable_extra_way), Timetable.BACKWARDS);
                        startActivity(intent);

                    }

                    @Override
                    public void OnError(int errorCode) {

                    }
                });
            }
        });


    }
}
