package trains.feup.org.trains;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import trains.feup.org.trains.api.ServerObjectCallback;
import trains.feup.org.trains.service.UserService;
import trains.feup.org.trains.util.ProgressHandler;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mRegister;
    private TextView mError;
    private ProgressHandler progress;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkInternetConnection();

        checkLoginStatus();
        showErrorMessageToast(getIntent().getIntExtra(getString(R.string.error_connection), 0));

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mRegister = (TextView) findViewById(R.id.register);
        mError = (TextView) findViewById(R.id.error);

        mRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegisterActivity();
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        progress = new ProgressHandler(mProgressView, mLoginFormView, this);
    }

    private void checkInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null;
        if (isConnected)
            isConnected = activeNetwork.isConnected();

        if (!isConnected) {
            startWalletActivity();
        }
    }

    private void startWalletActivity() {
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mError.setText("");

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

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
            final JSONObject[] jsonObject = new JSONObject[1];

            service.login(getApplicationContext(), email, password, new ServerObjectCallback() {
                @Override
                public void OnSuccess(JSONObject result) {
                    Log.i("Result", result.toString());

                    try {
                        String token = result.getJSONObject("headers").getString("Authorization").substring(7);
                        saveToken(token);
                        startSearchActivity();

                    } catch (JSONException je) {
                        Log.e("TOKEN ERROR", je.toString());
                    }

                    progress.hideProgress();
                }

                @Override
                public void OnError(int error) {
                    Log.i("Result", String.valueOf(error));
                    handleError(error);
                    progress.showProgress(false);
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

    private void startRegisterActivity() {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void startSearchActivity() {

        Intent intent = new Intent(this, SearchTripsActivity.class);
        startActivity(intent);
    }

    private void saveToken(String token) {

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.saved_token), token);
        editor.putString(getString(R.string.saved_username), mEmailView.getText().toString());
        editor.commit();

    }

    private void checkLoginStatus() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String data = sharedPreferences.getString(getString(R.string.saved_token), "");

        if (data != null && !data.isEmpty()) {
            startSearchActivity();
        }

    }

    private void handleError(int error) {
        if (error == ServerObjectCallback.UNAUTHORIZED) {
            mError.setText(getString(R.string.error_unauthorized));
        } else if (error == ServerObjectCallback.NOT_FOUND) {
            mError.setText(getString(R.string.error_connection));
        } else {
            mError.setText(getString(R.string.error_server));
        }
    }

    private void showErrorMessageToast(int errorCode) {

        switch (errorCode) {
            case ServerObjectCallback.NOT_FOUND:
                Toast.makeText(this, "Make sure you have Wifi Connection", Toast.LENGTH_LONG).show();
                break;
            case ServerObjectCallback.UNAUTHORIZED:
                Toast.makeText(this, "Your token has expired", Toast.LENGTH_LONG).show();
                break;
        }
    }

}

