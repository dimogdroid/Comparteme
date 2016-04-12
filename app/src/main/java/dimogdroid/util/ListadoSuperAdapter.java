package dimogdroid.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import dimogdroid.comparteme.R;
import dimogdroid.service.model.RowListSuper;

/**
 * Created by dgdavila on 10/03/2015.
 */
public class ListadoSuperAdapter extends ArrayAdapter<RowListSuper> {

    Context context;
    int layoutResourceId;
    List<RowListSuper> data = null;

    AdapterInterface buttonListener;
    public interface AdapterInterface {
        public void buttonPressed(int i, View v);
    }

    public List<RowListSuper> getData() {
        return data;
    }

    public void setData(List<RowListSuper> data) {
        this.data = data;
    }

    public ListadoSuperAdapter(Context context, int layoutResourceId, List<RowListSuper> data,
                               AdapterInterface buttonListener) {
        super(context,layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.buttonListener = buttonListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        puntos holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new puntos();
            holder.txtidprod = (TextView) row.findViewById(R.id.txtidprod);
            holder.txtConcepto = (TextView) row.findViewById(R.id.txtConcepto);
            holder.txtObservaciones = (TextView) row.findViewById(R.id.txtObservaciones);
            holder.imgcheck = (ImageView) row.findViewById(R.id.imgdblcheck);
            holder.txtUser = (TextView) row.findViewById(R.id.txtUser);

            holder.imgedit = (ImageButton) row.findViewById(R.id.imgEditProducto);
            holder.imgedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonListener.buttonPressed(1,v);
                }
            });


            holder.imgdelete = (ImageButton) row.findViewById(R.id.imgDelProducto);
            holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonListener.buttonPressed(2,v);
                }
            });

            holder.lnyProductos = (LinearLayout) row.findViewById(R.id.lytProductos);

            holder.lnyProductos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonListener.buttonPressed(0, v);
                }
            });

            row.setTag(holder);
        } else {
            holder = (puntos) row.getTag();
        }

        RowListSuper lstPuntuacion  = data.get(position);
        holder.txtidprod.setText(String.valueOf(lstPuntuacion.getId()));
        holder.txtUser.setText(lstPuntuacion.getUsercomprado());

        if (lstPuntuacion.getComprado()>0) {
            holder.txtConcepto.setPaintFlags(holder.txtConcepto.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtConcepto.setTag(1);
            holder.imgcheck.setVisibility(View.VISIBLE);
            holder.txtUser.setVisibility(View.VISIBLE);

            holder.imgedit.setVisibility(View.INVISIBLE);
            holder.imgdelete.setVisibility(View.INVISIBLE);

        }else{
            holder.txtConcepto.setPaintFlags(holder.txtConcepto.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.txtConcepto.setTag(0);
            holder.imgcheck.setVisibility(View.INVISIBLE);
            holder.txtUser.setVisibility(View.INVISIBLE);

            holder.imgedit.setVisibility(View.VISIBLE);
            holder.imgdelete.setVisibility(View.VISIBLE);

        }
        holder.txtConcepto.setText(lstPuntuacion.getConcepto());
        if ((lstPuntuacion.getObservaciones()!=null) && (!lstPuntuacion.getObservaciones().equalsIgnoreCase(""))) {
            holder.txtObservaciones.setVisibility(View.VISIBLE);
            holder.txtObservaciones.setText(lstPuntuacion.getObservaciones());
        }else{
            holder.txtObservaciones.setVisibility(View.GONE);
        }







        return row;
    }



    @Override
    public int getCount() {
        if (data==null){
            return 0;
        }else{
            return data.size();
        }
    }

    static class puntos {
        TextView txtidprod;
        ImageView imgcheck;
        ImageButton imgedit;
        ImageButton imgdelete;
        TextView txtUser;
        TextView txtConcepto;
        TextView txtObservaciones;
        LinearLayout lnyProductos;
    }
}
