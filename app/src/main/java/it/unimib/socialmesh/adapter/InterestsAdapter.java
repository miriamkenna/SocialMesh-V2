package it.unimib.socialmesh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.socialmesh.R;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.InterestViewHolder> {
    private List<String> interestsList;

    public InterestsAdapter(List<String> interestsList) {
        this.interestsList = interestsList;
    }

    public List<String> getInterestsList() {
        return interestsList;
    }

    public void setInterestsList(List<String> interestsList) {
        this.interestsList = interestsList;
    }

    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interest, parent, false);
        return new InterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder holder, int position) {
        String interest = interestsList.get(position);
        holder.bind(interest);
    }

    @Override
    public int getItemCount() {
        return interestsList.size();
    }

    public static class InterestViewHolder extends RecyclerView.ViewHolder {
        TextView interestTextView;

        public InterestViewHolder(@NonNull View itemView) {
            super(itemView);
            interestTextView = itemView.findViewById(R.id.textViewInterest);
        }

        public void bind(String interest) {
            interestTextView.setText(interest);
        }
    }
}
