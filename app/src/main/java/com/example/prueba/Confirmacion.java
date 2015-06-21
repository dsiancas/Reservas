package com.example.prueba;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Confirmacion extends Activity{
	private String emisor = "Juan Aguirre";
	private String tiempo = "12:30";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm); 
		
		TextView sender = (TextView) findViewById(R.id.textView18);
		TextView time = (TextView) findViewById(R.id.textView20);
		
		sender.setText(sender.getText().toString()+emisor);
		time.setText(time.getText().toString()+tiempo);
	}
}
