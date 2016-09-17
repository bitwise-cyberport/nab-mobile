package bh.nnab;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;

/**
 * Created by Shayan on 18-Sep-16.
 */
public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
    Cursor res;
    static transactionDatabaseHelper myDB;

    public static class ViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        public TextView mTextView;
        public TextView mTimeView;
        public TextView mPersonView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.my_text_view);
            mTimeView = (TextView) v.findViewById(R.id.my_time_view);
            mPersonView = (TextView) v.findViewById(R.id.my_person_view);

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    TextView tmpTextView = (TextView) view.findViewById(R.id.my_text_view);
                    String value = tmpTextView.getText().toString();
                    myDB = new transactionDatabaseHelper(view.getContext());
                    Toast.makeText(view.getContext(), "Stop touching me", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

        }
    }

    public myAdapter(Cursor results){
        res = results;
    }

    public void reAssCurs(Cursor results){res = results;}

    @Override
    public myAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_text_view_sample, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(myAdapter.ViewHolder holder, int position) {
        res.moveToPosition(position);
        holder.mTextView.setText("HK$ " + Double.toString(res.getDouble(3)));
        holder.mTimeView.setText(res.getString(1) + ", " + res.getString(2));
        holder.mPersonView.setText("Receiver ID: " + res.getString(5));
    }

    @Override
    public int getItemCount() {
        return res.getCount();
    }
}
