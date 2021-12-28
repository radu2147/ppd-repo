package controller;

import domain.Account;
import domain.Festival;
import domain.FestivalDTO;
import domain.Ticket;
import domain.validators.ValidationException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import service.IServices;
import service.MainPageService;
import service.ServiceException;
import utils.AlertDisplayer;

import java.util.ArrayList;

public class SellTicketController {
    private IServices server;
    private Account account;
    private FestivalDTO festival;

    public void setServices(Account account, IServices server, FestivalDTO festival) {
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
        long leftTickets= festival.getSeats()-festival.getSoldSeats();
        if (leftTickets > 15){
            leftTickets=15;
        }
        ArrayList<Long> arrayList=new ArrayList<>();
        for(long i=1;i<=leftTickets;i++)
            arrayList.add(i);
        choiceBoxNumarBilete.setItems(FXCollections.observableArrayList(arrayList));
    }

    @FXML
    TextField textFieldNumeCumparator;
    @FXML
    public void onBtnSell(){
        Long seats=choiceBoxNumarBilete.getValue();
        String client=textFieldNumeCumparator.getText();
        if(client.isEmpty())
        {
            AlertDisplayer.showErrorMessage(null,"Numele nu poate fi vid");
            return;
        }
        try {
            server.sellTicket(festival.getFestivalID().intValue(),seats,client);
            AlertDisplayer.showMessage(null, Alert.AlertType.CONFIRMATION,"Success!","Biletele au fost vandute cu succes!");
        } catch (ServiceException e) {
            AlertDisplayer.showErrorMessage(null,e.getMessage());
        }
    }
}
