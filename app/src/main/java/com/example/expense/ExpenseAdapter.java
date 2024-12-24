package com.example.expense;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ExpenseAdapter extends FirestoreRecyclerAdapter<Expense, ExpenseAdapter.NoteViewHolder> {
    Context context;




    public ExpenseAdapter(@NonNull FirestoreRecyclerOptions<Expense> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Expense expense) {
    holder.title.setText(expense.title);
    holder.amount.setText(expense.amount);
    holder.timestamp.setText(Utility.timestampToString(expense.timestamp));

    holder.itemView.setOnClickListener((v) -> {
        Intent intent = new Intent(context, ExpenseDetailsActivity.class);
        intent.putExtra("expense", expense.title);
        intent.putExtra("amount", expense.amount);
        String docId = this.getSnapshots().getSnapshot(position).getId();
        intent.putExtra("docId", docId);
        context.startActivity(intent);

            });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_note_item, parent, false);
       return new NoteViewHolder(view);

    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, amount, timestamp;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_recycler_view_item);
            amount = itemView.findViewById(R.id.amount_recycler_view_item);
            timestamp = itemView.findViewById(R.id.timestamp_recycler_view_item);

        }
    }
}
