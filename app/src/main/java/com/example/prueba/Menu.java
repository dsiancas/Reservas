package com.example.prueba;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class Menu extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
        mGoogleApiClient = buildGoogleApiClient();

        //mGoogleApiClient.connect();
        /*
		Button reservar = (Button) findViewById(R.id.button2);
		Button confirmar = (Button) findViewById(R.id.button3);
        TextView welcome = (TextView) findViewById(R.id.welcome);

        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        welcome.setText(String.format(
                getResources().getString(R.string.signed_in_as),
                currentUser.getDisplayName()));

		reservar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Menu.this, Rooms.class);
				startActivity(intent);
			}
		});
		confirmar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Menu.this, Confirmacion.class);
				startActivity(intent);
			}
		});
		*/
	}

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_PROFILE);

        /*if (mRequestServerAuthCode) {
            checkServerAuthConfiguration();
            builder = builder.requestServerAuthCode(WEB_CLIENT_ID, this);
        }*/

        return builder.build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (!mGoogleApiClient.isConnecting()) {

            Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            TextView welcome = (TextView) findViewById(R.id.welcome);
            welcome.setText(String.format(
                    getResources().getString(R.string.welcome),
                    currentUser.getDisplayName()));

            Button reservar = (Button) findViewById(R.id.button2);
            Button confirmar = (Button) findViewById(R.id.button3);
            Button logout = (Button) findViewById(R.id.logout);

            reservar.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Menu.this, Rooms.class);
                    startActivity(intent);
                }
            });
            confirmar.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Menu.this, Confirmacion.class);
                    startActivity(intent);
                }
            });

            logout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                    }
                    onSignedOut();
                }
            });
        }
    }

    private void onSignedOut() {
        finish();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
