package trains.feup.org.trains;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import trains.feup.org.trains.api.ServerListCallback;
import trains.feup.org.trains.api.ServerObjectCallback;
import trains.feup.org.trains.model.Ticket;
import trains.feup.org.trains.model.Travel;
import trains.feup.org.trains.service.TicketService;

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

                        //TODO
                        Toast.makeText(ChooseDayActivity.this, "SHOW TICKET DETAIL WITH QR CODE", Toast.LENGTH_LONG).show();

                        Log.i("success", "success");
                    }

                    @Override
                    public void OnError(int errorCode) {

                        if (errorCode == ServerListCallback.PRECONDITION_FAILED){
                            Vibrator v = (Vibrator) ChooseDayActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(1000);
                            showPaymentDialog();
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
}
