package bh.nnab;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RMClick extends AppCompatActivity implements View.OnClickListener {

    FragmentManager fm = getSupportFragmentManager();
    VolleySingleton helper = VolleySingleton.getInstance();
    final static String RECENT_API_ENDPOINT = "http://54.251.137.104:3001/api/transaction/verify";
    String responder = "";
    EditText etName;
    EditText etID;
    EditText etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmclick);

        etName = (EditText) findViewById(R.id.editTextName);
        etID = (EditText) findViewById(R.id.editTextID);
        etPass = (EditText) findViewById(R.id.editTextPIN);

        Intent in = getIntent();

        Button doneButton = (Button) findViewById(R.id.doneButton);


        doneButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String name = etName.getText().toString();
        String id = etID.getText().toString();
        String password = etPass.getText().toString();
        if (name.equals("") || id.equals("") || password.equals("")) {
            Toast.makeText(view.getContext(), "Invalid Entry", Toast.LENGTH_LONG).show();
        } else {
            sendTransactionData(id, password);
        }
    }


    private void sendTransactionData(String rID, String rPass) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("receiverId", rID);
        params.put("receiverPassword", rPass);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, RECENT_API_ENDPOINT, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            responder = response.getString("message");
                            Bundle bundle = new Bundle();
                            bundle.putString("message", "Verification Successful!");
                            bundle.putString("amount", Double.toString(response.getJSONObject("data").getDouble("amount")));
                            bundle.putString("uID", "2");
                            bundle.putString("tID", response.getJSONObject("data").getString("_id"));
                            AlertDFragment alertdFragment = new AlertDFragment();
                            alertdFragment.setArguments(bundle);
                            alertdFragment.show(fm, "Alert Dialog Fragment");

                        } catch (Exception e) {
                            Log.e("NNAB", "unexpected JSON exception", e);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volly Error", error.toString());
                        //responder = "No Transactions Exist or Wrong ID/Password";

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            Log.e("Status code", String.valueOf(networkResponse.statusCode));


                            responder = "No Transactions Exist or Wrong ID/Password";
                            Bundle bundle = new Bundle();
                            bundle.putString("message", responder);
                            bundle.putString("amount", "pls git gud");
                            AlertDFragment alertdFragment = new AlertDFragment();
                            alertdFragment.setArguments(bundle);
                            alertdFragment.show(fm, "Alert Dialog Fragment");
                        }
                        else{
                            responder = "No Network Connection";
                            Bundle bundle = new Bundle();
                            bundle.putString("message", "Not connected to the internet");
                            bundle.putString("amount", "pls git gud");
                            AlertDFragment alertdFragment = new AlertDFragment();
                            alertdFragment.setArguments(bundle);
                            alertdFragment.show(fm, "Alert Dialog Fragment");
                        }


                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("receiverId", "1");
                params.put("receiverPassword", "1");

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        helper.add(request);
    }


}
