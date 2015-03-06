package Models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/**
 * Created by jgibson on 3/5/2015.
 */
public class Workshop {
    private IntegerProperty wid;
    private IntegerProperty uid;
    private ObjectProperty<LocalDate> workshop_date;
    private ObservableList<IntegerProperty> additional_uids;
    private ObservableList<StringProperty> additional_names;
    private IntegerProperty auid;
    private StringProperty location;
    private IntegerProperty num_attending;
    private IntegerProperty workshop_type;
    private StringProperty description_property;
    private ObjectProperty<LocalDate> date_added;
    private ObjectProperty<LocalDate> date_modified;

    public Workshop(Integer wid,
                    Integer uid,
                    LocalDate workshop_date,
                    Integer additional_uids,
                    String location,
                    Integer num_attending,
                    Integer workshop_type,
                    String description_property,
                    LocalDate date_added,
                    LocalDate date_modified) {
        this.wid = new SimpleIntegerProperty(wid);
        this.uid = new SimpleIntegerProperty(uid);
        this.workshop_date = new SimpleObjectProperty<>(workshop_date);
        this.auid = new SimpleIntegerProperty(additional_uids);
        this.location = new SimpleStringProperty(location);
        this.num_attending = new SimpleIntegerProperty(num_attending);
        this.workshop_type = new SimpleIntegerProperty(workshop_type);
        this.description_property = new SimpleStringProperty(description_property);
    }

    public void setAdditionalUids(ObservableList<Integer> uids) {
        this.additional_uids = FXCollections.observableArrayList();
        for(Integer i : uids) {
            this.additional_uids.add(new SimpleIntegerProperty(i));
        }
    }

    public void setAdditionalAnalysts(ObservableList<String> names) {
        this.additional_names = FXCollections.observableArrayList();
        for(String s : names) {
            additional_names.add(new SimpleStringProperty(s));
        }
    }

    public StringProperty getAdditionalAnalysts() {
        StringBuilder s = new StringBuilder();
        if(additional_names == null) return new SimpleStringProperty("");
        for(StringProperty str : additional_names) {
            s.append(str.getValue() + "\n");
        }
        return new SimpleStringProperty(s.toString());
    }

    //<editor-fold desc="Getters">
    public int getWid() {
        return wid.get();
    }

    public IntegerProperty widProperty() {
        return wid;
    }

    public int getUid() {
        return uid.get();
    }

    public IntegerProperty uidProperty() {
        return uid;
    }

    public LocalDate getWorkshop_date() {
        return workshop_date.get();
    }

    public ObjectProperty<LocalDate> workshop_dateProperty() {
        return workshop_date;
    }

    public ObservableList<IntegerProperty> getAdditional_uids() {
        return additional_uids;
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public int getNum_attending() {
        return num_attending.get();
    }

    public IntegerProperty num_attendingProperty() {
        return num_attending;
    }

    public int getWorkshop_type() {
        return workshop_type.get();
    }

    public IntegerProperty workshop_typeProperty() {
        return workshop_type;
    }

    public String getDescription_property() {
        return description_property.get();
    }

    public StringProperty description_propertyProperty() {
        return description_property;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setWid(int wid) {
        this.wid.set(wid);
    }

    public void setUid(int uid) {
        this.uid.set(uid);
    }

    public void setWorkshop_date(LocalDate workshop_date) {
        this.workshop_date.set(workshop_date);
    }

    public void setAdditional_uids(ObservableList<IntegerProperty> additional_uids) {
        this.additional_uids = additional_uids;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public void setNum_attending(int num_attending) {
        this.num_attending.set(num_attending);
    }

    public void setWorkshop_type(int workshop_type) {
        this.workshop_type.set(workshop_type);
    }

    public void setDescription_property(String description_property) {
        this.description_property.set(description_property);
    }
    //</editor-fold>
}
