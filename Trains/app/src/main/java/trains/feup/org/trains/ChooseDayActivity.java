package trains.feup.org.trains;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import trains.feup.org.trains.api.ServerListCallback;
import trains.feup.org.trains.api.ServerObjectCallback;
import trains.feup.org.trains.model.Ticket;
import trains.feup.org.trains.model.Travel;
import trains.feup.org.trains.service.TicketService;
import trains.feup.org.trains.storage.TicketsContract;
import trains.feup.org.trains.storage.TicketsDbHelper;

public class ChooseDayActivity extends AppCompatActivity {

    private long date;
    private Travel travel;

    DatePicker datePicker;
    FloatingActionButton buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_day);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        date = today.getTime().getTime();

        datePicker = (DatePicker) findViewById(R.id.date_picker);
        buyButton = (FloatingActionButton) findViewById(R.id.buy_button);

        travel = (Travel) getIntent().getSerializableExtra(getString(R.string.extra_travel));

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Ticket ticket = new Ticket(travel);
                ticket.setDay(date);

                TicketService service = TicketService.getInstance();
                service.buyTicket(ChooseDayActivity.this, ticket, new ServerObjectCallback() {
                    @Override
                    public void OnSuccess(JSONObject result) {

                        Gson gson = new Gson();
                        Ticket ticket = gson.fromJson(result.toString(), Ticket.class);

                        //Add Ticket to Local Storage
                        TicketsDbHelper ticketsDbHelper = new TicketsDbHelper(TrainsApp.getContext());

                        try (SQLiteDatabase db = ticketsDbHelper.getWritableDatabase()) {

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

                        } catch(SQLiteAbortException e){
                            Log.e("ERROR", e.getStackTrace().toString());
                        }

                        ticketsDbHelper.close();

                        Intent intent = new Intent(ChooseDayActivity.this, WalletActivity.class);
                        startActivity(intent);

                        Log.i("success", "success");
                    }

                    @Override
                    public void OnError(int errorCode) {

                        Vibrator v = (Vibrator) ChooseDayActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(1000);

                        if (errorCode == ServerListCallback.PRECONDITION_FAILED){

                            showPaymentDialog();
                        } else if (errorCode == ServerListCallback.NOT_ACCEPTABLE){

                            showFullCapacityDialog();
                        }
                        Log.e("fail", "fail");
                    }
                });

            }
        });


        DatePicker.OnDateChangedListener listener = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar pickedDate = Calendar.getInstance();
                pickedDate.set(year, monthOfYear, dayOfMonth, 0, 0, 0);

                date = pickedDate.getTime().getTime();
            }
        };

        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        int day = today.get(Calendar.DAY_OF_MONTH);

        datePicker.init(year, month, day, listener);



    }

    private void showPaymentDialog(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ChooseDayActivity.this);

        // set title
        alertDialogBuilder.setTitle("Payment Failed");

        // set dialog message
        alertDialogBuilder
                .setMessage("Oops. There seems to be a problem with your credit card information.")
                .setCancelable(false)
                .setPositiveButton("Update Payment Information", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(ChooseDayActivity.this, PaymentInfoActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void showFullCapacityDialog(){

        AlertDialog.Builder fullCapacityDialogBuilder = new AlertDialog.Builder(
                ChooseDayActivity.this);

        // set title
        fullCapacityDialogBuilder.setTitle("Train is Full");

        // set dialog message
        fullCapacityDialogBuilder
                .setMessage("Oops. It seems the train is full, for this departure on some stations. Try picking another date.")
                .setCancelable(false)

                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = fullCapacityDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
