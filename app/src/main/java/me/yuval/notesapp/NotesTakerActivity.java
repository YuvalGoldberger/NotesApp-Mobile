package me.yuval.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.yuval.notesapp.Models.Notes;

public class NotesTakerActivity extends AppCompatActivity {

    EditText editTitle, editDescription;
    ImageView imageView_Save;
    Notes notes;
    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);

        notes = new Notes();

        try {
            notes = (Notes) getIntent().getSerializableExtra("old_note");
            editTitle.setText(notes.getNoteTitle());
            editDescription.setText(notes.getNoteDescription());
            isOldNote = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView_Save = findViewById(R.id.imageView_Save);
        imageView_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString();
                String description = editDescription.getText().toString();

                if(description.isEmpty()) {
                    Toast.makeText(NotesTakerActivity.this, "Please add text to your note.", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd/MM/yyyy, HH:mm");
                Date date = new Date();

                if(!isOldNote) { notes = new Notes(); }
                notes.setNoteTitle(title);
                notes.setNoteDescription(description);
                notes.setCreated(formatter.format(date));

                Intent intent = new Intent();
                intent.putExtra("note", notes);
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
    }
}