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

public class Rooms extends Activity{
	private Integer[] data = {0,1,0,0,1,1,1,0,0};
	private Integer[] botons = {R.id.button4,R.id.button5,R.id.button6,R.id.button7,R.id.button8,R.id.button9,R.id.button10,R.id.button11,R.id.button12};
	private Button boton, boton1, boton2, boton3, boton4, boton5, boton6, boton7, boton8, boton9;
	private AlertDialog.Builder dialog;
	private String descripcion = "";
	private String capacidad = "";
	private String accesorios = "";
	private String nivel = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rooms); 
		
		boton1 = (Button) findViewById(R.id.button4);
		boton2 = (Button) findViewById(R.id.button5);
		boton3 = (Button) findViewById(R.id.button6);
		boton4 = (Button) findViewById(R.id.button7);
		boton5 = (Button) findViewById(R.id.button8);
		boton6 = (Button) findViewById(R.id.button9);
		boton7 = (Button) findViewById(R.id.button10);
		boton8 = (Button) findViewById(R.id.button11);
		boton9 = (Button) findViewById(R.id.button12);
		
		for(int i=0; i < data.length; i++){
			boton = (Button) findViewById(botons[i]);
			if(data[i] == 1){
				boton.setBackgroundColor(0xFFFF0000);
				boton.setEnabled(false);
			}
			else{
				boton.setBackgroundColor(0xFF00FF00);
			}
		}
		
		//Agregando listener a los botones
		boton1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				capacidad = "5";
				accesorios = "Pizarra, mota, plumones, proyector, pantalla TV";
				nivel = "Primer nivel";
				descripcion = "Capacidad: "+capacidad+"\n"+
							  "Accesorios: "+accesorios+"\n"+
							  "Nivel: "+nivel+"\n";
				dialog = new Builder(Rooms.this);
				dialog.setTitle("Descripcion : Sala 1");
				dialog.setMessage(descripcion);
				dialog.setNegativeButton("Cancelar", null);
				dialog.setPositiveButton("Reservar", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Rooms.this, Agregar.class);
						intent.putExtra("descripcion", descripcion);
						startActivity(intent);
						
					}
				});
				AlertDialog dialogAlert = dialog.create();
				dialogAlert.show();
			}
		});
		boton2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		boton3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		boton4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		boton5.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		boton6.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		boton7.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		boton8.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		boton9.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		
		
	}
}
