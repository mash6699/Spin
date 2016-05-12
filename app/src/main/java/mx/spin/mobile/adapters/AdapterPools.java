package mx.spin.mobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mx.spin.mobile.R;

import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.interfaces.ISpin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miguel_angel
 */
public class AdapterPools extends BaseAdapter {

    private Context ctx;
    private int layoutResource;
    private List<Pool> data = new ArrayList<>();
    private ISpin iSpin;

    public AdapterPools(Context ctx, int layoutResource, List<Pool> misPiscinas, ISpin iSpin) {
        this.ctx = ctx;
        this.layoutResource = layoutResource;
        this.data = misPiscinas;
        this.iSpin = iSpin;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public class HolderView {
        private TextView txtTitle;
        private TextView txtSubtitle;
        private ImageView deletePool;
        private ImageView editPool;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View fila = convertView;
        HolderView holder = null;
        final Pool item = data.get(position);

        if (fila == null) {
            LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
            fila = inflater.inflate(layoutResource, parent, false);
            holder = new HolderView();
            holder.txtTitle     = (TextView) fila.findViewById(R.id.txtItemPoolName);
            holder.txtSubtitle  = (TextView) fila.findViewById(R.id.txtItemPoolLastAnalize);
            holder.deletePool   = (ImageView)   fila.findViewById(R.id.iv_deletePool);
            holder.editPool     = (ImageView)   fila.findViewById(R.id.iv_editPool);
            fila.setTag(holder);
        } else {
            holder = (HolderView) fila.getTag();
        }

        holder.txtTitle.setText(item.getPool_name());

        if(item.getAnalysis()!= null){
            holder.txtSubtitle.setText(item.getAnalysis());
        }

        holder.deletePool.setOnClickListener(new View.OnClickListener() {
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
        });

        return fila;
    }

}
