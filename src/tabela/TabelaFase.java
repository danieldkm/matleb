package tabela;

import banco.Dao;
import controller.ModuloController;
import controller.ViewController;
import entidades.Fase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

/**
 *
 * @author Daniel K. Morita
 */
public class TabelaFase extends TableView {

    public static int numeroTabela = 0;
    public static boolean ok = false;

    public TabelaFase() {

//        TableColumn codFaseCol = new TableColumn("Cód Fase");
//        codFaseCol.setCellValueFactory(new PropertyValueFactory("idFase"));
//        codFaseCol.setMinWidth(150);
//        codFaseCol.setMaxWidth(150);
//        codFaseCol.setSortable(false);
        TableColumn nomeFaseCol = new TableColumn("Nome Fase");
        nomeFaseCol.setCellValueFactory(new PropertyValueFactory("nomeFase"));
        nomeFaseCol.setMinWidth(300);
        nomeFaseCol.setMaxWidth(300);
        nomeFaseCol.setSortable(false);

        ObservableList<Fase> lista = FXCollections.observableArrayList();
        setItems(lista);
        getColumns().addAll(nomeFaseCol);
        setMinSize(320, 400);
        setMaxSize(320, 400);

        setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
//                System.out.println("Detected");
                Dragboard db = startDragAndDrop(TransferMode.COPY_OR_MOVE);
                ClipboardContent content = new ClipboardContent();
//                content.putString("Drag Me!");
                content.putString(Integer.toString(getSelectionModel().getSelectedIndex()));
                db.setContent(content);
                me.consume();
            }
        });

        setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent de) {
//                System.out.println("Entered");
            }
        });

        setOnDragExited(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
//                System.out.println("Tabela fase: OnDragExited");
            }
        });

        setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
//                System.out.println("Over");
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                event.consume();
            }
        });

        setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
//                System.out.println("Dropped");
                Dragboard db = event.getDragboard();
                boolean success = false;
//                System.out.println("dropped evento aceito? " + event.isAccepted());
//                if (event.isAccepted()) {
                if (db.hasString()) {
                    if (numeroTabela == 0 && event.getSource() == ModuloController.tbFaseNova) {
                        for (int i = 0; i < ModuloController.lista.size(); i++) {
                            if (Integer.parseInt(db.getString()) == i) {
                                ModuloController.listaNova.add(ModuloController.lista.get(i));
                                ModuloController.tbFaseNova.setItems(ModuloController.listaNova);
                                ok = true;
                            }
                        }
                    } else if (numeroTabela == 1 && event.getSource() == ModuloController.tabelaFase) {
                        for (int i = 0; i < ModuloController.listaNova.size(); i++) {
                            if (Integer.parseInt(db.getString()) == i) {
                                ModuloController.lista.add(ModuloController.listaNova.get(i));
                                ok = true;
//                                    ModuloController.tabelaFase.setItems(ModuloController.lista);
                            }
                        }
                    }
                    success = true;
                }
//                }
                event.setDropCompleted(success);
//                System.out.println("DROPPED evento DROP ACEITO? " + event.isDropCompleted());
                event.consume();
            }
        });


        setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture ended */
//                System.out.print("Done");
                Dragboard db = event.getDragboard();
//                System.out.println(" evento aceito? " + event.isAccepted());
                if (event.isAccepted()) {
                    for (int i = 0; i < 10; i++) {
                        if (Integer.parseInt(db.getString()) == i) {
                            if (ok) {
                                if (numeroTabela == 0) {
                                    ModuloController.lista.remove(i); // remove da lista
                                } else if (numeroTabela == 1) {
                                    ModuloController.listaNova.remove(i); // remove da lista
                                }
                                ok = false;
                            }
                        }
                    }
                }
                System.err.println("db " + db.getString());
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.COPY) {
//                    source.setText(""); ori
//                    System.out.println("OK foi transferido");
                }
                event.consume();
//                System.out.println("done evento DROP ACEITO? " + event.isDropCompleted());
            }
        });

        // dnd stuff end
    }

    public static ObservableList<Fase> getListaFases() {
        ObservableList<Fase> lista = null;
        try {
            lista = FXCollections.observableArrayList(ViewController.getFases());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }
    private static int tempoProgessBar = 5;

    public static int getTempoProgessBar() {
        return tempoProgessBar;
    }

    public static void setTempoProgessBar(int tempoProgessBar) {
        TabelaFase.tempoProgessBar = tempoProgessBar;
    }

    /*classe responsavel por ativat o progress bar*/
    public static class TaskTableFase extends Task<ObservableList<Fase>> {

        @Override
        protected ObservableList<Fase> call() throws Exception {
            for (int i = 0; i < tempoProgessBar * 100; i++) {
                updateProgress(i, tempoProgessBar * 100);
                Thread.sleep(tempoProgessBar);
            }

            return getListaFases();
        }

        public static ObservableList<Fase> getListaFases() {
            Dao<Fase> dao = new Dao(Fase.class);

            ObservableList<Fase> lista = null;
            try {
                lista = FXCollections.observableArrayList(ViewController.getFases());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
//            ModuloController.lista = lista;
            return lista;
        }
    }
}
