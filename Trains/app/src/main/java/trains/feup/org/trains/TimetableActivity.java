package trains.feup.org.trains;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

import trains.feup.org.trains.model.Departure;
import trains.feup.org.trains.model.Timetable;
import trains.feup.org.trains.model.Trip;

public class TimetableActivity extends AppCompatActivity {

    private Timetable timetable;
    private int way;

    TableLayout tableLayout;
    TextView arrivalCity;
    TextView departureCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        tableLayout = (TableLayout) findViewById(R.id.table_timetable);
        departureCity = (TextView) findViewById(R.id.departure_city);
        arrivalCity = (TextView) findViewById(R.id.arrival_city);

        timetable = (Timetable) getIntent().getSerializableExtra(getString(R.string.timetable_extra));
        way = getIntent().getIntExtra(getString(R.string.timetable_extra_way), 1);


        if (way == Timetable.FORWARD){
            departureCity.setText("Porto");
            arrivalCity.setText("Lisbon");
        } else {
            departureCity.setText("Lisbon");
            arrivalCity.setText("Porto");
        }

        buildTable();

    }

    //TODO. This is in need of some serious refactoring. But wtv.

    private void buildTable(){

        int padding = (int) getResources().getDimension(R.dimen.table_cell_padding);
        //create first row, for Departure Codes
        TableRow headerRow= new TableRow(this);
        TableRow.LayoutParams lp= new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        headerRow.setLayoutParams(lp);

        //dummy cell, for the edge of the table
        headerRow.addView(new TextView(this));

        //first row, for the initial station
        TableRow firstRow= new TableRow(this);
        firstRow.setWeightSum(timetable.getDepartures().size() + 1);
        firstRow.setLayoutParams(lp);

        tableLayout.addView(headerRow);
        tableLayout.addView(firstRow);

        //Iterate over Departures, that are the columns of the Table
        for (int i = 0; i < timetable.getDepartures().size(); i ++) {

            Departure departure = timetable.getDepartures().get(i);

            //This variable helps calculate Times for the different stations
            long cumulativeDuration = 0;

            //Add headers to the header Row
            TextView headerCell = new TextView(this);
            headerCell.setText(timetable.getDepartures().get(i).getCode());
            headerCell.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            headerCell.setGravity(Gravity.CENTER);
            headerCell.setPadding(padding, padding, padding, padding);
            headerCell.setTextColor(getResources().getColor(R.color.white));
            headerCell.setTypeface(Typeface.MONOSPACE);
            headerRow.addView(headerCell);

            //Add the values for Departure Station

            //for the first iteration, we have to get the Code
            if (i == 0) {
                TextView departureNameCell = new TextView(this);
                departureNameCell.setText(departure.getFrom().getCode());
                departureNameCell.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                departureNameCell.setPadding(padding, padding, padding, padding);
                departureNameCell.setTextColor(getResources().getColor(R.color.white));
                departureNameCell.setTypeface(Typeface.MONOSPACE);

                firstRow.addView(departureNameCell);
            }

            TextView departureTime = new TextView(this);
            departureTime.setText(String.format("%02d", new Date(departure.getTime()).getHours()) + ":" + String.format("%02d", new Date(departure.getTime()).getMinutes()));
            departureTime.setBackgroundResource(R.drawable.cell_border);
            departureTime.setPadding(padding, padding, padding, padding);
            departureTime.setGravity(Gravity.CENTER);
            firstRow.addView(departureTime);

            for (int j = 0; j < timetable.getTrips().size(); j++){

                Trip trip = timetable.getTrips().get(j);
                cumulativeDuration += trip.getDuration();

                if (i == 0){

                    //build rows, one for each departure only
                    TableRow row= new TableRow(this);
                    row.setWeightSum(timetable.getDepartures().size() + 1);
                    row.setLayoutParams(lp);

                    //build first cell of each row, with station names;
                    TextView stationNameCell = new TextView(this);
                    stationNameCell.setText(trip.getTo().getCode());
                    stationNameCell.setPadding(padding, padding, padding, padding);
                    stationNameCell.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    stationNameCell.setTextColor(getResources().getColor(R.color.white));
                    stationNameCell.setTypeface(Typeface.MONOSPACE);

                    //  stationNameCell.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    row.addView(stationNameCell);

                    tableLayout.addView(row);

                }

                TableRow tripRow = (TableRow) tableLayout.getChildAt(j + 2);
                TextView cell = new TextView(this);
                cell.setText(String.format("%02d", new Date(departure.getTime() + (cumulativeDuration * 1000)).getHours()) + ":" + String.format("%02d", new Date(departure.getTime() + (cumulativeDuration*1000)).getMinutes()));
                cell.setBackgroundResource(R.drawable.cell_border);
                cell.setGravity(Gravity.CENTER);
                cell.setPadding(padding, padding, padding, padding);

                tripRow.addView(cell);
            }
        }
    }
}
