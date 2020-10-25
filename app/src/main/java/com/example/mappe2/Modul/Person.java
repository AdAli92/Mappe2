package com.example.mappe2.Modul;

public class Person {

    private int personId;
    private String navn, telefonnr;

    public Person() { }

    public Person(int personId, String navn, String telefonnr) {
        this.personId = personId;
        this.navn = navn;
        this.telefonnr = telefonnr;
    }

    public Person(String navn, String telefonnr) {
        this.navn = navn;
        this.telefonnr = telefonnr;
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

}
