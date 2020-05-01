package view;

import controller.AlunoPontuacaoController;
import static controller.AlunoPontuacaoController.cbNomeALuno;
import controller.QuestaoController;
import entidades.Aluno;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import matleb.Config;
import tabela.TabelaAlunoPontuacao;

public class ViewAlunoPontuacao extends AlunoPontuacaoController {

    public ViewAlunoPontuacao(String content) {
        getStylesheets().add("css/Buttons.css");
        setTitle(content);
        setPrefSize(LARGURA_WINDOW, ALTURA_WINDOW);
        setLayoutX((Config.SCREENW * 0.5) - (LARGURA_WINDOW * 0.5));
        setLayoutY((Config.SCREENH * 0.5) - (ALTURA_WINDOW * 0.5));
        getLeftIcons().add(new CloseIcon(this));
        getRightIcons().add(new MinimizeIcon(this));
        Group grupo = new Group();
        grupo.getChildren().addAll(getGeral());
        getContentPane().getChildren().add(grupo);
        actionFechar();

        if (Config.ALUNO_LOGADO.getNivel().equals("Aluno")) {
            lista = TabelaAlunoPontuacao.atualizarTabela(Config.ALUNO_LOGADO.getIdAluno());
            getTabela().setItems(lista);
        }
    }

    private void actionFechar() {
        setOnCloseAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                vap = null;
            }
        });
    }

    /*-----------------------------Geral Inicio-------------------------------*/
    private VBox getGeral() {
        VBox v = new VBox(10);
        v.getChildren().addAll(getHBoxGrupo1(), getTabela());
        return v;
    }

    /*-----------------------------Geral Fim----------------------------------*/
    /*-----------------------------Inicio-------------------------------------*/
    private HBox getHBoxGrupo1() {
        HBox h = new HBox(10);
        h.getChildren().addAll(getLabelBuscar(), getCbNomeAluno());
        return h;
    }

    private Label getLabelBuscar() {
        if (Config.ALUNO_LOGADO.getNivel().equals("Aluno")) {
            return new Label("");
        }
        return new Label("Selecionar aluno: ");
    }

    private ComboBox getCbNomeAluno() {
        cbNomeALuno = new ComboBox();
        cbNomeALuno.setMinWidth(200);
        if (Config.ALUNO_LOGADO.getNivel().equals("Aluno")) {
            cbNomeALuno.setVisible(false);
        }
        final ArrayList<Aluno> alunos = QuestaoController.getAlunos();
        ObservableList strings = FXCollections.observableArrayList("");
        strings.add("Todos");
        if (!alunos.isEmpty()) {
            for (Aluno aluno : alunos) {
                if (!strings.isEmpty()) {
                    strings.add(aluno.getLoginAluno().trim());
                } else {
                }

            }
        }
        cbNomeALuno.setItems(strings);
        cbNomeALuno.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                setOnActionCbNomeAluno();
            }
        });
        return cbNomeALuno;
    }

    /*-----------------------------Fim----------------------------------------*/
    /*-----------------------------Inicio-------------------------------------*/
    private TableView getTabela() {
        getTabelaAlunoPontuacao().setItems(lista);
        return getTabelaAlunoPontuacao();
    }
    /*-----------------------------Fim----------------------------------------*/
}