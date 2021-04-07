package telefonbuch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
//    	Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        BorderPane root = new BorderPane();
        UserInputArea inputArea = new UserInputArea();
        SearchArea searchArea = new SearchArea();
        root.setTop(searchArea.getPane());
        root.setCenter(entryArea.getAnchorPane());
        root.setBottom(inputArea.getGridPane());
        entryArea.setItems(tB.telefonBook);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 335, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        initialize();
        launch(args);
    }


    // Globale Static Variable
    private static TelefonBook tB = new TelefonBook();
    public static TelefonBook getTB() {
		return tB;
	}
    
    private static EntryArea entryArea = new EntryArea(FXCollections.observableArrayList(tB.telefonBook));
    public static EntryArea getEntryArea() {
		return entryArea;
	}
    
    //Zum Auslesen
    private static void initialize() {
        try (FileInputStream fis = new FileInputStream(TelefonBook.path);
             ObjectInputStream ois = new ObjectInputStream(fis);) {
            tB = (TelefonBook) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread writingHook = new Thread(() -> writeTb());
        Runtime.getRuntime().addShutdownHook(writingHook);
    }

    //Write
    private static void writeTb() {
        try {
            File telefonBookFile = new File(TelefonBook.path);
            if (!telefonBookFile.exists()) {
            	telefonBookFile.setReadable(true);
            	telefonBookFile.setWritable(true);
            	telefonBookFile.createNewFile();    	
            }
            tB.telefonBook = entryArea.getItems();
            try (FileOutputStream fos = new FileOutputStream(TelefonBook.path, false); ObjectOutputStream oos = new ObjectOutputStream(fos);) {
				oos.writeObject(tB);
			} catch (Exception e) {
				e.printStackTrace();
			}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
