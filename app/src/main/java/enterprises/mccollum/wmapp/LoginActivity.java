package enterprises.mccollum.wmapp;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.nio.charset.StandardCharsets;

import enterprises.mccollum.wmapp.auth.AuthJunkie;
import enterprises.mccollum.wmapp.authobjects.TokenResponseContainer;
import enterprises.mccollum.wmapp.shuttle.control.ShuttleDataSyncService;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AccountAuthenticatorActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private EditText mUsernameView;
	private EditText mDeviceNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
	private AccountManager mAccountManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
		mAccountManager = AccountManager.get(getBaseContext());
        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
		mDeviceNameView = (EditText) findViewById(R.id.devicename);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
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
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
		String deviceName = mDeviceNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

		if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_username_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
			mPasswordView.setError(getString(R.string.error_password_required));
			focusView = mPasswordView;
			cancel = true;
        }
        if(TextUtils.isEmpty(deviceName))
			deviceName = null;

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
			
            //mAuthTask = new UserLoginTask(username, deviceName, password);
            //mAuthTask.execute((Void) null);
        }
        ApiJunkie.getInstance(this).addRequestToQueue(new AuthJunkie.LoginRequest(username, deviceName, password, new Response.Listener<TokenResponseContainer>(){
			@Override
			public void onResponse(TokenResponseContainer response) {
				Log.d("logintag", String.format("Received response: %d", response.getStatus()));
				switch (response.getStatus()){
					case 200:
						receiveResponse(response);
						break;
					case 401:
						showProgress(false);
						mUsernameView.setError(getString(R.string.error_incorrect_username_password));
						mPasswordView.setText("");
						mPasswordView.setError(getString(R.string.error_incorrect_username_password));
						mPasswordView.requestFocus();
						break;
					case 500:
						showProgress(false);
						mPasswordView.setError(getString(R.string.error_signing_in));
						mPasswordView.requestFocus();
						break;
					default:
						showProgress(false);
						Toast.makeText(getApplicationContext(), "Not sure what's happened.", Toast.LENGTH_LONG).show();
				}
			}
		}, new Response.ErrorListener(){
			@Override
			public void onErrorResponse(VolleyError error) {
				showProgress(false);
				if(error.networkResponse == null)
					error.printStackTrace();
				switch(error.networkResponse.statusCode){
					case 401:
						showProgress(false);
						mUsernameView.setError(getString(R.string.error_incorrect_username_password));
						mPasswordView.setText("");
						mPasswordView.setError(getString(R.string.error_incorrect_username_password));
						mPasswordView.requestFocus();
						break;
					case 500:
						showProgress(false);
						mPasswordView.setError(getString(R.string.error_signing_in));
						mPasswordView.requestFocus();
						break;
					default:
						showProgress(false);
						Toast.makeText(getApplicationContext(), "Not sure what's happened.", Toast.LENGTH_LONG).show();
				}
			}
		}));
    }
    
    public void receiveResponse(TokenResponseContainer response){
		Bundle userData = new Bundle(4);
		userData.putString(AccountManager.KEY_ACCOUNT_NAME, response.getToken().getUsername());
		userData.putString(AccountManager.KEY_ACCOUNT_TYPE, AuthenticatorService.ACCOUNT_TYPE);
		userData.putString(AccountManager.KEY_AUTHTOKEN, response.getB64Token());
		userData.putString(AccountManager.KEY_PASSWORD, response.getTokenSignature());
		
		final Account account = new Account(response.getToken().getUsername(), AuthenticatorService.ACCOUNT_TYPE);
		
		if(getIntent().getBooleanExtra(AuthenticatorService.ARG_NEW_ACCOUNT, false)) //if it's a new account
			mAccountManager.addAccountExplicitly(account, response.getTokenSignature(), userData);
		
		mAccountManager.setAuthToken(account, AuthenticatorService.KEY_TOKEN_REAL, response.getB64Token());
		mAccountManager.setAuthToken(account, AuthenticatorService.KEY_TOKEN_SIGNATURE, response.getTokenSignature());
		
		final Intent intent = new Intent();
		intent.putExtras(userData);
		
		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);
		ApiJunkie.getInstance(this).refreshTokens();
		//ContentResolver.setMasterSyncAutomatically(true);
		ContentResolver.setSyncAutomatically(account, AuthenticatorService.TOKEN_SYNC_PROVIDER, true);
		//ContentResolver.requestSync(account, AuthenticatorService.TOKEN_SYNC_PROVIDER, Bundle.EMPTY);
		//ContentResolver.setSyncAutomatically(account, ShuttleDataSyncService.SHUTTLE_SYNC_PROVIDER, true);
		finish();
	}

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
		int shortAnimTime = 200; //getResources().getInteger(android.R.integer.config_shortAnimTime);
	
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
	}
	
}

