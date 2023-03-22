/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

/**
 *
 * @author danie
 */
public class Process implements Comparable<Process> {
    
    private String id;
    private int arrivalTime;
    private int intakeTime;
    private String status;
    private String startTime;
    private String endTime;
    private int programmCount;
    private String base;
    private String limit;

    public Process(String name, int timeArrival, int intakeTime) {
        this.id = name;
        this.arrivalTime = timeArrival;
        this.intakeTime = intakeTime;
        this.status ="Ready";
    }

    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getIntakeTime() {
        return intakeTime;
    }

    public void setIntakeTime(int intakeTime) {
        this.intakeTime = intakeTime;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getProgrammCount() {
        return programmCount;
    }

    public void setProgrammCount(int programmCount) {
        this.programmCount = programmCount;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
    
    
    
    
    
    
    @Override
    public String toString() {
        return String.valueOf("id:"+this.id+" timeArrival:"+this.arrivalTime+" intakeTime:"+this.intakeTime+" Status:"+this.getStatus());
    }
    
    
    @Override
    public int compareTo(Process otherProecess) {
        return Integer.compare(this.arrivalTime, otherProecess.arrivalTime);
    }
    
    
    
    
}
