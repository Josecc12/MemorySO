/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author danie
 */
public class Planner {

    private int quantum;
    private List<Process> processList;
    private Process currentProcess;
    private List<Process> finishedProcess;
    private List<Process> auxList;
    private int currentTime;

    public Planner(List<Process> processList, int quatum) {
        
        this.quantum = quatum;
        this.processList = processList;
        //this.processList.add(processList.get(0));
        this.finishedProcess = new ArrayList<Process>();
        Collections.sort(processList);
        this.currentProcess = processList.get(0);
        currentTime = currentProcess.getArrivalTime();
        if(currentTime!=0){
            try {
                Thread.sleep(currentTime*1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Planner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //processList.remove(0);

    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }
    
    

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    public void setCurrentProcess(Process currentProcess) {
        this.currentProcess = currentProcess;
    }

    public List<Process> getFinishedProcess() {
        return finishedProcess;
    }

    public void setFinishedProcess(List<Process> finishedProcess) {
        this.finishedProcess = finishedProcess;
    }

    public void showProcessList() {
        for (Process procees : processList) {
            System.out.println(procees);
        }

    }

    public String getTime() {
        Calendar calendario;
        String horaSistema = "";
        calendario = Calendar.getInstance();
        if (calendario.get(Calendar.HOUR_OF_DAY) < 10) {
            horaSistema += String.valueOf("0" + calendario.get(Calendar.HOUR_OF_DAY)) + ":";
        } else {
            horaSistema += String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)) + ":";
        }
        if (calendario.get(Calendar.MINUTE) < 10) {
            horaSistema += String.valueOf("0" + calendario.get(Calendar.MINUTE)) + ":";
        } else {
            horaSistema += String.valueOf(calendario.get(Calendar.MINUTE)) + ":";
        }
        if (calendario.get(Calendar.SECOND) < 10) {
            horaSistema += String.valueOf("0" + calendario.get(Calendar.SECOND)) + ":";
        } else {
            horaSistema += String.valueOf(calendario.get(Calendar.SECOND)) + ":";
        }
        horaSistema += String.valueOf(calendario.get(Calendar.MILLISECOND)) + " hrs";
        return horaSistema;
    }

    public void roundRobbin() 
    {
        while (currentProcess != null) {
            try {
                //Si es su primera vez de ejecucion inia el tiempo
                if (currentProcess.getStartTime() == null) {
                    currentProcess.setStartTime(getTime());
                }
                
                //Ejecuta el proceso como Current Process
                this.currentProcess.setStatus("Executing");
                processList.remove(this.currentProcess);
                System.out.println("CurrenteProcess:" + this.currentProcess);

                //Reduce el tiempo de consumo y aumenta el contador del programa en 10
                for (int i = 0; i < quantum; i++) {
                    Thread.sleep(1000);
                    if(this.currentProcess.getIntakeTime()>0){
                        this.currentProcess.setIntakeTime(currentProcess.getIntakeTime() - 1);
                        this.currentProcess.setProgrammCount(this.currentProcess.getProgrammCount()+10);
                        
                        this.currentTime++;
                    }
                    else{
                        break;
                    }

                }

                showProcessList();
                System.out.println("");
                
                //Si el tiempo de consumo es >0 lo vuelve a poner en la lista de procesos para ejecutarse
                if (this.currentProcess.getIntakeTime() > 0) {
                    currentProcess.setStatus("Ready");
                    processList.add(currentProcess);
                } 
                //Si no lo pone en la  lista de terminados
                else {
                    currentProcess.setStatus("Finished");
                    currentProcess.setIntakeTime(0);
                    currentProcess.setEndTime(getTime());
                    this.finishedProcess.add(currentProcess);
                }
                
                //Pasa al siguiente proceso de la lista hasta que sea null
                if(!processList.isEmpty()){
                    
                    Process aux = processList.get(0);
                    
                    if(currentTime>=aux.getArrivalTime()){
                        //Cambio de Processo
                        this.currentProcess = aux;
                        //Conmutador
                        Thread.sleep(1000);
                        
                    }
                    /*if(currentProcess.getIntakeTime()<=0 && (aux.getArrivalTime()-this.currentTime>=0)){
                        //Tiempo muerto entre procesos
                        System.out.println("Tiempo Muerto...");
                        Thread.sleep((aux.getArrivalTime()-this.currentTime)*1000);
                        this.currentTime = aux.getArrivalTime();
                    }*/
                    
                }
                else{
                    this.currentProcess = null;
                }
                //this.currentProcess = !processList.isEmpty() ? processList.get(0) : null;

            } catch (InterruptedException ex) {
                Logger.getLogger(Planner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void setNewProcees(){
        
    }

}
