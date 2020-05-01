package tabela;

import banco.Dao;
import entidades.Aluno;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import controller.AlunoController;
import javafx.concurrent.Task;

public class TabelaAluno extends TableView {

    private static int idAluno;

    public TabelaAluno(final TextField login, final TextField senha) {

        TableColumn loginCol = new TableColumn("Login");
        loginCol.setCellValueFactory(new PropertyValueFactory("loginAluno"));
        loginCol.setMinWidth(225);
        loginCol.setMaxWidth(225);
        TableColumn senhaCol = new TableColumn("Senha");
        senhaCol.setCellValueFactory(new PropertyValueFactory("senhaAluno"));
        senhaCol.setMinWidth(225);
        senhaCol.setMaxWidth(225);
        TableColumn nivelCol = new TableColumn("Nível");
        nivelCol.setCellValueFactory(new PropertyValueFactory("nivel"));
        nivelCol.setMinWidth(225);
        nivelCol.setMaxWidth(225);

//        Dao<Aluno> dao = new Dao(Aluno.class);
//        ObservableList<Aluno> lista = null;
//        try {
//            lista = FXCollections.observableArrayList(dao.select());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        setItems(lista);

        setItems(atualizarTabela());

        getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Integer> change) {
                if (!change.getList().isEmpty()) {
                    idAluno = ((Aluno) getItems().get(change.getList().get(0))).getIdAluno();
                    login.setText("" + ((Aluno) getItems().get(change.getList().get(0))).getLoginAluno());
                    senha.setText("" + ((Aluno) getItems().get(change.getList().get(0))).getSenhaAluno());
                    String nivel = ((Aluno) getItems().get(change.getList().get(0))).getNivel();
                    if (nivel.equals("Administrador")) {
                        AlunoController.rbAdmin.setSelected(true);
                    } else if (nivel.equals("Aluno")) {
                        AlunoController.rbAluno.setSelected(true);
                    }
                    AlunoController.changeButtonDisable("cadastrar");
                }
                AlunoController.padraoCampos();
            }
        });
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        getColumns().addAll(loginCol, senhaCol, nivelCol);
        setMinSize(695, 400);
        setMaxSize(695, 400);
    }

    public static ObservableList<Aluno> atualizarTabela() {
        Dao<Aluno> dao = new Dao(Aluno.class);
        ObservableList<Aluno> lista = null;
        try {
            lista = FXCollections.observableArrayList(dao.select());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lista;

    }

    public static int getIdA() {
        return idAluno;
    }
    private static int tempoProgessBar = 1;

    public static int getTempoProgessBar() {
        return tempoProgessBar;
    }

    public static void setTempoProgessBar(int tempoProgessBar) {
        TabelaAluno.tempoProgessBar = tempoProgessBar;
    }

    public static class TaskTableAluno extends Task<ObservableList<Aluno>> {

        @Override
        protected ObservableList<Aluno> call() throws Exception {
            for (int i = 0; i < tempoProgessBar * 100; i++) {
                updateProgress(i, tempoProgessBar * 100);
                Thread.sleep(tempoProgessBar);
            }

            Dao<Aluno> dao = new Dao(Aluno.class);
            ObservableList<Aluno> lista = null;
            try {
                lista = FXCollections.observableArrayList(dao.select());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return lista;
        }
    }
}