package dimogdroid.comparteme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.melnykov.fab.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import dimogdroid.service.RankingServiceFactory;
import dimogdroid.service.model.ListadoSuper;
import dimogdroid.service.model.RowListSuper;
import dimogdroid.util.ListadoSuperAdapter;
import dimogdroid.util.ObjectSerializer;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import retrofit.RetrofitError;

/**
 * Created by dgdavila on 01/04/2015.
 */
public class FragmentSecundario extends Fragment implements ListadoSuperAdapter.AdapterInterface {


    ArrayList<RowListSuper> lstInicial;
    RowListSuper[] listadoInicial;

    ListadoSuperAdapter adapter;

    ProgressBar myProgresG;
    int listado;
    String nombreUser;
    String deviceID;


    private static ListadoSuperTask task;
    private static NuevoProductoTask taskNuevoProd;
    private static ModificarProductoTask taskModProd;
    private static EliminarProductoTask taskEliminaProd;
    private static CompradoTask taskComprado;

    EditText edtProducto;

    String nuevoProducto = "";
    String nuevaObserv = "";


    int idProducto = 0;
    int comprado = 0;
    int primera=0;
    String lista;
    String nombreUsuario;

    ListView mainListView;

    private Activity mActivity;

    Handler mHandler;

    SharedPreferences prefe;


    public FragmentSecundario() {

    }

    @Override
    public void onStart() {
        super.onStart();

        // set the indicator for child fragments
        // N.B. call this method AFTER the init() to leave the time to instantiate the ActionBarDrawerToggle
        ((MaterialNavigationDrawer)this.getActivity()).setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }


