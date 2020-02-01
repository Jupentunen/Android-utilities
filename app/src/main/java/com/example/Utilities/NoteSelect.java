package com.example.Utilities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NoteSelect extends AppCompatActivity {

    private List<Note> noteList = new ArrayList<Note>();
    private List<Note> checkedNotes = new ArrayList<Note>();
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_select);
        setToolbarAndFab();
        setFiles();
        setRecyclerView();
    }

    /**
     * Creates RecyclerView showing the files and its functionality
     */
    private void setRecyclerView() {
        RecyclerView RecyclerView = findViewById(R.id.recyclerView);
        RecyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerViewAdapter(noteList);
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(this);

        RecyclerView.setLayoutManager(LayoutManager);
        RecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String filename = noteList.get(position).getTitle() + ".txt";
                Intent intent = new Intent(NoteSelect.this, NoteEdit.class);
                intent.putExtra("filename", filename);
                startActivity(intent);
            }
        });
    }

    /**
     * Creates toolbar menu and fab functionality
     */
    private void setToolbarAndFab() {
        Toolbar Toolbar = findViewById(R.id.toolbar);
        Toolbar.setTitle(getString(R.string.notes));
        Toolbar.setTitleTextColor(Color.WHITE);
        Toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(Toolbar);
        Toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(NoteSelect.this, MainActivity.class);
            startActivity(intent);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewFileDialog();
            }
        });
    }

    /**
     * Creates AlertDialog and asks user input for creating a new note
     */
    private void createNewFileDialog() {
        final EditText input = new EditText(NoteSelect.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        final AlertDialog builder = new AlertDialog.Builder(NoteSelect.this)
            .setTitle("Give note title")
            .setView(input)
            .setPositiveButton("OK", null)
            .setNegativeButton("Cancel", null)
            .create();

        input.requestFocus();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button buttonPositive = ((AlertDialog) builder).getButton(AlertDialog.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String filename = input.getText().toString() + ".txt";

                        if (FileExist(filename)) {
                            Toast.makeText(NoteSelect.this, "Filename exists. Please provide another one.", Toast.LENGTH_LONG).show();
                        } else if (filename.length() < 5) {
                            Toast.makeText(NoteSelect.this, "Provide filename", Toast.LENGTH_LONG).show();
                        } else {
                            builder.dismiss();
                            Intent intent = new Intent(NoteSelect.this, NoteEdit.class);
                            intent.putExtra("filename", filename);
                            startActivity(intent);
                        }
                    }
                });
                Button buttonNegative = ((AlertDialog) builder).getButton(AlertDialog.BUTTON_NEGATIVE);
                buttonNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.cancel();
                    }
                }) ;

            }
        });
        builder.show();
    }

    /**
     * Loads filenames from internal memory and adds them to the noteList
     */
    private void setFiles() {
        File dir = getFilesDir();
        File[] files = dir.listFiles();
        String file;
        for (int i = 0;  i < files.length; i++) {
            file = files[i].getName();
            if (file.endsWith(".txt")) {
                Note note = new Note(file.replace(".txt", ""), OpenNotePreview(file));
                noteList.add(note);
            }
        }
    }

    /**
     * Gets the text content from a saved file
     * @param fileName File to be opened
     * @return String with files content or empty if given file doesn't exist
     */
    public String OpenNotePreview(String fileName) {
        if (FileExist(fileName)) {
            try {
                InputStream in = openFileInput(fileName);
                if (in != null) {
                    InputStreamReader temp = new InputStreamReader(in);
                    BufferedReader fileReader = new BufferedReader(temp);
                    String str = fileReader.readLine();
                    StringBuilder fileContent = new StringBuilder();
                    if (str != null) {
                        if (str.length() > 40) {
                            fileContent.append(str.substring(0, 40) + "...");
                        } else { fileContent.append(str); }
                    } else {
                        fileContent.append("Empty note...");
                    }
                    in.close();
                    return fileContent.toString();
                }
            } catch (java.io.FileNotFoundException e) {} catch (Throwable t) {
                Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return "";
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
     * Adds menu items to mToolbar
     * @param menu
     * @return True
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Menu item functionality
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.deleteItem) {
            for (int i = 0; i < noteList.size(); i++) {
                Note note = noteList.get(i);
                if (note.getIsSelected()) {
                    checkedNotes.add(note);
                }
            }
            return removeItems(checkedNotes);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Removes item(s) from recycler view
     * @param notesToRemove removable notes in a list
     */
    public boolean removeItems(List<Note> notesToRemove) {
        /*
        if (noteList.size() != 0) {
            mAdapter.notifyItemRemoved(position);
            File dir = getFilesDir();
            File file = new File(dir, noteList.get(position).getTitle());
            noteList.remove(position);
            deleteFile(file.getNa me() + ".txt");
            return true;
        }
        Toast.makeText(NoteSelect.this, "No files to remove!", Toast.LENGTH_LONG).show();
        return false;
        */

        if (!notesToRemove.isEmpty()) {
            StringBuilder deleted = new StringBuilder();
            Note note;

            for (int i = 0; i < notesToRemove.size(); i++) {
                note = notesToRemove.get(i);
                mAdapter.notifyItemRemoved(noteList.indexOf(note));
                mAdapter.notifyItemRangeChanged(noteList.indexOf(note), mAdapter.getItemCount());
                noteList.remove(note);
                deleteFile( note.getTitle() + ".txt");
                deleted.append( "\n" + note.getTitle() );
            }
            checkedNotes.clear();
            Toast.makeText(NoteSelect.this, "Deleted: " + deleted, Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(NoteSelect.this, "None selected", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
