package view;

import controller.ViewController;
import entidades.Fase;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import matleb.Animacao;
import matleb.EndInfo;

public class WinGame extends Scene {

    Stage stage;
    Group grupo;
    Scene cenaMenu;
    Text text;
    Text tFase;
    Text tMaxPoint;
    Pane paneMenino;
    int numeroFases;
    double w, h;
    ArrayList<Fase> fases;
    Text textPontTotal;
    Text textAcertos;
    Text textErrors;
    ArrayList<EndInfo> endInfo;

    public WinGame(Parent parent, double d, double d1, Paint paint, ArrayList<Fase> fases, ArrayList<EndInfo> endInfo) {
        super(parent, d, d1, paint);
        getStylesheets().add("css/Buttons.css");
        grupo = (Group) parent;
        this.fases = fases;      
        this.endInfo = endInfo;
    }

    public void init(Stage stage, Scene cenaMenu, double w, double h, int numeroFases) {
        this.stage = stage;
        this.cenaMenu = cenaMenu;
        this.w = w;
        this.h = h;
        this.numeroFases = numeroFases;

        Animacao animacao = new Animacao();
        paneMenino = animacao.getPane(w * 0.04, h * 0.4);
        
        MenuItem esc = MenuItemBuilder.create().accelerator(new KeyCodeCombination(KeyCode.ESCAPE))
                .onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        }).build();

        esc.setVisible(false);
        Menu menu = new Menu();
        menu.getItems().add(esc);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        grupo.getChildren().addAll(getText(), getFase(), getPoint(), getButtonBack(), paneMenino, menuBar);
    }

    private Text getText() {
        if (text == null) {
            text = new Text("");
        }
        text.setText("Você completou todas as fases.");
        Font serif = Font.font("Serif", 35);
        text.setTranslateY(this.getHeight() * 0.21);
        text.setTranslateX(this.getWidth() * 0.04);
        text.setFill(Color.WHITE);
        text.setFont(serif);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.rgb(50, 50, 50, .588));
        text.setEffect(dropShadow);
        return text;
    }

    private Text getFase() {
        if (tFase == null) {
            tFase = new Text("");
        }
        tFase.setText("Fases concluídas: " + endInfo.size());
        Font serif = Font.font("Serif", 35);
        tFase.setTranslateY(this.getHeight() * 0.26);
        tFase.setTranslateX(this.getWidth() * 0.04);
        tFase.setFill(Color.WHITE);
        tFase.setFont(serif);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.rgb(50, 50, 50, .588));
        tFase.setEffect(dropShadow);
        return tFase;
    }  

    private Text getPoint() {
        if (tMaxPoint == null) {
            tMaxPoint = new Text("");
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < endInfo.size(); i++) {
            s.append("\r\n")
                .append(fases.get(i).getNomeFase())
                .append(": ")
                .append(endInfo.get(i).getScore())
                .append("  Acertos: ")
                .append(endInfo.get(i).getAcertos())
                .append("  Erros: ")
                .append(endInfo.get(i).getErrors())
                .append("  Nº Questões: ")
                .append(fases.get(i).getNumQuestao());
        }
        tMaxPoint.setText(s.toString());
        Font serif = Font.font("Serif", 35);
        tMaxPoint.setTranslateY(this.getHeight() * 0.02);
        tMaxPoint.setTranslateX(this.getWidth() * 0.5 - (tMaxPoint.getText().length() / 2) - serif.getSize());
        tMaxPoint.setFill(Color.WHITE);
        tMaxPoint.setFont(serif);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.rgb(50, 50, 50, .588));
        tMaxPoint.setEffect(dropShadow);
        return tMaxPoint;
    }

    private Button getButtonBack() {
        Button b = new Button("Voltar para o menu");

        b.setTranslateX(this.getHeight() * 0.04);
        b.setTranslateY(this.getWidth() * 0.02);
        b.setId(ViewController.idStyleSheet);
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stage.setScene(cenaMenu);
            }
        });
        return b;
    }
    //animacao do rosto sem graça
//    public void animation()
//     {                  
//         this.setOnMouseMoved(new EventHandler<MouseEvent>() {             
//            @Override public void handle(MouseEvent event) {                
//                double dragX = event.getSceneX();
//                smile.criarTransicao();
//                if (smile.verificarOlho) {
//                    if (dragX > (w * 0.5)) {
//                        smile.verificarOlho = false;
//                        smile.ptOlhoDireito.play();
//                        smile.ptOlhoEsquerdo.pause();
//                    }
//                } else {
//                    if (dragX <= (w * 0.5)) {
//                        smile.verificarOlho = true;
//                        smile.ptOlhoEsquerdo.play();
//                        smile.ptOlhoDireito.pause();
//                    }
//                }                            
//            }
//        });         
//     }
//    if (acertos < 2) {
//        smile.resetSorriso();
//        smile.sorrisoNovo1();
//    } else if (acertos > 2 && acertos < 5) {
//        smile.resetSorriso();
//        smile.sorrisoNovo2();
//    } else {
//        smile.resetSorriso();
//        smile.sorrisoNovo2();
//    }
//    if (errors < 2) {
//        smile.resetSorriso();
//        smile.tristeNovo1();
//    } else if (errors > 2 && errors < 5) {
//        smile.resetSorriso();
//        smile.tristeNovo2();
//    } else {
//        smile.resetSorriso();
//        smile.tristeNovo2();
//    }
}