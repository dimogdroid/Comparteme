package dimogdroid.comparteme;

import android.app.AlertDialog;


import android.os.Handler;
import android.support.v4.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dimogdroid.service.RankingServiceFactory;
import dimogdroid.service.model.ListadoPrincipal;
import dimogdroid.service.model.RowListPrincipal;
import dimogdroid.service.utils.DeviceID;
import dimogdroid.util.ListadoInicialAdapter;
import dimogdroid.util.SocialNetwork;
import dimogdroid.util.SocialNetworkSpinnerAdapter;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import retrofit.RetrofitError;

public class FragmentPrincipal extends Fragment implements ListadoInicialAdapter.AdapterInterface {

    final private static int DIALOG_LOGIN = 1;

    private ListView mainListView;

    List<RowListPrincipal> lstInicial;
    RowListPrincipal[] listadoInicial;
    ListadoInicialAdapter adapter;

    ProgressBar myProgresG;

    ImageButton imgDeleteList;
    RelativeLayout rltlayoutPush;


    private static ListadoInicialTask task;
    private static NuevaListaTask taskNueva;
    private static EliminarListaTask taskElimina;
    private static NuevoUserTask taskNuevoUser;

    public static FragmentSecundario fragmentSecundario;

    String nuevalista;
    String nuevoSeleccionado;
    String icono;
    SharedPreferences settings;

    String nombreUser="";
    String frase="";

    int idListaEliminar=0;

    String deviceID;

    private Spinner spinner;

    boolean primeravez =false;

    ViewGroup containerPrincipal;

    Handler mHandler;

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            // Toast.makeText(getActivity(),"in runnable",Toast.LENGTH_SHORT).show();

            refrescarLista();

