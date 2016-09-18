package bh.nnab;

import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private myAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private WebView webView;

    Cursor res;
    static transactionDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.activity_transaction_history);

        //setTitle("TRANSACTION HISTORY");

        myDB = new transactionDatabaseHelper(this);

        /*LineChart transactionHistoryChart = (LineChart) findViewById(R.id.transactionHistoryChart);

        res = myDB.getAllData();

        List<Entry> entries = new ArrayList<Entry>();

        for(int i = 0; i < res.getCount(); i++){
            res.moveToPosition(i);
            entries.add(new Entry((float) res.getInt(2), (float) res.getDouble(3)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        LineData lineData = new LineData(dataSet);
        transactionHistoryChart.setData(lineData);
        transactionHistoryChart.invalidate(); // refresh*/

        webView = (WebView) findViewById(R.id.my_web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl("http://54.251.137.104:3002/");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        res = myDB.getAllData();
        mAdapter = new myAdapter(res);
        mRecyclerView.setAdapter(mAdapter);


    }

}