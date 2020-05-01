package controller;

import entidades.Aluno;
import entidades.Aluno_and_fases;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import tabela.TabelaAlunoPontuacao;
import view.ViewAlunoPontuacao;

public class AlunoPontuacaoController extends ViewController {

    public static ComboBox cbNomeALuno;
    private static TabelaAlunoPontuacao tabelaAlunoPontuacao = new TabelaAlunoPontuacao();
    public static ViewAlunoPontuacao vap = null;
    public static ObservableList<TabelaAlunoPontuacao.AlunoPontuacao> lista;

    public static TabelaAlunoPontuacao getTabelaAlunoPontuacao() {
        return tabelaAlunoPontuacao;
    }

    public static void setTabelaAlunoPontuacao(TabelaAlunoPontuacao tabelaAlunoPontuacao) {
        AlunoPontuacaoController.tabelaAlunoPontuacao = tabelaAlunoPontuacao;
    }

    public static void setOnActionCbNomeAluno() {
        boolean existe = false;
        boolean achou = false;
        if (((String) cbNomeALuno.getSelectionModel().getSelectedItem()).equals("Todos")) {
            existe = true;
            lista = TabelaAlunoPontuacao.atualizarTabela(-1);
        } else {
            lista = TabelaAlunoPontuacao.atualizarTabela(0);            
            ArrayList<Aluno> alunos = getAlunos();
            for (int i = 0; i < lista.size(); i++) {
                if (((String) cbNomeALuno.getSelectionModel().getSelectedItem()).equals(lista.get(i).getLoginAluno())) {
                    existe = true;
                    for (int j = 0; j < alunos.size(); j++) {                        
                        if (alunos.get(j).getLoginAluno().equals(lista.get(i).getLoginAluno())) {
                            ArrayList<Aluno_and_fases> afs = ViewController.getAlunoAndFasesById(alunos.get(j).getIdAluno(), 0);
                            for (int k = 0; k < afs.size(); k++) {
                                if (afs.get(k).getFkIdAluno() == alunos.get(j).getIdAluno()) {
                                    lista = TabelaAlunoPontuacao.atualizarTabela(alunos.get(j).getIdAluno());
                                    achou = true;
                                    break;
                                } else {
//                                    System.out.println("nao existe nada");
                                }
                            }
                        } else {
//                            System.out.println("total!=lista   " + alunos.get(j).getLoginAluno() + " != " + lista.get(i).getLoginAluno());
                        }
                        if (achou) {
                            break;
                        }
                    }
                } else {
//                    System.out.println("selected!=lista  " + (String) cbNomeALuno.getSelectionModel().getSelectedItem() + " != " + lista.get(i).getLoginAluno());
                }
                if (existe) {
                    break;
                }
            }
        }
        if (existe) {
            tabelaAlunoPontuacao.setItems(lista);
        } else {
            tabelaAlunoPontuacao.setItems(null);            
            Dialogs.showInformationDialog(null, "Esse aluno não contém nenhum score.", "Busca de Alunos", "");
        }
    }
}