package com.iacaneda.www.qsticker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.iacaneda.www.qsticker.Intents.Home;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static java.security.AccessController.getContext;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View mHomeView;
    private View mAddCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {

                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Reset errors.
                mEmailView.setError(null);
                mPasswordView.setError(null);

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
                    //showProgress(true);
                    createHomeScreen();


                }
            }
        });

        View view = this.getCurrentFocus();
        if (view!=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_IMPLICIT_ONLY);
        }


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mHomeView = findViewById(R.id.user_home_layout);


 }

    private void createHomeScreen(){
        setContentView(R.layout.activity_home);

        ImageButton pay = (ImageButton) findViewById(R.id.btn_homepay);
        pay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                createPayScreen();
            }
        });

        ImageButton go = (ImageButton) findViewById(R.id.btn_homego);
        go.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createGoScreen();
            }
        });

        ImageButton manage = (ImageButton) findViewById(R.id.btn_homemange);
        manage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createManageScreen();
            }
        });
    }
    private void createPayScreen(){
        setContentView(R.layout.your_cards_layout);
        Button home = (Button) findViewById(R.id.btn_gohome);
        home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_home);
                createHomeScreen();
            }
        });
        Button addNewCard_btn = (Button)findViewById(R.id.btn_pay_add_new_card);
        addNewCard_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createCardStartScreen();

            }
        });

        /**MasterCard**/
        ImageButton mastercard  = (ImageButton)findViewById(R.id.img_mastercard);
        mastercard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createMastercardOverview();
            }
        });

        /**Campus Cash**/
        final ImageButton campuscash = (ImageButton)findViewById(R.id.img_GenericID);
        campuscash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createCampusCashOverview();
            }
        });

    }
    private void createGoScreen(){
        setContentView(R.layout.your_transpo_layout);
        Button home = (Button) findViewById(R.id.btn_gohome);
        home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_home);
                createHomeScreen();
            }
        });
        Button addTranspoCard = (Button) findViewById(R.id.add_new_transpo_btn);
        addTranspoCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddTranspo();
            }
        });

        /**MetroCard**/
        ImageButton mta = (ImageButton)findViewById(R.id.img_metrocard);
        mta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createMTACardOverview();
            }
        });
    }
    private void createManageScreen(){
        setContentView(R.layout.your_account_layout);
        Button home = (Button) findViewById(R.id.btn_gohome);
        home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_home);
                createHomeScreen();
            }
        });
    }
    private void createCardStartScreen(){
        setContentView(R.layout.create_card_selection);
        Button home = (Button) findViewById(R.id.btn_gohome);
        home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_home);
                createHomeScreen();
            }
        });
        ImageButton addNewCard = (ImageButton)findViewById(R.id.add_new_credit_or_debit_imgbtn);
        addNewCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddCard();
            }
        });
        ImageButton addNewBal = (ImageButton)findViewById(R.id.add_new_bal_card_imgbtn);
        addNewBal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddBalance();
            }
        });
    }
    private void createAddCard(){
        setContentView(R.layout.add_new_card);
        Button add = (Button)findViewById(R.id.add_Card_btn);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createPayScreen();
            }
        });
        Button cancel = (Button)findViewById(R.id.cancel_btn_card);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createPayScreen();
            }
        });
    }
    private void createAddBalance(){
        setContentView(R.layout.add_new_balance);
        Spinner campusSpin = (Spinner)findViewById(R.id.spinner_campus);
        String[] campuses = new String[]{"University at Buffalo","Daemon College","Buffalo State College", "Erie Community College","SUNY Cortland","SUNY Geneseo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, campuses);
        campusSpin.setAdapter(adapter);
        campusSpin.setSelection(0);


        Button add = (Button)findViewById(R.id.add_balance_Card_btn);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createPayScreen();
            }
        });
        Button cancel = (Button)findViewById(R.id.cancel_btn_bal);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createPayScreen();
            }
        });
    }

    private void createAddTranspo(){
        setContentView(R.layout.add_new_transpo);

        /**Tranportation Provider Spinner**/
        Spinner dropdown = (Spinner)findViewById(R.id.transpo_provider_spinner);
        String[] transportation_providers = new String[]{"MTA Metrocard", "NFTA Metro Rail/Bus", "Long Island Rail Road (LIRR)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, transportation_providers);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(0);


        /*Starting Balance Spinner*/
        Spinner startBal = (Spinner)findViewById(R.id.spinner_start_bal);
        String[] startBalOptions = new String[]{"$0", "$10", "$15", "$20", "$25", "$30","$40","$50","$60", "$70","$80","$90","$100"};
        ArrayAdapter<String> adap_StartBal = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, startBalOptions);
        startBal.setAdapter(adap_StartBal);
        startBal.setSelection(0);


        /*Auto Refill Balance Spinner*/
        final Spinner refillBal = (Spinner)findViewById(R.id.spinner_refill_amt);
        String[] refillBal_options = new String[]{"$0", "$10", "$15", "$20", "$25", "$30","$40","$50","$60", "$70","$80","$90","$100"};
        ArrayAdapter<String> adap_refill = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, refillBal_options);
        refillBal.setAdapter(adap_refill);
        refillBal.setSelection(0);

        /*Auto Refill Switch*/
        Switch autoRefill = (Switch)findViewById(R.id.auto_refill_transpo_switch);
        autoRefill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                refillBal.setVisibility(View.VISIBLE);
            }
        });

        /*Expiration Date Spinners*/
        Spinner expmonth = (Spinner)findViewById(R.id.exp_month_spinner);
        String[] months = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};
        ArrayAdapter<String> adap_expmonth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, months);
        expmonth.setAdapter(adap_expmonth);
        expmonth.setSelection(0);

        Spinner expDay = (Spinner)findViewById(R.id.exp_day_spinner);
        String[] days = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12",
                        "13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        ArrayAdapter<String> adap_expday = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, days);
        expDay.setAdapter(adap_expday);
        expDay.setSelection(0);

        Spinner expyear = (Spinner)findViewById(R.id.exp_year_spinner);
        String[] years = new String[]{"2017","2018","2019","2020","2021","2022","2023","2024","2025","2026","2027","2028","2029","2030"};
        ArrayAdapter<String> adap_expyear = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years);
        expyear.setAdapter(adap_expyear);
        expyear.setSelection(0);


        Button add = (Button)findViewById(R.id.add_button);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createGoScreen();
            }
        });
        Button cancel = (Button)findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createGoScreen();
            }
        });




    }
    private void createMTACardOverview(){
        setContentView(R.layout.this_transpo_layout);
        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createGoScreen();
            }
        });
    }
    private void createMastercardOverview(){
        setContentView(R.layout.this_card_layout);
        Button back = (Button)findViewById(R.id.back_cards);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createPayScreen();
            }
        });
    }
    private void createCampusCashOverview(){
        setContentView(R.layout.this_money_layout);
        Button back = (Button)findViewById(R.id.back_cards);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createPayScreen();
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
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

}