            FragmentPrincipal.this.mHandler.postDelayed(m_Runnable, 20000);
        }

    };

    @Override
    public void buttonPressed(View v) {
        mostrarLista(v);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inicial,
                container, false);

        containerPrincipal = container;

        lstInicial = new ArrayList<>();

        deviceID = DeviceID.getID(getActivity());

        settings = getActivity().getSharedPreferences("ajustes",
                Context.MODE_MULTI_PROCESS);

        myProgresG = (ProgressBar) view.findViewById(R.id.myProgressG);


        nombreUser = settings.getString("nombreUser", "");
        frase = settings.getString("frase", "");

        if (nombreUser.equalsIgnoreCase("")){
            pedirNombreInicial();

            //Guardamos el ID
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("deviceID", deviceID);
            editor.commit();

            primeravez=true;
        }

      //  LinearLayout lyNombre = (LinearLayout)view.findViewById(R.id.lytNombreyFecha);
      //  lyNombre.setOnClickListener(this);


        adapter = new ListadoInicialAdapter(getActivity(),
                R.layout.inicialrow, null,this);

        ListView mainListView = (ListView) view.findViewById(android.R.id.list);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToListView(mainListView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NuevaLista();

            }
        });

        if (!primeravez) refrescarLista();

        if (adapter != null) {
            mainListView.setAdapter(adapter);
        }


        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarLista(view);
            }
        });



        final PullRefreshLayout layout = (PullRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        // listen refresh event
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refrescarLista();

                // refresh complete
                layout.setRefreshing(false);
            }
        });

        fragmentSecundario = new FragmentSecundario();



        this.mHandler = new Handler();

        this.mHandler.postDelayed(m_Runnable,5000);


        return view;
    }


    public void setText(String item) {
//        TextView view = (TextView) getView().findViewById(R.id.detailsText);
//        view.setText(item);
    }


    private void pedirNombreInicial() {


        AlertDialog dialogDetails = null;

        final SharedPreferences.Editor editor = settings.edit();
        editor.putString("deviceID", deviceID);


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View dialogview = inflater.inflate(R.layout.nombre_dialog, null);

        final AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());
        dialogbuilder.setTitle(getString(R.string.nombreuser));
        dialogbuilder.setView(dialogview);

        dialogbuilder.setPositiveButton(getString(R.string.guardar),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText edtUsuario = (EditText) dialogview.findViewById(R.id.txt_name);
                        nombreUser = edtUsuario.getText().toString();
                        editor.putString("nombreUser", nombreUser);
                        EditText edtFrase = (EditText) dialogview.findViewById(R.id.txt_frase);
                        frase = edtFrase.getText().toString();
                        editor.putString("frase", frase);

                        editor.commit();

                        taskNuevoUser = new NuevoUserTask();

                        taskNuevoUser.execute();


                    }
                });

        dialogbuilder.setNegativeButton(getString(R.string.cancelar),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });



        dialogDetails = dialogbuilder.create();

        dialogDetails.show();

    }

    private void refrescarLista() {

        task = new ListadoInicialTask();

        task.execute();
    }

    public void deleteList(View view){

        RelativeLayout vwParentRow = (RelativeLayout)view.getParent().getParent();
        TextView txtId = (TextView) vwParentRow.findViewById(R.id.txtidlist);
        TextView txtNombreLista = (TextView) vwParentRow.findViewById(R.id.txtNombre);
        String nombreLista = txtNombreLista.getText().toString();
        idListaEliminar = Integer.parseInt(txtId.getText().toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setMessage("¿ Desea Eliminar la lista " + nombreLista)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Yes-code
                        //TODO Eliminar lista y Productos

                        taskElimina = new EliminarListaTask();

                        taskElimina.execute();



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



    public void mostrarLista(View view){



        RelativeLayout vwParentRow = (RelativeLayout)view.getParent().getParent();
        TextView txtId = (TextView) vwParentRow.findViewById(R.id.txtidlist);
        TextView txtNombreLista =(TextView) vwParentRow.findViewById(R.id.txtNombre);

        SharedPreferences preferencias=getActivity().getSharedPreferences("datos",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("listado", txtId.getText().toString());
        editor.putString("nombreUser", nombreUser);
        editor.putString("deviceID", deviceID);

        editor.commit();

        ((MaterialNavigationDrawer)this.getActivity()).setFragmentChild
                (fragmentSecundario,txtNombreLista.getText().toString().toUpperCase());

    }
//

    private void NuevaLista() {

        AlertDialog dialogDetails = null;

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View dialogview = inflater.inflate(R.layout.nuevoprod_dialog, null);

        final AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());
        dialogbuilder.setTitle(getString(R.string.nombre));
        dialogbuilder.setView(dialogview);

        //datos a mostrar
        List<SocialNetwork> items = new ArrayList<SocialNetwork>(15);
        items.add(new SocialNetwork(getString(R.string.eligeuno), R.drawable.sinicono));
        items.add(new SocialNetwork(getString(R.string.lista), R.drawable.lista));
        items.add(new SocialNetwork(getString(R.string.fruta), R.drawable.fruta));
        items.add(new SocialNetwork(getString(R.string.verdura), R.drawable.verdura));
        items.add(new SocialNetwork(getString(R.string.carne), R.drawable.carne));
        items.add(new SocialNetwork(getString(R.string.pescado), R.drawable.pescado));
        items.add(new SocialNetwork(getString(R.string.farmacia), R.drawable.farmacia));
        items.add(new SocialNetwork(getString(R.string.drogueria), R.drawable.drogueria));

        spinner = (Spinner) dialogview.findViewById(R.id.spinner);
        spinner.setAdapter(new SocialNetworkSpinnerAdapter(getActivity(),items));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                // Toast.makeText(adapterView.getContext(), ((SocialNetwork) adapterView.getItemAtPosition(position)).getNombre(), Toast.LENGTH_SHORT).show();
                nuevoSeleccionado = ((SocialNetwork) adapterView.getItemAtPosition(position)).getNombre();
                icono = ((SocialNetwork) adapterView.getItemAtPosition(position)).getNombre();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                nuevoSeleccionado = "";
            }
        });



        dialogbuilder.setPositiveButton(getString(R.string.comenzar),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO Actualizar Lista

                        EditText edt = (EditText) dialogview.findViewById(R.id.txt_name);

                        nuevalista = edt.getText().toString();

                        if (nuevalista.equalsIgnoreCase("") || (nuevalista==null) ||nuevalista.isEmpty() ){
                            nuevalista = nuevoSeleccionado;
                        }

                        taskNueva = new NuevaListaTask();

                        taskNueva.execute();


                    }
                });

        dialogbuilder.setNegativeButton(getString(R.string.cancelar),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        dialogDetails = dialogbuilder.create();

        dialogDetails.show();





    }




    class EliminarListaTask extends AsyncTask<Void, Boolean, List<RowListPrincipal>> {


        protected void onPreExecute(){

            myProgresG.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<RowListPrincipal> doInBackground(Void... voids) {

            List<RowListPrincipal> lstFinal = null;

            int contador;
            contador = 0;
            ListadoPrincipal result = null;
            while (contador <= 3) {

                try {


                    result = RankingServiceFactory.createRankingService()
                            .eliminalista(idListaEliminar,nombreUser,deviceID);


                } catch (RetrofitError e) {
                    //
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

                lstFinal = result.getLstListadoPrincipal();

            }


            return lstFinal;
        }



        //
        @Override
        protected void onPostExecute(List<RowListPrincipal> lstFinal) {

            myProgresG.setVisibility(View.INVISIBLE);

            adapter.setData(lstFinal);

            adapter.notifyDataSetChanged();

        }
    }





    class NuevoUserTask extends AsyncTask<Void, Boolean, String> {


        protected void onPreExecute(){

            myProgresG.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... voids) {

            int contador;
            contador = 0;
            String result = null;
            while (contador <= 3) {

                try {

                    result = RankingServiceFactory.createRankingService()
                            .nuevouser(deviceID, nombreUser, frase);

                } catch (RetrofitError e) {
                    //    System.out.print(e.getResponse().toString());
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

            return result;
        }



        //
        @Override
        protected void onPostExecute(String result) {

            myProgresG.setVisibility(View.INVISIBLE);


        }
    }





    class NuevaListaTask extends AsyncTask<Void, Boolean, List<RowListPrincipal>> {


        protected void onPreExecute(){

            myProgresG.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<RowListPrincipal> doInBackground(Void... voids) {

            // List<RowListPrincipal> lstFinal = null;

            int contador;
            contador = 0;
            ListadoPrincipal result = null;
            while (contador <= 3) {

                try {

                    Date date = new Date();
                    long tiempoCreada = date.getTime();
                    result = RankingServiceFactory.createRankingService()
                            .nuevalista(deviceID, nombreUser,nuevalista, tiempoCreada, icono);

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

                lstInicial = result.getLstListadoPrincipal();

            }


            return lstInicial;
        }



        //
        @Override
        protected void onPostExecute(List<RowListPrincipal> lstFinal) {

            myProgresG.setVisibility(View.INVISIBLE);

            adapter.setData(lstInicial);

            adapter.notifyDataSetChanged();

        }
    }













    class ListadoInicialTask extends AsyncTask<Void, Boolean, List<RowListPrincipal>> {

        @Override
        protected void onPreExecute() {
            myProgresG.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<RowListPrincipal> doInBackground(Void... voids) {

            // List<RowListPrincipal> lstFinal = null;

            int contador;
            contador = 0;
            ListadoPrincipal result = null;
            while (contador <= 3) {

                try {


                    result = RankingServiceFactory.createRankingService().listaini(nombreUser, deviceID);
                    // result = RankingServiceFactory.createRankingService().listainipru("Diego");

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

                lstInicial = result.getLstListadoPrincipal();

            }


            return lstInicial;
        }

        //
        @Override
        protected void onPostExecute(List<RowListPrincipal> lstFinal) {

            myProgresG.setVisibility(View.INVISIBLE);

            adapter.setData(lstInicial);

            adapter.notifyDataSetChanged();

        }
    }

}
