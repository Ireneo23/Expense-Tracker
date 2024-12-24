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

public class ExpenseDetailsActivity extends AppCompatActivity {
        EditText titleEditText, amountEditText;
        ImageButton delete_expense;
        Button save;
        TextView titleText;
        String title, amount, expenseId;
        Boolean isEditMode = false;
        ImageView backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_details);


        titleEditText = findViewById(R.id.expense_title);
        amountEditText = findViewById(R.id.amount);
        save = findViewById(R.id.expense_save);
        titleText = findViewById(R.id.title_textView);
        delete_expense = findViewById(R.id.note_delete);
        backbtn = findViewById(R.id.back_btn);
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseDetailsActivity.this, MainActivity.class);
            startActivity(intent);
        });


        title = getIntent().getStringExtra("expense");
        amount = getIntent().getStringExtra("amount");
        expenseId = getIntent().getStringExtra("docId");
        if (expenseId != null && !expenseId.isEmpty()) {
            isEditMode = true;
        }

        titleEditText.setText(title);
        amountEditText.setText(amount);
        if (isEditMode) {
            titleText.setText("Edit Your Expense");
            delete_expense.setVisibility(View.VISIBLE);
        }


        save.setOnClickListener((v) -> saveNote());
        delete_expense.setOnClickListener((v) -> deleteNoteFromFirebase());
    }



    private void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteDescription = amountEditText.getText().toString();

        Expense expense = new Expense();
        expense.setTitle(noteTitle);
        expense.setAmount(noteDescription);
        expense.setTimestamp(Timestamp.now());
        saveNoteToFirebase(expense);
        if (noteTitle.isEmpty()) {
            return;
        }

    }

    void saveNoteToFirebase(Expense expense) {
        DocumentReference documentReference = null;

        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForNotes().document(expenseId);

        }else{
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }



        documentReference.set(expense).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Utility.showToast(ExpenseDetailsActivity.this, "Expense added successfully");
                    Intent intent = new Intent(ExpenseDetailsActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Utility.showToast(ExpenseDetailsActivity.this, "Failed while adding Expense");

                }
            }
        });
    }
    private void deleteNoteFromFirebase() {
        // Show a confirmation dialog before deleting the note
        new AlertDialog.Builder(ExpenseDetailsActivity.this)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this Expense record?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Proceed with deleting the note
                    DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document(expenseId);
                    documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Utility.showToast(ExpenseDetailsActivity.this, "Expense Deleted Successfully");
                                Intent intent = new Intent(ExpenseDetailsActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Utility.showToast(ExpenseDetailsActivity.this, "Failed while Deleting Expense record");
                            }
                        }
                    });
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }


}