package com.example.Utilities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class NoteEdit extends AppCompatActivity {

    private EditText Notepad;
    private String mFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        mFilename = getIntent().getExtras().getString("filename");

        setToolbarAndFab();

        Notepad = findViewById(R.id.notepad);
        Notepad.setText(Open(mFilename));
    }

    /**
     * Saves the content of textfield in a file
     * @param fileName Name of the saved content
     */
    public void Save(String fileName) {
        try {
            OutputStreamWriter out =
                    new OutputStreamWriter(openFileOutput(fileName, 0));
            out.write(Notepad.getText().toString());
            out.close();
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
        } catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Check if given filename exists
     * @param fileName name of the file
     * @return True or false
     */
    public boolean FileExist(String fileName) {
        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }

    /**
     * Gets the text content from a saved file
     * @param fileName File to be opened
     * @return String with files content or empty if given file doesn't exist
     */
    public String Open(String fileName) {
        if (FileExist(fileName)) {
            try {
                InputStream in = openFileInput(fileName);
                if (in != null) {
                    InputStreamReader temp = new InputStreamReader(in);
                    BufferedReader fileReader = new BufferedReader(temp);
                    String str;
                    StringBuilder fileContent = new StringBuilder();
                    while ((str = fileReader.readLine()) != null) {
                        fileContent.append(str).append("\n");
                    } in.close();
                    return fileContent.toString();
                }
            } catch (java.io.FileNotFoundException e) {} catch (Throwable t) {
                Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return "";
    }

    /**
     * Creates toolbar menu and fab functionality
     */
    private void setToolbarAndFab() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(mFilename.replace(".txt",""));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save(mFilename);
                Intent intent = new Intent(NoteEdit.this, NoteSelect.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Save(mFilename);
            }
        });
    }
}
