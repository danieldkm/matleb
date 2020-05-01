package view;

import controller.ModuloController;
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
import tabela.TabelaFase;

/**
 *
 * @author Daniel K. Morita
 */
public class ViewModulo extends ModuloController {

    private ObservableList<Node> screen = getContentPane().getChildren();
    
    public ViewModulo(String content) {
        getStylesheets().add("css/Buttons.css");
        setTitle(content);
        setPrefSize(LARGURA_WINDOW, ALTURA_WINDOW);
        setLayoutX((Config.SCREENW * 0.5) - (LARGURA_WINDOW * 0.5));
        setLayoutY((Config.SCREENH * 0.5) - (ALTURA_WINDOW * 0.7));
        getLeftIcons().add(new CloseIcon(this));
        getRightIcons().add(new MinimizeIcon(this));
        Group grupo = new Group();
        grupo.getChildren().addAll(getGeral());
        screen.addAll(grupo);
        actionFechar();
        changeButtonDisable("");
    }

    /**
     * Método para fazer uma ação antes de fechar a janela, o atributo vf recebe
     * valor nulo
     */
    private void actionFechar() {
        setOnActionBtnLimpar(null);
        setOnCloseAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                vm = null;
            }
        });
    }
    /*-----------------------------Geral-Inicio-------------------------------*/

    private VBox getGeral() {
        VBox v = new VBox(20);
        v.getChildren().addAll(getVBoxGrupo1(), getBoxGrupo2(), getHboxGrupo3());
        return v;
    }

    /*-----------------------------Geral-Fim----------------------------------*/
    /*-----------------------------Inicio-------------------------------------*/
    private VBox getVBoxGrupo1() {
        VBox v = new VBox(10);
        v.getChildren().addAll(getLblModulo(), getHBoxGrupo1());
        return v;
    }

    private Label getLblModulo() {
        return new Label("Jogo");
    }

    private HBox getHBoxGrupo1() {
        HBox v = new HBox(350);
        v.getChildren().addAll(getCbModuloAndBtnOk(), getBtnLimpar());
        return v;
    }

    private Button getBtnLimpar() {
        btnLimpar = new Button("Limpar");
        btnLimpar.setId("dark-blue");
        btnLimpar.setOnAction(handler);
        return btnLimpar;
    }

    private HBox getCbModuloAndBtnOk() {
        HBox h = new HBox(10);
        h.getChildren().addAll(getCbModulo());
        return h;
    }

    private ComboBox getCbModulo() {
        cbModulos = new ComboBox<String>();
        cbModulos.setMinWidth(150);
        cbModulos.setItems(getPopularcbModulos());
        cbModulos.setOnAction(handler);
        return cbModulos;
    }
    /*----------------------------Fim-----------------------------------------*/

    /*-----------------------------Inicio-------------------------------------*/
    private HBox getBoxGrupo2() {
        HBox h = new HBox(5);
        h.getChildren().addAll(getVboxTbFase(), getVboxPolygon(), getVboxTbModulo(), getVBoxAA());
        return h;
    }

    private VBox getVboxTbModulo() {
        VBox v = new VBox(4);
        v.getChildren().addAll(getLblTabelaModulo(), getTabelaModulo());
        return v;
    }

    private Label getLblTabelaModulo() {
        return new Label("Tabela do Jogo");
    }

    private TableView getTabelaModulo() {
        tbFaseNova = new TabelaFase();
//        listaNova.addListener(new ListChangeListener<Fase>() {
//            @Override
//            public void onChanged(ListChangeListener.Change<? extends Fase> change) {
//                System.out.println("listener lista nova");
//                System.out.println("listener getPosi tab nova " + tbFaseNova.getSelectionModel().getSelectedItem());
//            }
//        });

        tbFaseNova.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                TabelaFase.numeroTabela = 1;
            }
        });
        return tbFaseNova;
    }


    /*-----------------------------Polygon------------------------------------*/
    private VBox getVboxPolygon() {
        VBox v = new VBox(150);
        v.getChildren().addAll(getLblGrupo2(), getVbox2());
        return v;
    }

    private Label getLblGrupo2() {
        return new Label();
    }

    private VBox getVbox2() {
        VBox v = new VBox(10);
        v.getChildren().addAll(getPolygon1(), getPolygon2());
        return v;
    }

    private Polygon getPolygon2() {
        Polygon p = new Polygon(new double[]{
            10, 50,
            10, 80,
            40, 65,});
        p.setFill(Color.GREEN);
        p.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                setOnMousePolygon2(t);
            }
        });
        return p;
    }

    private Polygon getPolygon1() {
        Polygon p = new Polygon(new double[]{
            40, 50,
            10, 65,
            40, 80,});
        p.setFill(Color.GREEN);
        p.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                setOnMousePolygon1(t);
            }
        });
        return p;
    }

    /*-----------------------------Polygon------------------------------------*/

    /*-----------------------------Tabela de fases----------------------------*/
    private VBox getVboxTbFase() {
        VBox v = new VBox(4);
        v.getChildren().addAll(getLblTabelaFase(), getTabelaFase());
        return v;
    }

    private Label getLblTabelaFase() {
        return new Label("Tabela de Fases");
    }

    private TableView getTabelaFase() {
        tabelaFase = new TabelaFase();
        tabelaFase.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                TabelaFase.numeroTabela = 0;
            }
        });

        tabelaFase.setItems(lista);
        return tabelaFase;
    }
    /*-----------------------------Tabela de fases----------------------------*/


    /*----------------------------Inicio--------------------------------------*/
    private HBox getHboxGrupo3() {
        HBox h = new HBox(190);
        h.getChildren().addAll(getLblGrupo3(), getHbox3());
        return h;
    }

    private Label getLblGrupo3() {
        return new Label();
    }

    private HBox getHbox3() {
        HBox h = new HBox(10);
        h.getChildren().addAll(getTxtNomeModulo(), getBtnSalvar(), getBtnAlterar(), getBtnDeletar());
        return h;
    }

    private TextField getTxtNomeModulo() {
        txtNomeModulo = new TextField();
        return txtNomeModulo;
    }

    private Button getBtnSalvar() {
        btnCadastrar = new Button("Cadastrar");
        btnCadastrar.setId(idStyleSheet);
        btnCadastrar.setOnAction(handler);
        return btnCadastrar;
    }

    private Button getBtnAlterar() {
        btnAlterar = new Button("Alterar");
        btnAlterar.setId(idStyleSheet);
        btnAlterar.setOnAction(handler);
        return btnAlterar;
    }

    private Button getBtnDeletar() {
        btnDeletar = new Button("Deletar");
        btnDeletar.setId(idStyleSheet);
        btnDeletar.setOnAction(handler);
        return btnDeletar;
    }

    /*----------------------------Inicio--------------------------------------*/
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
