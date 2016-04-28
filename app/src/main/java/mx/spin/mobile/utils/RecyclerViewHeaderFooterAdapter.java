package mx.spin.mobile.utils;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by jorge on 6/01/15.
 */
public abstract class RecyclerViewHeaderFooterAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private final RecyclerView.ViewHolder header;
    private final RecyclerView.ViewHolder footer;
    protected final List<T> info;

    public RecyclerViewHeaderFooterAdapter(RecyclerView.ViewHolder header, RecyclerView.ViewHolder footer, List<T> info) {
        this.header = header;
        this.footer = footer;
        this.info = info;
    }

    public boolean isPositionHeader(int position) {
        if (header == null) {
            return false;
        }
        return position == 0;
    }

    public boolean isPositionFooter(int position) {
        if (footer == null) {
            return false;
        }
        int head = header == null ? 0 : 1;
        return position == info.size() + head;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        if (isPositionFooter(position))
            return TYPE_FOOTER;

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        int head = header == null ? 0 : 1;
        int foot = footer == null ? 0 : 1;
        return info.size() + head + foot;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return header;
        }
        else if (viewType == TYPE_FOOTER) {
            return footer;
        }
        return onCreateViewHolderItem(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isPositionHeader(position) || isPositionFooter(position)) {
            return;
        }
        int head = header == null ? 0 : 1;
        onBindViewHolderItem(holder, position - head);
    }

    public int getInfoSize() {
        return info.size();
    }

    public void addItem(T item) {
        int head = header == null ? 0 : 1;
        this.notifyItemInserted(head + info.size());
        info.add(item);
    }

    public void insertItem(T item, int position) {
        int head = header == null ? 0 : 1;
        info.add(position, item);
        this.notifyItemInserted(position + head);
    }

    public void clearItems() {
        int head = header == null ? 0 : 1;
        this.notifyItemRangeRemoved(head, info.size());
        info.clear();
    }

    public abstract RecyclerView.ViewHolder onCreateViewHolderItem(ViewGroup parent);

    public abstract void onBindViewHolderItem(RecyclerView.ViewHolder holder, int position);
}
