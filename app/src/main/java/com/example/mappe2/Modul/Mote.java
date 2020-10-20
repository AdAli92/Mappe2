package com.example.mappe2.Modul;

public class Mote {

    private int moteId;
    private String navn, type, dato, sted, tid;
    private int img;

    public int getMoteId() {
        return moteId;
    }

    public void setMoteId(int moteId) {
        this.moteId = moteId;
    }

    public Mote(String navn, String type, String sted,String dato , String tid) {
        this.navn = navn;
        this.type = type;
        this.dato = dato;
        this.sted = sted;
        this.tid = tid;
    }


    public Mote(int moteId, String navn, String type, String dato, String sted, String tid) {
        this.moteId = moteId;
        this.navn = navn;
        this.type = type;
        this.dato = dato;
        this.sted = sted;
        this.tid = tid;
    }
/*    public Mote(String navn, String type, String dato, String sted, int img) {
        this.navn = navn;
        this.type = type;
        this.dato = dato;
        this.sted = sted;
        this.img = img;
        this.tid = tid;
    }

    public Mote(String navn, String type, String dato, String sted) {
        this.navn = navn;
        this.type = type;
        this.dato = dato;
        this.sted = sted;
        this.tid = tid;

    }

    public Mote( String navn, String type, String dato, String sted,String tid) {
        this.moteId = moteId;
        this.navn = navn;
        this.type = type;
        this.dato = dato;
        this.sted = sted;
        this.tid = tid;
    }*/

    public Mote(){

    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public String getSted() {
        return sted;
    }

    public void setSted(String sted) {
        this.sted = sted;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
