package it.unimib.socialmesh.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.Event;
import it.unimib.socialmesh.model.jsonFields.Location;
import it.unimib.socialmesh.ui.main.HomeActivity;

public class RecyclerViewEventsAdapter extends RecyclerView.Adapter<RecyclerViewEventsAdapter.EventsViewHolder> {
    private static final String TAG = RecyclerViewEventsAdapter.class.getSimpleName();
    private final List<Event> eventsList; // Lista originale
    private List<Event> filteredList; // Lista filtrata
    private int genre;
    private final OnItemClickListener onItemClickListener;
    Context context;

    public interface OnItemClickListener {
        void onEventItemClick(Event event);
    }


    public RecyclerViewEventsAdapter(Context context, List<Event> eventsList,
                                     OnItemClickListener onItemClickListener) {
        this.eventsList = eventsList;
        this.filteredList = new ArrayList<>(eventsList);
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void filterByGenre(String genre) {
        filteredList.clear();
        for (Event event : eventsList) {
            if (event.getGenreName().equalsIgnoreCase(genre)) {
                filteredList.add(event);
            }
        }
        notifyDataSetChanged();
    }

    public void filterByQuery(String query) {
        filteredList.clear();
        for (Event event : eventsList) {
            if (event.getGenreName().equalsIgnoreCase(query)) {
                filteredList.add(event);
            }
            if (event.getName().equalsIgnoreCase(query)) {
                filteredList.add(event);
            }
            if (event.getDates1().equalsIgnoreCase(query)) {
                filteredList.add(event);
            }
        }
        if (filteredList.isEmpty()) {
            filteredList.addAll(eventsList);
        }
        notifyDataSetChanged();
    }


    public void setItems(List<Event> eventsList) {
        filteredList.clear();
        filteredList.addAll(eventsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list, parent, false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        Event event = filteredList.get(position);
        holder.bind(event.getName1(), event.getDates1(), event.getUrlImages());
    }

    @Override
    public int getItemCount() {
        if (filteredList != null) {
            return filteredList.size();
        }
        return 0;
    }

    public void updateData() {
        // Aggiorna la lista degli eventi
        eventsList.clear();
        eventsList.addAll(eventsList);

        // Notifica la RecyclerView
        notifyDataSetChanged();
    }

    public void clearFilters() {
        filteredList.clear();
        filteredList.addAll(eventsList);
        notifyDataSetChanged();
    }


    public class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textViewName, textViewDate;
        private final ImageView imageView;

        public EventsViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.item1);
            textViewDate = itemView.findViewById(R.id.item2);
            imageView = itemView.findViewById(R.id.item3);
            itemView.setOnClickListener(this);
        }

        public void bind(String name, String date, String image) {
            textViewName.setText(name);
            textViewDate.setText(date);
            CircularProgressDrawable drawable = new CircularProgressDrawable(itemView.getContext());
            drawable.setColorSchemeColors(R.color.md_theme_light_primary, R.color.md_theme_dark_primary, R.color.md_theme_dark_inversePrimary);
            drawable.setCenterRadius(30f);
            drawable.setStrokeWidth(5f);
            drawable.start();
            Glide.with(itemView.getContext())
                    .load(image)
                    .placeholder(drawable)
                    .error(drawable)
                    .into(imageView);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                int position = getAdapterPosition();
                onItemClickListener.onEventItemClick(filteredList.get(position));
            }
        }
    }
}