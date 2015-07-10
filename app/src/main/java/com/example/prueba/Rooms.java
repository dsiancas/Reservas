package com.example.prueba;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Rooms extends Activity implements OnItemClickListener {


	//listview personalized
	private static final String TAG = "Rooms";
	private SalaArrayAdapter salaArrayAdapter;
	private ListView listView;
	private Button getsalas;
	private TextView test;

	private List<Sala> salas = new ArrayList<Sala>();
	private static int colorIndex;

	String salaid;
	// Progress Dialog
	private ProgressDialog pDialog;


	// get salas
	private static final String url_salas_details = "http://ubika.tk/misalas.php";


	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "product";
	private static final String TAG_PID = "pid";
	private static final String TAG_NAME = "name";
	private static final String TAG_PRICE = "price";
	private static final String TAG_DESCRIPTION = "description";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rooms);

		getsalas = (Button) findViewById(R.id.reload);
		getsalas.setEnabled(true);

		test = (TextView) findViewById(R.id.texto);

		colorIndex = 0;
		listView = (ListView) findViewById(R.id.listViewSalas);

		new GetSalasDetails().execute();
		salaArrayAdapter = new SalaArrayAdapter(getApplicationContext(), R.layout.listview_row_layout,salas);
		listView.setAdapter(salaArrayAdapter);

		listView.setOnItemClickListener(Rooms.this);

		getsalas.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				salas.clear();
				//salaArrayAdapter.clear();
				new GetSalasDetails().execute();
			}
		});



	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(Rooms.this, Horario.class);
		intent.putExtra("salaID", String.valueOf(salas.get(position).getSalaID()));
		startActivity(intent);

	}


	public static String GET(String url){
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}


	class GetSalasDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Rooms.this);
			pDialog.setMessage("Cargando detalles de las salas. Espere por favor...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			getsalas.setEnabled(false);
			pDialog.show();
		}

		/**
		 * Getting salas details in background thread
		 */
		protected String doInBackground(String... params) {

			String dataJson = GET(url_salas_details);;
			// Check for success tag
			/*String strJson= "{	\"Employee\" :[	{\"id\":\"01\",	\"name\":\"Gopal Varma\",\"salary\":\"500000\"},{\"id\":\"02\",	\"name\":\"Sairamkrishna\"," +
					"	\"salary\":\"500000\"},	{\"id\":\"03\",	\"name\":\"Sathish kallakuri\",	\"salary\":\"600000\"}	]}";*/
			String msg = "";
			try {
				JSONObject  jsonRootObject = new JSONObject(dataJson);
				int exito = Integer.parseInt(jsonRootObject.optString("success").toString());
				msg = jsonRootObject.optString("message").toString();
				if(exito == 0)
					return msg;
				//Get the instance of JSONArray that contains JSONObjects
				JSONArray jsonArray = jsonRootObject.optJSONArray("salas");


				//Iterate the jsonArray and print the info of JSONObjects
				for(int i=0; i < jsonArray.length(); i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					int id = Integer.parseInt(jsonObject.optString("id").toString());
					int estado = Integer.parseInt(jsonObject.optString("estado").toString());
					int statusImgResId = getResources().getIdentifier(estado == 0 ? "freebook" : "busybook", "drawable", "com.example.prueba");


					int capacidad = Integer.parseInt(jsonObject.optString("capacidad").toString());
					String name = jsonObject.optString("nombre").toString();
					String detalles = jsonObject.optString("detalles").toString();
					String ubicacion = jsonObject.optString("ubicacion").toString();

					salas.add(new Sala(id, capacidad, statusImgResId, name, detalles, ubicacion));
				}
			} catch (JSONException e) {e.printStackTrace();}
			return msg;

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * *
		 */
		protected void onPostExecute(String data) {

			// dismiss the dialog once got all details


			//for (Sala SalaData : salas) {

			//	salaArrayAdapter.add(SalaData);
			//}
			salaArrayAdapter.notifyDataSetChanged();
			test.setText(data);

			pDialog.dismiss();
			getsalas.setEnabled(true);

		}
	}
}