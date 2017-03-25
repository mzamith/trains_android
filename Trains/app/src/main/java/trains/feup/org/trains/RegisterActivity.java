package trains.feup.org.trains;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import trains.feup.org.trains.api.ServerObjectCallback;
import trains.feup.org.trains.service.UserService;
import trains.feup.org.trains.util.ProgressHandler;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {

    // UI references.

    //private DatePicker datePicker;
    private final int DATE_PICKER_ID = 999;

    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private EditText mCreditCardNumber;
    private EditText mCreditCardDate;

    private View mProgressView;
    private View mRegisterFormView;
    private TextView mError;

    private ProgressHandler progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupActionBar();

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mError = (TextView) findViewById(R.id.error_text);
        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirmpassword);
        mCreditCardNumber = (EditText) findViewById(R.id.creditcard);
        mCreditCardDate = (EditText) findViewById(R.id.creditcarddate);
        //datePicker = (DatePicker) findViewById(R.id.cardDate);

        mCreditCardDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showDialog(DATE_PICKER_ID);
                } else {

                }
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        progress = new ProgressHandler(mProgressView, mRegisterFormView, this);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword = mConfirmPasswordView.getText().toString();
        String cardNumber = mCreditCardNumber.getText().toString();
        String cardDate = mCreditCardDate.getText().toString();

        Date formattedDate = getValidCardDate(cardDate);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!password.equals(confirmPassword)) {
            mPasswordView.setError(getString(R.string.error_password_match));
            focusView = mPasswordView;
            cancel = true;
        /*} else if (TextUtils.isEmpty(cardNumber)){
            mCreditCardNumber.setError(getString(R.string.error_field_required));
            focusView = mCreditCardNumber;
            cancel = true;
        } else if (TextUtils.isEmpty(cardDate)){
            mCreditCardDate.setError(getString(R.string.error_field_required));
            focusView = mCreditCardDate;
            cancel = true;*/
        } else if (!isCardValid(cardNumber)){
            mCreditCardNumber.setError(getString(R.string.error_invalid_card));
            focusView = mCreditCardNumber;
            cancel = true;
        } else if (formattedDate == null){
            mCreditCardDate.setError(getString(R.string.error_invalid_card_date));
            focusView = mCreditCardDate;
            this.dismissDialog(DATE_PICKER_ID);
            cancel = true;
        } else if (formattedDate.before(new Date())){
            mCreditCardDate.setError(getString(R.string.error_expired_card));
            focusView = mCreditCardDate;
            this.dismissDialog(DATE_PICKER_ID);
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            progress.showProgress();

            UserService service = new UserService();
            JsonObjectRequest registerRequest = service.register(getApplicationContext(), email, password, cardNumber, formattedDate, new ServerObjectCallback() {
                @Override
                public void OnSuccess(JSONObject result) {

                    mError.setText("");

                    Log.i("Result", result.toString());

                    tryLogin();

                    progress.hideProgress();
                }

                @Override
                public void OnError (int errorCode){

                    if (errorCode == ServerObjectCallback.CONFLICT) {
                        mError.setText(R.string.error_conflict);
                    } else{
                        mError.setText(R.string.error_server);
                    }

                    progress.hideProgress();
                }
            });

        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isCardValid(String cardNumber) {

        return cardNumber.length() == 16;
    }

    private Date getValidCardDate(String date){

        try{
            DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
            Date formatedDate = dateFormatter.parse(date);

            return formatedDate;

        } catch (ParseException pe){
            return null;
        }
    }

    private void tryLogin(){

        UserService service = new UserService();
        service.login(this, mEmailView.getText().toString(), mPasswordView.getText().toString(), new ServerObjectCallback() {
            @Override
            public void OnSuccess(JSONObject result) {

                mEmailView.setText("");
                mPasswordView.setText("");

                try {
                    String token = result.getJSONObject("headers").getString("Authorization").substring(7);
                    saveToken(token);
                    startMainActivity();

                } catch (JSONException je){
                    Log.e("TOKEN ERROR", je.toString());
                }

            }

            @Override
            public void OnError(int errorCode) {

                //TODO this is silly, i was lazy
                mError.setText("something");

            }
        });
    }

    private void saveToken(String token){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.saved_token), token);
        editor.putString(getString(R.string.saved_username), mEmailView.getText().toString());
        editor.commit();

    }

    private void startMainActivity(){

        Intent intent = new Intent(this, SearchTripsActivity.class);
        startActivity(intent);
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
            mCreditCardDate.setText(new StringBuilder().append(selectedDay)
                    .append("/").append(selectedMonth + 1).append("/").append(selectedYear));


        }
    };



}
