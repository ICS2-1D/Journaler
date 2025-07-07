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

    EditText entryTitle, mood, entry;
    Button btnInsert, btnUpdate, btnView, btnDelete;
    dbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Optional: adjust for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Ray- Linking the CRUD to the buttons
        // Initialize EditTexts and Buttons
        entryTitle = findViewById(R.id.entrytitle);
        mood = findViewById(R.id.mood);
        entry = findViewById(R.id.entry);

        btnInsert = findViewById(R.id.btnInsert);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnView = findViewById(R.id.btnView);
        btnDelete = findViewById(R.id.btnDelete);

        db = new dbHelper(this);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleTXT = title.getText().toString();
                String contentTXT = content.getText().toString();

                boolean checkInsertData = db.insertEntry(titleTXT, contentTXT);
                if (checkInsertData) {
                    Toast.makeText(MainActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idTXT = id.getText().toString();
                String titleTXT = title.getText().toString();
                String contentTXT = content.getText().toString();

                boolean checkUpdateData = db.updateEntry(Integer.parseInt(idTXT), titleTXT, contentTXT);
                if (checkUpdateData) {
                    Toast.makeText(MainActivity.this, "Entry Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Entry Not Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idTXT = id.getText().toString();

                boolean checkDeleteData = db.deleteEntry(Integer.parseInt(idTXT));
                if (checkDeleteData) {
                    Toast.makeText(MainActivity.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Entry Not Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
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
                builder.setTitle("User Entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
    }
}



        })
        }

    }
}
