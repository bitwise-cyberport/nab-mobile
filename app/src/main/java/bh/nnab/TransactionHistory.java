package bh.nnab;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    Cursor res;
    static transactionDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        LineChart transactionHistoryChart = (LineChart) findViewById(R.id.transactionHistoryChart);
        myDB = new transactionDatabaseHelper(this);
        res = myDB.getAllData();

        List<Entry> entries = new ArrayList<Entry>();

        for(int i = 0; i < res.getCount(); i++){
            res.moveToPosition(i);
            entries.add(new Entry((float) res.getInt(2), (float) res.getDouble(3)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        LineData lineData = new LineData(dataSet);
        transactionHistoryChart.setData(lineData);
        transactionHistoryChart.invalidate(); // refresh

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        res = myDB.getAllData();
        mAdapter = new myAdapter(res);
        mRecyclerView.setAdapter(mAdapter);


    }

}