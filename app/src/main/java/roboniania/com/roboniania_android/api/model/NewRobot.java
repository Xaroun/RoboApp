package roboniania.com.roboniania_android.api.model;

import roboniania.com.roboniania_android.api.enums.Constructions;
import roboniania.com.roboniania_android.api.enums.PairingState;
import roboniania.com.roboniania_android.api.enums.RobotSystem;

/**
 * Created by Mateusz on 26.04.2016.
 */
public class NewRobot {

    private String serial_number;
    private String robot_id;
    private PairingState pairing_state;
    private RobotSystem current_system;
    private Constructions robot_model;
    private String creation_date;
    private String modification_date;
    private RobotConstruction current_lego_construction;
    private String robot_ip;
    private String last_seen;

    public String getRobot_ip() {
        return robot_ip;
    }

    public void setRobot_ip(String robot_ip) {
        this.robot_ip = robot_ip;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getRobot_id() {
        return robot_id;
    }

    public void setRobot_id(String robot_id) {
        this.robot_id = robot_id;
    }

    public PairingState getPairing_state() {
        return pairing_state;
    }

    public void setPairing_state(PairingState pairing_state) {
        this.pairing_state = pairing_state;
    }

    public RobotSystem getCurrent_system() {
        return current_system;
    }

    public void setCurrent_system(RobotSystem current_system) {
        this.current_system = current_system;
    }

    public Constructions getRobot_model() {
        return robot_model;
    }

    public void setRobot_model(Constructions robot_model) {
        this.robot_model = robot_model;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getModification_date() {
        return modification_date;
    }

    public void setModification_date(String modification_date) {
        this.modification_date = modification_date;
    }

    public RobotConstruction getCurrent_lego_construction() {
        return current_lego_construction;
    }

    public void setCurrent_lego_construction(RobotConstruction current_lego_construction) {
        this.current_lego_construction = current_lego_construction;
    }

}

