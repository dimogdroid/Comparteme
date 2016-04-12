package dimogdroid.service.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dgdavila on 12/03/2015.
 */
public class RowListSuper implements Parcelable{

    private int id;
    private int id_lista;
    private String cantidad;
    private String concepto;
    private int comprado;
    private String observaciones;
    private String usercomprado;

    public RowListSuper() {

    }

    public RowListSuper(int id, int id_lista, String cantidad, String concepto, int comprado, String observaciones, String usercomprado) {
        this.id = id;
        this.id_lista = id_lista;
        this.cantidad = cantidad;
        this.concepto = concepto;
        this.comprado = comprado;
        this.observaciones = observaciones;
        this.usercomprado = usercomprado;
    }

    public RowListSuper(Parcel in) {

        id = in.readInt();
        id_lista = in.readInt();
        comprado = in.readInt();
        cantidad = in.readString();
        concepto = in.readString();
        observaciones = in.readString();
        usercomprado = in.readString();


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_lista() {
        return id_lista;
    }

    public void setId_lista(int id_lista) {
        this.id_lista = id_lista;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public int getComprado() {
        return comprado;
    }

    public void setComprado(int comprado) {
        this.comprado = comprado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getUsercomprado() {
        return usercomprado;
    }

    public void setUsercomprado(String usercomprado) {
        this.usercomprado = usercomprado;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(id_lista);
        dest.writeInt(comprado);

        dest.writeString(cantidad);
        dest.writeString(concepto);
        dest.writeString(observaciones);
        dest.writeString(usercomprado);

    }

    public static final Parcelable.Creator<RowListSuper> CREATOR = new Parcelable.Creator<RowListSuper>() {
        public RowListSuper createFromParcel(Parcel in) {
            return new RowListSuper(in);
        }

        public RowListSuper[] newArray(int size) {
            return new RowListSuper[size];
        }
    };
}
