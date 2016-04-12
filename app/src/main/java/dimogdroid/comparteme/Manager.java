package dimogdroid.comparteme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import junit.framework.Assert;

import dimogdroid.service.RankingServiceFactory;
import dimogdroid.service.utils.DeviceID;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit.RetrofitError;

/**
 * Created by dgdavila on 30/03/2015.
 */



public class Manager extends MaterialNavigationDrawer {

    MaterialDialog mMaterialDialog;
    int idListaEliminar=0;

    FragmentPrincipal fragmentPrincipal;
    FragmentBusqueda fragmentBusqueda;
    private static GuardarUserTask taskGuardarUser;

    String nombreUser="";
    String nuevafrase="";

    String deviceID;

    SharedPreferences settings;

    @Override
    public void init(Bundle savedInstanceState) {


        //Añadimos la 'cuenta'
        MaterialAccount account = new MaterialAccount(this.getResources(),getString(R.string.app_name),
                getString(R.string.app_subtitle),null, R.drawable.pescado);
        this.addAccount(account);

//        //Añadimos las secciones
//        FragmentPrincipal fragmentWallet = new FragmentPrincipal();
////        MaterialSection sectionWallet = newSection("USUARIO",
////               getDrawable(this,"ic_action_editor_insert_emoticon"), fragmentWallet);
//        MaterialSection sectionWallet = newSection("USUARIO",
//                R.drawable.ic_action_editor_insert_emoticon, fragmentWallet);
//        sectionWallet.setSectionColor(Color.parseColor("#33B5E5"));
//        this.addSection(sectionWallet);

        // añade los créditos al final
        this.addBottomSection(newSection("Usuario",R.drawable.ic_action_editor_insert_emoticon, new MaterialSectionListener(){
                    public void onClick(MaterialSection materialSection){
                      /*  View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.nombre_dialog, null);

                        mMaterialDialog = new MaterialDialog(Manager.this)

                                .setContentView(view)
                                .setTitle("Nombre del Usuario")
                                .setNegativeButton(getString(R.string.cancelar), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mMaterialDialog.dismiss();
                                    }
                                })
                                .setPositiveButton(getString(R.string.guardar), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //TODO Guardar usuario


                                        mMaterialDialog.dismiss();
                                    }
                                });

                        mMaterialDialog.show();*/

                        mostrarUserFrase();
                    }
                }
        ));

        //Añadimos las secciones
        fragmentPrincipal = new FragmentPrincipal();
        MaterialSection sectionCompras = newSection("LISTAS", R.drawable.ic_action_action_shopping_cart, fragmentPrincipal);
        sectionCompras.setSectionColor(Color.parseColor("#33B5E5"));
        this.addSection(sectionCompras);

        //Añadimos las secciones
        fragmentBusqueda = new FragmentBusqueda();
        MaterialSection sectionBuscar = newSection("Buscar Usuarios", R.drawable.ic_action_maps_local_atm, fragmentBusqueda);
        sectionBuscar.setSectionColor(Color.parseColor("#33B5E5"));
        this.addSection(sectionBuscar);

        //TODO Añadir más secciones. Crear tabla con Usuario e iconos + nombre apartado.

        //Añadimos las secciones
//        FragmentPrincipal fragmentOtros = new FragmentPrincipal();
//        MaterialSection sectionOtros = newSection("OTROS", R.drawable.ic_action_maps_local_atm, fragmentOtros);
//        sectionOtros.setSectionColor(Color.parseColor("#33B5E5"));
//        this.addSection(sectionOtros);


        // añade los créditos al final
        this.addBottomSection(newSection("Acerca de",R.drawable.abc_btn_check_to_on_mtrl_000, new MaterialSectionListener(){
                    public void onClick(MaterialSection materialSection){

                        //TODO
//                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.nombre_dialog, null);
//
//                        mMaterialDialog = new MaterialDialog(Manager.this)
//                                .setContentView(view)
//                                .setTitle("Titulo")
//                                .setMessage("Mensajes")
//                                .setPositiveButton("OK", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        mMaterialDialog.dismiss();
//                                    }
//                                });
//                        mMaterialDialog.show();
                    }
                }
        ));



        deviceID = DeviceID.getID(this);

        settings = getSharedPreferences("ajustes",
                Context.MODE_MULTI_PROCESS);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("deviceID", deviceID);
        nombreUser = settings.getString("nombreUser", "");
        nuevafrase = settings.getString("frase", "");
        editor.commit();

    }

    private void mostrarUserFrase() {

        final SharedPreferences.Editor editor = settings.edit();
        AlertDialog dialogDetails = null;

        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogview = inflater.inflate(R.layout.nombre_dialog, null);
        final EditText edtUsuario = (EditText) dialogview.findViewById(R.id.txt_name);
        final EditText edtFrase = (EditText) dialogview.findViewById(R.id.txt_frase);

        final AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        dialogbuilder.setTitle(getString(R.string.nombreuser));
        dialogbuilder.setView(dialogview);



        edtUsuario.setText(nombreUser);
        edtFrase.setText(nuevafrase);

        dialogbuilder.setPositiveButton(getString(R.string.guardar),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {




                        nombreUser = edtUsuario.getText().toString();

                        editor.putString("nombreUser", nombreUser);
                        editor.commit();


                        nuevafrase = edtFrase.getText().toString();
                        editor.putString("frase", nuevafrase);

                        taskGuardarUser = new GuardarUserTask();

                        taskGuardarUser.execute();


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

    public void deleteList(View view){

       fragmentPrincipal.deleteList(view);

    }
/*
    public void mostrarLista(View view){
        fragmentPrincipal.mostrarLista(view);
    }

    public void editProd(View view){
        fragmentSecundario.editProd(view);
    }

    public void clickProd(View view){



//        SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
//
//        String lista= prefe.getString("listado","");
//        fragmentSecundario.nombreUser = prefe.getString("nombreUser","");
//        fragmentSecundario.deviceID  = prefe.getString("deviceID","");
//
//        if ((lista!=null)  && (!lista.isEmpty())) {
//
//            fragmentSecundario.listado = Integer.parseInt(lista);
//        }

        fragmentSecundario.clickProd(view);
    }

    public void deleteProd(View view){
        fragmentSecundario.deleteProd(view);
    }

*/
    public void buscarUsers(View view){
        fragmentBusqueda.buscarUsers(view);
    }

    public void unir(View view) {
        fragmentBusqueda.unir(view);
    }

    public void anular(View view) {
        fragmentBusqueda.anular(view);
    }

    public static int getDrawable(Context context, String name)
    {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);

        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }


    class GuardarUserTask extends AsyncTask<Void, Boolean, String> {


        protected void onPreExecute(){

          //  myProgresG.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(Void... voids) {

            int contador;
            contador = 0;
            String result = null;
            while (contador <= 3) {

                try {

                    result = RankingServiceFactory.createRankingService()
                            .nuevouser(deviceID, nombreUser, nuevafrase);

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

        //    myProgresG.setVisibility(View.INVISIBLE);


        }
    }

}