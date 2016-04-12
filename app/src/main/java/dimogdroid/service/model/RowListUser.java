package dimogdroid.service.model;

/**
 * Created by dgdavila on 11/06/2015.
 */
public class RowListUser {

    private String nombreUser;
    private String fraseUser;
    private String deviceId;

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getFraseUser() {
        return fraseUser;
    }

    public void setFraseUser(String fraseUser) {
        this.fraseUser = fraseUser;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
