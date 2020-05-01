package view;

import controller.SobreController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.text.Text;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import matleb.Config;

public class ViewSobre extends SobreController implements Runnable {

    @Override
    public void run() {
        iniciar();
    }

    private void iniciar() {
        getStylesheets().add("css/Buttons.css");
        actionFechar();
//        setTitle(content);
        setPrefSize(LARGURA_WINDOW, ALTURA_WINDOW);
        setLayoutX((Config.SCREENW * 0.5) - (LARGURA_WINDOW * 0.5));
        setLayoutY((Config.SCREENH * 0.5) - (ALTURA_WINDOW * 0.5));
        getLeftIcons().add(new CloseIcon(this));
        getRightIcons().add(new MinimizeIcon(this));
        Group grupo = new Group();
        grupo.getChildren().addAll(getText());
        getContentPane().getChildren().add(grupo);
    }

    public ViewSobre(String content) {
//        getStylesheets().add("css/Buttons.css");
//        actionFechar();
        setTitle(content);
//        setPrefSize(LARGURA_WINDOW, ALTURA_WINDOW);
//        setLayoutX((Config.SCREENW * 0.5) - (LARGURA_WINDOW * 0.5));
//        setLayoutY((Config.SCREENH * 0.5) - (ALTURA_WINDOW * 0.5));
//        getLeftIcons().add(new CloseIcon(this));
//        getRightIcons().add(new MinimizeIcon(this));
//        Group grupo = new Group();
//        grupo.getChildren().addAll(getText());
//        getContentPane().getChildren().add(grupo);
    }

    private void actionFechar() {
        setOnCloseAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                vs = null;
            }
        });
    }

    private Text getText() {
        Text t = new Text("Copyright 2013-2014. Todos os direitos reservados."
                + "\nVersão: 1.0"
                + "\n\nDesenvolvimento: \nDaniel K. Morita - danielmorita@hotmail.com\nEduardo Tesser Filho - du.ka@hotmail.com"
                + "\n\nCo-Responsável: \nLuiz Eduardo Barreto - luizedu-barreto@bol.com.br");
        return t;
    }
}