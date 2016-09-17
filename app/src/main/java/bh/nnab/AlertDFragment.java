package bh.nnab;

/**
 * Created by Shayan on 18-Sep-16.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AlertDFragment extends DialogFragment {

    final static String RECENT_API_ENDPOINT = "http://54.251.137.104:3001/api/transaction/confirm";
    VolleySingleton helper = VolleySingleton.getInstance();
    boolean transactionRegistered;

    private void sendTransactionConfirmationData(String uID, String tID) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userId", uID);
        params.put("transactionId", tID);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, RECENT_API_ENDPOINT, new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            transactionRegistered = response.getBoolean("success");
                        } catch (Exception e) {
                            Log.e("NNAB", "unexpected JSON exception", e);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volly Error", error.toString());

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            Log.e("Status code", String.valueOf(networkResponse.statusCode));

                        }
                        else{
                            Log.d("Error", "Not Connected to Internet");
                        }


                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", "1");
                params.put("transactionId", "57dd7ca172337a585414996a");

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        String title = bundle.getString("message", "lolufail");
        String amount = bundle.getString("amount", "lolufailagain");
        final String uID = bundle.getString("uID");
        final String tID = bundle.getString("tID");



        if(!amount.equals("pls git gud")) {
            return new android.support.v7.app.AlertDialog.Builder(getActivity())
                    // Set Dialog Icon
                    //.setIcon(R.mipmap.ic_launcher)
                    // Set Dialog Title
                    .setTitle(title)
                    // Set Dialog Message
                    .setMessage("Amount to be transferred: HK$" + amount)

                    // Positive button
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendTransactionConfirmationData(uID, tID);
                            Log.d("Payment Confirmed", "Payment received by shop.");
                        }
                    })

                    // Negative Button
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something else
                        }
                    }).create();
        }
        else{
            return new android.support.v7.app.AlertDialog.Builder(getActivity())
                    // Set Dialog Icon
                    //.setIcon(R.mipmap.ic_launcher)
                    // Set Dialog Title
                    .setTitle(title)
                    // Set Dialog Message
                    .setMessage("Please try again")

                    // Positive button
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something else
                        }
                    }).create();
        }
    }
}
