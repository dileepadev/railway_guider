/*
   --------------------------------------
      Developed by
      Dileepa Bandara
      https://dileepabandara.github.io
      contact.dileepabandara@gmail.com
      ©dileepabandara.dev
      2020
   --------------------------------------
*/

package dev.dileepabandara.railwayguider.Databases;

public class SchedulesHelperClass {

    String endTime, startTime, trainName, trainNo;

    public SchedulesHelperClass() {

    }

    public SchedulesHelperClass(String endTime, String startTime, String trainName, String trainNo) {
        this.endTime = endTime;
        this.startTime = startTime;
        this.trainName = trainName;
        this.trainNo = trainNo;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }
}