    @Override
    public void buttonPressed(int i, View v) {
        if (i==0) {
            clickProd(v);
        }
        if (i==1) {
            editProd(v);
        }
        if (i==2) {
            deleteProd(v);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }


    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            // Toast.makeText(getActivity(),"in runnable",Toast.LENGTH_SHORT).show();

            refrescarLista();

            FragmentSecundario.this.mHandler.postDelayed(m_Runnable, 30000);
        }

    };

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList("listaInicial", lstInicial);

        SharedPreferences prefe=mActivity.getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefe.edit();
        try {
            editor.putString("listaInicial", ObjectSerializer.serialize(lstInicial));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();

        super.onSaveInstanceState(outState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.secundario,
                container, false);

        prefe=getActivity().getSharedPreferences("datos", Context.MODE_PRIVATE);

        String lista = prefe.getString("listado","");
        if ((lista!=null)  && (!lista.isEmpty())) {

            listado = Integer.parseInt(lista);
        }

        nombreUser = prefe.getString("nombreUser","");
        deviceID  = prefe.getString("deviceID","");

        lstInicial = new ArrayList<RowListSuper>();


        if( savedInstanceState != null ) {
            lstInicial =savedInstanceState.getParcelableArrayList("listaInicial");
            adapter = new ListadoSuperAdapter(getActivity(),
                    R.layout.secundariorow,lstInicial,this);

        }else{

            adapter = new ListadoSuperAdapter(getActivity(),
                    R.layout.secundariorow, null,this);


        }

        mainListView = (ListView) view.findViewById(R.id.listasec);

        if (adapter != null) {
            mainListView.setAdapter(adapter);
        }

        /*listadoInicial = new RowListSuper[lstInicial.size()];


        if (lstInicial != null) {

            for (RowListSuper ptn : lstInicial) {

                listadoInicial[ptn.getId() - 1] = ptn;

            }

        }*/

        myProgresG = (ProgressBar) view.findViewById(R.id.myProgressS);

        task = new ListadoSuperTask();

        task.execute();






        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabs);
        fab.attachToListView(mainListView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                primera = 0;
                crearNuevo();

            }
        });




        final PullRefreshLayout layout = (PullRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutProduct);
        // listen refresh event
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refrescarLista();

                // refresh complete
                layout.setRefreshing(false);
            }
        });

        this.mHandler = new Handler();

        this.mHandler.postDelayed(m_Runnable,5000);



        return view;
    }

    public void editProd(View view) {

        RelativeLayout ly =(RelativeLayout) view.getParent();

        final TextView txtIdProducto = (TextView) ly.findViewById(R.id.txtidprod);
        TextView txtProducto = (TextView) ly.findViewById(R.id.txtConcepto);
        TextView txtObservaciones = (TextView) ly.findViewById(R.id.txtObservaciones);

        AlertDialog dialogDetails = null;

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View dialogview = inflater.inflate(R.layout.productonew, null);

        final AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());
        dialogbuilder.setTitle(getString(R.string.editpro));
        dialogbuilder.setView(dialogview);

        EditText txtConcepto= (EditText) dialogview.findViewById(R.id.txt_producto);
        EditText txtObs= (EditText) dialogview.findViewById(R.id.txt_observaciones);

        txtConcepto.setText(txtProducto.getText());
        txtObs.setText(txtObservaciones.getText());


        dialogbuilder.setPositiveButton(getString(R.string.modificar),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText edtProducto = (EditText) dialogview.findViewById(R.id.txt_producto);
                        EditText edtObser = (EditText) dialogview.findViewById(R.id.txt_observaciones);

                        nuevoProducto = edtProducto.getText().toString().trim();
                        nuevaObserv =  edtObser.getText().toString().trim();
                        idProducto = Integer.parseInt(txtIdProducto.getText().toString().trim());

                        taskModProd = new ModificarProductoTask();

                        taskModProd.execute();


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

    public void clickProd(View view) {

        //tachar elegido


        TextView txtProducto = (TextView) view.findViewById(R.id.txtConcepto);
        TextView txtIdProducto = (TextView) view.findViewById(R.id.txtidprod);


        RelativeLayout rlyLayout = (RelativeLayout) view.getParent();

        ImageView imgCheck = (ImageView) rlyLayout.findViewById(R.id.imgdblcheck);
        ImageButton imgDel = (ImageButton) rlyLayout.findViewById(R.id.imgDelProducto);
        ImageButton imgEdit = (ImageButton) rlyLayout.findViewById(R.id.imgEditProducto);

       if (myProgresG == null) {
            RelativeLayout rlyLayoutPrin = (RelativeLayout) view.getParent().getParent().getParent().getParent();
            myProgresG = (ProgressBar) rlyLayoutPrin.findViewById(R.id.myProgressS);
        }

        idProducto = Integer.parseInt(txtIdProducto.getText().toString());

        if ( Integer.parseInt(txtProducto.getTag().toString())==0) {
            txtProducto.setPaintFlags(txtProducto.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtProducto.setTag(1);
            imgCheck.setVisibility(View.VISIBLE);

            imgDel.setVisibility(View.INVISIBLE);
            imgEdit.setVisibility(View.INVISIBLE);

            comprado = 1;


        }else{
            txtProducto.setPaintFlags(txtProducto.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            txtProducto.setTag(0);
            imgCheck.setVisibility(View.INVISIBLE);

            imgDel.setVisibility(View.VISIBLE);
            imgEdit.setVisibility(View.VISIBLE);

            comprado = 0;
        }

        prefe=mActivity.getSharedPreferences("datos", Context.MODE_PRIVATE);


       if (lstInicial==null) {
           lstInicial = new ArrayList<RowListSuper>();
           try {
               lstInicial = (ArrayList<RowListSuper>) ObjectSerializer.deserialize(prefe.getString("listaInicial", ObjectSerializer.serialize(new ArrayList<RowListSuper>())));
           } catch (IOException e) {
               e.printStackTrace();
           }
       }

        int i=0;
        for (RowListSuper temp: lstInicial){

            if (temp.getId()==idProducto){
                lstInicial.get(i).setComprado(comprado);
                adapter.setData(lstInicial);
                adapter.notifyDataSetChanged();
                break;
            }
            i++;
        }




        //poner a 1 el campo comprado en la BD
        taskComprado = new CompradoTask();

        taskComprado.execute();



    }




    public void deleteProd(View view) {

        RelativeLayout ly =(RelativeLayout) view.getParent();

        final TextView txtIdProducto = (TextView) ly.findViewById(R.id.txtidprod);
        final TextView txtProducto = (TextView) ly.findViewById(R.id.txtConcepto);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder
                .setMessage("¿ Desea Eliminar el producto " + txtProducto.getText().toString() + "?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Yes-code
                        //TODO Eliminar lista y Productos
                        nuevoProducto = txtProducto.getText().toString().trim();
                        idProducto = Integer.parseInt(txtIdProducto.getText().toString().trim());
                        if (lstInicial==null){
                            lstInicial = new ArrayList<RowListSuper>();
                        }
                        for(RowListSuper rw : lstInicial){
                            if (rw.getId()==idProducto){
                                lstInicial.remove(rw);
                                break;
                            }
                        }

                        adapter.setData(lstInicial);

                        adapter.notifyDataSetChanged();

                        taskEliminaProd = new EliminarProductoTask();

                        taskEliminaProd.execute();



                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();



    }


    class CompradoTask extends AsyncTask<Void, Boolean, ArrayList<RowListSuper>> {


        protected void onPreExecute(){



            myProgresG.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<RowListSuper> doInBackground(Void... voids) {

            ArrayList<RowListSuper> lstFinal = null;

            int contador;
            contador = 0;
            ListadoSuper result = null;
            while (contador <= 3) {

                try {


                    result = RankingServiceFactory.createRankingService()
                            .productocomprado(listado, idProducto, comprado, deviceID);

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

                lstFinal = (ArrayList<RowListSuper>) result.getLstListadoSuper();

            }




            return lstFinal;
        }



        //
        @Override
        protected void onPostExecute(ArrayList<RowListSuper> lstFinal) {

            myProgresG.setVisibility(View.INVISIBLE);
            if (lstInicial==null){
                lstInicial = new ArrayList<RowListSuper>();
            }

            lstInicial = lstFinal;

            adapter.setData(lstInicial);

            adapter.notifyDataSetChanged();

        }
    }



    private void refrescarLista() {

        task = new ListadoSuperTask();

        task.execute();
    }


    public void crearNuevo(){


        //TODO
        AlertDialog dialogDetails = null;

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View dialogview = inflater.inflate(R.layout.productonew, null);

        final AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());
        dialogbuilder.setTitle(getString(R.string.nuevopro));
        dialogbuilder.setView(dialogview);

        edtProducto = (EditText) dialogview.findViewById(R.id.txt_producto);
        final EditText edtObser = (EditText) dialogview.findViewById(R.id.txt_observaciones);


        dialogbuilder.setPositiveButton(getString(R.string.comenzarmas),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        nuevoProducto = edtProducto.getText().toString().trim();
                        nuevaObserv =  edtObser.getText().toString().trim();

                        //Lo damos de alta en la listaInicial
                        //listado, nuevoProducto, nuevaObserv, deviceID
                        RowListSuper newProducto = new RowListSuper();
                        newProducto.setConcepto(nuevoProducto);
                        newProducto.setObservaciones(nuevaObserv);
                        newProducto.setId_lista(listado);
                        newProducto.setComprado(0);
                        if (lstInicial==null){
                            lstInicial = new ArrayList<RowListSuper>();
                        }
                        lstInicial.add(newProducto);
                        adapter.setData(lstInicial);
                        adapter.notifyDataSetChanged();

                        taskNuevoProd = new NuevoProductoTask();

                        taskNuevoProd.execute();


                         edtObser.setText("");
                         edtProducto.setText("");
                         edtProducto.requestFocus();


                         primera ++;

                        saliryreiniciar();


                    }
                });
        dialogbuilder.setNeutralButton(getString(R.string.anular),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        dialog.cancel();
                    }
                });

        dialogbuilder.setNegativeButton(getString(R.string.comenzar),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        nuevoProducto = edtProducto.getText().toString().trim();
                        nuevaObserv = edtObser.getText().toString().trim();

                        //Lo damos de alta en la listaInicial
                        //listado, nuevoProducto, nuevaObserv, deviceID
                        RowListSuper newProducto = new RowListSuper();
                        newProducto.setConcepto(nuevoProducto);
                        newProducto.setObservaciones(nuevaObserv);
                        newProducto.setId_lista(listado);
                        newProducto.setComprado(0);
                        if (lstInicial==null){
                            lstInicial = new ArrayList<RowListSuper>();
                        }
                        lstInicial.add(newProducto);
                        adapter.setData(lstInicial);
                        adapter.notifyDataSetChanged();


                        taskNuevoProd = new NuevoProductoTask();

                        taskNuevoProd.execute();

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


                    }
                });

        dialogDetails = dialogbuilder.create();

        dialogDetails.show();


        edtProducto.requestFocus();
     //   getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        if (primera==0) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }



    }

    private void saliryreiniciar(){

        crearNuevo();
    }


    class NuevoProductoTask extends AsyncTask<Void, Boolean, ArrayList<RowListSuper>> {


        protected void onPreExecute(){

            myProgresG.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<RowListSuper> doInBackground(Void... voids) {

            ArrayList<RowListSuper> lstFinal = null;

            int contador;
            contador = 0;
            ListadoSuper result = null;
            while (contador <= 3) {

                try {


                    result = RankingServiceFactory.createRankingService()
                            .nuevoproducto(listado, nuevoProducto, nuevaObserv, deviceID);

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

            // VALIDO !!!!!
            if (result != null) {

                lstFinal = (ArrayList<RowListSuper>) result.getLstListadoSuper();

            }




            return lstFinal;
        }



        //
        @Override
        protected void onPostExecute(ArrayList<RowListSuper> lstFinal) {

            myProgresG.setVisibility(View.INVISIBLE);

            if (lstInicial==null){
                lstInicial = new ArrayList<RowListSuper>();
            }
            lstInicial = lstFinal;

            adapter.setData(lstInicial);

            adapter.notifyDataSetChanged();

        }
    }


    /**
     * Modificar
     */

    class ModificarProductoTask extends AsyncTask<Void, Boolean, ArrayList<RowListSuper>> {


        protected void onPreExecute(){

            myProgresG.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<RowListSuper> doInBackground(Void... voids) {

            ArrayList<RowListSuper> lstFinal = null;

            int contador;
            contador = 0;
            ListadoSuper result = null;
            while (contador <= 3) {

                try {


                    result = RankingServiceFactory.createRankingService()
                            .modificaproducto(listado,idProducto, nuevoProducto, nuevaObserv);

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

            // VALIDO !!!!!
            if (result != null) {

                lstFinal = (ArrayList<RowListSuper>) result.getLstListadoSuper();

            }




            return lstFinal;
        }



        //
        @Override
        protected void onPostExecute(ArrayList<RowListSuper> lstFinal) {

            myProgresG.setVisibility(View.INVISIBLE);

            if (lstInicial==null){
                lstInicial = new ArrayList<RowListSuper>();
            }
            lstInicial = lstFinal;

            adapter.setData(lstInicial);

            adapter.notifyDataSetChanged();

        }
    }


    /**********************************/


    /**
     * Eliminar Prod
     */

    class EliminarProductoTask extends AsyncTask<Void, Boolean, ArrayList<RowListSuper>> {


        protected void onPreExecute(){

            myProgresG.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<RowListSuper> doInBackground(Void... voids) {

            ArrayList<RowListSuper> lstFinal = null;

            int contador;
            contador = 0;
            ListadoSuper result = null;
            while (contador <= 3) {

                try {


                    result = RankingServiceFactory.createRankingService()
                            .eliminaproducto(listado,idProducto, nuevoProducto);

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

            // VALIDO !!!!!
            if (result != null) {

                lstFinal = (ArrayList<RowListSuper>) result.getLstListadoSuper();

            }




            return lstFinal;
        }



        //
        @Override
        protected void onPostExecute(ArrayList<RowListSuper> lstFinal) {

            myProgresG.setVisibility(View.INVISIBLE);

            if (lstInicial==null){
                lstInicial = new ArrayList<RowListSuper>();
            }

            lstInicial = lstFinal;

          //  adapter.setData(lstInicial);

          //  adapter.notifyDataSetChanged();

        }
    }


    /**********************************/
















    class ListadoSuperTask extends AsyncTask<Void, Boolean, ArrayList<RowListSuper>> {


        protected void onPreExecute(){

            myProgresG.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<RowListSuper> doInBackground(Void... voids) {

            ArrayList<RowListSuper> lstFinal = null;

            int contador;
            contador = 0;
            ListadoSuper result = null;
            while (contador <= 3) {

                try {


                    result = RankingServiceFactory.createRankingService().listasuper(listado);

                } catch (RetrofitError e) {
                    //  System.out.print(e.getResponse().toString());
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

                lstFinal = (ArrayList<RowListSuper>) result.getLstListadoSuper();

            }


            return lstFinal;
        }

        //
        @Override
        protected void onPostExecute(ArrayList<RowListSuper> lstFinal) {

            myProgresG.setVisibility(View.INVISIBLE);

            if (lstInicial==null){
                lstInicial = new ArrayList<RowListSuper>();
            }

            lstInicial = lstFinal;

            adapter.setData(lstInicial);

            adapter.notifyDataSetChanged();
        }
    }




}
