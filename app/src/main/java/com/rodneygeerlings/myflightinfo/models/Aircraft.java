package com.rodneygeerlings.myflightinfo.models;

public class Aircraft {
    private String name;
    private String iataSubCode;
    private String iataMainCode;

    public Aircraft() {
    }

    public Aircraft(String name, String iataSubCode, String iataMainCode) {
        setName(name);
        setIataSubCode(iataSubCode);
        setIataMainCode(iataMainCode);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIataSubCode() {
        return iataSubCode;
    }

    public void setIataSubCode(String iataSubCode) {
        this.iataSubCode = iataSubCode;
    }

    public String getIataMainCode() {
        return iataMainCode;
    }

    public void setIataMainCode(String iataMainCode) {
        this.iataMainCode = iataMainCode;
    }
}

