package com.example.expense;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {
        EditText titleEditText, descriptionEditText;
        ImageButton deleteNote;
        Button saveNote;
        TextView titleText;
        String title, description, noteId;
        Boolean isEditMode = false;
        ImageView backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_details);


        titleEditText = findViewById(R.id.note_title);
        descriptionEditText = findViewById(R.id.note_description);
        saveNote = findViewById(R.id.note_save);
        titleText = findViewById(R.id.title_textView);
        deleteNote = findViewById(R.id.note_delete);
        backbtn = findViewById(R.id.back_btn);
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(NoteDetailsActivity.this, MainActivity.class);
            startActivity(intent);
        });


        title = getIntent().getStringExtra("expense");
        description = getIntent().getStringExtra("amount");
        noteId = getIntent().getStringExtra("docId");
        if (noteId != null && !noteId.isEmpty()) {
            isEditMode = true;
        }

        titleEditText.setText(title);
        descriptionEditText.setText(description);
        if (isEditMode) {
            titleText.setText("Edit Your Expense");
            deleteNote.setVisibility(View.VISIBLE);
        }


        saveNote.setOnClickListener((v) -> saveNote());
        deleteNote.setOnClickListener((v) -> deleteNoteFromFirebase());
    }



    private void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteDescription = descriptionEditText.getText().toString();

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setDescription(noteDescription);
        note.setTimestamp(Timestamp.now());
        saveNoteToFirebase(note);
        if (noteTitle.isEmpty()) {
            return;
        }

    }

    void saveNoteToFirebase(Note note) {
        DocumentReference documentReference = null;

        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForNotes().document(noteId);

        }else{
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }



        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utility.showToast(NoteDetailsActivity.this, "Expense added successfully");
                    Intent intent = new Intent(NoteDetailsActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Utility.showToast(NoteDetailsActivity.this, "Failed while adding Expense");

                }
            }
        });
    }
    private void deleteNoteFromFirebase() {
        // Show a confirmation dialog before deleting the note
        new AlertDialog.Builder(NoteDetailsActivity.this)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this Expense record?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Proceed with deleting the note
                    DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document(noteId);
                    documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Utility.showToast(NoteDetailsActivity.this, "Expense Deleted Successfully");
                                Intent intent = new Intent(NoteDetailsActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Utility.showToast(NoteDetailsActivity.this, "Failed while Deleting Expense record");
                            }
                        }
                    });
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }


}