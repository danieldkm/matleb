package view;

import controller.VisualizarAlunoController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import matleb.Config;
import tabela.TabelaModulo;

public class ViewVisualizarAluno extends VisualizarAlunoController {

    public ViewVisualizarAluno(String content) {
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
    }

    private void actionFechar() {
        setOnCloseAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                vva = null;
            }
        });
    }
    
    private VBox getGeral(){
        VBox v = new VBox(10);
        v.getChildren().addAll(getGrupo1(), new Label("Tabela Modulo"), getTabela());
        return v;
    }
    
    
    private VBox getGrupo1(){
        VBox v = new VBox();
        v.getChildren().addAll(new Label("Aluno"), getCbAluno());
        return v;
    }
    
    private ComboBox getCbAluno() {
        cbAlunos = new ComboBox<String>();
        cbAlunos.setMinWidth(150);
        cbAlunos.setItems(getPopularcbAlunos());
        cbAlunos.setOnAction(handler);
        return cbAlunos;
    }
    
    
    private TableView getTabela() {
        tabelaModulo = new TabelaModulo();
        tabelaModulo.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
            }
        });
        tabelaModulo.setItems(lista);
        return tabelaModulo;
    }
    
}