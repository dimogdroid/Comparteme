package dimogdroid.service.model;

/**
 * Created by dgdavila on 10/03/2015.
 */
public class RowListPrincipal {

    private int id;
    private String nombre;
    private String user;
    private Long fechacreacion;
    private Long fechamod;
    private String icono;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Object getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(Long fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public Long getFechamod() {
        return fechamod;
    }

    public void setFechamod(Long fechamod) {
        this.fechamod = fechamod;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}
