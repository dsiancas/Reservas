package com.example.prueba;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Confirmacion extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	private String emisor = "Juan Aguirre";
	private String tiempo = "12:30";

    private GoogleApiClient mGoogleApiClient;
    private ArrayList<String> mCirclesList;
    private ArrayAdapter<String> mCirclesAdapter;
    private static final int RC_SIGN_IN = 0;
    private int mSignInProgress;
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;

    private Button ab;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            emisor = extras.getString("user");
            tiempo = extras.getString("time");
        }

		//emisor = getIntent().getStringExtra("name");
        ab = (Button) findViewById(R.id.button1);
		TextView sender = (TextView) findViewById(R.id.textView18);
		TextView time = (TextView) findViewById(R.id.textView20);
		
		sender.setText(sender.getText().toString()+emisor);
		time.setText(time.getText().toString() + tiempo);

        mGoogleApiClient = buildGoogleApiClient();
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
            final Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            //Log.i("taag",currentUser.getName().toString());

            ab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SendAccept(currentUser).execute();
                }
            });

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    private class SendAccept extends AsyncTask<Void, Void, String> {

        private String URL = "http://ubicom.pe.hu/getUsuarios.php";
        HttpClient httpclient2;
        HttpPost httppost2;
        List<NameValuePair> nameValuePairs2;
        HttpResponse response2;
        Person currentPerson;

        public SendAccept(Person user) {
            currentPerson=user;
        }

        @Override
        protected void onPreExecute() {
            //mactv.setEnabled(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            //String stringEditText = mactv.getText().toString();
            //Log.i("dsadsa",stringEditText);
            //List<String> items = Arrays.asList(stringEditText.split("\\s*,\\s*"));
            //items.add("Daniel Jesus Siancas Salas");
            try {

                //for(String i: items) {
                httpclient2 = new DefaultHttpClient();
                httppost2 = new HttpPost("http://ubika.tk/service.php"); // make sure the url is correct.
                //add your data
                nameValuePairs2 = new ArrayList<NameValuePair>(2);
                // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
                String hour = "12:12";// tp.getCurrentHour().toString()+":"+ tp.getCurrentMinute().toString();

                nameValuePairs2.add(new BasicNameValuePair("name", "d"));  // $Edittext_value = $_POST['Edittext_value'];
                nameValuePairs2.add(new BasicNameValuePair("emisor", "dsadsA"));
                nameValuePairs2.add(new BasicNameValuePair("hour", hour));
                httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
                //Execute HTTP Post Request
                response2 = httpclient2.execute(httppost2);
                // edited by James from coderzheaven.. from here....
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String response = httpclient2.execute(httppost2, responseHandler);
                System.out.println("Response : " + response);
                //}
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
			/*Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, result, Toast.LENGTH_LONG);
			toast.show();
			mactv.setEnabled(true);*/
        }
    }
}

