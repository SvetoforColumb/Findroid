package ga.winterhills.findroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class registraitActivity extends AppCompatActivity {


    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private boolean createNewUser;
    private CreateNewProduct create;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrait);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email_nav);
        mPasswordView = (EditText) findViewById(R.id.password);
        createNewUser=false;
        Button registretion = (Button) findViewById(R.id.regist);
        registretion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    attemptLogin();
                    if(createNewUser){
                        Log.i("TAG", "create");
                        String email = mEmailView.getText().toString();
                        String password = mPasswordView.getText().toString();
                        create = new CreateNewProduct(email, password);
                        create.execute();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Intent intentObj = getIntent();
        getSupportActionBar().hide();
    }

    private void attemptLogin() throws InterruptedException {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);


        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean checkCreate=true;
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
            checkCreate=false;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
            checkCreate=false;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
            checkCreate=false;
        }
        createNewUser=checkCreate;
    }

    private boolean isEmailValid(String email) {
        return (email.contains("@")&&(email.contains(".ru")||email.contains(".com")));
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    JSONObject json=null;
    class CreateNewProduct extends AsyncTask<Void, Void, Boolean> {

        String mEmail;
        String mPassword;
        boolean userCreaterEarlier;

        CreateNewProduct(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute(){
            userCreaterEarlier=false;
        }

        private static final String url_newUser="https://findroid.napoleonthecake.ru/new_user.php";
        private static final String url_checkUser="https://findroid.napoleonthecake.ru/check_user.php";
        private static final String TAG_SUCCESS="success";
        @Override
        protected Boolean doInBackground(Void... args) {
            Log.i("TAG", "back");


            List<NameValuePair> values = new ArrayList<NameValuePair>();
            values.add(new BasicNameValuePair("login", mEmail));
            json = jsonParser.makeHttpRequest(url_checkUser, "GET", values);
            values.add(new BasicNameValuePair("password", mPassword));
            int success;
            try {
                success = json.getInt(TAG_SUCCESS);
                if (success == 0) {
                    json = jsonParser.makeHttpRequest(url_newUser, "POST", values);
                    userCreaterEarlier = false;
                }else {
                    userCreaterEarlier = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success){
            if(userCreaterEarlier) mEmailView.setError("User was creater erlier");
        }

    }

}
