package seriesfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

public class SeriesFX extends Application {

    TreeView<String> treeView;
    MultipleSelectionModel selected;
    FileManagement g;
    ArrayList list = new ArrayList();
    TreeItem<String> root;
    static boolean clicked = false;
    TreeItem treeItem;
    VBox info;
    HBox buttons;

    public ArrayList<String> list(VBox in) {
        ArrayList<String> lista = new ArrayList<>();
        for (int i = 0; i < in.getChildren().size(); i++) 
            if (((HBox) in.getChildren().get(i)).getChildren().get(1) instanceof TextField) 
                lista.add(((TextField) ((HBox) in.getChildren().get(i)).getChildren().get(1)).getText());
        return lista;
    }

    public void clean() {
        for (int i = 0; i < info.getChildren().size(); i++)
            if (((HBox) info.getChildren().get(i)).getChildren().get(1) instanceof TextField) 
                ((TextField)((HBox) info.getChildren().get(i)).getChildren().get(1)).setText("");
    }
    
    public void buttonsOut() {
        for (int i = 0; i < buttons.getChildren().size(); i++)
            ((Button) buttons.getChildren().get(i)).setDisable(!((Button) buttons.getChildren().get(i)).isDisabled());
    }

    @Override
    public void start(Stage stage) {

        g = new FileManagement("series.dat");
        list = g.readData(list);

        root = new TreeItem<>("Series");
        root.setExpanded(true);

        for (Object o : list) makeShow((Info) o, root);
        
        HBox mainBox = new HBox();
        info = new VBox();
        String[] tags = {"Serie:", "Scriptwriter:", "Seasons:",
             "Genre:", "Watched seasons:"};

        for (String x : tags) {
            Label label = new Label(x);
            label.setPadding(new Insets(4, 0, 0, 0));
            TextField textField = new TextField();
            HBox hbox = new HBox(label, textField);
            label.setPrefWidth(100);
            info.getChildren().addAll(hbox);
        }
        
        Button first = new Button("|<");
        Button previous = new Button("<");
        Button next = new Button(">");
        Button last = new Button(">|");
        Button add = new Button("+");
        Button remove = new Button("-");
        Button newData = new Button("*");
        Button save = new Button("SAVE");

        first.setOnMouseClicked(e-> {
           selected.select(root.getChildren().get(0));
        });
        
        previous.setOnMouseClicked(e-> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

            if (selectedItem != null){
            int index = selectedItem.getParent().getChildren().indexOf(selectedItem) - 1;
            selected.select(root.getChildren().get(index >= 0 ? index : root.getChildren().size()-1));
            }
            else selected.select(root.getChildren().get(0));
        });
        
        next.setOnMouseClicked(e-> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

            if (selectedItem != null){
            int index = selectedItem.getParent().getChildren().indexOf(selectedItem) + 1;
            selected.select(root.getChildren().get(
            index < root.getChildren().size() ? index : 0)
            );

            } else {
                selected.select(root.getChildren().get(0));
            }
        });
        
        last.setOnMouseClicked( e-> selected.select(root.getChildren().get(root.getChildren().size()-1)));
        
        add.setOnMouseClicked(e -> {
            if (clicked = !clicked) {
                add.setText("***");
                clean();
                buttonsOut();
                add.setDisable(false);
            } else {
                add.setText("+");
                buttonsOut();
                add.setDisable(false);
                ArrayList<String> list = list(info);
                clean();
                makeShow(new Info(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4)), root);
            }
        });

        remove.setOnMouseClicked(e -> {
            if (treeItem.getParent() == root) root.getChildren().remove(treeItem);
        });

        newData.setOnMouseClicked(e -> {
            if (treeItem.getParent() == root) {
                ArrayList<String> list = list(info);
                treeItem.setValue(list.get(0));
                for (int i = 1; i < list.size(); i++)
                    root.getChildren()
                            .get(treeItem.getParent().getChildren()
                            .indexOf(treeItem)).getChildren().get(i - 1)
                            .setValue(list.get(i));
            }
        });

        save.setOnMouseClicked(e -> {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < root.getChildren().size(); i++) {
                list.add(root.getChildren().get(i).getValue());
                for (int j = 0; j < root.getChildren().get(i).getChildren().size(); j++) 
                    list.add(root.getChildren().get(i).getChildren().get(j).getValue());
                
                this.list.add(new Info(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4)));
                list = new ArrayList<>();
            }
            g.writeData(this.list);
            this.list = new ArrayList<>();
        });

        buttons = new HBox(first, previous, next, last, newData, add, remove, save);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 10, 10, 10));
        buttons.setSpacing(10);
        info.getChildren().add(buttons);

        treeView = new TreeView<>(root);
        selected = treeView.getSelectionModel();
        treeView.getSelectionModel().selectedItemProperty().addListener(((v, oldValue, newValue) -> {
            if (newValue.getParent() == root) {
                treeItem = newValue;
                ((TextField) ((HBox) info.getChildren().get(0)).getChildren().get(1)).setText(newValue.getValue());
                for (int i = 1; i <= 4; i++) 
                    ((TextField) ((HBox) info.getChildren().get(i)).getChildren().get(1)).setText(newValue.getChildren().get(i-1).getValue());
                
            } else treeItem = newValue;
        }));

        info.setSpacing(10);
        info.setPadding(new Insets(50, 50, 50, 50));
        mainBox.getChildren().addAll(treeView, info);

        mainBox.setPadding(new Insets(10, 10, 10, 10));
        mainBox.setSpacing(10);
        stage.setTitle("Series");
        stage.setScene(new Scene(mainBox, 700, 450));
        stage.show();
    }

    public void makeShow(Info info, TreeItem<String> parent) {
        TreeItem<String> scriptwriter = new TreeItem<>(info.getScriptwriter());
        TreeItem<String> seasons = new TreeItem<>(info.getSeason());
        TreeItem<String> genre = new TreeItem<>(info.getGenre());
        TreeItem<String> seasonsWatched = new TreeItem<>(info.getSeasonsWatched());
        TreeItem<String> title = new TreeItem<>(info.getTitle());
        title.getChildren().addAll(scriptwriter, seasons, genre, seasonsWatched);
        parent.getChildren().add(title);
    }

    public static void main(String[] args) {launch(args);}
}