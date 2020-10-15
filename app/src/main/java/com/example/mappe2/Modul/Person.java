package com.example.mappe2.Modul;

public class Person {
    private int personId ;
    private String navn , telefonnr;
    private int img;
    private boolean checkbox;



    public Person(){
    }




    public Person(String navn, String telefonnr, int img) {
        this.navn = navn;
        this.telefonnr = telefonnr;
       this.img = img;
    }
    public Person(String navn, String telefonnr) {
        this.navn = navn;
        this.telefonnr = telefonnr;
    }

    public Person(String navn, String telefonnr, boolean checkbox) {
        this.navn = navn;
        this.telefonnr = telefonnr;
        this.checkbox = checkbox;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getTelefonnr() {
        return telefonnr;
    }

    public void setTelefonnr(String telefonnr) {
        this.telefonnr = telefonnr;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }
}
