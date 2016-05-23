package roboniania.com.roboniania_android.api.model;

/**
 * Created by Mateusz on 26.04.2016.
 */
public class Robot {

    private String ip;
    private String sn;
    private String uuid;

    public Robot(String ip, String sn, String uuid) {
        this.ip = ip;
        this.sn = sn;
        this.uuid = uuid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
