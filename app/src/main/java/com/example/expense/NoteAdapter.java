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

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;




    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
    holder.title.setText(note.title);
    holder.description.setText(note.description);
    holder.timestamp.setText(Utility.timestampToString(note.timestamp));

    holder.itemView.setOnClickListener((v) -> {
        Intent intent = new Intent(context, NoteDetailsActivity.class);
        intent.putExtra("expense", note.title);
        intent.putExtra("amount", note.description);
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
        TextView title, description, timestamp;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_recycler_view_item);
            description = itemView.findViewById(R.id.description_recycler_view_item);
            timestamp = itemView.findViewById(R.id.timestamp_recycler_view_item);

        }
    }
}
