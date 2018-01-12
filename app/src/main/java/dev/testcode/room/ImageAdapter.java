package dev.testcode.room;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hienl on 1/12/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private int itemSize;
    private Random random;
    private static final String TAG = "ImageAdapter";
    private RecyclerView mRecyclerView;
    private Context context;
    private int recyclerHeight;
    private List<Integer> integers;
    private int selectedIndex = -1;

    public ImageAdapter(Context context) {
        this.context = context;
        integers = new ArrayList<>();
        registerAdapterDataObserver(adapterDataObserver);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        bindColRowGrid(holder, position);
        holder.imageView.setScrollPosition(1, 0);
        if (holder.getAdapterPosition() == selectedIndex) {
            holder.itemView.setBackgroundResource(R.drawable.item_border);
            holder.imageView.setAllowPinch(true);
        } else {
            holder.itemView.setBackground(null);
            holder.imageView.setAllowPinch(false);
        }
        holder.imageView.setImageResource(integers.get(holder.getAdapterPosition()));
    }

    private void bindColRowGrid(final ViewHolder holder, final int position) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        switch (getItemCount()) {
            case 1:
            case 2:
                layoutParams.height = recyclerHeight;
                break;
            case 3:
                if (position == 0) {
                    layoutParams.height = recyclerHeight;
                } else {
                    layoutParams.height = recyclerHeight / 2;
                }
                break;
            case 4:
                layoutParams.height = recyclerHeight / 2;
                break;
        }
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return integers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TouchImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_item);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedIndex = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

    public void add(int resId) {
        if (integers.size() < 4) {
            integers.add(resId);
            notifyDataSetChanged();
        }
    }

    public void remove() {
        if (selectedIndex >= 0 && selectedIndex < integers.size()) {
            integers.remove(selectedIndex);
            clearSelect();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        recyclerHeight = mRecyclerView.getHeight();
        updateLayoutManager();
    }

    RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            recyclerHeight = mRecyclerView.getHeight();
            updateLayoutManager();
        }
    };

    private void updateLayoutManager() {
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
        if (getItemCount() == 1) {
            layoutManager.setSpanCount(1);
        } else {
            layoutManager.setSpanCount(2);
        }
    }

    public void clearSelect() {
        selectedIndex = -1;
        notifyDataSetChanged();
    }
}
