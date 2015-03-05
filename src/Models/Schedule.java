package Models;

import javafx.beans.property.*;
import libs.Lookups;

import java.time.LocalDate;

/**
 * Created by jgibson on 3/4/2015.
 */
public class Schedule {
    private IntegerProperty sid;
    private IntegerProperty uid;
    private ObjectProperty<LocalDate> effective_date;
    private IntegerProperty schedule_type;
    private StringProperty agency;
    private StringProperty job_number;
    private IntegerProperty job_type;
    private IntegerProperty num_items;
    private StringProperty description;
    private ObjectProperty<LocalDate> date_added;
    private ObjectProperty<LocalDate> date_modified;


    public Schedule(Integer sid,
                    Integer uid,
                    LocalDate effective_date,
                    Integer schedule_type,
                    String agency,
                    String job_number,
                    Integer job_type,
                    Integer num_items,
                    String description,
                    LocalDate date_added,
                    LocalDate date_modified) {
        this.sid = new SimpleIntegerProperty(sid);
        this.uid = new SimpleIntegerProperty(uid);
        this.effective_date = new SimpleObjectProperty<>(effective_date);
        this.schedule_type = new SimpleIntegerProperty(schedule_type);
        this.agency = new SimpleStringProperty(agency);
        this.job_number = new SimpleStringProperty(job_number);
        this.job_type = new SimpleIntegerProperty(job_type);
        this.num_items = new SimpleIntegerProperty(num_items);
        this.description = new SimpleStringProperty(description);
        this.date_added = new SimpleObjectProperty<>(date_added);
        this.date_modified = new SimpleObjectProperty<>(date_modified);
    }

    //<editor-fold desc="Getters">

    public int getUid() {
        return uid.get();
    }

    public IntegerProperty uidProperty() {
        return uid;
    }

    public int getSid() {
        return sid.get();
    }

    public IntegerProperty sidProperty() {
        return sid;
    }

    public LocalDate getEffective_date() {
        return effective_date.get();
    }

    public ObjectProperty<LocalDate> effective_dateProperty() {
        return effective_date;
    }

    public int getSchedule_type() {
        return schedule_type.get();
    }

    public IntegerProperty schedule_typeProperty() {
        return schedule_type;
    }

    public String getAgency() {
        return agency.get();
    }

    public StringProperty agencyProperty() {
        return agency;
    }

    public String getJob_number() {
        return job_number.get();
    }

    public StringProperty job_numberProperty() {
        return job_number;
    }

    public int getJob_type() {
        return job_type.get();
    }

    public StringProperty getJobTypeAsString() {
        return new SimpleStringProperty(Lookups.sched_job_type.get(getJob_type()));
    }

    public IntegerProperty job_typeProperty() {
        return job_type;
    }

    public int getNum_items() {
        return num_items.get();
    }

    public IntegerProperty num_itemsProperty() {
        return num_items;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public LocalDate getDate_added() {
        return date_added.get();
    }

    public ObjectProperty<LocalDate> date_addedProperty() {
        return date_added;
    }

    public LocalDate getDate_modified() {
        return date_modified.get();
    }

    public ObjectProperty<LocalDate> date_modifiedProperty() {
        return date_modified;
    }
    //</editor-fold>


    //<editor-fold desc="Setters">

    public void setUid(int uid) {
        this.uid.set(uid);
    }

    public void setSid(int sid) {
        this.sid.set(sid);
    }

    public void setEffective_date(LocalDate effective_date) {
        this.effective_date.set(effective_date);
    }

    public void setSchedule_type(int schedule_type) {
        this.schedule_type.set(schedule_type);
    }

    public void setAgency(String agency) {
        this.agency.set(agency);
    }

    public void setJob_number(String job_number) {
        this.job_number.set(job_number);
    }

    public void setJob_type(int job_type) {
        this.job_type.set(job_type);
    }

    public void setNum_items(int num_items) {
        this.num_items.set(num_items);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setDate_added(LocalDate date_added) {
        this.date_added.set(date_added);
    }

    public void setDate_modified(LocalDate date_modified) {
        this.date_modified.set(date_modified);
    }
    //</editor-fold>
}
