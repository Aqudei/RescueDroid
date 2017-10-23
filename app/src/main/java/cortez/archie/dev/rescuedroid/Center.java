package cortez.archie.dev.rescuedroid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 10/16/2017.
 */

public class Center {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public Center() {

    }

    public String getCenterName() {
        return CenterName;
    }

    public void setCenterName(String centerName) {
        CenterName = centerName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    private String CenterName;
    private String Address;

    public String getInCharge() {
        return InCharge;
    }

    public void setInCharge(String inCharge) {
        InCharge = inCharge;
    }

    public String getInChargeCellphone() {
        return InChargeCellphone;
    }

    public void setInChargeCellphone(String inChargeCellphone) {
        InChargeCellphone = inChargeCellphone;
    }

    private String InCharge;
    private String InChargeCellphone;

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    String Latitude;
    String Longitude;
}
