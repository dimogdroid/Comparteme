/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dimogdroid.service;


import dimogdroid.service.model.ListadoPrincipal;
import dimogdroid.service.model.ListadoSuper;
import dimogdroid.service.model.ListadoUser;
import dimogdroid.service.model.RowListPrincipal;
import dimogdroid.service.model.User;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;




/**
 *
 * @author ffernandez
 */
public interface RankingService {
    
//    @FormUrlEncoded
//    @POST("/ranking")
//    Response listRanking(
//            @Field("level") int level, 
//            @Field("deviceId") String deviceId);
	
	
	@FormUrlEncoded
    @POST("/listaini")
    ListadoPrincipal listaini(
            @Field("user") String usuario,
            @Field("deviceid") String deviceid);


    @FormUrlEncoded
    @POST("/listasuper")
    ListadoSuper listasuper(
            @Field("idlista") int idlista);

    @FormUrlEncoded
    @POST("/nuevalista")
    ListadoPrincipal nuevalista(
            @Field("deviceid") String deviceid,
            @Field("user") String usuario,
            @Field("nombrelista") String nombrelista,
            @Field("fechac") Long fechac,
            @Field("icono") String icono
    );

    @FormUrlEncoded
    @POST("/nuevouser")
    String nuevouser(
            @Field("deviceId") String deviceId,
            @Field("user") String usuario,
            @Field("frase") String frase);



    @FormUrlEncoded
    @POST("/listainipru")
    RowListPrincipal listainipru(
            @Field("user") String usuario);


    @FormUrlEncoded
    @POST("/eliminalista")
    ListadoPrincipal eliminalista(
            @Field("idlista") int idlista,
            @Field("user") String usuario,
            @Field("deviceId") String deviceId);

    @FormUrlEncoded
    @POST("/nuevoproducto")
    ListadoSuper nuevoproducto(
            @Field("idlista") int idlista,
            @Field("nuevoProducto") String nuevoProducto,
            @Field("nuevaObserv") String nuevaObserv,
            @Field("deviceId") String deviceId);


    @FormUrlEncoded
    @POST("/modificaproducto")
    ListadoSuper modificaproducto(
            @Field("idlista") int idlista,
            @Field("idProducto") int idProducto,
            @Field("nuevoProducto") String nuevoProducto,
            @Field("nuevaObserv") String nuevaObserv);


    @FormUrlEncoded
    @POST("/eliminaproducto")
    ListadoSuper eliminaproducto(
            @Field("idlista") int idlista,
            @Field("idProducto") int idProducto,
            @Field("nuevoProducto") String nuevoProducto);

    @FormUrlEncoded
    @POST("/productocomprado")
    ListadoSuper productocomprado(
            @Field("idlista") int idlista,
            @Field("idproducto") int idproducto,
            @Field("comprado") int comprado,
            @Field("idusercomprado") String idusercomprado);


    @FormUrlEncoded
    @POST("/busquedauser")
    ListadoUser busquedauser(
            @Field("patron")  String patronBusqueda);

    @FormUrlEncoded
    @POST("/uniruser")
    ListadoUser uniruser(
            @Field("deviceId")  String deviceId,
            @Field("deviceIdDos") String deviceIdDos,
            @Field("patron")  String patronBusqueda);

    @FormUrlEncoded
    @POST("/anularuser")
    ListadoUser anularuser(
            @Field("deviceId")  String deviceId,
            @Field("deviceIdDos") String deviceIdDos,
            @Field("patron")  String patronBusqueda);

    
	 @FormUrlEncoded
	 @POST("/guarda")
     User postGuarda(
             @Field("level") int level,
             @Field("deviceId") String deviceId,
             @Field("time") int time,
             @Field("username") String username);
    
	        
    
    @FormUrlEncoded
    @POST("/time")
    User postTime(
            @Field("level") int level,
            @Field("deviceId") String deviceId);


    //todo Hacer en fichero netfp2
    @FormUrlEncoded
    @POST("/guardaicon")
    User postGuardaIcon(
            @Field("deviceId") String deviceId,
            @Field("imgIcon") int imgIcon);

    //todo Hacer en fichero netfp2
    @FormUrlEncoded
    @POST("/cogericon")
    User postCogerIcon(
            @Field("deviceId") String deviceId,
            @Field("imgIcon") int imgIcon);



//    
//    @FormUrlEncoded
//    @POST("/time")
//    Response postTime(
//            @Field("level") int level, 
//            @Field("deviceId") String deviceId, 
//            @Field("time") int time, 
//            @Field("username") String username);
//    
}
