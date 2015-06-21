package com.example.prueba;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.google.android.gms.plus.People.LoadPeopleResult;

public class Agregar extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LoadPeopleResult> {
	private MultiAutoCompleteTextView mactv;
	private String[] users = {"Adrian Montoya", "976939089", "Daniel Siancas", "954729198", "Adriana Juarez", "002344567"};
	private GoogleApiClient mGoogleApiClient;
    private ArrayList<String> mCirclesList;
    private ArrayAdapter<String> mCirclesAdapter;
    private static final int RC_SIGN_IN = 0;
    private int mSignInProgress;
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refer);

        mCirclesList = new ArrayList<String>();
        mCirclesAdapter = new ArrayAdapter<String>(
                this, R.layout.circle_member, mCirclesList);
        //mCirclesListView.setAdapter(mCirclesAdapter);

        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users);
        mactv = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView1);
        mCirclesAdapter.setNotifyOnChange(true);
        mactv.setAdapter(mCirclesAdapter);
        mactv.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        mGoogleApiClient = buildGoogleApiClient();

		//String result = getUsers();
		//new Verify().execute();
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
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    // If the error resolution was successful we should continue
                    // processing errors.
                    mSignInProgress = STATE_SIGN_IN;
                } else {
                    // If the error resolution was not successful or the user canceled,
                    // we should stop processing errors.
                    mSignInProgress = STATE_DEFAULT;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    // If Google Play services resolved the issue with a dialog then
                    // onStart is not called so we need to re-attempt connection here.
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (!mGoogleApiClient.isConnecting()) {
            Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            //Log.i("taag","dsadsa");
            Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
                    .setResultCallback(this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(LoadPeopleResult peopleData) {

        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            mCirclesList.clear();
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {
                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    //Log.i("taag", personBuffer.get(i).getDisplayName());
                    mCirclesList.add(personBuffer.get(i).getDisplayName());
                }
            } finally {
                personBuffer.close();
            }

            mCirclesAdapter.notifyDataSetChanged();
        } else {
            Log.e("TAG", "Error requesting visible circles: " + peopleData.getStatus());
        }
    }

    private class Verify extends AsyncTask<Void, Void, String> {

		private String URL = "http://ubicom.pe.hu/getUsuarios.php";
		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");
		
		@Override
		protected void onPreExecute() {
			mactv.setEnabled(false);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			HttpGet request = new HttpGet(URL);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			try {
				return mClient.execute(request,responseHandler);
			} catch (ClientProtocolException exception) {
				exception.printStackTrace();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, result, Toast.LENGTH_LONG);
			toast.show();	
			mactv.setEnabled(true);
		}
	}
	
	public String getUsers() {
		String URL = "http://ubicom.pe.hu/getUsuarios.php";
		
		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");
		
		HttpGet request = new HttpGet(URL);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			return  mClient.execute(request,responseHandler);
		} catch (ClientProtocolException exception) {
			exception.printStackTrace();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return null;
	}
}
