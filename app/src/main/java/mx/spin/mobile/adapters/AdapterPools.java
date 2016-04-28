package mx.spin.mobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import mx.spin.mobile.R;

import mx.spin.mobile.entitys.Piscina;

import java.util.ArrayList;

/**
 * Created by gorro on 12/01/16.
 */
public class AdapterPools extends BaseAdapter {

    private Context ctx;
    private int layoutResource;
    private ArrayList<Piscina> data = new ArrayList<>();

    public AdapterPools(Context ctx, int layoutResource, ArrayList<Piscina> data) {
        this.ctx = ctx;
        this.layoutResource = layoutResource;
        this.data = data;
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
        TextView txtTitle;
        TextView txtSubtitle;
      //  ImageView deletePool;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View fila = convertView;
        HolderView holder = null;
        Piscina item = data.get(position);

        if (fila == null) {
            LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
            fila = inflater.inflate(layoutResource, parent, false);
            holder = new HolderView();
            holder.txtTitle = (TextView) fila.findViewById(R.id.txtItemPoolName);
            holder.txtSubtitle = (TextView) fila.findViewById(R.id.txtItemPoolLastAnalize);
         //   holder.deletePool  = (ImageView)   fila.findViewById(R.id.deletePool);
            fila.setTag(holder);
        } else {
            holder = (HolderView) fila.getTag();
        }


        holder.txtTitle.setText(item.getNombre());
        holder.txtSubtitle.setText("Sin analizar");

//        holder.deletePool.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ctx, "WIP", Toast.LENGTH_SHORT).show();
//            }
//        });

        return fila;
    }
}
