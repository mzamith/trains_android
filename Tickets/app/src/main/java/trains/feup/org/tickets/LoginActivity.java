package trains.feup.org.tickets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import trains.feup.org.tickets.api.ServerCallback;
import trains.feup.org.tickets.service.UserService;
import trains.feup.org.tickets.util.ProgressHandler;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private_key UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mError;
    private ProgressHandler progress;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkLoginStatus();

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mError = (TextView) findViewById(R.id.error);

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

            UserService service = new UserService(getApplicationContext());
            final JSONObject[] jsonObject = new JSONObject[1];

            service.login(getApplicationContext(), email, password, new ServerCallback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    Log.i("Result", result.toString());

                    try {
                        String token = result.getJSONObject("headers").getString("Authorization").substring(7);
                        saveToken(token);
                        startMainActivity();

                    } catch (JSONException je) {
                        Log.e("TOKEN ERROR", je.toString());
                    }

                    progress.hideProgress();
                }

                @Override
                public void onError(int error) {
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

    private void startMainActivity() {

        //Main activity only showed the token. Unnecessary for the final app. Will show the download tickets instead
//        Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent(this, DownloadTicketsActivity.class);
        startActivity(intent);
    }

    private void saveToken(String token) {

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.saved_token), token);
        editor.commit();

    }

    private void checkLoginStatus() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String data = sharedPreferences.getString(getString(R.string.saved_token), "");

        if (data != null && !data.isEmpty()) {
            startMainActivity();
        }

    }

    private void handleError(int error) {
        if (error == ServerCallback.UNAUTHORIZED) {
            mError.setText(getString(R.string.error_unauthorized));
        } else {
            mError.setText(getString(R.string.error_server));
        }
    }

}

