package trains.feup.org.trains;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Calendar;

import trains.feup.org.trains.api.ServerObjectCallback;
import trains.feup.org.trains.model.Account;
import trains.feup.org.trains.model.SimpleAccount;
import trains.feup.org.trains.service.UserService;
import trains.feup.org.trains.util.ProgressHandler;

public class PaymentInfoActivity extends DrawerActivity {

    private final int DATE_PICKER_ID = 998;

    TextView username;
    EditText cardNumber;
    EditText cardDate;
    Button updateProfile;

    private View mProgressView;
    private View mProfileFormView;

    private SimpleAccount account;

    private ProgressHandler progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_payment_info, null, false);
        drawer.addView(contentView, 0);

        username = (TextView) findViewById(R.id.email);
        cardNumber = (EditText) findViewById(R.id.creditcard);
        cardDate = (EditText) findViewById(R.id.creditcarddate);
        updateProfile = (Button) findViewById(R.id.update_profile_button);

        cardDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showDialog(DATE_PICKER_ID);
                } else {

                }
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleAccount updatedAccount = new SimpleAccount(account.getUsername(), cardNumber.getText().toString(), cardDate.getText().toString());
                if (!updatedAccount.equals(account)) {

                    UserService service = new UserService();
                    service.updateProfile(PaymentInfoActivity.this, updatedAccount, new ServerObjectCallback() {
                        @Override
                        public void OnSuccess(JSONObject result) {
                            Toast.makeText(PaymentInfoActivity.this, "Successfully updated payment information", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnError(int errorCode) {
                            Log.e("ERROR", String.valueOf(errorCode));
                        }
                    });

                } else {
                    Toast.makeText(PaymentInfoActivity.this, "No information was changed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mProfileFormView = findViewById(R.id.drawer_layout);
        mProgressView = findViewById(R.id.profile_progress);

        progress = new ProgressHandler(mProgressView, mProfileFormView, this);

        getProfile();
    }

    private void getProfile(){

        UserService service = new UserService();
        service.getProfile(this, new ServerObjectCallback() {
            @Override
            public void OnSuccess(JSONObject result) {

                Gson gson = new Gson();
                account = gson.fromJson(result.toString(), SimpleAccount.class);

                username.setText(account.getUsername());
                cardNumber.setText(account.getCardNumber());
                cardDate.setText(account.getCardDateString());

            }

            @Override
            public void OnError(int errorCode) {

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                // set date picker as current date

                final Calendar c = Calendar.getInstance();

                return new DatePickerDialog(this, datePickerListener,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            // set selected date into textview
            cardDate.setText(new StringBuilder().append(selectedDay)
                    .append("/").append(selectedMonth + 1).append("/").append(selectedYear));

        }
    };
}
