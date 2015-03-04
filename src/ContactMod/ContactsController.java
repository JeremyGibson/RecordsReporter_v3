package ContactMod;

import Models.Contact;
import TableModels.ContactsTableModel;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import libs.ControlledScreen;
import libs.Database;
import libs.ScreenViewSwitcher;
import LoginMod.classes.User;


import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;


/**
 * Created by jgibson on 2/11/2015.
 */
public class ContactsController implements Initializable, ControlledScreen {
    private ScreenViewSwitcher myController;
    private User user;
    private Database db;


    @FXML private TableColumn<Contact, Number> cidCol = new TableColumn<>();
    @FXML private TableColumn<Contact, String> userCol = new TableColumn<>();
    @FXML private TableColumn<Contact, LocalDate> cdCol = new TableColumn<>();
    @FXML private TableColumn<Contact, String> agencyCol = new TableColumn<>();
    @FXML private TableColumn<Contact, String> agencyOfficeCol = new TableColumn<>();
    @FXML private TableColumn<Contact, String> contactPersonCol = new TableColumn<>();
    @FXML private TableColumn<Contact, Number> numberContacts = new TableColumn<>();
    @FXML private Button add_contact;
    @FXML private Button delete_contact;

    @FXML private TableView<Contact> contacts_table = new TableView<Contact>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void init() {
        cidCol.setCellValueFactory(cellData -> cellData.getValue().cidProperty());
        userCol.setCellValueFactory(cellData -> cellData.getValue().user_nameProperty());
        cdCol.setCellValueFactory(cellData -> cellData.getValue().contact_dateProperty());
        agencyCol.setCellValueFactory(cellData -> cellData.getValue().contact_agencyProperty());
        agencyOfficeCol.setCellValueFactory(cellData -> cellData.getValue().contact_agency_officeProperty());
        contactPersonCol.setCellValueFactory(cellData -> cellData.getValue().contact_personProperty());
        numberContacts.setCellValueFactory(cellData -> cellData.getValue().num_contactsProperty());

        final ObservableList<Contact> data = getList(user.getUid(), ContactsTableModel.USER_CONTACTS);
        contacts_table.setItems(data);
        contacts_table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)) {
                    if(event.getClickCount() == 2) {
                        try {
                            editContact();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        contacts_table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    public ObservableList<Contact> getList(int uid, int type) {
        ContactsTableModel ctm = new ContactsTableModel();
        ObservableList<Contact> contacts = null;
        try {
            contacts = ctm.getModel(uid, type, db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
        return contacts;
    }

    @FXML
    private void handleAddContact() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_contact.fxml"));
        Stage dialogStage = new Stage(StageStyle.DECORATED);
        dialogStage.setTitle("Add A New Contact");
        dialogStage.setScene(new Scene((AnchorPane) loader.load()));
        AddContactsController acc = loader.<AddContactsController>getController();
        acc.setUser(user);
        acc.registerTable(contacts_table);
        acc.setParent(dialogStage);
        acc.init();
        dialogStage.show();
    }

    private void editContact() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_contact.fxml"));
        Stage dialogStage = new Stage(StageStyle.DECORATED);
        dialogStage.setTitle("Edit Contact");
        dialogStage.setScene(new Scene((AnchorPane) loader.load()));
        AddContactsController acc = loader.<AddContactsController>getController();
        acc.setUser(user);
        acc.registerTable(contacts_table);
        acc.setParent(dialogStage);
        acc.init(contacts_table.getSelectionModel().getSelectedItem());
        dialogStage.show();
    }

    @Override
    public void setUser(User u) {
        user = u;
    }

    @Override
    public void setScreenParent(ScreenViewSwitcher screenPage) {
        myController = screenPage;
    }

    @FXML
    private void handleDelete() throws SQLException {
        String delete_contacts = "DELETE FROM contacts where cid=?";
        PreparedStatement ps = db.getConnection().prepareStatement(delete_contacts);
        for(Contact c : contacts_table.getSelectionModel().getSelectedItems()) {
            ps.setInt(1, c.getCid());
            ps.executeUpdate();
            contacts_table.getItems().remove(c);
        }
        ps.close();
    }

    @Override
    public void setDatabase(Database db) {
        this.db = db;
    }
}
