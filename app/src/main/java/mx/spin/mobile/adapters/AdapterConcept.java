package mx.spin.mobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mx.spin.mobile.R;
import mx.spin.mobile.entitys.ItemConcepts;
import mx.spin.mobile.interfaces.ConceptoSelector;
import mx.spin.mobile.utils.RecyclerViewHeaderFooterAdapter;
import mx.spin.mobile.viewholders.ConceptoViewHolder;

import java.util.List;

/**
 * Created by gorro on 12/01/16.
 */
public class AdapterConcept extends RecyclerViewHeaderFooterAdapter<ItemConcepts> {

    private Context ctx;
    private ConceptoSelector selector;

    public AdapterConcept(List<ItemConcepts> info,Context context, ConceptoSelector selector) {
        super(null,null,info);
        this.ctx = context;
        this.selector = selector;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolderItem(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_concepts, parent, false);
        RecyclerView.ViewHolder vh = new ConceptoViewHolder(v,selector,ctx);
        return vh;
    }

    @Override
    public void onBindViewHolderItem(RecyclerView.ViewHolder holder, int position) {
        ItemConcepts concepts = info.get(position);
        ConceptoViewHolder vh = (ConceptoViewHolder) holder;
        vh.bindConcepto(concepts);
    }
}
