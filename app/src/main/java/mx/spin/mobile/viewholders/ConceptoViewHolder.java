package mx.spin.mobile.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import mx.spin.mobile.R;
import mx.spin.mobile.entitys.ItemConcepts;
import mx.spin.mobile.interfaces.ConceptoSelector;

/**
 * Created by robe on 15/07/15.
 */
public class ConceptoViewHolder extends RecyclerView.ViewHolder {
    private TextView vNombre;
    private Context context;
    private ConceptoSelector selector;

    public ConceptoViewHolder(View itemView, ConceptoSelector selector, Context context) {
        super(itemView);
        vNombre= (TextView) itemView.findViewById( R.id.txtItemConcept);
        this.selector = selector;
        this.context = context;
    }
    public void bindConcepto(final ItemConcepts concepts){
        vNombre.setText(concepts.getTitulo());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selector.seleccionarConcepto(concepts);
            }
        });
    }
}
