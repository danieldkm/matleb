package tabela;

import banco.Dao;
import entidades.Modulo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TabelaModulo extends TableView {

    public TabelaModulo() {

        TableColumn nomeCol = new TableColumn("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory("nomeModulo"));
        nomeCol.setMinWidth(150);
        nomeCol.setMaxWidth(150);
        TableColumn codCol = new TableColumn("Código");
        codCol.setCellValueFactory(new PropertyValueFactory("idModulo"));
        codCol.setMinWidth(150);
        codCol.setMaxWidth(150);
        getColumns().addAll(nomeCol, codCol);

        setMaxSize(320, 400);
        setMinSize(320, 400);
    }
    private static int tempoProgessBar = 5;

    public static int getTempoProgessBar() {
        return tempoProgessBar;
    }

    public static void setTempoProgessBar(int _tempoProgessBar) {
        tempoProgessBar = _tempoProgessBar;
    }

    public static class TaskTableModulo extends Task<ObservableList<Modulo>> {

        @Override
        protected ObservableList<Modulo> call() throws Exception {
            for (int i = 0; i < tempoProgessBar * 100; i++) {
                updateProgress(i, tempoProgessBar * 100);
                Thread.sleep(tempoProgessBar);
            }

            Dao<Modulo> dao = new Dao(Modulo.class);

            ObservableList<Modulo> lista = null;
            try {
                lista = FXCollections.observableArrayList(dao.select());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return lista;
        }
    }
}