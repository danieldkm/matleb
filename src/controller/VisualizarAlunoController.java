package controller;

import entidades.Aluno;
import entidades.Modulo;
import entidades.Modulo_has_alunos;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import tabela.TabelaModulo;
import view.ViewVisualizarAluno;

public class VisualizarAlunoController extends ViewController {

    public static ViewVisualizarAluno vva = null;
    protected Handler handler = new Handler();
    protected TabelaModulo tabelaModulo;
    public static ObservableList<Modulo> lista = FXCollections.observableArrayList(); //= TabelaSelecionarAluno.atualizarTabela();
    protected int idAluno;
    protected static final int LARGURA_WINDOW = 600;
    protected static final int ALTURA_WINDOW = 600;

    private boolean verificarCbSelecionado() {
        ArrayList<Aluno> alunos = ViewController.getAlunos();
        for (Aluno aluno : alunos) {
            if (aluno.getLoginAluno().equals(cbAlunos.getSelectionModel().getSelectedItem())) {
                idAluno = aluno.getIdAluno();
                return true;
            }
        }
        return false;
    }

    private void resetarTabela() {
        try {
            int tamanho = lista.size();
            for (int i = (tamanho - 1); i >= 0; i--) {
                lista.remove(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setActionCbModulo() {
        resetarTabela();
        if (verificarCbSelecionado()) {
            ArrayList<Modulo_has_alunos> moduloAlunos = getModuloWithAlunos();
            ArrayList<Modulo> m = getModulos();
            for (Modulo_has_alunos modulo_has_alunos : moduloAlunos) {
                if (modulo_has_alunos.getFkIdAluno() == idAluno) {
                    for (Modulo modulo : m) {
                        if (modulo.getIdModulo() == modulo_has_alunos.getFkIdModulo()) {
                            lista.add(modulo);
                        }
                    }
                }
            }
            tabelaModulo.setItems(lista);
        }
    }

    public class Handler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent t) {
            if (t.getSource() == cbAlunos) {
                btnDeletar.setDisable(false);
                setActionCbModulo();
            }
        }
    }
}