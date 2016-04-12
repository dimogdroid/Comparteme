package dimogdroid.comparteme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dimogdroid.service.RankingServiceFactory;
import dimogdroid.service.model.ListadoSuper;
import dimogdroid.service.model.ListadoUser;
import dimogdroid.service.model.RowListSuper;
import dimogdroid.service.model.RowListUser;
import dimogdroid.util.ListadoInicialAdapter;
import dimogdroid.util.ListadoUsersAdapter;
import retrofit.RetrofitError;

/**
 * Created by dgdavila on 11/06/2015.
 */
public class FragmentBusqueda extends Fragment {

    List<RowListUser> lstInicial = new ArrayList<>();
    ProgressBar  myProgresB;
    SharedPreferences settings;
    private static BuscarTask taskBuscar;
    private static UnirTask taskUnir;
    private static AnularTask taskAnular;

    ListadoUsersAdapter adapter;
    ListView mainListView;

    String nombreUser="";
    String frase="";
    String deviceId;
    String deviceIdDos;

    String patronBusqueda;
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buscar,
                container, false);

        myProgresB = (ProgressBar) view.findViewById(R.id.myProgressB);

        settings = getActivity().getSharedPreferences("ajustes",
                Context.MODE_MULTI_PROCESS);

        nombreUser = settings.getString("nombreUser", "");
        frase = settings.getString("frase", "");
        deviceId = settings.getString("deviceID", "");

        adapter = new ListadoUsersAdapter(getActivity(),
                R.layout.buscarrow, null);

        mainListView = (ListView) view.findViewById(R.id.listBusqueda);

        if (adapter != null) {
            mainListView.setAdapter(adapter);
        }


        return view;
    }


    public void buscarUsers(View view) {

        RelativeLayout ly =(RelativeLayout) view.getParent();

        TextView txtBusqueda = (TextView) ly.findViewById(R.id.editTextBusqueda);

        patronBusqueda=txtBusqueda.getText().toString();

        //poner a 1 el campo comprado en la BD
        taskBuscar = new BuscarTask();

        taskBuscar.execute();


    }

    public void unir(View view) {

        RelativeLayout ly =(RelativeLayout) view.getParent();

        TextView txtDevice = (TextView) ly.findViewById(R.id.txtiddevice);
        TextView txtuserBusqueda = (TextView) ly.findViewById(R.id.txtUserBusqueda);
        TextView txtfraseBusqueda = (TextView) ly.findViewById(R.id.txtfraseBusqueda);


        deviceIdDos = txtDevice.getText().toString();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setMessage("¿ Desea Unirse al Usuario " + txtuserBusqueda.getText().toString() + " con frase '" +
                        txtfraseBusqueda.getText().toString() + "'?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Yes-code
                        taskUnir = new UnirTask();

                        taskUnir.execute();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();




    }


    class UnirTask extends AsyncTask<Void, Boolean, List<RowListUser>> {


        protected void onPreExecute(){



            myProgresB.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<RowListUser> doInBackground(Void... voids) {

            List<RowListUser> lstFinal = null;

            int contador;
            contador = 0;
            ListadoUser result = null;
            while (contador <= 3) {

                try {


                    result = RankingServiceFactory.createRankingService()
                            .uniruser(deviceId, deviceIdDos, patronBusqueda);

                } catch (RetrofitError e) {
                    //   System.out.print(e.getResponse().toString());
                }
                if (result == null) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception ex) {

                    }
                    contador++;
                } else {
                    contador = 4;
                }
            }

            // VALIDO !!!!!
            if (result != null) {

                lstFinal = result.getLstListadoUser();

            }




            return lstFinal;
        }



        //
        @Override
        protected void onPostExecute(List<RowListUser> lstFinal) {

            myProgresB.setVisibility(View.INVISIBLE);

            adapter.setData(lstFinal);

            adapter.notifyDataSetChanged();

            Toast toast1 =
                    Toast.makeText(getActivity(),
                            getString(R.string.realizado), Toast.LENGTH_LONG);

            toast1.show();

        }
    }


    public void anular(View view) {

        RelativeLayout ly =(RelativeLayout) view.getParent();

        TextView txtDevice = (TextView) ly.findViewById(R.id.txtiddevice);
        TextView txtuserBusqueda = (TextView) ly.findViewById(R.id.txtUserBusqueda);
        TextView txtfraseBusqueda = (TextView) ly.findViewById(R.id.txtfraseBusqueda);


        deviceIdDos = txtDevice.getText().toString();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setMessage("¿ Desea Anular al Usuario " + txtuserBusqueda.getText().toString() + " con frase '" +
                        txtfraseBusqueda.getText().toString() + "'?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Yes-code
                        taskAnular = new AnularTask();

                        taskAnular.execute();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();




    }


    class AnularTask extends AsyncTask<Void, Boolean, List<RowListUser>> {


        protected void onPreExecute(){



            myProgresB.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<RowListUser> doInBackground(Void... voids) {

            List<RowListUser> lstFinal = null;

            int contador;
            contador = 0;
            ListadoUser result = null;
            while (contador <= 3) {

                try {


                    result = RankingServiceFactory.createRankingService()
                            .anularuser(deviceId, deviceIdDos, patronBusqueda);

                } catch (RetrofitError e) {
                    //   System.out.print(e.getResponse().toString());
                }
                if (result == null) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception ex) {

                    }
                    contador++;
                } else {
                    contador = 4;
                }
            }

            // VALIDO !!!!!
            if (result != null) {

                lstFinal = result.getLstListadoUser();

            }




            return lstFinal;
        }



        //
        @Override
        protected void onPostExecute(List<RowListUser> lstFinal) {

            myProgresB.setVisibility(View.INVISIBLE);

            adapter.setData(lstFinal);

            adapter.notifyDataSetChanged();

            Toast toast1 =
                    Toast.makeText(getActivity(),
                            getString(R.string.realizado), Toast.LENGTH_LONG);

            toast1.show();

        }
    }


    class BuscarTask extends AsyncTask<Void, Boolean, List<RowListUser>> {


        protected void onPreExecute(){



            myProgresB.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<RowListUser> doInBackground(Void... voids) {

            List<RowListUser> lstFinal = null;

            int contador;
            contador = 0;
            ListadoUser result = null;
            while (contador <= 3) {

                try {


                    result = RankingServiceFactory.createRankingService()
                            .busquedauser(patronBusqueda);

                } catch (RetrofitError e) {
                    //   System.out.print(e.getResponse().toString());
                }
                if (result == null) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception ex) {

                    }
                    contador++;
                } else {
                    contador = 4;
                }
            }

            // VALIDO !!!!!
            if (result != null) {

                lstFinal = result.getLstListadoUser();

            }




            return lstFinal;
        }



        //
        @Override
        protected void onPostExecute(List<RowListUser> lstFinal) {

            myProgresB.setVisibility(View.INVISIBLE);

            adapter.setData(lstFinal);

            adapter.notifyDataSetChanged();

        }
    }






}
