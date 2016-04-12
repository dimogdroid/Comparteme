package dimogdroid.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dimogdroid.comparteme.R;
import dimogdroid.service.model.RowListPrincipal;
import dimogdroid.service.model.RowListUser;

/**
 * Created by dgdavila on 11/06/2015.
 */
public class ListadoUsersAdapter extends ArrayAdapter<RowListUser> {

    Context context;
    int layoutResourceId;
    List<RowListUser> data = null;

    public List<RowListUser> getData() {
        return data;
    }

    public void setData(List<RowListUser> data) {
        this.data = data;
    }


    public ListadoUsersAdapter(Context context, int layoutResourceId, List<RowListUser> data) {
        super(context,layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
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


            holder.userBusqueda = (TextView) row.findViewById(R.id.txtUserBusqueda);
            holder.fraseBusqueda = (TextView) row.findViewById(R.id.txtfraseBusqueda);
            holder.deviceIdBusqueda =  (TextView) row.findViewById(R.id.txtiddevice);
            holder.btnanular = (Button) row.findViewById(R.id.btnanular);
            holder.btnunir = (Button) row.findViewById(R.id.btnunir);

            holder.rlyLayoutBusqueda = (RelativeLayout) row.findViewById(R.id.rltlayoutBusqueda);


            row.setTag(holder);
        } else {
            holder = (puntos) row.getTag();
        }

        RowListUser lstPuntuacion  = data.get(position);
        holder.userBusqueda.setText(lstPuntuacion.getNombreUser());
        holder.fraseBusqueda.setText(lstPuntuacion.getFraseUser());
        holder.deviceIdBusqueda.setText(lstPuntuacion.getDeviceId());

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
        Button btnanular;
        Button btnunir;
        TextView userBusqueda;
        TextView fraseBusqueda;
        TextView deviceIdBusqueda;
        RelativeLayout rlyLayoutBusqueda;

    }

}
