package it.unimib.socialmesh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.unimib.socialmesh.R;
import it.unimib.socialmesh.model.Event;

public class RecyclerViewEventsNearYouAdapter extends RecyclerView.Adapter<RecyclerViewEventsNearYouAdapter.EventsViewHolder>{
    private static final String TAG = RecyclerViewEventsNearYouAdapter.class.getSimpleName();
    private final List<Event> eventsList; // Lista originale
    private List<Event> filteredListByPosition; // Lista filtrata
    private int genre, km;
    private final RecyclerViewEventsNearYouAdapter.OnItemClickListener onItemclickListener;
    private Double userLongitude, userLatitude;
    Context context;

    public interface OnItemClickListener {
        void onEventItemClick(Event event);
    }


    public RecyclerViewEventsNearYouAdapter(Context context, Double latitude, Double longitude, List<Event> eventsList,
                                    OnItemClickListener onItemclickListener) {
        this.eventsList = eventsList;
        this.onItemclickListener = onItemclickListener;
        this.context = context;
        this.userLongitude = longitude;
        this.userLatitude = latitude;
        this.km = 500;
        this.filteredListByPosition = new ArrayList<>(eventsList);

    }


    public static double distance(double startLat, double startLong, double endLat, double endLong) {
        int earthRadius = 6371; // in chilometri

        double latDistance = Math.toRadians(endLat - startLat);
        double longDistance = Math.toRadians(endLong - startLong);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat))
                * Math.sin(longDistance / 2) * Math.sin(longDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    public void filterByQuery(String query){
        filteredListByPosition.clear();
        if(userLatitude != null && userLongitude != null) {
            for (Event event : eventsList) {
                if (distanceFromUserToEvent(userLatitude, userLongitude, event.getLatitude(), event.getLongitude())) {
                    if (event.getGenreName().equalsIgnoreCase(query)) {
                        filteredListByPosition.add(event);
                    }
                    if (event.getName().equalsIgnoreCase(query)) {
                        filteredListByPosition.add(event);
                    }
                    if (event.getDates1().equalsIgnoreCase(query)) {
                        filteredListByPosition.add(event);
                    }
                }
            }
        }
        if (filteredListByPosition.isEmpty()) {
            filteredByPosition(eventsList);
        }
        notifyDataSetChanged();
    }

    public boolean distanceFromUserToEvent(Double userLatitude, Double userLongitude, Double eventLatitude, Double eventLongitude){
        if(userLatitude != null && userLongitude != null) {
            double distance = distance(userLatitude, userLongitude, eventLatitude, eventLongitude);
            if (distance < km) {
                return true;
            }
        }
        return false;
    }
    public void filteredByPosition(List<Event> eventsList){
        filteredListByPosition.clear();
        if(userLatitude != null && userLongitude != null) {
            for (Event event1 : eventsList) {
                Double eventLongitude = event1.getLongitude();
                Double eventLatitude = event1.getLatitude();
                double distance = distance(userLatitude, userLongitude, eventLatitude, eventLongitude);

                if (distance <= km) {
                    filteredListByPosition.add(event1);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterByGenre(String genre) {
        filteredListByPosition.clear();
        for (Event event : eventsList) {
            if (event.getGenreName().equalsIgnoreCase(genre) && distanceFromUserToEvent(userLatitude, userLongitude, event.getLatitude(), event.getLongitude())) {
                filteredListByPosition.add(event);
            }
        }
        notifyDataSetChanged();
    }

    public void clearFilters() {
        filteredListByPosition.clear();
        filteredByPosition(eventsList);
        notifyDataSetChanged();
    }

    public void setKM(Integer km){
        this.km=km;
        notifyDataSetChanged();
    }

    public void setItems(List<Event> eventsList){
        filteredListByPosition.clear();
        filteredByPosition(eventsList);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerViewEventsNearYouAdapter.EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_nearyou, parent, false);
        return new RecyclerViewEventsNearYouAdapter.EventsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewEventsNearYouAdapter.EventsViewHolder holder, int position) {
        if(filteredListByPosition.isEmpty()) {
            filteredByPosition(eventsList);
        }
        Event event = filteredListByPosition.get(position);
        holder.bind(event.getName1(), event.getDates1(), event.getUrlImages());

    }

    @Override
    public int getItemCount() {
        if (filteredListByPosition != null) {
            return filteredListByPosition.size();
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

    public void updateLocation(Double userLatitude, Double userLongitude){
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
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
            drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);
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
                if(!filteredListByPosition.isEmpty()){
                    onItemclickListener.onEventItemClick(filteredListByPosition.get(position));
                }
                else{
                    filteredByPosition(eventsList);
                    notifyDataSetChanged();
                }
            }
        }
    }
}