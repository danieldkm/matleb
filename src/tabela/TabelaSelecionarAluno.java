package tabela;

import banco.Dao;
import controller.SelecionarAlunoController;
import entidades.Aluno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import static tabela.TabelaFase.ok;

public class TabelaSelecionarAluno extends TableView {

    private static int idAluno;
    public static int numeroTabela = 0;

    public TabelaSelecionarAluno() {

        TableColumn loginCol = new TableColumn("Login");
        loginCol.setCellValueFactory(new PropertyValueFactory("loginAluno"));
        loginCol.setMinWidth(150);
        TableColumn nivelCol = new TableColumn("Nível");
        nivelCol.setCellValueFactory(new PropertyValueFactory("nivel"));
        nivelCol.setMinWidth(150);

        getColumns().addAll(loginCol, nivelCol);
        setMinSize(320, 400);
        setMaxSize(320, 400);

        setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                Dragboard db = startDragAndDrop(TransferMode.COPY_OR_MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(Integer.toString(getSelectionModel().getSelectedIndex()));
                db.setContent(content);
                me.consume();
            }
        });

        setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                event.consume();
            }
        });

        setOnDragDropped(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    if (numeroTabela == 0 && event.getSource() == SelecionarAlunoController.tbAlunoSelecionado) {
                        for (int i = 0; i < SelecionarAlunoController.lista.size(); i++) {
                            if (Integer.parseInt(db.getString()) == i) {
                                SelecionarAlunoController.listaNova.add(SelecionarAlunoController.lista.get(i));
                                SelecionarAlunoController.tbAlunoSelecionado.setItems(SelecionarAlunoController.listaNova);
                                ok = true;
                            }
                        }
                    } else if (numeroTabela == 1 && event.getSource() == SelecionarAlunoController.tbSelecionarAluno) {
                        for (int i = 0; i < SelecionarAlunoController.listaNova.size(); i++) {
                            if (Integer.parseInt(db.getString()) == i) {
                                SelecionarAlunoController.lista.add(SelecionarAlunoController.listaNova.get(i));
                                ok = true;
                            }
                        }
                    }
                    success = true;
                }
//                }
                event.setDropCompleted(success);
                event.consume();
            }
        });


        setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (event.isAccepted()) {
                    for (int i = 0; i < 10; i++) {
                        if (Integer.parseInt(db.getString()) == i) {
                            if (ok) {
                                if (numeroTabela == 0) {
                                    SelecionarAlunoController.lista.remove(i); // remove da lista
                                } else if (numeroTabela == 1) {
                                    SelecionarAlunoController.listaNova.remove(i); // remove da lista
                                }
                                ok = false;
                            }
                        }
                    }
                }
                System.err.println("db " + db.getString());
                event.consume();
            }
        });
    }

    public static ObservableList<Aluno> atualizarTabela() {
        Dao<Aluno> dao = new Dao(Aluno.class);
        ObservableList<Aluno> lista = null;
        try {
            lista = FXCollections.observableArrayList(dao.select());
            for (int i = 0; i < lista.size(); i++) {
                if(lista.get(i).getNivel().equals("Administrador")){
                    lista.remove(i);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public static int getIdA() {
        return idAluno;
    }
}