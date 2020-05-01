package matleb;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 *
 * @author Administrador
 */
public class Animacao {

    private Timeline timeLine;
    private Timeline timeLine2;
    private List<Image> images;
    private ImageView animation = null;
    private int n = 0;
    private static final int VELOCIDADE = 650;
    private Pane paneAnimation;

    private void setImagens(int[] i) {
        images = new ArrayList<>();
//        for (int j : i) {
//            System.out.println("awdawd " + j);
//            images.add(Config.getImage(i[j]));
//        }
        images.add(Config.getImage(Config.A1));
        images.add(Config.getImage(Config.A2));
        images.add(Config.getImage(Config.A3));
    }

    private void actionEventAnimacao(ActionEvent t, int qtd) {
        if (n > qtd) {
            n = 0;
        }
        Image kajak = images.get(n);
        final ImageView previous = animation;
        animation = new ImageView(kajak);
        paneAnimation.getChildren().remove(previous);
        paneAnimation.getChildren().add(animation);
        n++;

        /*
         Image i = Config.getImage(Config.ERRO);
         ImageView iAnterior = animation;
         animation = new ImageView(i);
         painel.getChildren().remove(iAnterior);
         painel.getChildren().add(animation);
         */
    }

    public Pane getPane(double w, double h) {
        paneAnimation = new Pane();
        setAnimacao();
//        getFeliz();
        paneAnimation.setTranslateX(w);
        paneAnimation.setTranslateY(h);
        paneAnimation.setPrefSize(Config.SCREENW * 0.3, Config.SCREENH * 0.3);
        return paneAnimation;
    }

    public void setAnimacao() {
        int[] i = {Config.A1, Config.A2, Config.A3};
        setImagens(i);
        timeLine = new Timeline();
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.getKeyFrames().add(
                new KeyFrame(Duration.millis(VELOCIDADE),
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                actionEventAnimacao(t, 2);
            }
        },
                new KeyValue[0]) // don't use binding
                );
        startAnimacao();
    }

    public void setTriste2() {
        stopAnimacao();
        int[] i = {Config.ERRO, Config.ERRO, Config.ERRO};
        setImagens(i);
        timeLine2 = new Timeline();
        timeLine2.setCycleCount(Timeline.INDEFINITE);
        timeLine2.setDelay(Duration.seconds(10));
        timeLine2.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1000),
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                actionEventAnimacao(t, 2);

            }
        },
                new KeyValue[0]));
    }

    public void setTriste() {
        Image i = Config.getImage(Config.ERRO);
        ImageView iAnterior = animation;
        animation = new ImageView(i);
        paneAnimation.getChildren().remove(iAnterior);
        paneAnimation.getChildren().add(animation);
    }

    public void setNormal() {
//        stopAnimacao();
        Image i = Config.getImage(Config.A4);
        ImageView iAnterior = animation;
        animation = new ImageView(i);
        paneAnimation.getChildren().remove(iAnterior);
        paneAnimation.getChildren().add(animation);
    }

    public void setFeliz() {
//        stopAnimacao();
        Image i = Config.getImage(Config.A5);
        ImageView iAnterior = animation;
        animation = new ImageView(i);        
        paneAnimation.getChildren().remove(iAnterior);
        paneAnimation.getChildren().add(animation);
    }

    public void startAnimacao() {
        timeLine.playFromStart();
    }

    public void stopAnimacao() {
        timeLine.stop();
    }

    public void pauseAnimacao() {
        timeLine.pause();
    }
}
