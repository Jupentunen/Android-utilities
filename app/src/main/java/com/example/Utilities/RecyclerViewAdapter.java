package com.example.Utilities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private List<Note> mNoteList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public RecyclerViewAdapter(List<Note> notes) {
        mNoteList = notes;
    }

    /**
     * Holds view for a card item
     */
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitleView;
        public TextView mContentView;
        public CheckBox checkSelected;

        public RecyclerViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mTitleView = itemView.findViewById(R.id.titleView);
            mContentView = itemView.findViewById(R.id.contentView);
            checkSelected = itemView.findViewById(R.id.checkBox);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_cardview, parent, false);
        return new RecyclerViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        Note note = mNoteList.get(position);
        holder.mTitleView.setText(note.getTitle());
        holder.mContentView.setText(note.getContent());
        holder.checkSelected.setChecked(note.getIsSelected());

        holder.checkSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                if (cb.isChecked()) {
                    mNoteList.get(position).setIsSelected(true);
                } else if (!cb.isChecked()) {
                    mNoteList.get(position).setIsSelected(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() { return mNoteList.size(); }
}