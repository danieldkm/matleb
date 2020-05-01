package view;

import controller.FaseController;
import entidades.Fase;
import controller.ViewController;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import matleb.Config;
import tabela.TabelaFase;

public class ViewFase extends FaseController {

    ObservableList<Node> screen = getContentPane().getChildren();

    public ViewFase(String content) {
        getStylesheets().add("css/Buttons.css");
        setTitle(content);
        setMinSize(LARGURA_WINDOW, ALTURA_WINDOW);
        setMaxSize(LARGURA_WINDOW, ALTURA_WINDOW);
        setPrefSize(LARGURA_WINDOW, ALTURA_WINDOW);
        setLayoutX((Config.SCREENW * 0.5) - (LARGURA_WINDOW * 0.5));
        setLayoutY((Config.SCREENH * 0.5) - (ALTURA_WINDOW * 0.5));
        getLeftIcons().add(new CloseIcon(this));
        getRightIcons().add(new MinimizeIcon(this));
        Group grupo = new Group();
        grupo.getChildren().addAll(vbox());
        screen.addAll(grupo);
        actionFechar();
    }

    /**
     * Método para fazer uma ação antes de fechar a janela, o atributo vf recebe
     * valor nulo
     */
    private void actionFechar() {
        setOnCloseAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                vf = null;
            }
        });
    }

    /**
     * Geral -> esse vertical box recebe todos os demais "BOXs"
     *
     * @return VBox
     */
    //vertical
    private VBox vbox() {
        VBox v = new VBox(20);
        v.getChildren().addAll(hbox(), hbButtons());
        return v;

    }

    /**
     * Geral -> recebe vBox's e a Tabela para ficar alinhado na horizontal
     *
     * @return HBox
     */
    private HBox hbox() {
        HBox h = new HBox(20);
        h.getChildren().addAll(vbUm(), vbDois(), vbox2());
        return h;
    }

    private VBox vbox2() {
        VBox v = new VBox(10);
        v.getChildren().addAll(new Label("Buscar por nome"), getTxtBusca(), getTabela());
        return v;
    }

    private TextField getTxtBusca() {
        txtBusca = new TextField();
        txtBusca.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                ObservableList<Fase> novaLista = FXCollections.observableArrayList();
                if (txtBusca.getText().equals("")) {
                    try {
                        atualizaTabela();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    for (Fase fase : ViewController.getFases()) {
                        if (fase.getNomeFase().toLowerCase().contains(txtBusca.getText().toLowerCase().trim())) {
                            novaLista.add(fase);
                        }
                    }
                    tabelaFase.setItems(novaLista);
                }
                
            }
        });

        return txtBusca;

    }

    /**
     * Inicio da Tabela
     */
    private TableView getTabela() {
        tabelaFase.setItems(TabelaFase.getListaFases());
        tabelaFase.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                setOnMouseTabela(me);
            }
        });
        try {
            atualizaTabela();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tabelaFase;
    }

    /**
     * Fim da Tabela
     */
    /**
     * **************Inicio*Horizontal(linha)*um********************************
     */
    private HBox hbButtons() {
        HBox h = new HBox(25);
        h.getChildren().addAll(btCadastrar(), btDeletar(), btAlterar(), btNovo());
        return h;
    }

    private Button btCadastrar() {
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

    private Button btDeletar() {
        btnDeletar = new Button("Deletar");
        btnDeletar.setDisable(true);
        btnDeletar.setId(idStyleSheet);
        btnDeletar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                actionButtonDeletar(t);
            }
        });
        return btnDeletar;
    }

    private Button btAlterar() {
        btnAlterar = new Button("Alterar");
        btnAlterar.setDisable(true);
        btnAlterar.setId(idStyleSheet);
        btnAlterar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                actionButtonAlterar(t);
            }
        });
        return btnAlterar;
    }

    private Button btNovo() {
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

    /**
     * **************Fim*Horizontal(linha)*um***********************************
     */
    /**
     * **************Inicio*Vertical(coluna)*dois*******************************
     */
    private VBox vbDois() {
        VBox vb = new VBox(15);
        vb.getChildren().addAll(txtNomeFase(),
                txtQtdQuestaoLiberadas(), txtPontosQuestao(),
                hbTempoMaxQuestao(), hbTolerancia(), hbDescontar(),
                lbl(), hbPontoMin(), txtPontoMaxPossivel());
        return vb;
    }

    private TextField txtNomeFase() {
        ArrayList<Fase> f = getFases();
        String n = "";
        if (!f.isEmpty()) {
            n = Integer.toString(f.get(f.size() - 1).getIdFase() + 1);
        }
        txtNomeFase = new TextField("ML " + n);
        txtNomeFase.setTooltip(new Tooltip("Nome da fase"));
        txtNomeFase.setMaxWidth(150);
        txtNomeFase.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                maxLength = 15;
                verificaQtdDigit(oldValue, newValue, txtNomeFase);
            }
        });
        return txtNomeFase;
    }

    private TextField txtQtdQuestaoLiberadas() {
        txtQtdQuestaoLiberadas = setTextFieldDigit();
        txtQtdQuestaoLiberadas.setText("0");
        txtQtdQuestaoLiberadas.setTooltip(new Tooltip("Qtd. Questões Cadastradas"));
        txtQtdQuestaoLiberadas.setMaxWidth(100);
        txtQtdQuestaoLiberadas.setDisable(true);
        return txtQtdQuestaoLiberadas;
    }

    private TextField txtPontosQuestao() {
        txtPontosQuestao = setTextFieldDigit();
        txtPontosQuestao.setText("0");
        txtPontosQuestao.setTooltip(new Tooltip("Pontos por Questão"));
        txtPontosQuestao.setMaxWidth(100);
        txtPontosQuestao.setDisable(true);
        return txtPontosQuestao;
    }

    /**
     * ***************Inicio*Tempo*Maximo*Questão*******************************
     */
    private HBox hbTempoMaxQuestao() {
        HBox h = new HBox(5);
        h.getChildren().addAll(txtTempoMaxQuestao(), lblTempoMaxQuestaoS());
        return h;
    }

    private Label lblTempoMaxQuestaoS() {
        Label lbl = new Label(" s ");
        return lbl;
    }

    private TextField txtTempoMaxQuestao() {
        txtTempoMaxQuestao = setTextFieldDigit();
        txtTempoMaxQuestao.setText("10");
        txtTempoMaxQuestao.setTooltip(new Tooltip("Tempo Maximo por Questão"));
        txtTempoMaxQuestao.setMaxWidth(100);
        return txtTempoMaxQuestao;
    }

    /**
     * ***************Fim*Tempo*Maximo*Questão**********************************
     */
    /**
     * ********************Inicio*Tolerancia************************************
     */
    private HBox hbTolerancia() {
        HBox h = new HBox(5);
        h.getChildren().addAll(txtTolerancia(), lblToleranciaSegundo());
        return h;
    }

    private Label lblToleranciaSegundo() {
        Label lbl = new Label(" s ");
        return lbl;
    }

    private TextField txtTolerancia() {
        txtTolerancia = setTextFieldDigit();
        txtTolerancia.setText("2");
        txtTolerancia.setTooltip(new Tooltip("Tolerância"));
        txtTolerancia.setMaxWidth(100);
        return txtTolerancia;
    }

    /**
     * ********************Fim*Tolerancia***************************************
     */
    private Label lbl() {
        Label lbl = new Label();
        return lbl;
    }

    /**
     * ********************Inicio*Descontar*************************************
     */
    private HBox hbDescontar() {
        HBox h = new HBox(5);
        h.getChildren().addAll(txtDescontar(), lblPontosCada());
        return h;
    }

    private Label lblPontosCada() {
        Label lbl = new Label(" a cada 1s ");
        return lbl;
    }

    private TextField txtDescontar() {
        txtDesconto = setTextFieldDigit();
        txtDesconto.setText("5");
        txtDesconto.setTooltip(new Tooltip("Descontar"));
        txtDesconto.setMaxWidth(100);
        return txtDesconto;
    }

    /**
     * ********************Fim*Descontar****************************************
     */
    /**
     * ********************Inicio*Pnto*Min**************************************
     */
    private HBox hbPontoMin() {
        HBox h = new HBox(5);
        h.getChildren().addAll(txtPontoMinMudancaFase(), lblPorcentagem());
        return h;
    }

    private Label lblPorcentagem() {
        Label lbl = new Label(" % ");
        return lbl;
    }

    private TextField txtPontoMinMudancaFase() {
        txtPontoMinMudancaFase = setTextFieldDigit();
        txtPontoMinMudancaFase.setText("50");
        txtPontoMinMudancaFase.setTooltip(new Tooltip("Ponto Mínimo em Porcentagem"));
        txtPontoMinMudancaFase.setMaxWidth(100);
        txtPontoMinMudancaFase.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                maxLength = 2;
                verificaQtdDigit(oldValue, newValue, txtPontoMinMudancaFase);
            }
        });
        return txtPontoMinMudancaFase;
    }

    /**
     * ********************Fim**Pnto*Min****************************************
     */
    private TextField txtPontoMaxPossivel() {
        txtPontoMaxPossivel = setTextFieldDigit();
        txtPontoMaxPossivel.setText(Integer.toString(Config.PONTO_MAXIMO));
        txtPontoMaxPossivel.setTooltip(new Tooltip("Ponto Máximo possível"));
        txtPontoMaxPossivel.setMaxWidth(100);
        txtPontoMaxPossivel.setDisable(true);
        return txtPontoMaxPossivel;
    }

    /**
     * **************Fim*Vertical(coluna)*dois**********************************
     */
    /**
     * **************Inicio*Vertical(coluna)*um*********************************
     */
    private VBox vbUm() {
        VBox vb = new VBox(20);
        vb.getChildren().addAll(lblNomeFase(),
                lblQtdQuestoesLiberadas(), lblPontoQuestao(),
                lblTempoMaxQuestao(), lblTolerancia(), lblDescontar(),
                hbAleatorio(), lblPontoMinMudancaFase(), lblPontoMaxPossivel());
        return vb;
    }

    private Label lblNomeFase() {
        Label lbl = new Label("Nome da Fase: ");
        return lbl;
    }

    private Label lblQtdQuestoesLiberadas() {
        Label lbl = new Label("Qtd. Questões Cadastradas: ");
        return lbl;
    }

    private Label lblPontoQuestao() {
        Label lbl = new Label("Pontos por Questão: ");
        return lbl;
    }

    private Label lblTempoMaxQuestao() {
        Label lbl = new Label("Tempo Máximo por Questão: ");
        return lbl;
    }

    private Label lblTolerancia() {
        Label lbl = new Label("Tolerância: ");
        return lbl;
    }

    private Label lblDescontar() {
        Label lbl = new Label("Descontar: ");
        return lbl;
    }

    /**
     * **************Inicio*Aleatorio*******************************************
     */
    private HBox hbAleatorio() {
        HBox h = new HBox();
        h.getChildren().addAll(chAleatorio(), lblAleatorio());
        return h;
    }

    private CheckBox chAleatorio() {
        chAleatorio = new CheckBox();
        return chAleatorio;
    }

    private Label lblAleatorio() {
        Label lbl = new Label("Mostrar questões aleatoreamente");
        return lbl;
    }

    /**
     * **************Fim*Aleatorio**********************************************
     */
    private Label lblPontoMinMudancaFase() {
        Label lbl = new Label("Pontuação Mínima para a Mudança de fase: ");
        return lbl;
    }

    private Label lblPontoMaxPossivel() {
        Label lbl = new Label("Pontuação Máxima Possível: ");
        return lbl;
    }

    /**
     * **************Fim*Vertical(coluna)*um************************************
     */
    public static void limparCampos() {
        codFase = 0;
        txtNomeFase.clear();
        txtQtdQuestaoLiberadas.clear();
        txtPontosQuestao.setText("0");
        txtTempoMaxQuestao.clear();
        txtTolerancia.clear();
        txtDesconto.clear();
        txtPontoMinMudancaFase.clear();
//        txtPontoMaxPossivel.clear();

        txtNomeFase.setStyle(null);
        txtQtdQuestaoLiberadas.setStyle(null);
        txtPontosQuestao.setStyle(null);
        txtTempoMaxQuestao.setStyle(null);
        txtTolerancia.setStyle(null);
        txtDesconto.setStyle(null);
        txtPontoMinMudancaFase.setStyle(null);
//        txtPontoMaxPossivel.setStyle(null);
    }

    public static void padraoCampos() {
        if (!txtNomeFase.getText().equals("")) {
            txtNomeFase.setStyle(null);
        }
        if (!txtPontosQuestao.getText().equals("")) {
            txtPontosQuestao.setStyle(null);
        }
        if (!txtTempoMaxQuestao.getText().equals("")) {
            txtTempoMaxQuestao.setStyle(null);
        }
        if (!txtTolerancia.getText().equals("")) {
            txtTolerancia.setStyle(null);
        }
        if (!txtDesconto.getText().equals("")) {
            txtDesconto.setStyle(null);
        }
        if (!txtPontoMinMudancaFase.getText().equals("")) {
            txtPontoMinMudancaFase.setStyle(null);
        }
    }
}
