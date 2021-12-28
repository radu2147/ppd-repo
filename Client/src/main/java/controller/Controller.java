package controller;

import domain.Account;
import domain.Employee;
import domain.FestivalDTO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import service.*;
import utils.AlertDisplayer;

import java.io.IOException;

public class Controller{

    IServices server;

    private MainPageController ctrl;
    private Parent parent;

    public void setParent(Parent parent){ this.parent=parent; }

    public void setServer(IServices server) {
        this.server = server;
    }

    public void setController(MainPageController controller){ this.ctrl=controller; }
    @FXML
    TextField textFieldUsername;
    @FXML
    TextField textFieldPassword;
    @FXML
    public void onBtnLogin(){
        String username=textFieldUsername.getText();
        String password=textFieldPassword.getText();

        System.out.println(username+" "+password);
        if(username.isEmpty() || password.isEmpty()){
            AlertDisplayer.showMessage(null, Alert.AlertType.ERROR,"Eroare!","Username sau parola gresita!");
        }
        Account account=null;
        try {
            account=server.login(new Account(username, password, null),ctrl);

        } catch (BadCredentialsException | ServiceException e) {
            AlertDisplayer.showErrorMessage(null,e.getMessage());
            return;
        }
        connect(account);
        textFieldPassword.getScene().getWindow().hide();
    }

    private void connect(Account account){
        Stage stage=new Stage();
        //stage.setTitle("Window for " +account.getName());
        stage.setScene(new Scene(parent));

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                ctrl.logout();
                System.exit(0);
            }
        });

        stage.show();
        ctrl.setAccount(account);
        //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

}
