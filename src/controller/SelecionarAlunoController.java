package controller;

import banco.Dao;
import entidades.Aluno;
import entidades.Modulo;
import entidades.Modulo_has_alunos;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.input.MouseEvent;
import tabela.TabelaSelecionarAluno;
import view.FXWindows;
import view.ViewSelecionarAluno;
import view.ViewVisualizarAluno;

/**
 *
 * @author Daniel K. Morita
 */
public class SelecionarAlunoController extends ViewController {

    public static ViewSelecionarAluno vsa = null;
    public static TabelaSelecionarAluno tbSelecionarAluno;
    public static TabelaSelecionarAluno tbAlunoSelecionado;
    protected static Button btnVisualizarAluno;
    protected Handler handler = new Handler();
    public static ObservableList<Aluno> listaNova = FXCollections.observableArrayList();
    public static ObservableList<Aluno> lista = TabelaSelecionarAluno.atualizarTabela();
    private int idModulo;

    protected void setOnMousePolygon2(MouseEvent t) {
        int tamanho = lista.size();
        for (int i = 0; i < tamanho; i++) {
            listaNova.add(lista.get(0));
            lista.remove(0);
        }
        tbAlunoSelecionado.setItems(listaNova);
    }

    protected void setOnMousePolygon1(MouseEvent t) {
        int tamanho = listaNova.size();
        for (int i = 0; i < tamanho; i++) {
            lista.add(listaNova.get(0));
            listaNova.remove(0);
        }
        tbSelecionarAluno.setItems(lista);
    }

    public void setOnActionBtnCadastrar(ActionEvent t) {
        try {
            Dao<Modulo_has_alunos> daoModuloAlunos = new Dao(Modulo_has_alunos.class);
            verificarCbSelecionado();
            for (int i = 0; i < listaNova.size(); i++) {
                Modulo_has_alunos m = new Modulo_has_alunos();
                m.setFkIdModulo(idModulo);
                m.setFkIdAluno(listaNova.get(i).getIdAluno());
                daoModuloAlunos.insert(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro: ao gravar alunos no modulo. Metodo: setOnActionBtnCadastrar. Classe: SelecionarAlunoController");
        }
    }

    private static void resetarTabela() {
        try {
            lista = TabelaSelecionarAluno.atualizarTabela();
            tbSelecionarAluno.setItems(lista);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro: ao atualizar a tabela. metodo: setOnActionBtnLimpar, classe: SelecionarAlunoController");
        }
        int tamanho = listaNova.size();
        // reseta a tabela de modulo - inicio -
        for (int i = (tamanho - 1); i >= 0; i--) {
            listaNova.remove(i);
        }
    }

    public static void setOnActionBtnLimpar(ActionEvent t) {
        try {
            if (cbModulos != null) {
                ModuloController.getPopularcbModulos();
                cbModulos.getSelectionModel().selectFirst();
            }
            resetarTabela();
            btnDeletar.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro: ao limpar os campos -> metodo: setOnActionBtnLimpar -> classe: SelecionarAlunoController");
        }
    }

    private boolean verificarCbSelecionado() {
        ArrayList<Modulo> modulos = ViewController.getModulos();
        for (Modulo modulo : modulos) {
            if (modulo.getNomeModulo().equals(SelecionarAlunoController.cbModulos.getSelectionModel().getSelectedItem())) {
                idModulo = modulo.getIdModulo();
                return true;
            }
        }
        return false;
    }

    private void setOnActionBtnDeletar(ActionEvent t) {
        try {
            verificarCbSelecionado();
            Dao<Modulo_has_alunos> daoModuloAlunos = new Dao(Modulo_has_alunos.class);
            daoModuloAlunos.delete(idModulo, 0);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro: ao deletar -> metodo: setOnActionBtnDeletar -> classe: SelecionarAlunoController");
        }
    }

    private void setActionCbModulo() {
        resetarTabela();
        verificarCbSelecionado();
        ArrayList<Modulo_has_alunos> mha = getModuloWithAlunos();
        ArrayList<Aluno> a = getAlunos();
        for (Modulo_has_alunos modulo_has_alunos : mha) {
            if (modulo_has_alunos.getFkIdModulo() == idModulo) {
                for (Aluno aluno : a) {
                    if (modulo_has_alunos.getFkIdAluno() == aluno.getIdAluno()) {
                        for (int i = 0; i < lista.size(); i++) {
                            if (lista.get(i).getIdAluno() == aluno.getIdAluno()) {
                                lista.remove(i);
                            }
                        }
                        listaNova.add(aluno);
                    }
                }
            }
        }
        tbAlunoSelecionado.setItems(listaNova);
    }

    private void setOnActionBtnVisualizarAluno(ActionEvent t) {
        try {
            if (VisualizarAlunoController.vva == null) {
                VisualizarAlunoController.vva = new ViewVisualizarAluno("Visualizar por Aluno");
                FXWindows.setRoot(VisualizarAlunoController.vva);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class Handler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent t) {
            if (t.getSource() == cbModulos) {
                btnDeletar.setDisable(false);
                setActionCbModulo();
            } else if (t.getSource() == btnDeletar) {
                setOnActionBtnDeletar(t);
                setChangeBtnAndFireClean();
            } else if (t.getSource() == btnCadastrar) {
                if (verificarCbSelecionado()) {
                    setOnActionBtnDeletar(t);
                    setOnActionBtnCadastrar(t);
                    setChangeBtnAndFireClean();
                    Dialogs.showInformationDialog(null, "Salvo com sucesso!", "Aviso!", "");
                } else {
                    Dialogs.showInformationDialog(null, "Selecione um Jogo antes de salvar", "Aviso!", "");
                }
            } else if (t.getSource() == btnNovo) {
                setOnActionBtnLimpar(t);
            } else if (t.getSource() == btnVisualizarAluno) {
                setOnActionBtnVisualizarAluno(t);
            }

        }

        private void setChangeBtnAndFireClean() {
            btnNovo.arm();
            btnNovo.fire();
        }
    }
}
