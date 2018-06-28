package com.rodneygeerlings.myflightinfo.models;



public class Flight {

    private String flightDirection;
    private Long flightId;
    private String flightName;
    private String flightNumber;
    private String scheduleDate;
    private String scheduleTime;
    private String route;
    private String flightState;
    private String terminal;
    private String gate;
    private String aircraftType;

    public Flight(String flightDirection, Long flightId,String flightName, String flightNumber, String scheduleDate, String scheduleTime, String route, String flightState, String terminal, String gate, String aircraftType) {
        this.flightDirection = flightDirection;
        this.flightId = flightId;
        this.flightName = flightName;
        this.flightNumber = flightNumber;
        this.scheduleDate = scheduleDate;
        this.scheduleTime = scheduleTime;
        this.route = route;
        this.flightState = flightState;
        this.terminal = terminal;
        this.gate = gate;
        this.aircraftType = aircraftType;
    }

    public Flight(Long flightId, String flightName) {
        this.flightId = flightId;
        this.flightName = flightName;
    }

    public Long getFlightId(){
        return flightId;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public String getGate() {
        return gate;
    }

    public String getTerminal() {
        return terminal;
    }

    public String getFlightState() {
        return flightState;
    }

    public String getRoute() {
        return route;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getFlightName() {
        return flightName;
    }

    public String getFlightDirection() {
        return flightDirection;
    }
}
