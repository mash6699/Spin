package mx.spin.mobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import mx.spin.mobile.R;
import mx.spin.mobile.entitys.ItemBitacora;

import java.util.ArrayList;

/**
 * Created by gorro on 13/01/16.
 */
public class AdapterBitacora extends BaseAdapter {

    private Context ctx;
    private int layoutResource;
    private ArrayList<ItemBitacora> data = new ArrayList<>();

    public AdapterBitacora(Context ctx, int layoutResource, ArrayList<ItemBitacora> data) {
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
        TextView txtDate, txtHour, txtDescr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View fila = convertView;
        HolderView holder = null;

        if (fila == null) {
            LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
            fila = inflater.inflate(layoutResource, parent, false);
            holder = new HolderView();
            holder.txtDate = (TextView) fila.findViewById(R.id.txtItemBitacoraDate);
            holder.txtHour = (TextView) fila.findViewById(R.id.txtItemBitacoraHour);
            holder.txtDescr = (TextView) fila.findViewById(R.id.txtItemBitacoraDesc);
            fila.setTag(holder);
        } else {
            holder = (HolderView) fila.getTag();
        }

        ItemBitacora item = data.get(position);
        holder.txtDate.setText(item.getDate());
        holder.txtHour.setText(item.getHour());
        holder.txtDescr.setText(item.getDescr());

        return fila;
    }
}
