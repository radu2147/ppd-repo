
import controller.Controller;
import controller.MainPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import objectProtocol.ServicesObjectProxy;
import service.IServices;

import java.io.IOException;
import java.util.Properties;


public class StartObjectClient extends Application {
    private static int defaultPort =55555;
    private static String defaultServer="localhost";
    public static void main(String[] args) {
        launch(args);
    }
    private void initView(Stage primaryStage,IServices server) throws IOException {
        FXMLLoader loginViewLoader = new FXMLLoader();
        loginViewLoader.setLocation(getClass().getResource("views/loginView.fxml"));
        AnchorPane anchorPane = loginViewLoader.load();
        primaryStage.setScene(new Scene(anchorPane));

        Controller controller= loginViewLoader.getController();
        controller.setServer(server);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties clientProps=new Properties();
        try {
            clientProps.load(StartObjectClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties "+e);
            return;
        }
        String serverIP=clientProps.getProperty("server.host",defaultServer);
        int serverPort= defaultPort;
        try{
            serverPort=Integer.parseInt(clientProps.getProperty("server.port"));
        }catch(NumberFormatException ex){
            System.err.println("Wrong port number "+ex.getMessage());
            System.out.println("Using default port: "+ defaultPort);
        }
        System.out.println("Using server IP "+serverIP);
        System.out.println("Using server port "+serverPort);
        IServices server=new ServicesObjectProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("views/loginView.fxml"));
        Parent root=loader.load();


        Controller ctrl =
                loader.<Controller>getController();
        ctrl.setServer(server);




        FXMLLoader mloader = new FXMLLoader(
                getClass().getClassLoader().getResource("views/mainPageView.fxml"));
        Parent croot=mloader.load();


        MainPageController chatCtrl =
                mloader.<MainPageController>getController();
        chatCtrl.setServer(server);

        ctrl.setController(chatCtrl);
        ctrl.setParent(croot);

        //initView(primaryStage,server);
        primaryStage.setTitle("Login Festival Tickets");
        primaryStage.setScene(new Scene(root));
        primaryStage.setWidth(800);
        primaryStage.show();
    }
}
