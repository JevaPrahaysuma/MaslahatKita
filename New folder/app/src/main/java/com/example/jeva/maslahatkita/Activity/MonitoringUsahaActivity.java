package com.example.jeva.maslahatkita.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.jeva.maslahatkita.Adapter.MonitoringUsahaAdapter;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.MonitoringUsahaModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MonitoringUsahaActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference database;
    private ArrayList<MonitoringUsahaModel> daftar;
    private LineChart lineChart, chartTotal;
    Context context;
    ArrayList<Entry> listChart;
    ArrayList<String> listLabel;
    ArrayList<Entry> listChartTotalPenjualan;
    ArrayList<String> listLabelTotalPenjualan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_usaha);
        recyclerView =(RecyclerView) findViewById(R.id.recyclerview);
        lineChart = findViewById(R.id.chart);
        chartTotal = findViewById(R.id.chart_total);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance().getReference();
        daftar = new ArrayList<MonitoringUsahaModel>();
        database.child("MonitoringUsaha").child(getIntent().getStringExtra("idUsaha")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftar = new ArrayList<>();
                listChart = new ArrayList<>();
                listLabel = new ArrayList<>();
                listChartTotalPenjualan = new ArrayList<>();
                listLabelTotalPenjualan = new ArrayList<>();
                listChart.add(new Entry(0, 0));
                listLabel.add("0");
                listChartTotalPenjualan.add(new Entry(0, 0));
                listLabelTotalPenjualan.add("0");
                daftar.clear();
                adapter = new MonitoringUsahaAdapter(MonitoringUsahaActivity.this,daftar);
                recyclerView.setAdapter(adapter);
                float counter = 1;
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    final MonitoringUsahaModel mu = noteDataSnapshot.getValue(MonitoringUsahaModel.class);
                    mu.setIdUsaha(noteDataSnapshot.getKey());
                    daftar.add(mu);

                    String x = mu.getTanggal();
                    Date date1= null;
                    try {
                        date1 = new SimpleDateFormat("dd-MM-yyyy").parse(x);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
                    x = String.valueOf(sdf.format(date1));

                    float y = (float) mu.getLabaKotor();
                    adapter.notifyDataSetChanged();
                    listChart.add(new Entry(counter, y));
                    listLabel.add(x);
                    listChartTotalPenjualan.add(new Entry(counter++, mu.getTotalPenjualan()));
                    listLabelTotalPenjualan.add(x);
                }
                LineDataSet dataset = new LineDataSet(listChart, "Laba Kotor");

                lineChart.getAxisRight().setEnabled(false);
                lineChart.getXAxis().setTextSize(15f);
                lineChart.getXAxis().setLabelRotationAngle(90);
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                lineChart.getXAxis().setGranularity(1f); // restrict interval to 1 (minimum)
                lineChart.getXAxis().setGranularityEnabled(true);
                LineData data = new LineData(dataset);
                lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return listLabel.get((int)value);
                    }
                });
                lineChart.setData(data);
                lineChart.invalidate(); // refresh


                LineDataSet datasetTotalPenjualan = new LineDataSet(listChartTotalPenjualan, "Total Penjualan");
                datasetTotalPenjualan.setColor(getResources().getColor(R.color.colorOrange));
                chartTotal.getAxisRight().setEnabled(false);
                chartTotal.getXAxis().setTextSize(15f);
                chartTotal.getXAxis().setLabelRotationAngle(90);
                chartTotal.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                chartTotal.getXAxis().setGranularity(1f); // restrict interval to 1 (minimum)
                chartTotal.getXAxis().setGranularityEnabled(true);
                LineData dataPenjualan = new LineData(datasetTotalPenjualan);
                chartTotal.getXAxis().setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return listLabel.get((int)value);
                    }
                });
                chartTotal.setData(dataPenjualan);
                chartTotal.invalidate(); // refresh


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }

}
