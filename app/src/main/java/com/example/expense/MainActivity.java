package com.example.expense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    private ImageView profileIcon;
    private FloatingActionButton addNote;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNote = findViewById(R.id.add_note);
        profileIcon = findViewById(R.id.profileIcon);
        recyclerView = findViewById(R.id.recyclerView);


        // Navigate to ProfileActivity when profile icon is clicked
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        addNote.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NoteDetailsActivity.class)));
        setupRecyclerView();
    }

    void setupRecyclerView() {
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoteAdapter noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        NoteAdapter adapter = (NoteAdapter) recyclerView.getAdapter();
        adapter.startListening();

    }
    @Override
    protected void onStop() {
        super.onStop();
        NoteAdapter adapter = (NoteAdapter) recyclerView.getAdapter();
        adapter.stopListening();

    }
    @Override
    protected void onResume() {
        super.onResume();
        NoteAdapter adapter = (NoteAdapter) recyclerView.getAdapter();
        adapter.notifyDataSetChanged();

    }
}