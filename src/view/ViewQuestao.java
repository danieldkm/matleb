package view;

import controller.QuestaoController;
import entidades.Fase;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import matleb.Config;
import tabela.TabelaQuestao;

/**
 * @author Daniel K. Morita
 */
public class ViewQuestao extends QuestaoController {

    ObservableList<Node> screen = getContentPane().getChildren();

    ViewQuestao(String content) {
        getStylesheets().add("css/Buttons.css");
        actionFechar();
        setTitle(content);
        setLayoutX((Config.SCREENW * 0.5) - (LARGURA_WINDOW * 0.5));
        setLayoutY((Config.SCREENH * 0.5) - (ALTURA_WINDOW * 0.5));
        getLeftIcons().add(new CloseIcon(this));
        getRightIcons().add(new MinimizeIcon(this));
        Group grupo = new Group();
        grupo.getChildren().addAll(todasAsLinhasComTabela());
        screen.addAll(grupo);
        setPrefSize(LARGURA_WINDOW, ALTURA_WINDOW);
    }

    private void actionFechar() {
        setOnCloseAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                vq = null;
                tabelaQuestao.setItems(TabelaQuestao.atualizarTabela(0));
            }
        });
    }

    private HBox todasAsLinhasComTabela() {
        HBox h = new HBox(50);
        h.getChildren().addAll(todasAsLinhas(), getTabela(), getVBoxAA());
        return h;
    }

//    private Group getProgress() {
//        Group g = new Group();
//        p.setTranslateX(TabelaQuestao.getAltura() * 0.15);
//        p.setTranslateY(TabelaQuestao.getLargura() * 0.4);
//        g.getChildren().addAll(getTabela(), region, p);
//        return g;
//    }
    private VBox todasAsLinhas() {
        VBox v = new VBox(15);
        v.getChildren().addAll(linhaUm(), linhaDois(), linhaTres(), btNovo());
        return v;
    }

    private Button btNovo() {
        btnNovo = new Button("Limpar");
        btnNovo.setId(idStyleSheet);
        btnNovo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                limparCampos();
                changeButtonDisable("");
            }
        });
        return btnNovo;
    }

    /**
     * Linha Tres
     */
    private HBox linhaTres() {
        HBox h = new HBox(15);
        h.getChildren().addAll(btnCadastrar(), btnDeletar(), btnAlterar());
        return h;
    }

    private Button btnAlterar() {
        btnAlterar = new Button("Alterar");
        btnAlterar.setId(idStyleSheet);
        btnAlterar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                actionButtonAlterar(t);
            }
        });
        btnAlterar.setDisable(true);
        return btnAlterar;
    }

    private Button btnDeletar() {
        btnDeletar = new Button("Deletar");
        btnDeletar.setId(idStyleSheet);
        btnDeletar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                actionButtonDeletar(t);
                padraoCampos();
            }
        });
        btnDeletar.setDisable(true);
        return btnDeletar;
    }

    
    
    private Button btnCadastrar() {
        btnCadastrar = new Button("Cadastrar");
        btnCadastrar.setId(idStyleSheet);
        btnCadastrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                actionButtonCadastrar(t);
            }
        });
        return btnCadastrar;
    }

    /**
     * Linha Dois***************************************************************
     */
    private HBox linhaDois() {
        HBox h = new HBox(15);
        h.getChildren().addAll(colunaDoisLinhaDois());
        return h;
    }

    /**
     * Coluna DOIS Linha dois
     */
    private VBox colunaDoisLinhaDois() {
        VBox v2 = new VBox(5);
        v2.setMinWidth(200);
        v2.getChildren().addAll(getLblQuestao(), getLinhaQuestao());
        return v2;
    }

    private Label getLblQuestao() {
        Label lblQuestao = new Label("Questão");
        return lblQuestao;
    }

    private HBox getLinhaQuestao() {
        HBox h = new HBox(10);
        h.getChildren().addAll(txtDigUm(), cbQuestao(), txtDigDois(), getIgual(), getTxtResultado());
        return h;
    }

    private TextField getTxtResultado() {
        txtDigResult = setTextFieldDigit();
        txtDigResult.setEditable(true);
        txtDigResult.setMinWidth(100);
        txtDigResult.setMaxWidth(100);
        txtDigResult.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                btnCadastrar.arm();
                btnCadastrar.fire();

            }
        });
        return txtDigResult;
    }

    private Label getIgual() {
        Label igual = new Label(" = ");
        return igual;
    }

    private ComboBox cbQuestao() {
        cbQuestao = new ComboBox<String>();
        ObservableList stringQuestoes = FXCollections.observableArrayList(
                "", "+", "-", "÷", "x");
        cbQuestao.setItems(stringQuestoes);
        cbQuestao.setId("questao");
        cbQuestao.setMinWidth(50);
        cbQuestao.setMaxWidth(50);
        cbQuestao.setPromptText("?");
        cbQuestao.setEditable(false);
        return cbQuestao;
    }

    private TextField txtDigDois() {
        txtDigDois = setTextFieldDigit();
        txtDigDois.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                maxLength = 5;
                verificaQtdDigit(oldValue, newValue, txtDigDois);
            }
        });
