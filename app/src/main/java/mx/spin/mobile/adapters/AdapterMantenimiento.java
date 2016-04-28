package mx.spin.mobile.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import mx.spin.mobile.R;
import mx.spin.mobile.entitys.ItemMantenimiento;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gorro on 18/01/16.
 */
public class AdapterMantenimiento extends BaseAdapter {

    private Context ctx;
    private int layoutResource;
    private ArrayList<ItemMantenimiento> data = new ArrayList<>();

    public AdapterMantenimiento(Context ctx, int layoutResource, ArrayList<ItemMantenimiento> data) {
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
        TextView txtType, txtNumer, txtNeeded, txtDescr;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View fila = convertView;
        HolderView holder = null;

        if (fila == null) {
            LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
            fila = inflater.inflate(layoutResource, parent, false);
            holder = new HolderView();
            holder.txtType = (TextView) fila.findViewById(R.id.txtItemMantenType);
            holder.txtNumer = (TextView) fila.findViewById(R.id.txtItemMantenNumberProblem);
            holder.txtNeeded = (TextView) fila.findViewById(R.id.txtItemMantenWhatNeed);
            holder.txtDescr = (TextView) fila.findViewById(R.id.txtItemMantenDescr);
            holder.img = (ImageView) fila.findViewById(R.id.imgItemMantenimiento);
            fila.setTag(holder);
        } else {
            holder = (HolderView) fila.getTag();
        }

        ItemMantenimiento item = data.get(position);
        holder.txtType.setText(item.getTitleType());
        holder.txtNumer.setText(item.getNumberProblem());
        holder.txtNeeded.setText(item.getNeeded());
        holder.txtDescr.setText(item.getDescr());

        if (item.getUrlImg() == null) {
            holder.img.setImageResource(item.getImg());
        } else {
            Picasso.with(ctx).load(item.getUrlImg()).into(holder.img);
        }

        return fila;
    }

}
