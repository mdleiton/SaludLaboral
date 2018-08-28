package ec.edu.espol.mdleiton.saludlaboral;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<Aviso> {

    Context context;
    public CustomListViewAdapter(Context context, int resourceId,List<Aviso> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
        TextView fecha;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Aviso aviso = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.fecha = (TextView) convertView.findViewById(R.id.fecha);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtDesc.setText(aviso.getSugerencias());
        holder.txtTitle.setText(aviso.getCategoria());
        holder.imageView.setImageResource(aviso.getImageId());
        holder.fecha.setText(aviso.getFecha());

        return convertView;
    }
}