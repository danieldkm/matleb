package view;

import controller.TeclaAtalhoController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.text.Text;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import matleb.Config;

public class ViewTeclaAtalhos extends TeclaAtalhoController {

    public ViewTeclaAtalhos(String content) {
        getStylesheets().add("css/Buttons.css");
        actionFechar();
        setTitle(content);
        setPrefSize(LARGURA_WINDOW, ALTURA_WINDOW);
        setLayoutX((Config.SCREENW * 0.5) - (LARGURA_WINDOW * 0.5));
        setLayoutY((Config.SCREENH * 0.5) - (ALTURA_WINDOW * 0.5));
        getLeftIcons().add(new CloseIcon(this));
        getRightIcons().add(new MinimizeIcon(this));
        Group grupo = new Group();
        grupo.getChildren().addAll(getText());
        getContentPane().getChildren().add(grupo);
    }

    private void actionFechar() {
        setOnCloseAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                vta = null;
            }
        });
    }

    private Text getText() {
        Text t = new Text("ESC - Sair do Programa"
                + "\nP - Pause (não habilitado)"
                + "\nF - Inicia Jogo");
        return t;
    }
}