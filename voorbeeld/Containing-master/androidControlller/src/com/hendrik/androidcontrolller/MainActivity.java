package com.hendrik.androidcontrolller;

import java.util.Date;

import containingcontroller.Controller;
import containingcontroller.IWindow;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements IWindow {
	Spinner	areaspinner;
	Controller c;
	TextView textArea;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
	            android.R.layout.simple_spinner_item, new String[]{"1x",
	    		"2x",
	    		"5x",
	    		"25x",
	    		"50x",
	    		"100x"});
	    Spinner  speedSpinner = (Spinner) findViewById(R.id.spinner1);
	    adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    speedSpinner.setAdapter(adapter1);
	    speedSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int post, long arg3) {
				switch(post)
				{
				case 1:
					MainActivity.this.c.setSpeed(1);
					break;
				case 2:
					MainActivity.this.c.setSpeed(2);
					break;
				case 3:
					MainActivity.this.c.setSpeed(5);
					break;
				case 4:
					MainActivity.this.c.setSpeed(25);
					break;
				case 5:
					MainActivity.this.c.setSpeed(50);
					break;
				case 6:
					MainActivity.this.c.setSpeed(100);
					break;
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		
		
			areaspinner = (Spinner) findViewById(R.id.xmlSelector);
	    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
	            android.R.layout.simple_spinner_item, new String[]{"XML 1",
	    		"XML 2",
	    		"XML 3",
	    		"XML 4",
	    		"XML 5",
	    		"XML 6"});
	    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    areaspinner.setAdapter(adapter2);
	    Button loadBtn = (Button)findViewById(R.id.button1);
	    Button startBtn = (Button)findViewById(R.id.button2);
	    startBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

					c.Start();
		
				
				
			}
		});
	    
	    loadBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Resources res = MainActivity.this.getResources();
				XmlResourceParser xrp =null;
				String selected =(String)areaspinner.getItemAtPosition(areaspinner.getSelectedItemPosition());
				if(selected.contains("1"))
				{
				 xrp = res.getXml(R.xml.xml1);
				}
				else if(selected.contains("2"))
				{
				 xrp = res.getXml(R.xml.xml2);
				}
				else if(selected.contains("3"))
				{
				 xrp = res.getXml(R.xml.xml3);
				}
				else if(selected.contains("4"))
				{
				 xrp = res.getXml(R.xml.xml4);
				}
				else if(selected.contains("5"))
				{
				 xrp = res.getXml(R.xml.xml5);
				}
				else if(selected.contains("6"))
				{
				 xrp = res.getXml(R.xml.xml6);
				}
				c.setContainers(XMLParser.parseXMLFile(xrp));
				MainActivity.this.WriteLogLine("XML loaded");
				
			}
		});
	    textArea = (TextView) findViewById(R.id.editText1);
	    c=new Controller((IWindow) this);
	    c.startServer();
	    ((TextView)findViewById(R.id.textView2)).setText(Utils.getIPAddress(true));
	    
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void WriteLogLine(final String s) {
		MainActivity.this.runOnUiThread(new Runnable()
		{

			@Override
			public void run() {
				textArea.append("\n"+s);
				
			}
			
		});
	
		
	}

	@Override
	public void setTime(Date arg0) {
		// TODO Auto-generated method stub
		
	}

}
