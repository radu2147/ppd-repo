package controller;

import domain.Account;
import domain.SpetacolDTO;
import domain.VanzareDTO;
import javafx.beans.property.SimpleObjectProperty;
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
import service.IObserver;
import service.IServices;
import service.ServiceException;
import utils.AlertDisplayer;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainPageController implements IObserver {
    ObservableList<SpetacolDTO> festivalModel = FXCollections.observableArrayList();
    ObservableList<SpetacolDTO> festivalSModel = FXCollections.observableArrayList();
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
    TableView<SpetacolDTO> tableViewArtist;
    @FXML
    TableColumn<SpetacolDTO,String> tcArtistNume;
    @FXML
    TableColumn<SpetacolDTO, Date> tcArtistDate;
    @FXML
    TableColumn<SpetacolDTO, String> tcArtistLocatie;
    @FXML
    TableColumn<SpetacolDTO, Long> tcArtistNrl;
    @FXML
    TableColumn<SpetacolDTO, Long> tcArtistNrlo;

    //filtrare artisti
    @FXML
    TableView<SpetacolDTO> tableViewSArtist;
    @FXML
    TableColumn<SpetacolDTO,String> tcSArtistNume;
    @FXML
    TableColumn<SpetacolDTO, Date> tcSArtistDate;
    @FXML
    TableColumn<SpetacolDTO, String> tcSArtistLocatie;
    @FXML
    TableColumn<SpetacolDTO, Long> tcSArtistNrl;
    @FXML
    TableColumn<SpetacolDTO, Time> tcSArtistOra;
    private void init(){
        labelUser.setText(account.getName());
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
        tableViewArtist.setRowFactory(tv -> new TableRow<SpetacolDTO>() {
            @Override
            protected void updateItem(SpetacolDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (item!=null && item.getSold()-item.getPriceVanzare() == 0)
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
            Long seats=cell.getValue().getSold();
            Long soldSeats=cell.getValue().getPriceVanzare();

            return new SimpleObjectProperty(seats-soldSeats);
        });

        tcSArtistOra.setCellValueFactory(cell->{
            Time time=Time.valueOf("20:00:00");

            return new SimpleObjectProperty(time);
        });
        tableViewSArtist.setItems(festivalSModel);
    }

    @FXML
    DatePicker datePickerArtist;
    public void onBtnSearchByDate(ActionEvent actionEvent) {
        LocalDate date=datePickerArtist.getValue();
    }


    @FXML
    public void onBtnSellVanzare(){
        SpetacolDTO festival=tableViewArtist.getSelectionModel().getSelectedItem();
        if(festival==null){
            AlertDisplayer.showErrorMessage(null,"Trebuie sa selectati un festival");
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/sellVanzareView.fxml"));


        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Sell Vanzare");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);

        SellVanzareController controller= loader.getController();
        controller.setServices(account,server,festival);
        dialogStage.show();
    }

    @Override
    public void VanzaresSold(VanzareDTO Vanzare) throws ServiceException {
        //TODO implement
        for(int index=0;index<festivalModel.size();index++){
            SpetacolDTO festivalDTO=festivalModel.get(index);
            if(festivalDTO.getFestivalID().intValue()==Vanzare.getFestivalID()){
                festivalModel.set(index,festivalDTO);
            }
        }
    }
}
