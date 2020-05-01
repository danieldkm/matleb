package view;

import banco.Dao;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import matleb.Config;
import controller.AlunoController;
import static controller.ViewController.verificaQtdDigit;
import entidades.Aluno;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class ViewAluno extends AlunoController {

    public ViewAluno(String content) {
//        region = new Region();
//        region.setStyle("-fx-background-color: rgba(0,0,0,0.4)");
//        p = getStyleProgressBar();

        getStylesheets().add("css/Buttons.css");
        actionFechar();
        setTitle(content);
        setPrefSize(LARGURA_WINDOW, ALTURA_WINDOW);
        setLayoutX((Config.SCREENW * 0.5) - (LARGURA_WINDOW * 0.5));
        setLayoutY((Config.SCREENH * 0.5) - (ALTURA_WINDOW * 0.5));
        getLeftIcons().add(new CloseIcon(this));
        getRightIcons().add(new MinimizeIcon(this));
        Group grupo = new Group();
        grupo.getChildren().addAll(geralTela());
        getContentPane().getChildren().add(grupo);
        changeButtonDisable("a");
        
//        TabelaAluno.setTempoProgessBar(5);
//        getProgessTable();
    }

    private void actionFechar() {
        setOnCloseAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
//                Config.mataProcesso();
                va = null;
                System.gc();
            }
        });
    }

    /**
     * **********************GERAL**********************************************
     */
    private VBox geralTela() {
        VBox geral = new VBox(20);
        geral.getChildren().addAll(linhaCampos(), getTabela(), linhaButton());
        return geral;
    }

    private Group getTabelaProgress() {
        Group g = new Group();
//        p.setTranslateX(700 * 0.27);
//        p.setTranslateY(600 * 0.4);
        g.getChildren().addAll(getTabela()/*, region, p*/);
        return g;
    }

    private TableView getTabela() {
        return tabelaAluno;
    }

    /**
     * **********************linha*campos***************************************
     */
    private TextField getFieldLogin() {
        fieldLogin.setMaxWidth(200);
        fieldLogin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                maxLength = 15;
                verificaQtdDigit(oldValue, newValue, fieldLogin);
            }
        });
        return fieldLogin;
    }

    private PasswordField getFieldSenha() {
        fieldSenha.setMaxWidth(200);
        fieldSenha.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                maxLength = 16;
                verificaQtdDigit(oldValue, newValue, fieldSenha);
            }
        });
        fieldSenha.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (!btnCadastrar.isDisable()) {
                    btnCadastrar.arm();
                    btnCadastrar.fire();
                } else if (!btnAlterar.isDisable()) {
                    btnAlterar.arm();
                    btnAlterar.fire();
                }
            }
        });
        return fieldSenha;
    }

    private HBox linhaCampos() {
        HBox v = new HBox(10);
        v.getChildren().addAll(getLabelLogin(), getFieldLogin(), getLabelSenha(), getFieldSenha());
        return v;
    }

    private Label getLabelLogin() {
        return labelLogin;
    }

    private Label getLabelSenha() {
        return labelSenha;
    }

    /**
     * **********************linha*botão****************************************
     */
    private HBox linhaButton() {
        HBox h = new HBox(10);
        h.getChildren().addAll(getBtnCadastrar(), getBtnAlterar(), getBtnDeletar(), getBtnNovo(), getRBAdmin(), getRBAluno());
        return h;
    }
    ToggleGroup tgRadioButtons = new ToggleGroup();

    private RadioButton getRBAluno() {
        rbAluno = new RadioButton("Aluno");
        rbAluno.setToggleGroup(tgRadioButtons);
        rbAluno.setSelected(true);
        return rbAluno;
    }

    private RadioButton getRBAdmin() {
        rbAdmin = new RadioButton("Administrador");;
        rbAdmin.setToggleGroup(tgRadioButtons);
        return rbAdmin;
    }

    private Button getBtnCadastrar() {
        btnCadastrar = new Button("Cadastrar");
        btnCadastrar.setId(idStyleSheet);
        btnCadastrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                actionButtonCadastrar(t);
                fieldLogin.requestFocus();
            }
        });
        return btnCadastrar;
    }

    private Button getBtnDeletar() {
        btnDeletar = new Button("Deletar");
        btnDeletar.setId(idStyleSheet);
        btnDeletar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                actionButtonDeletar(t);
            }
        });
        return btnDeletar;
    }

    private Button getBtnAlterar() {
        btnAlterar = new Button("Alterar");
        btnAlterar.setId(idStyleSheet);
        btnAlterar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                actionButtonAlterar(t);
            }
        });
        return btnAlterar;
    }

    private Button getBtnNovo() {
        btnNovo = new Button("Limpar");
        btnNovo.setId(idStyleSheet);
        btnNovo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                actionButtonNovo(t);
            }
        });
        return btnNovo;
    }

    public class TaskTableAluno extends Task<ObservableList<Aluno>> {

        @Override
        protected ObservableList<Aluno> call() throws Exception {
            for (int i = 0; i < 100; i++) {
                updateProgress(i, 100);
                Thread.sleep(5);
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