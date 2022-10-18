package me.yuval.notesapp.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import me.yuval.notesapp.Models.Notes;

@Dao
public interface MainDAO {

    @Insert(onConflict = REPLACE)
    void insert(Notes notes);

    @Query("SELECT * FROM notes ORDER BY noteID DESC")
    List<Notes> getNotes();


    @Query("UPDATE notes SET Title = :noteTitle, Description = :noteDescription WHERE noteID = :noteID")
    void update(int noteID, String noteTitle, String noteDescription);

    @Delete
    void del(Notes note);

    @Query("UPDATE notes SET isPinned = :value WHERE noteID = :id")
    void pin(int id, boolean value);
}
