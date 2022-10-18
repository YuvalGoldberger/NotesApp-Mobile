package me.yuval.notesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.yuval.notesapp.Models.Notes;
import me.yuval.notesapp.NotesClickListener;
import me.yuval.notesapp.R;

public class NotesListAdapter extends RecyclerView.Adapter<ViewHolder> {

    Context context;
    List<Notes> list;
    NotesClickListener listener;

    public NotesListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView_Title.setText(list.get(position).getNoteTitle());
        holder.textView_Title.setSelected(true);

        holder.textView_notes.setText(list.get(position).getNoteDescription());

        holder.textView_Date.setText(list.get(position).getCreated());
        holder.textView_Date.setSelected(true);

        if(list.get(position).isPinned()) holder.imageView_Pinned.setImageResource(R.drawable.pin);
        else holder.imageView_Pinned.setImageResource(0);

        int colorCode = getRandomColor();
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCode));

        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onHold(list.get(holder.getAdapterPosition()), holder.notes_container);
                return true;
            }
        });



    }

    public int getRandomColor() {
        List<Integer> colors = new ArrayList<>();

        colors.add(R.color.green);
        colors.add(R.color.yellow);
        colors.add(R.color.pink);
        colors.add(R.color.red);
        colors.add(R.color.orange);
        colors.add(R.color.purple_200);
        colors.add(R.color.purple_500);
        colors.add(R.color.purple_700);
        colors.add(R.color.teal_200);
        colors.add(R.color.teal_700);

        Random random = new Random();
        int color = random.nextInt(colors.size());

        return colors.get(color);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Notes> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {

    CardView notes_container;
    TextView textView_Title, textView_notes, textView_Date;
    ImageView imageView_Pinned;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        {
            notes_container = itemView.findViewById(R.id.notes_container);
            textView_Title = itemView.findViewById(R.id.textView_Title);
            textView_notes = itemView.findViewById(R.id.textView_notes);
            textView_Date = itemView.findViewById(R.id.textView_Date);
            imageView_Pinned = itemView.findViewById(R.id.imageView_Pinned);
        }
    }
}