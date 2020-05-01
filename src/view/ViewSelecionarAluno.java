package view;

import controller.ModuloController;
import controller.SelecionarAlunoController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import matleb.Config;
import tabela.TabelaSelecionarAluno;

/**
 *
 * @author Daniel K Morita
 */
public class ViewSelecionarAluno extends SelecionarAlunoController {

    public ViewSelecionarAluno(String content) {
        getStylesheets().add("css/Buttons.css");
        setTitle(content);
        setPrefSize(LARGURA_WINDOW, ALTURA_WINDOW);
        setLayoutX((Config.SCREENW * 0.5) - (LARGURA_WINDOW * 0.5));
        setLayoutY((Config.SCREENH * 0.5) - (ALTURA_WINDOW * 0.5));
        getLeftIcons().add(new CloseIcon(this));
        getRightIcons().add(new MinimizeIcon(this));
        Group grupo = new Group();
        grupo.getChildren().addAll(geralTela());
        getContentPane().getChildren().add(grupo);
        actionFechar();
        btnDeletar.setDisable(true);
        btnNovo.arm();
        btnNovo.fire();
    }

    private void actionFechar() {
        setOnCloseAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                vsa = null;
                System.gc();
            }
        });
    }

    //-----------------------------Geral----------------------------------------
    private HBox geralTela() {
        HBox g = new HBox();
        g.getChildren().addAll(getGrupo1());
        return g;
    }

    private VBox getGrupo1() {
        VBox g = new VBox(10);
        g.getChildren().addAll(getLinha1(), getGrupo2(), getGrupo3());
        return g;
    }

    //--------------------------Combo-Box---------------------------------------
    private VBox getLinha1() {
        VBox h = new VBox(10);
        h.getChildren().addAll(getLblModulo(), getAlinhar());
        return h;
    }
    
    private HBox getAlinhar(){
        HBox v = new HBox(10);
        v.getChildren().addAll(getCbModulo(), getBtnLimpar(), getVisualizarAluno());
        return v;
    }

    private Label getLblModulo() {
        return new Label("Jogo");
    }

    private ComboBox getCbModulo() {
        cbModulos = new ComboBox<String>();
        cbModulos.setMinWidth(150);
        cbModulos.setItems(getPopularcbModulos());
        cbModulos.setOnAction(handler);
        return cbModulos;
    }

    private Button getBtnLimpar() {
        btnNovo = new Button("Limpar");
        btnNovo.setId("dark-blue");
        btnNovo.setOnAction(handler);
        btnNovo.setTranslateX(150);
        return btnNovo;
    }
    
    private Button getVisualizarAluno() {
        btnVisualizarAluno = new Button("Visualizar por Aluno");
        btnVisualizarAluno.setId("dark-blue");
        btnVisualizarAluno.setOnAction(handler);
        btnVisualizarAluno.setTranslateX(150);
        return btnVisualizarAluno;
    }
    
    private Label getLabel(String texto){
        return new Label(texto);
    }
    

    //----------------------------Grupo-1---------------------------------------
    private HBox getGrupo2() {
        HBox g = new HBox(5);
        g.getChildren().addAll(getAlinharTab1(), getVboxPolygon(), getAlinharTab2());
        return g;
    }
    
    private VBox getAlinharTab1() {
        VBox v = new VBox(5);
        v.getChildren().addAll(new Label("Tabela de Alunos"), getTabelaSelecionar());
        return v;
    }
    
    private VBox getAlinharTab2() {
        VBox v = new VBox(5);
        v.getChildren().addAll(new Label("Tabela de Alunos Selecionados"), getTabelaSelecionado());
        return v;
    }

    //-----------------------------Tabela---------------------------------------
    private TableView getTabelaSelecionar() {
        tbSelecionarAluno = new TabelaSelecionarAluno();
        tbSelecionarAluno.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                TabelaSelecionarAluno.numeroTabela = 0;
            }
        });
        tbSelecionarAluno.setItems(lista);
        return tbSelecionarAluno;
    }

    private TableView getTabelaSelecionado() {
        tbAlunoSelecionado = new TabelaSelecionarAluno();
        tbAlunoSelecionado.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                TabelaSelecionarAluno.numeroTabela = 1;
            }
        });
        return tbAlunoSelecionado;
    }

    //-----------------------------Polygon--------------------------------------
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

    //-----------------------------Linha-3--------------------------------------
    private HBox getGrupo3() {
        HBox h = new HBox(350);
        h.getChildren().addAll(getLbl(), getButtons());
        return h;
    }

    private Label getLbl() {
        return new Label();
    }

    private HBox getButtons() {
        HBox h = new HBox(10);
        h.getChildren().addAll(getBtnSalvar(), getBtnDeletar());
        return h;
    }

    private Button getBtnSalvar() {
        btnCadastrar = new Button("Cadastrar");
        btnCadastrar.setId(idStyleSheet);
        btnCadastrar.setOnAction(handler);
        return btnCadastrar;
    }

    private Button getBtnDeletar() {
        btnDeletar = new Button("Deletar");
        btnDeletar.setId(idStyleSheet);
        btnDeletar.setOnAction(handler);
        return btnDeletar;
    }
}