//        QuestoesController.txtDigDois.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            public void handle(KeyEvent ke) {
//                QuestoesController.verificaQtdDigit(ke);
//            }
//        });
        txtDigDois.setEditable(true);
        txtDigDois.setMinWidth(70);
        txtDigDois.setMaxWidth(70);
        return txtDigDois;
    }

    private TextField txtDigUm() {
        txtDigUm = setTextFieldDigit();
        txtDigUm.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                maxLength = 5;
                verificaQtdDigit(oldValue, newValue, txtDigUm);
            }
        });
        txtDigUm.setEditable(true);
        txtDigUm.setMinWidth(70);
        txtDigUm.setMaxWidth(70);
        return txtDigUm;
    }

    /**
     * Primeira Linha***********************************************************
     */
    private HBox linhaUm() {
        HBox h1 = new HBox(15);
        h1.getChildren().addAll(colunaUmLinhaUm(), colunaTresLinhaUm());
        return h1;
    }

    /**
     * Coluna três linha Um
     */
    private VBox colunaTresLinhaUm() {
        VBox v2 = new VBox(10);
        v2.getChildren().addAll(getLblQtdQuestaoCadastrada(), getTxtQtdQuestaoCadastrada());
        return v2;
    }

    private Label getLblQtdQuestaoCadastrada() {
        Label lblNQuestoes = new Label("Qtd. Quest. Cadastradas");
        lblNQuestoes.setMinWidth(100);
//        lblNQuestoes.setMaxWidth(100);
        return lblNQuestoes;
    }

    private TextField getTxtQtdQuestaoCadastrada() {
        txtQtdQuestaoCadastrada = new TextField();
        txtQtdQuestaoCadastrada.setMaxWidth(80);
        txtQtdQuestaoCadastrada.setEditable(false);
        return txtQtdQuestaoCadastrada;
    }

    /**
     * Coluna Um linha Um
     */
    private VBox colunaUmLinhaUm() {
        VBox v1 = new VBox(10);
        v1.getChildren().addAll(getLblFase(), getComboBox());
        return v1;
    }

    private Label getLblFase() {
        Label lblFase = new Label("Fase:");
        return lblFase;
    }

    private ComboBox getComboBox() {
        comboBox = new ComboBox<String>();
//        comboBox.setId("fases");
        comboBox.setMinWidth(150);
        comboBox.setPromptText("Fase");
        comboBox.setVisibleRowCount(10);
        final ArrayList<Fase> fases = getFases();
        ObservableList strings = FXCollections.observableArrayList("");
        if (!fases.isEmpty()) {
            for (Fase fase : fases) {
                if (!strings.isEmpty()) {
                    strings.add(fase.getNomeFase().trim());
                }

            }
        }
        comboBox.setItems(strings);
        comboBox.setOnAction(new EventHandler<ActionEvent>() {
            @ Override
            public void handle(ActionEvent e) {
                atualizaComboBox();
                limparCampos();
            }
        });
        return comboBox;
    }

    /**
     * Tabela
     */
    private TableView getTabela() {
        tabelaQuestao.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                setOnMouseTabela(me);
            }

        });
        return tabelaQuestao;
    }

    private VBox getVBoxAA() {
        VBox v = new VBox(10);
        v.getChildren().addAll(getPolygonAcima(), getPolygonAbaixo());
        return v;
    }

    private Polygon getPolygonAcima() {
        Polygon p = new Polygon(new double[]{
            -20, 40,
            20, 40,
            0, 0,});
        p.setFill(Color.AQUA);
        p.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                setOnMouseActionPolygonAcima(t);
            }
        });
        return p;
    }

    private Polygon getPolygonAbaixo() {
        Polygon p = new Polygon(new double[]{
            -20, 40,
            20, 40,
            0, 0,});
        p.setRotate(180);
        p.setFill(Color.AQUA);
        p.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                setOnMouseActionPolygonAbaixo(t);
            }
        });
        return p;
    }
}
