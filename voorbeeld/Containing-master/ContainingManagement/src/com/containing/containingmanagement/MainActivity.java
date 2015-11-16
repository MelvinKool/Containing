package com.containing.containingmanagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;

import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class MainActivity extends Activity {

	static String ipAdress = "";
	static SharedPreferences settings;
	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE,
			Color.MAGENTA, Color.CYAN };
	XYMultipleSeriesDataset barData = null;
	private static double[] VALUES = new double[] { 10, 11, 12, 13 };

	private static String[] NAME_LIST = new String[] { "Transport", "Buffer",
			"Crane", "Agv" };
	XYMultipleSeriesRenderer renderer;
	private CategorySeries mSeries = new CategorySeries("");

	private DefaultRenderer mRenderer = new DefaultRenderer();
	private GraphicalView BarChartView;

	private GraphicalView mChartView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		settings = getSharedPreferences("containingSettings",
				Activity.MODE_PRIVATE);

		getIPAdres();

	}

	private void getIPAdres() {
		String ip = settings.getString("ip", "192.168.0.1");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Geef ip van controller");
		alert.setMessage("bv 192.168.0.1");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);

		alert.setView(input);
		input.setText(ip);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				String ipAd = input.getText().toString();
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("ip", ipAd);
				editor.commit();
				ipAdress = ipAd;
				Timer myTimer = new Timer();
				myTimer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						getData();
					}

				}, 0, 2500);

			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
					}
				});

		alert.show();
		// see
		// http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
		    case R.id.item1:
		        b=!b;
		    	LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
				layout.removeAllViews();
				mChartView = null;
				BarChartView= null;
		        getData();
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
		    }
	}

	public void getData() {
		new ServerListener(this, ipAdress);
	}
	boolean b = false;
	public void messageRecieved(final Message decodedMessage) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				VALUES = new double[] {
						Integer.parseInt(decodedMessage.getParameters()[1]
								.toString()),
						Integer.parseInt(decodedMessage.getParameters()[2]
								.toString()),
						Integer.parseInt(decodedMessage.getParameters()[3]
								.toString()),
						Integer.parseInt(decodedMessage.getParameters()[4]
								.toString()) };

				if (b) {
					mSeries.clear();
					mRenderer.removeAllRenderers();
					for (int i = 0; i < VALUES.length; i++) {
						mSeries.add(
								NAME_LIST[i] + " - "
										+ (int) Math.round(VALUES[i]) + " ",
								VALUES[i]);
						SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
						renderer.setColor(COLORS[(mSeries.getItemCount() - 1)
								% COLORS.length]);
						mRenderer.addSeriesRenderer(renderer);

					}
					if (mChartView == null) {

						mRenderer.setApplyBackgroundColor(true);
						mRenderer.setBackgroundColor(Color
								.argb(100, 50, 50, 50));
						mRenderer.setChartTitleTextSize(20);
						mRenderer.setLabelsTextSize(15);
						mRenderer.setLegendTextSize(15);
						// mRenderer.setLabelsColor(Color.WHITE);
						mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
						mRenderer.setZoomButtonsVisible(true);
						mRenderer.setStartAngle(90);

						LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
						mChartView = ChartFactory.getPieChartView(
								MainActivity.this, mSeries, mRenderer);
						layout.addView(mChartView, new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT));

					}

					else {

						mChartView.repaint();
					}

				} else {
			
					if (BarChartView == null) {
						renderer = getBarRenderer();
						barData = getBarDataset();
						setChartSettings(renderer);
						barData = getBarDataset();
						BarChartView = ChartFactory.getBarChartView(
								MainActivity.this, barData, renderer,
								Type.DEFAULT);
						LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
						layout.addView(BarChartView, new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT));
					} else {
				
						dataset.clear();
						CategorySeries series = new CategorySeries("container series");
						for (int i = 0; i < VALUES.length; i++) {
							series.add(VALUES[i]);
						}
						dataset.addSeries(series.toXYSeries());
						BarChartView.repaint();
						
					}
					/*
					 * GraphView graphView = new BarGraphView(MainActivity.this
					 * // context , "Aantal containers" // heading );
					 * GraphViewSeriesStyle seriesStyle = new
					 * GraphViewSeriesStyle(); seriesStyle.color=COLORS[0];
					 * 
					 * GraphViewData[] data = new GraphViewData[VALUES.length];
					 * for (int i = 0; i < VALUES.length; i++) {
					 * 
					 * 
					 * 
					 * GraphViewData d = new GraphViewData(i, VALUES[i]);
					 * data[i] = d; } GraphViewSeries exampleSeries = new
					 * GraphViewSeries("Containers",seriesStyle,data);
					 * 
					 * graphView.addSeries(exampleSeries);
					 * graphView.setHorizontalLabels(NAME_LIST);
					 * graphView.setShowLegend(true);
					 * graphView.setLegendAlign(LegendAlign.BOTTOM);
					 * graphView.setLegendWidth(200);
					 * graphView.setScrollable(true); // optional - activate
					 * scaling / zooming graphView.setScalable(true); // data
					 * LinearLayout layout = (LinearLayout)
					 * findViewById(R.id.chart); layout.removeAllViews();
					 * layout.addView(graphView);
					 */
				}

			}

		});
	}
	
	public XYMultipleSeriesRenderer getBarRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setBarSpacing(1);
		/*
		 * private static String[] NAME_LIST = new String[] { "Transport",
		 * "Buffer", "Crane", "Agv" };
		 */
		renderer.addXTextLabel(1, "Transport");
		renderer.addXTextLabel(2, "Buffer");
		renderer.addXTextLabel(3, "Crane");
		renderer.addXTextLabel(4, "Agv");
		renderer.setMargins(new int[] { 20, 30, 15, 0 });
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.RED);
		renderer.addSeriesRenderer(r);
		return renderer;
	}

	private void setChartSettings(XYMultipleSeriesRenderer renderer) {
		renderer.setChartTitle("Containers");
		renderer.setXTitle("Opslag plek");
		renderer.setYTitle("Containers");
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(4);
		renderer.setYAxisMin(0);
	}
	XYMultipleSeriesDataset dataset;
	private XYMultipleSeriesDataset getBarDataset() {
		 dataset = new XYMultipleSeriesDataset();
		Random r = new Random();
		CategorySeries series = new CategorySeries("container series");
		for (int i = 0; i < VALUES.length; i++) {
			series.add(VALUES[i]);
		}
		dataset.addSeries(series.toXYSeries());
		return dataset;
	}

	private static double maxValue(double[] chars) {
		double max = chars[0];
		for (int ktr = 0; ktr < chars.length; ktr++) {
			if (chars[ktr] > max) {
				max = chars[ktr];
			}
		}
		return max;
	}

}
