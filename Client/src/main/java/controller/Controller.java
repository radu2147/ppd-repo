package controller;

import domain.Account;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import service.*;
import utils.AlertDisplayer;

import java.sql.Date;
import java.util.List;

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
        textFieldPassword.getScene().getWindow().hide();


        // quick test
        try{
            this.server.addVanzare(1, Date.valueOf("2019-09-09"), List.of(1, 2, 3, 4));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

}
