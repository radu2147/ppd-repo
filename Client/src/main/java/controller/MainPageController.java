package controller;

import domain.Account;
import domain.Employee;
import domain.FestivalDTO;
import domain.TicketDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.BadCredentialsException;
import service.IObserver;
import service.IServices;
import service.ServiceException;
import utils.AlertDisplayer;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainPageController implements IObserver {
    ObservableList<FestivalDTO> festivalModel = FXCollections.observableArrayList();
    ObservableList<FestivalDTO> festivalSModel = FXCollections.observableArrayList();
    private Account account;
    private IServices server;

    public void setServer(IServices server){
        this.server=server;
    }
    public void setAccount(Account account){
        this.account=account;
        init();
    }
    public void setServices(Account account, IServices server) {
        this.account = account;
        this.server=server;
        init();


    }
    @FXML
    Label labelUser;
    //toti artistii
    @FXML
    TableView<FestivalDTO> tableViewArtist;
    @FXML
    TableColumn<FestivalDTO,String> tcArtistNume;
    @FXML
    TableColumn<FestivalDTO, Date> tcArtistDate;
    @FXML
    TableColumn<FestivalDTO, String> tcArtistLocatie;
    @FXML
    TableColumn<FestivalDTO, Long> tcArtistNrl;
    @FXML
    TableColumn<FestivalDTO, Long> tcArtistNrlo;

    //filtrare artisti
    @FXML
    TableView<FestivalDTO> tableViewSArtist;
    @FXML
    TableColumn<FestivalDTO,String> tcSArtistNume;
    @FXML
    TableColumn<FestivalDTO, Date> tcSArtistDate;
    @FXML
    TableColumn<FestivalDTO, String> tcSArtistLocatie;
    @FXML
    TableColumn<FestivalDTO, Long> tcSArtistNrl;
    @FXML
    TableColumn<FestivalDTO, Time> tcSArtistOra;
    private void init(){
        labelUser.setText(account.getName());
        initModelFestivals();
    }
    //TODO actualizez in ambele tabele
    //TODO selectez sa vand bilet din tabelul de search

    @FXML
    public void initialize(){
        tcArtistNume.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcArtistDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tcArtistLocatie.setCellValueFactory(new PropertyValueFactory<>("location"));
        tcArtistNrl.setCellValueFactory(new PropertyValueFactory<>("seats"));
        tcArtistNrlo.setCellValueFactory(new PropertyValueFactory<>("soldSeats"));
        tableViewArtist.setRowFactory(tv -> new TableRow<FestivalDTO>() {
            @Override
            protected void updateItem(FestivalDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (item!=null && item.getSeats()-item.getSoldSeats() == 0)
                    setStyle("-fx-background-color: red");
                else{
                    setStyle("");
                }
            }
        });
        tableViewArtist.setItems(festivalModel);

        tcSArtistNume.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcSArtistDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tcSArtistLocatie.setCellValueFactory(new PropertyValueFactory<>("location"));
        tcSArtistNrl.setCellValueFactory(cell->{
            Long seats=cell.getValue().getSeats();
            Long soldSeats=cell.getValue().getSoldSeats();

            return new SimpleObjectProperty(seats-soldSeats);
        });

        tcSArtistOra.setCellValueFactory(cell->{
            Time time=Time.valueOf("20:00:00");

            return new SimpleObjectProperty(time);
        });
        tableViewSArtist.setItems(festivalSModel);
    }
    private void initModelFestivals(){
        Iterable<FestivalDTO> festivals= null;
        try {
            festivals = server.searchByDate(null);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        List<FestivalDTO> festivalList= StreamSupport.stream(festivals.spliterator(),false).collect(Collectors.toList());
        festivalModel.setAll(festivalList);
    }

    private void initModelSFestivals(Date date){
        Iterable<FestivalDTO> festivals= null;
        try {
            festivals = server.searchByDate(date);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        List<FestivalDTO> festivalList= StreamSupport.stream(festivals.spliterator(),false).collect(Collectors.toList());
        festivalSModel.setAll(festivalList);
    }

    @FXML
    DatePicker datePickerArtist;
    public void onBtnSearchByDate(ActionEvent actionEvent) {
        LocalDate date=datePickerArtist.getValue();
        initModelSFestivals(Date.valueOf(date));
    }


    @FXML
    public void onBtnSellTicket(){
        FestivalDTO festival=tableViewArtist.getSelectionModel().getSelectedItem();
        if(festival==null){
            AlertDisplayer.showErrorMessage(null,"Trebuie sa selectati un festival");
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/sellTicketView.fxml"));


        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Sell ticket");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        SellTicketController controller= loader.getController();
        controller.setServices(account,server,festival);
        dialogStage.show();
    }

    @Override
    public void ticketsSold(TicketDTO ticket) throws ServiceException {
        //TODO implement
        for(int index=0;index<festivalModel.size();index++){
            FestivalDTO festivalDTO=festivalModel.get(index);
            if(festivalDTO.getFestivalID().intValue()==ticket.getFestivalID()){
                festivalDTO.setSoldSeats(ticket.getSeats().intValue());
                festivalModel.set(index,festivalDTO);
            }
        }
    }

    public void logout() {
        try {
            server.logout(account,this);
        } catch (ServiceException e) {
            AlertDisplayer.showErrorMessage(null,e.getMessage());
        }
    }
}
