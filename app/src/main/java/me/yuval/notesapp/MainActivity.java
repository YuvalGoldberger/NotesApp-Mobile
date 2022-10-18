package me.yuval.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Insert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import me.yuval.notesapp.Adapters.NotesListAdapter;
import me.yuval.notesapp.Database.RoomDB;
import me.yuval.notesapp.Models.Notes;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    NotesListAdapter adapter;
    List<Notes> notes = new ArrayList<>();
    RoomDB db;
    FloatingActionButton fabAdd;
    SearchView searchBar;
    Notes selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        fabAdd = findViewById(R.id.fab_add);
        searchBar = findViewById(R.id.searchBar);

        db = RoomDB.getInstance(this);
        notes = db.mainDAO().getNotes();

        updateRecycler(notes);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
                startActivityForResult(intent, 101);

            }
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        List<Notes> filterlist = new ArrayList<>();
        for(Notes note : notes) {
            if(note.getNoteTitle().toLowerCase().contains(newText.toLowerCase())
                || note.getNoteDescription().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(note);
            }
        }
        adapter.filterList(filterlist);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101) {
            if(resultCode == Activity.RESULT_OK) {
                Notes newNote = (Notes) data.getSerializableExtra("note");
                db.mainDAO().insert(newNote);
                notes.clear();
                notes.addAll(db.mainDAO().getNotes());
                adapter.notifyDataSetChanged();
            }
        }
        else if(requestCode == 102) {
            if(resultCode == Activity.RESULT_OK) {
                Notes newNotes = (Notes) data.getSerializableExtra("note");
                db.mainDAO().update(newNotes.getNoteID(), newNotes.getNoteTitle(), newNotes.getNoteDescription());
                notes.clear();
                notes.addAll(db.mainDAO().getNotes());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        adapter = new NotesListAdapter(MainActivity.this, notes, listener);
        recyclerView.setAdapter(adapter);
    }

    private final NotesClickListener listener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onHold(Notes notes, CardView object) {
            selectedNote = new Notes();
            selectedNote = notes;

            showPopup(object);
        }
    };

    private void showPopup(CardView object) {
        PopupMenu menu = new PopupMenu(this, object);
        menu.setOnMenuItemClickListener(this);
        menu.inflate(R.menu.popup_menu);
        menu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pin:
                if(selectedNote.isPinned()) {
                    db.mainDAO().pin(selectedNote.getNoteID(), false);
                    Toast.makeText(MainActivity.this, "Unpinned Note: " + selectedNote.getNoteTitle(), Toast.LENGTH_SHORT).show();
                }
                else if(!selectedNote.isPinned()) {
                    db.mainDAO().pin(selectedNote.getNoteID(), true);
                    Toast.makeText(MainActivity.this, "Pinned Note: " + selectedNote.getNoteTitle(), Toast.LENGTH_SHORT).show();
                }
                notes.clear();
                notes.addAll(db.mainDAO().getNotes());
                adapter.notifyDataSetChanged();
                return true;

            case R.id.delete:
                db.mainDAO().del(selectedNote);
                Toast.makeText(MainActivity.this, "Deleted Note: " + selectedNote.getNoteTitle(), Toast.LENGTH_SHORT).show();
                notes.remove(selectedNote);
                adapter.notifyDataSetChanged();
                return true;
        }
        return false;
    }
}