package Models;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Created by jgibson on 2/20/2015.
 */
public class Minute {
    private IntegerProperty mid;
    private IntegerProperty uid;
    private StringProperty sender;
    private StringProperty sender_office;
    private StringProperty type;
    private IntegerProperty num_pages;
    private IntegerProperty tally;
    private StringProperty notes;
    private ObjectProperty<LocalDate> date_added;

    public Minute(Integer mid, Integer uid, String sender, String sender_office, String type, Integer num_pages, Integer tally, String notes, LocalDate date_added) {
        this.mid = new SimpleIntegerProperty(mid);
        this.uid = new SimpleIntegerProperty(uid);
        this.sender = new SimpleStringProperty(sender);
        this.sender_office = new SimpleStringProperty(sender_office);
        this.type = new SimpleStringProperty(type);
        this.num_pages = new SimpleIntegerProperty(num_pages);
        this.tally = new SimpleIntegerProperty(tally);
        this.notes = new SimpleStringProperty(notes);
        this.date_added = new SimpleObjectProperty<LocalDate>(date_added);
    }



    //<editor-fold desc="Getters">

    public int getMid() {
        return mid.get();
    }

    public IntegerProperty midProperty() {
        return mid;
    }

    public int getUid() {
        return uid.get();
    }

    public IntegerProperty uidProperty() {
        return uid;
    }

    public String getSender() {
        return sender.get();
    }

    public StringProperty senderProperty() {
        return sender;
    }

    public String getSender_office() {
        return sender_office.get();
    }

    public StringProperty sender_officeProperty() {
        return sender_office;
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public int getNum_pages() {
        return num_pages.get();
    }

    public IntegerProperty num_pagesProperty() {
        return num_pages;
    }

    public int getTally() {
        return tally.get();
    }

    public IntegerProperty tallyProperty() {
        return tally;
    }

    public String getNotes() {
        return notes.get();
    }

    public StringProperty notesProperty() {
        return notes;
    }

    public LocalDate getDate_added() {
        return date_added.get();
    }

    public ObjectProperty<LocalDate> date_addedProperty() {
        return date_added;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">

    public void setMid(int mid) {
        this.mid.set(mid);
    }

    public void setUid(int uid) {
        this.uid.set(uid);
    }

    public void setSender(String sender) {
        this.sender.set(sender);
    }

    public void setSender_office(String sender_office) {
        this.sender_office.set(sender_office);
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public void setNum_pages(int num_pages) {
        this.num_pages.set(num_pages);
    }

    public void setTally(int tally) {
        this.tally.set(tally);
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public void setDate_added(LocalDate date_added) {
        this.date_added.set(date_added);
    }
    //</editor-fold>
}
