package mx.spin.mobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.spin.mobile.R;
import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.interfaces.ISpin;
import mx.spin.mobile.utils.UtilViews;

/**
 * Created by miguel_angel on 23/05/2016.
 */
public class AnalysisAdapter extends BaseAdapter{

    private Context context;
    private List<Pool> pools = new ArrayList<>();
   // private ISpin iSpin;
    private UtilViews utilViews;
    private int layoutResource;


    public AnalysisAdapter(Context context, int layout, List<Pool> mPools){ //, ISpin iSpin
        this.context = context;
        this.utilViews = new UtilViews().getInstance(context);
        this.pools = mPools;
       // this.iSpin = iSpin;
        this.layoutResource = layout;
    }

    @Override
    public int getCount() {
        return pools.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class HolderView {
        private TextView txtTitle;
        private TextView txtSubtitle;
    /*    private ImageView deletePool;
        private ImageView editPool;*/
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View fila = convertView;
        HolderView holder = null;
        final Pool item = pools.get(position);

        if (fila == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            fila = inflater.inflate(layoutResource, parent, false);
            holder = new HolderView();
            holder.txtTitle     = (TextView) fila.findViewById(R.id.txtItemPoolName);
            holder.txtSubtitle  = (TextView) fila.findViewById(R.id.txtItemPoolLastAnalize);
          /*  holder.deletePool   = (ImageView)   fila.findViewById(R.id.iv_deletePool);
            holder.editPool     = (ImageView)   fila.findViewById(R.id.iv_editPool);
       */
            fila.setTag(holder);
            holder.txtTitle.setTypeface(utilViews.setFontRegular());
            holder.txtSubtitle.setTypeface(utilViews.setFontNormal());
        } else {
            holder = (HolderView) fila.getTag();
        }

        holder.txtTitle.setText(item.getPool_name());

        if(item.getAnalysis()!= null){
            holder.txtSubtitle.setText(item.getAnalysis());
        }

      /*  holder.deletePool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iSpin.setAction(1, item.getPool_id());
            }
        });

        holder.editPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iSpin.setAction(0, item.getPool_id());
            }
        });*/

        return fila;
    }

}
