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

public class BookedTrainsHelperClass {

    String ticketID, ticketTrainName, ticketTrainNo, ticketTrainFrom, ticketTrainFromTime, ticketTrainTo,
            ticketTrainToTime, bookedDate, ticketClass, ticketPrice, ticketQty, ticketTotalPrice;


    public BookedTrainsHelperClass() {

    }

    public BookedTrainsHelperClass(String ticketID, String ticketTrainName, String ticketTrainNo, String ticketTrainFrom, String ticketTrainFromTime, String ticketTrainTo,
                                   String ticketTrainToTime, String bookedDate, String ticketClass, String ticketPrice, String ticketQty, String ticketTotalPrice) {

        this.ticketID = ticketID;
        this.ticketTrainName = ticketTrainName;
        this.ticketTrainNo = ticketTrainNo;
        this.ticketTrainFrom = ticketTrainFrom;
        this.ticketTrainFromTime = ticketTrainFromTime;
        this.ticketTrainTo = ticketTrainTo;
        this.ticketTrainToTime = ticketTrainToTime;
        this.bookedDate = bookedDate;
        this.ticketClass = ticketClass;
        this.ticketPrice = ticketPrice;
        this.ticketQty = ticketQty;
        this.ticketTotalPrice = ticketTotalPrice;
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getTicketTrainName() {
        return ticketTrainName;
    }

    public void setTicketTrainName(String ticketTrainName) {
        this.ticketTrainName = ticketTrainName;
    }

    public String getTicketTrainNo() {
        return ticketTrainNo;
    }

    public void setTicketTrainNo(String ticketTrainNo) {
        this.ticketTrainNo = ticketTrainNo;
    }

    public String getTicketTrainFrom() {
        return ticketTrainFrom;
    }

    public void setTicketTrainFrom(String ticketTrainFrom) {
        this.ticketTrainFrom = ticketTrainFrom;
    }

    public String getTicketTrainFromTime() {
        return ticketTrainFromTime;
    }

    public void setTicketTrainFromTime(String ticketTrainFromTime) {
        this.ticketTrainFromTime = ticketTrainFromTime;
    }

    public String getTicketTrainTo() {
        return ticketTrainTo;
    }

    public void setTicketTrainTo(String ticketTrainTo) {
        this.ticketTrainTo = ticketTrainTo;
    }

    public String getTicketTrainToTime() {
        return ticketTrainToTime;
    }

    public void setTicketTrainToTime(String ticketTrainToTime) {
        this.ticketTrainToTime = ticketTrainToTime;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }

    public String getTicketClass() {
        return ticketClass;
    }

    public void setTicketClass(String ticketClass) {
        this.ticketClass = ticketClass;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getTicketQty() {
        return ticketQty;
    }

    public void setTicketQty(String ticketQty) {
        this.ticketQty = ticketQty;
    }

    public String getTicketTotalPrice() {
        return ticketTotalPrice;
    }

    public void setTicketTotalPrice(String ticketTotalPrice) {
        this.ticketTotalPrice = ticketTotalPrice;
    }
}
