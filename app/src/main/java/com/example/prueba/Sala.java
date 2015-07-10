package com.example.prueba;

/**
 * Created by syueh on 05/07/15.
 */

public class Sala {

    private static final String TAG = "Sala";

    private int idSala;
    private int status;
    private String name;
    private String details;
    private String location;
    private int capacity;

    public Sala(int id, int ca, int st, String na, String det, String loc) {
        super();
        idSala = id;
        status = st;
        name = na;
        details = det;
        location = loc;
        capacity = ca;
    }

    //methods get and set
    public int getSalaID() {
        return idSala;
    }

    public void setSalaID(int id) {
        this.idSala = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int ca) {
        this.capacity = ca;
    }

    public String getSalaName() {
        return name;
    }

    public void setSalaName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails (String details) {
        this.details = details;
    }


    public String getLocation() {
        return this.location;
    }

    public void setLocation (String loc) {
        this.location = loc;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus (int st) {
        this.status = st;
    }

    @Override
    public String toString() {
        return details + "\n" + location + "\nCapacidad: " + capacity;
    }
}