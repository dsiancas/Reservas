package com.example.prueba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog.Builder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

public class Horario extends Activity implements OnItemClickListener {



    // This Data show in grid ( Used by adapter )

    static final String[] GRID_DATA = new String[] {

            "8:30 to 9:00 am.", "9:00 to 9:30 am.", "9:30 to 10:00 am.", "10:00 to 10:30 am.", "10:30 to 11:00 am.", "11:00 to 11:30 am.",
            "11:30 to 12:00 pm.", "12:00 to 12:30 pm.", "12:30 to 1:00 pm.", "1:00 to 1:30 pm.", "1:30 to 2:00 pm.", "2:00 to 2:30 pm.",
            "2:30 to 3:00 pm.", "3:00 to 3:30 pm.", "3:30 to 4:00 pm.", "4:00 to 4:30 pm.", "4:30 to 5:00 pm.", "5:00 to 5:30 pm.",
            "5:30 to 6:00 pm.", "6:00 to 6:30 pm.", "6:30 to 7:00 pm.", "7:00 to 7:30 pm.", "7:30 to 8:00 pm.", "8:00 to 8:30 pm."
    };
    private List<Integer> horas = new ArrayList<Integer>();
        private Builder dialog;
        private ProgressDialog pDialog;
        private Button retro;
        private TextView text;
        private  String salaid;
        GridView gridView;
        ArrayList<ItemGrid> gridArray = new ArrayList<ItemGrid>();
        CustomGridAdapter customGridAdapter;
        private Bitmap hourIcon;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.horario_layout);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                salaid = extras.getString("salaID");
                Toast toast = Toast.makeText(getApplicationContext(),
                        salaid,
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Error al elegir la sala.",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                Intent intent = new Intent(Horario.this, Rooms.class);
                startActivity(intent);
            }

            hourIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.time);

            gridView = (GridView) findViewById(R.id.grid);
            retro = (Button) findViewById(R.id.back);
            retro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Horario.this, Rooms.class);
                    startActivity(intent);

                }
            });
            //new get_schedule().execute();
            text = (TextView)findViewById(R.id.texto);

            customGridAdapter = new CustomGridAdapter(this, R.layout.grid_item, gridArray);
            gridView.setAdapter(customGridAdapter);
            gridView.setOnItemClickListener(Horario.this);
            new get_schedule().execute();
            // estado 1, libre




        }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        if(horas.contains(position+1)) {
            dialog = new Builder(Horario.this);
            dialog.setTitle("Resevar Sala");
            dialog.setMessage(" La sala esta ocupada.");
            dialog.setNegativeButton("Continuar", null);

            AlertDialog dialogAlert = dialog.create();
            dialogAlert.show();
        }
        else    {
            dialog = new Builder(Horario.this);
            dialog.setTitle("Resevar Sala");
            dialog.setMessage(" La sala esta disponible.");
            dialog.setNegativeButton("Cancelar", null);
            dialog.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Horario.this, Agregar.class);
                    intent.putExtra("salaID", salaid);
                    intent.putExtra("hora", String.valueOf(position+1));
                    intent.putExtra("texto", GRID_DATA[position]);
                    startActivity(intent);

                }
            });
            AlertDialog dialogAlert = dialog.create();
            dialogAlert.show();
        }


    }
    private class get_schedule extends AsyncTask<Void, Void, String> {

        private String URL = "http://ubika.tk/checkHorario.php?idSala=";
        //AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Horario.this);
            pDialog.setMessage("Cargando Horarios de la Sala, wait me please!...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            retro.setEnabled(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            URL = URL+ salaid;
          /*  HttpGet request = new HttpGet(URL);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String jsonText;
            try {
                return mClient.execute(request,responseHandler);
            } catch (ClientProtocolException exception) {
                exception.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            }*/

            String dataJson = GET(URL);;
            // Check for success tag
            String msg = "";
            try {
                JSONObject jsonRootObject = new JSONObject(dataJson);
                int exito = Integer.parseInt(jsonRootObject.optString("success").toString());
                msg = jsonRootObject.optString("message").toString();
                if(exito == 0)
                    return msg;
                //Get the instance of JSONArray that contains JSONObjects
                JSONArray jsonArray = jsonRootObject.optJSONArray("sala");


                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Integer horr = Integer.parseInt(jsonObject.optString("hora").toString());
                    horas.add(horr);
                }
            } catch (JSONException e) {e.printStackTrace();}
            return dataJson;
        }

        @Override
        protected void onPostExecute(String result) {
            text.setText(result);
            for (Integer i = 0; i< 24; ++i) {
                if(horas.contains(i+1))
                    gridArray.add(new ItemGrid(hourIcon, GRID_DATA[i],0));
                else
                    gridArray.add(new ItemGrid(hourIcon, GRID_DATA[i],1));
            }
            pDialog.dismiss();
            retro.setEnabled(true);

        }
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
}



/*


        protected String doInBackground(String... params) {

            String dataJson = GET(url_get_reservas);;
            // Check for success tag
            String msg = "";
            try {
                JSONObject jsonRootObject = new JSONObject(dataJson);
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

                    //salas.add(new Sala(id, capacidad, statusImgResId, name, detalles, ubicacion));
                }
            } catch (JSONException e) {e.printStackTrace();}
            return msg;

        }



*/