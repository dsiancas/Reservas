package com.example.prueba;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TimePicker;
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

import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
//import org.json.simple.JSONValue;
//import org.json.simple.parser.JSONParser;

public class Agregar extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LoadPeopleResult> {
	private MultiAutoCompleteTextView mactv;
	private String[] users = {"Adrian Montoya", "976939089", "Daniel Siancas", "954729198", "Adriana Juarez", "002344567"};
	private GoogleApiClient mGoogleApiClient;
    private ArrayList<String> mCirclesList;
    private ArrayAdapter<String> mCirclesAdapter;
    private static final int RC_SIGN_IN = 0;
    private static final String TAG_SUCCESS = "success";
    private int mSignInProgress;
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private Button send;
    private static final String url_reservar = "http://www.ubika.tk/reservar.php?";
    private TimePicker tp;
    Person currentUser;

    HttpClient httpclient2;
    HttpPost httppost2;
    List<NameValuePair> nameValuePairs2;
    HttpResponse response2;
    Button retro;
    private String sala;
    private String hourr;
    private String text_hour;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.refer);

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            sala = extras.getString("salaID");
            hourr = extras.getString("hora");
            text_hour = extras.getString("texto");
        }

        send = (Button) findViewById(R.id.button1);
        retro = (Button) findViewById(R.id.back);
        retro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Agregar.this, Rooms.class);
                startActivity(intent);

            }
        });

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

    public static String remove2(String input) {
        // Descomposición canónica
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Nos quedamos únicamente con los caracteres ASCII
        Pattern pattern = Pattern.compile("\\P{ASCII}+");
        return pattern.matcher(normalized).replaceAll("");
    }//remove2

    @Override
    public void onConnected(Bundle bundle) {
        if (!mGoogleApiClient.isConnecting()) {
            currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            //Log.i("taag","dsadsa");
            Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
                    .setResultCallback(this);

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Notify().execute();
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

    private class Notify extends AsyncTask<Void, Void, String> {

        private String URL = "http://ubicom.pe.hu/getUsuarios.php";
        private String res="";

		@Override
		protected void onPreExecute() {
			//mactv.setEnabled(false);
		}

        private final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if(msg.arg1 == 1) {
                    //Toast.makeText(getApplicationContext(),"Your message", Toast.LENGTH_LONG).show();
                    Toast toast = Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }
        };

		@Override
		protected String doInBackground(Void... params) {
            String stringEditText = mactv.getText().toString();
            //Log.i("dsadsa",stringEditText);
            List<String> items = Arrays.asList(stringEditText.split("\\s*,\\s*"));
            nameValuePairs2 = new ArrayList<>(2);
            //items.add("Daniel Jesus Siancas Salas");
            try{
                httpclient2 = new DefaultHttpClient();
                httppost2 = new HttpPost("http://ubika.tk/reservar.php");
                //String hour = tp.getCurrentHour().toString();
                //String hour = "2";
                //hour = String.valueOf(temp);
                String hour2 = "3";
                String leader = currentUser.getDisplayName();
                String members = stringEditText;
                //String cant = String.valueOf(items.size()+1);
                String cant="1";
                nameValuePairs2.clear();
                nameValuePairs2.add(new BasicNameValuePair("sala", sala));
                nameValuePairs2.add(new BasicNameValuePair("hour", hourr));
                nameValuePairs2.add(new BasicNameValuePair("leader", remove2(leader)));
                nameValuePairs2.add(new BasicNameValuePair("members", remove2(members)));
                nameValuePairs2.add(new BasicNameValuePair("cant", cant));
                httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                //response2 = httpclient2.execute(httppost2);

                //
                String response = httpclient2.execute(httppost2, responseHandler);
                System.out.println("Response : " + response);
                res= response;

                Message msg = handler.obtainMessage();
                msg.arg1 = 1;
                handler.sendMessage(msg);

                for(String i: items) {
                    httpclient2 = new DefaultHttpClient();
                    httppost2 = new HttpPost("http://ubika.tk/service.php"); // make sure the url is correct.
                    //add your data
                    nameValuePairs2.clear();
                    // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,

                    //double tempmin =  (((tp.getCurrentHour()+(double)tp.getCurrentMinute()/60)*3600)-30600)/1800;
                    //int temp = (int)tempmin;
                    //if(tempmin-temp>0) temp++;
                    //String hour = String.valueOf(temp);
                    //String hour=tp.getCurrentHour().toString()+":"+tp.getCurrentMinute().toString();

                    //nameValuePairs2.add(new BasicNameValuePair("name", i));  // $Edittext_value = $_POST['Edittext_value'];
                    nameValuePairs2.add(new BasicNameValuePair("name", remove2(i)));  // $Edittext_value = $_POST['Edittext_value'];
                    nameValuePairs2.add(new BasicNameValuePair("emisor", remove2(currentUser.getDisplayName())));  // $Edittext_value = $_POST['Edittext_value'];
                    nameValuePairs2.add(new BasicNameValuePair("hour", text_hour));
                    nameValuePairs2.add(new BasicNameValuePair("id", hourr));
                    nameValuePairs2.add(new BasicNameValuePair("msg", remove2(currentUser.getDisplayName()) + " te ha invitado a reservar una sala en Biblioteca, hora: "+text_hour));
                    httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
                    //Execute HTTP Post Request
                    //response2 = httpclient2.execute(httppost2);
                    // edited by James from coderzheaven.. from here....
                    //responseHandler = new BasicResponseHandler();
                    response = httpclient2.execute(httppost2, responseHandler);
                    System.out.println("Response : " + response);


                    //httppost2.


                    // Building Parameters
                    /*params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("sala", "1"));
                    params.add(new BasicNameValuePair("hour", hour));
                    params.add(new BasicNameValuePair("leader", remove2(leader)));
                    params.add(new BasicNameValuePair("members", remove2(members)));
                    params.add(new BasicNameValuePair("cant", cant));
                    */
                    //new send_to_server().execute();
                }
            }catch(Exception e){
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
