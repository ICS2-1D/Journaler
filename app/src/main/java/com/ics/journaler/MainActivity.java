package com.ics.journaler;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Updated variable declarations to include ID field
    EditText entryId, entryTitle, mood, entry;
    Button btnInsert, btnUpdate, btnDelete, btnView;
    dbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Ray- Linking the CRUD to the buttons
        // Initialize EditTexts and Buttons
        entryId = findViewById(R.id.entryid);
        entryTitle = findViewById(R.id.entrytitle);
        mood = findViewById(R.id.mood);
        entry = findViewById(R.id.entry);

        btnInsert = findViewById(R.id.btnInsert);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnView = findViewById(R.id.btnView);
        btnDelete = findViewById(R.id.btnDelete);

        db = new dbHelper(this);

        // INSERT - Only uses title and content (ID is auto-generated)
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleTXT = entryTitle.getText().toString().trim();
                String contentTXT = entry.getText().toString().trim();
                String moodTXT = mood.getText().toString().trim();

                // Check if required fields are not empty
                if (titleTXT.isEmpty() || contentTXT.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in title and content", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Note: You might want to modify your dbHelper to include mood
                boolean checkInsertData = db.insertEntry(titleTXT, contentTXT);
                if (checkInsertData) {
                    Toast.makeText(MainActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(MainActivity.this, "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // UPDATE - Uses ID field to identify which entry to update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idTXT = entryId.getText().toString().trim();
                String titleTXT = entryTitle.getText().toString().trim();
                String contentTXT = entry.getText().toString().trim();

                // Check if required fields are not empty
                if (idTXT.isEmpty() || titleTXT.isEmpty() || contentTXT.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in ID, title, and content", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int id = Integer.parseInt(idTXT);
                    boolean checkUpdateData = db.updateEntry(id, titleTXT, contentTXT);
                    if (checkUpdateData) {
                        Toast.makeText(MainActivity.this, "Entry Updated", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(MainActivity.this, "Entry Not Updated - ID might not exist", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Please enter a valid ID number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // DELETE - Uses ID field to identify which entry to delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idTXT = entryId.getText().toString().trim();

                if (idTXT.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter an ID to delete", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int id = Integer.parseInt(idTXT);
                    boolean checkDeleteData = db.deleteEntry(id);
                    if (checkDeleteData) {
                        Toast.makeText(MainActivity.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(MainActivity.this, "Entry Not Deleted - ID might not exist", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Please enter a valid ID number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // VIEW - Shows all entries
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = db.getAllEntries();
                if (res.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuilder buffer = new StringBuilder();
                while (res.moveToNext()) {
                    buffer.append("ID: ").append(res.getInt(0)).append("\n");
                    buffer.append("Title: ").append(res.getString(1)).append("\n");
                    buffer.append("Content: ").append(res.getString(2)).append("\n\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Journal Entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
    }

    // Helper method to clear all input fields
    private void clearFields() {
        entryId.setText("");
        entryTitle.setText("");
        mood.setText("");
        entry.setText("");
    }
}