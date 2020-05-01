package controller;

import banco.Dao;
import entidades.Aluno;
import entidades.Aluno_and_fases;
import entidades.Modulo_has_alunos;
import javafx.event.ActionEvent;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Dialogs.DialogOptions;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import tabela.TabelaAluno;
import view.ViewAluno;

/**
 * @author Administrador
 */
public class AlunoController extends ViewController {

    protected static final int LARGURA_WINDOW = 800;
    protected static final int ALTURA_WINDOW = 600;
    protected static TextField fieldLogin = new TextField();
    protected static PasswordField fieldSenha = new PasswordField();
    protected static TabelaAluno tabelaAluno = new TabelaAluno(fieldLogin, fieldSenha);
    protected static Label labelLogin = new Label("Digite o login: ");
    protected static Label labelSenha = new Label("Digite a senha: ");
    public static RadioButton rbAdmin;
    public static RadioButton rbAluno;
    public static ViewAluno va = null;
//
//    public static void getProgessTable() {
//        Task<ObservableList<Aluno>> task = new TabelaAluno.TaskTableAluno();
//        p.progressProperty().bind(task.progressProperty());
//        region.visibleProperty().bind(task.runningProperty());
//        p.visibleProperty().bind(task.runningProperty());
//        AlunoController.tabelaAluno.itemsProperty().bind(task.valueProperty());
//        new Thread(task).start();
//    }

    public static void actionButtonCadastrar(ActionEvent t) {
        try {
            if (validate()) {
                Aluno aluno = new Aluno();

                aluno.setIdAluno(0);
                aluno.setLoginAluno(fieldLogin.getText());
                aluno.setSenhaAluno(fieldSenha.getText());
                if (rbAdmin.isSelected()) {
                    aluno.setNivel(rbAdmin.getText());
                } else if (rbAluno.isSelected()) {
                    aluno.setNivel(rbAluno.getText());
                }

                Dao<Aluno> dao = new Dao(Aluno.class);
                dao.insert(aluno);
                atualizaTabela();
                changeButtonDisable("");
                clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Erro: ao cadastrar aluno. Metodo: actionButtonCadastrar. Classe AlunoController");
        }
    }

    public static void actionButtonAlterar(ActionEvent t) {
        try {
            if (validate()) {
                Aluno aluno = new Aluno();
                aluno.setIdAluno(TabelaAluno.getIdA());
                aluno.setLoginAluno(fieldLogin.getText());
                aluno.setSenhaAluno(fieldSenha.getText());
                if (rbAdmin.isSelected()) {
                    aluno.setNivel(rbAdmin.getText());
                } else if (rbAluno.isSelected()) {
                    aluno.setNivel(rbAluno.getText());
                }

                Dao<Aluno> dao = new Dao(Aluno.class);
                dao.update(aluno);
                atualizaTabela();
                changeButtonDisable("a");
                clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Erro: ao alterar os dados do aluno. Metodo: actionButtonAlterar. Classe AlunoController");
        }
    }

    public static void actionButtonDeletar(ActionEvent t) {
        Dao<Aluno> dao = new Dao(Aluno.class);
        boolean ok = true;
        Dialogs.DialogResponse n = Dialogs.showConfirmDialog(null, "Deseja realmente deletar?", "Este aluno será deletado, assim como todo o seu relacionamento com a Fase e o Modulo!!", "Deletar Aluno", DialogOptions.YES_NO_CANCEL);
        try {
            if (n.name().equals("YES")) {
                Dao<Aluno_and_fases> af = new Dao(Aluno_and_fases.class);
                Dao<Modulo_has_alunos> ma = new Dao(Modulo_has_alunos.class);
                af.delete(TabelaAluno.getIdA(),0);
                ma.delete(TabelaAluno.getIdA(),1);
                dao.delete(TabelaAluno.getIdA(),0);
                atualizaTabela();
                changeButtonDisable("a");
                clear();
            } else {
                ok = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ok = false;
            System.err.println("Erro: ao deletar o aluno. Metodo: actionButtonDeletar. Classe AlunoController");
        }
        if (ok) {
            Dialogs.showInformationDialog(null, "Deletado com sucesso!", "Aviso", "");
        }
    }

    public static void actionButtonNovo(ActionEvent t) {
        clear();
        fieldLogin.setStyle(null);
        fieldSenha.setStyle(null);
        changeButtonDisable("a");
    }

    public static void atualizaTabela() throws Exception {
        tabelaAluno.setItems(TabelaAluno.atualizarTabela());
    }

    public static void clear() {
        fieldLogin.clear();
        fieldSenha.clear();

        fieldLogin.setStyle(null);
        fieldSenha.setStyle(null);
    }

    public static void padraoCampos() {
        if (!fieldSenha.getText().equals("")) {
            fieldSenha.setStyle(null);
        }
        if (!fieldLogin.getText().equals("")) {
            fieldLogin.setStyle(null);
        }
    }

    public static boolean validate() {
        padraoCampos();
        if (fieldLogin.getText().equals("")) {
            validate(fieldLogin);
            fieldLogin.requestFocus();
            Dialogs.showWarningDialog(null, "Campo senha não foi preenchido\n "
                    + "Favor preencher corretamente", "Campo não preenchido", "");
            return false;
        } else if (fieldSenha.getText().equals("")) {
            validate(fieldSenha);
            fieldSenha.requestFocus();
            Dialogs.showWarningDialog(null, "Campo senha não foi preenchido\n "
                    + "Favor preencher corretamente", "Campo não preenchido", "");
            return false;
        } else {
            return true;
        }
    }
//    public static void getProgessTable(Object o, Object controller ) {
//        Task<ObservableList<Aluno>> task = new TabelaAluno.TaskTableAluno();
//        p.progressProperty().bind(task.progressProperty());
//        region.visibleProperty().bind(task.runningProperty());
//        p.visibleProperty().bind(task.runningProperty());
//        AlunoController.tabelaAluno.itemsProperty().bind(task.valueProperty());
//        new Thread(task).start();
//    }
}
