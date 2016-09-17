package bh.nnab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    transactionDatabaseHelper myDB;

    VolleySingleton helper = VolleySingleton.getInstance();
    final static String RECENT_API_ENDPOINT = "http://54.251.137.104:3001/api/transaction/1";



    Boolean print;
    String responder = "";
    JSONArray jsarray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadTransactionData();

        Button RM = (Button) findViewById(R.id.RMButton);
        RM.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), RMClick.class);
                startActivity(in);
            }

        });


        Button TH = (Button) findViewById(R.id.THButton);
        TH.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), TransactionHistory.class);
                startActivity(in);
            }

        });
    }



    private void loadTransactionData() {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, RECENT_API_ENDPOINT, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            print = response.getBoolean("success");

                            responder = response.getString("message");

                            jsarray = response.getJSONArray("data");

                            updateDatabase();

                        } catch (Exception e) {
                            Log.e("NNAB", "unexpected JSON exception", e);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volly Error", error.toString());
                        responder = "Endpoint not configured properly.";

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            Log.e("Status code", String.valueOf(networkResponse.statusCode));
                            responder = "No Network Connection";
                        }
                    }
                });

        helper.add(request);

    }

    public void updateDatabase(){
        myDB = new transactionDatabaseHelper(this);
        myDB.deleteAllData();

        for(int i = 0; i < jsarray.length(); i++){

            JSONObject tmp;
            try{
                tmp = jsarray.getJSONObject(i);

                String transactionID = tmp.getString("paymentId");
                String date = tmp.getString("timestamp").substring(0, 10);
                String time = tmp.getString("timestamp").substring(11, 16);
                double transfer_amount = tmp.getDouble("amount");
                String receiverID = tmp.getString("receiverId");
                myDB.insertData(time, transfer_amount, transactionID, date, receiverID);

            } catch (Exception e){
                Log.e("NNAB", "unexpected JSON exception", e);
            }
        }
    }

}
