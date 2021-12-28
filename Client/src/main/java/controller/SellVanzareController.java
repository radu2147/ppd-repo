package controller;

import domain.Account;
import domain.SpetacolDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import service.IServices;
import service.ServiceException;
import utils.AlertDisplayer;

import java.sql.Date;
import java.util.ArrayList;

public class SellVanzareController {
    private IServices server;
    private Account account;
    private SpetacolDTO festival;

    public void setServices(Account account, IServices server, SpetacolDTO festival) {
        this.account=account;
        this.server=server;
        this.festival=festival;
        init();
    }
    @FXML
    TextArea textAreaFestival;

    @FXML
    ChoiceBox<Long> choiceBoxNumarBilete;
    private void init(){
        textAreaFestival.setText(festival.toString());
        long leftVanzares= festival.getSold()-festival.getPriceVanzare();
        if (leftVanzares > 15){
            leftVanzares=15;
        }
        ArrayList<Long> arrayList=new ArrayList<>();
        for(long i=1;i<=leftVanzares;i++)
            arrayList.add(i);
        choiceBoxNumarBilete.setItems(FXCollections.observableArrayList(arrayList));
    }

    @FXML
    TextField textFieldNumeCumparator;
    @FXML
    public void onBtnSell(){
        var date= Date.valueOf(textFieldNumeCumparator.getText());
        try {
            server.sellVanzare(festival.getFestivalID().intValue(), date);
            AlertDisplayer.showMessage(null, Alert.AlertType.CONFIRMATION,"Success!","Biletele au fost vandute cu succes!");
        } catch (ServiceException e) {
            AlertDisplayer.showErrorMessage(null,e.getMessage());
        }
    }
}
