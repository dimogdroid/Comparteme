package dimogdroid.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dimogdroid.comparteme.FragmentPrincipal;
import dimogdroid.comparteme.R;
import dimogdroid.service.model.RowListPrincipal;

/**
 * Created by dgdavila on 10/03/2015.
 */
public class ListadoInicialAdapter extends ArrayAdapter<RowListPrincipal> {

    AdapterInterface buttonListener;

    Context context;
    int layoutResourceId;
    List<RowListPrincipal> data = null;


    public interface AdapterInterface {
        public void buttonPressed(View v);
    }


    public List<RowListPrincipal> getData() {
        return data;
    }

    public void setData(List<RowListPrincipal> data) {
        this.data = data;
    }

    public ListadoInicialAdapter(Context context, int layoutResourceId, List<RowListPrincipal> data,
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
        boolean bandera =false;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new puntos();
            holder.txtidlist = (TextView) row.findViewById(R.id.txtidlist);
            holder.imgUser = (ImageView) row.findViewById(R.id.imgIconG);
            holder.txtuser = (TextView) row.findViewById(R.id.txtuser);
            holder.txtNombre = (TextView) row.findViewById(R.id.txtNombre);
            holder.txtFechaCreacion = (TextView) row.findViewById(R.id.txtFechaCreacion);
            holder.txtFechaMod = (TextView) row.findViewById(R.id.txtFechaMod);

            holder.lnlNombre = (LinearLayout) row.findViewById(R.id.lytNombreyFecha);
            holder.lnlNombre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonListener.buttonPressed(v);
                }
            });
            row.setTag(holder);
        } else {
            holder = (puntos) row.getTag();
        }

        RowListPrincipal lstPuntuacion  = data.get(position);
        holder.txtidlist.setText(String.valueOf(lstPuntuacion.getId()));
        holder.txtuser.setText(lstPuntuacion.getUser());
        holder.txtNombre.setText(lstPuntuacion.getNombre());

        if (lstPuntuacion.getIcono().equalsIgnoreCase("Lista")) {
            holder.imgUser.setImageResource(R.drawable.lista);
            bandera=true;
        }
        if (lstPuntuacion.getIcono().equalsIgnoreCase("Fruta")) {
            holder.imgUser.setImageResource(R.drawable.fruta);
            bandera=true;
        }
        if (lstPuntuacion.getIcono().equalsIgnoreCase("Verdura")) {
            holder.imgUser.setImageResource(R.drawable.verdura);
            bandera=true;
        }
        if (lstPuntuacion.getIcono().equalsIgnoreCase("Carne")) {
            holder.imgUser.setImageResource(R.drawable.carne);
            bandera=true;
        }
        if (lstPuntuacion.getIcono().equalsIgnoreCase("Pescado")) {
            holder.imgUser.setImageResource(R.drawable.pescado);
            bandera=true;
        }
        if (lstPuntuacion.getIcono().equalsIgnoreCase("Farmacia")) {
            holder.imgUser.setImageResource(R.drawable.farmacia);
            bandera=true;
        }
        if (lstPuntuacion.getIcono().equalsIgnoreCase("Drogueria")) {
            holder.imgUser.setImageResource(R.drawable.drogueria);
            bandera=true;
        }


        if (!bandera){
            holder.imgUser.setImageResource(R.drawable.sinicono);
        }

        Date dateFecha = new Date();
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ");

        dateFecha.setTime(Long.parseLong(lstPuntuacion.getFechacreacion().toString()));
        holder.txtFechaCreacion.setText("Creada: " + format.format(dateFecha));

        if (Long.parseLong(lstPuntuacion.getFechamod().toString())>0) {
            dateFecha.setTime(Long.parseLong(lstPuntuacion.getFechamod().toString()));
            holder.txtFechaMod.setText("Modificada: " + format.format(dateFecha));
        }

     /*   holder.lnlNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


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
        TextView txtidlist;
        ImageView imgUser;
        TextView txtuser;
        TextView txtNombre;
        TextView txtFechaCreacion;
        TextView txtFechaMod;
        LinearLayout lnlNombre;

    }
}
