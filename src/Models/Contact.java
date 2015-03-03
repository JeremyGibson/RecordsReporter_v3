package Models;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Created by jgibson on 2/11/2015.
 */
public class Contact {
    private final IntegerProperty cid;
    private final IntegerProperty uid;
    private final StringProperty user_name;
    private final ObjectProperty<LocalDate> contact_date;
    private final ObjectProperty<LocalDate> date_added;
    private final ObjectProperty<LocalDate> date_modified;
    private final IntegerProperty agency_type;
    private final IntegerProperty contact_type;
    private final StringProperty contact_agency;
    private final StringProperty contact_agency_office;
    private final StringProperty contact_person;
    private final StringProperty contact_description;
    private final IntegerProperty contact_class;
    private final IntegerProperty num_contacts;
    private final IntegerProperty additional_analysts;


    public Contact(int cid, int uid,
                   String user_name,
                   LocalDate contact_date,
                   LocalDate date_added,
                   int agency_type,
                   int contact_type,
                   String contact_agency,
                   String contact_agency_office,
                   String contact_person,
                   String contact_description,
                   int contact_class,
                   int num_contacts,
                   int additional_analysts,
                   LocalDate date_modified) {
        this.cid = new SimpleIntegerProperty(cid);
        this.uid = new SimpleIntegerProperty(uid);
        this.user_name = new SimpleStringProperty(user_name);
        this.contact_date = new SimpleObjectProperty<LocalDate>(contact_date);
        this.date_added = new SimpleObjectProperty<LocalDate>(date_added);
        this.date_modified = new SimpleObjectProperty<LocalDate>(date_modified);
        this.agency_type =  new SimpleIntegerProperty(agency_type);
        this.contact_type = new SimpleIntegerProperty(contact_type);
        this.contact_agency = new SimpleStringProperty(contact_agency);
        this.contact_agency_office = new SimpleStringProperty(contact_agency_office);
        this.contact_person = new SimpleStringProperty(contact_person);
        this.contact_description = new SimpleStringProperty(contact_description);
        this.contact_class = new SimpleIntegerProperty(contact_class);
        this.num_contacts = new SimpleIntegerProperty(num_contacts);
        this.additional_analysts = new SimpleIntegerProperty(additional_analysts);
    }

    //<editor-fold desc="Getters">
    public int getCid() {
        return cid.get();
    }

    public IntegerProperty cidProperty() {
        return cid;
    }

    public int getUid() {
        return uid.get();
    }

    public IntegerProperty uidProperty() {
        return uid;
    }

    public String getUser_name() {
        return user_name.get();
    }

    public StringProperty user_nameProperty() {
        return user_name;
    }

    public LocalDate getContact_date() {
        return contact_date.get();
    }

    public ObjectProperty<LocalDate> contact_dateProperty() {
        return contact_date;
    }

    public LocalDate getDate_added() {
        return date_added.get();
    }

    public ObjectProperty<LocalDate> date_addedProperty() {
        return date_added;
    }

    public int getAgency_type() {
        return agency_type.get();
    }

    public IntegerProperty agency_typeProperty() {
        return agency_type;
    }

    public Integer getContact_type() {
        return contact_type.get();
    }

    public IntegerProperty contact_typeProperty() {
        return contact_type;
    }

    public String getContact_agency() {
        return contact_agency.get();
    }

    public StringProperty contact_agencyProperty() {
        return contact_agency;
    }

    public String getContact_agency_office() {
        return contact_agency_office.get();
    }

    public StringProperty contact_agency_officeProperty() {
        return contact_agency_office;
    }

    public String getContact_person() {
        return contact_person.get();
    }

    public StringProperty contact_personProperty() {
        return contact_person;
    }

    public String getContact_description() {
        return contact_description.get();
    }

    public StringProperty contact_descriptionProperty() {
        return contact_description;
    }

    public Integer getContact_class() {
        return contact_class.get();
    }

    public IntegerProperty contact_classProperty() {
        return contact_class;
    }

    public int getNum_contacts() {
        return num_contacts.get();
    }

    public IntegerProperty num_contactsProperty() {
        return num_contacts;
    }

    public int getAdditional_analysts() {
        return additional_analysts.get();
    }

    public IntegerProperty additional_analystsProperty() {
        return additional_analysts;
    }

    public LocalDate getDate_modified() {
        return date_modified.get();
    }

    public ObjectProperty<LocalDate> date_modifiedProperty() {
        return date_modified;
    }

    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setCid(int cid) {
        this.cid.set(cid);
    }

    public void setUid(int uid) {
        this.uid.set(uid);
    }

    public void setUser_name(String user_name) {
        this.user_name.set(user_name);
    }

    public void setContact_date(LocalDate contact_date) {
        this.contact_date.set(contact_date);
    }

    public void setDate_added(LocalDate date_added) {
        this.date_added.set(date_added);
    }

    public void setAgency_type(int agency_type) {
        this.agency_type.set(agency_type);
    }

    public void setContact_type(int contact_type) {
        this.contact_type.set(contact_type);
    }

    public void setContact_agency(String contact_agency) {
        this.contact_agency.set(contact_agency);
    }

    public void setContact_agency_office(String contact_agency_office) {
        this.contact_agency_office.set(contact_agency_office);
    }

    public void setContact_person(String contact_person) {
        this.contact_person.set(contact_person);
    }

    public void setContact_description(String contact_description) {
        this.contact_description.set(contact_description);
    }

    public void setContact_class(int contact_class) {
        this.contact_class.set(contact_class);
    }

    public void setNum_contacts(int num_contacts) {
        this.num_contacts.set(num_contacts);
    }

    public void setAdditional_analysts(int additional_analysts) {
        this.additional_analysts.set(additional_analysts);
    }

    public void setDate_modified(LocalDate date_modified) {
        this.date_modified.set(date_modified);
    }

    //</editor-fold>
}