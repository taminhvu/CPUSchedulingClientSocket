
package entities;

import java.io.Serializable;

public class Process implements Serializable{
    private String nameProcess;
    private int arrivalTime;
    private int burstTime;
    private  int waitingTime;
    private int completionTime;
    private int priority;
    private int turnaroundTime;
    public int getWaitingTime() {
        return waitingTime;
    }
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int completionTime) {
        this.turnaroundTime = completionTime;
    }
    
    public Process() {
    	
    }
    
    public Process(String nameProcess, int burstTime){
        this.nameProcess = nameProcess;
        this.burstTime = burstTime;
    }
    public Process(String nameProcess, int arrivalTime, int burstTime) {
        this.nameProcess = nameProcess;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }

    public Process(String nameProcess,int arrivalTime, int burstTime, int priority) {
        this.nameProcess = nameProcess;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    public String getNameProcess() {
        return nameProcess;
    }

    public void setNameProcess(String nameProcess) {
        this.nameProcess = nameProcess;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }
	@Override
	public String toString() {
		return "Process [nameProcess=" + nameProcess + ", arrivalTime=" + arrivalTime + ", burstTime=" + burstTime
				+ ", waitingTime=" + waitingTime + ", completionTime=" + completionTime + ", priority=" + priority
				+ ", turnaroundTime=" + turnaroundTime + "]";
	}
    
}
