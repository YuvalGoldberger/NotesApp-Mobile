package me.yuval.notesapp;

import androidx.cardview.widget.CardView;

import me.yuval.notesapp.Models.Notes;

public interface NotesClickListener {
    void onClick(Notes notes);
    void onHold(Notes notes, CardView object);
}
