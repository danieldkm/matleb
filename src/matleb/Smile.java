package matleb;
import javafx.animation.ParallelTransition;
import javafx.animation.ParallelTransitionBuilder;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField; 
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.EllipseBuilder;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Smile extends Preloader {

    private Ellipse olhoDireito;
    private Ellipse olhoEsquerdo;
    private Pane paneSmille;
    private StackPane raiz = new StackPane();
    // atributo para fazer o olho dirieto mover
    public ParallelTransition ptOlhoDireito;
    // atributo para fazer o olho esquerdo mover
    public ParallelTransition ptOlhoEsquerdo;
    // booleana para verificar que lado do olho está movendo
    public boolean verificarOlho = true;
    // booleana para veri0ricar se o ParallelTransition já foi feito
    public boolean verificarPT = true;
    private Arc sorrir;
    private ScaleTransition efeitoScaleTransition;
    private Circle rosto;
    // --//
    private TextField texto;
    private Button botao;    
    
    
    @Override
    public void start(final Stage primaryStage) {        
        // "maximizar" janela
        Screen screen = Screen.getPrimary();        
        final Rectangle2D bounds = screen.getVisualBounds();
               
        final Scene cena = new Scene(raiz);
        // -- TODO -- smille
        sorriso();

        // testando sorriso
        texto = new TextField();
        texto.setMaxWidth(100);
        raiz.getChildren().add(texto);
        botao = new Button();
        botao.setMaxWidth(100);
        botao.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (Integer.parseInt(texto.getText()) < 9) {
                    resetSorriso();
                    tristeNovo1();
                } else if (Integer.parseInt(texto.getText()) > 19) {
                    resetSorriso();
                    tristeNovo3();
                } else {
                    resetSorriso();
                    tristeNovo2();
                }
            }
        });
        raiz.getChildren().add(botao);
        // testando sorriso

        // Evento do mouse pra mover os olhos
        cena.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double dragX = event.getSceneX();
                criarTransicao();
                if (verificarOlho) {
                    if (dragX > (bounds.getWidth() / 2)) {
                        verificarOlho = false;
                        ptOlhoDireito.play();
                        ptOlhoEsquerdo.pause();
                    }
                } else {
                    if (dragX <= (bounds.getWidth() / 2)) {
                        verificarOlho = true;
                        ptOlhoEsquerdo.play();
                        ptOlhoDireito.pause();
                    }
                }
            }
        });

        primaryStage.setTitle("Aprendendo JavaFX");
        primaryStage.setScene(cena);

        // setar janela p/ maximizada
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        // 
        primaryStage.show();

        texto.setTranslateY(300);
        botao.setTranslateY(350);

    }

//	public static void main(String[] args) { launch(args); }
    /**
     * TODO
     *
     * @return desenhar smille
	 *
     */
    public void sorriso() {
        
    }

    public Pane getPane(double w, double h)
    {
        paneSmille = new Pane();
        paneSmille.setMaxSize(200, 200);
        
        rosto = new Circle(0, 0, 100);
        rosto.setStroke(Color.BLACK);
        rosto.setFill(Color.YELLOW);
        paneSmille.getChildren().add(rosto);

        sorrir = new Arc(0, 0, 100, 100, 210, 120);
        sorrir.setStroke(Color.BLACK);
        sorrir.setStrokeWidth(5);
        sorrir.setFill(null);
        paneSmille.getChildren().add(sorrir);

        olhoDireito = EllipseBuilder.create()
                .radiusX(25 / 2).radiusY(45 / 2).build();
        olhoDireito.setCenterX(60);
        olhoDireito.setCenterY(10);
        paneSmille.getChildren().add(olhoDireito);

        olhoEsquerdo = EllipseBuilder.create()
                .radiusX(25 / 2).radiusY(45 / 2).build();
        olhoEsquerdo.setCenterX(0);
        olhoEsquerdo.setCenterY(10);
        paneSmille.getChildren().add(olhoEsquerdo);

//        raiz.getChildren().add(paneSmille);
        // posição do rosto no pane
        rosto.setTranslateY(50);
        rosto.setTranslateX(50);
        // posição do "sorriso" no pane
        sorrir.setTranslateX(50);
        // posição do smille -- TODO
        paneSmille.setTranslateX(w * 0.5 - (50));
        paneSmille.setTranslateY(h * 0.78 - (50));
        
        return paneSmille;
    }
    
    /**
     *
     * @return aplicar efeito do sorriso
	 *
     */
    public void arcPlay() {
        if(efeitoScaleTransition != null)
        {
            efeitoScaleTransition.play();
        }
    }

    /**
     *
     * @return parar efeito do sorriso
	 *
     */
    public void arcStop() {
        if(efeitoScaleTransition != null)
        {
            efeitoScaleTransition.stop();
        }
    }

    /**
     *
     * @return criar efeito de "Parallel Transition" nos olhos
	 *
     */
    public void criarTransicao() {
        if (verificarPT) {
            ptOlhoDireito = ParallelTransitionBuilder
                    .create()
                    .node(olhoDireito)
                    .children(
                    TranslateTransitionBuilder.create()
                    .duration(Duration.seconds(2)).fromX(0)
                    .toX(40).cycleCount(10).autoReverse(true)
                    .build()).cycleCount(Timeline.INDEFINITE)
                    .autoReverse(true).build();
            ptOlhoEsquerdo = ParallelTransitionBuilder
                    .create()
                    .node(olhoEsquerdo)
                    .children(
                    TranslateTransitionBuilder.create()
                    .duration(Duration.seconds(2)).fromX(0)
                    .toX(50).cycleCount(10).autoReverse(true)
                    .build()).cycleCount(Timeline.INDEFINITE)
                    .autoReverse(true).build();
            verificarPT = false;
        }
    }

    /**
     *
     * @return tipo - boca triste, tamanho 1
	 *
     */
    public void tristeNovo1() {
        sorrir = new Arc(50, 120, 30, 30, 30, 120);
        sorrir.setStroke(Color.BLACK);
        sorrir.setStrokeWidth(5);
        sorrir.setFill(null);
        paneSmille.getChildren().add(sorrir);
        criarEfeito();
    }

    /**
     *
     * @return tipo - boca triste, tamanho 2
	 *
     */
    public void tristeNovo2() {
        sorrir = new Arc(50, 120, 50, 50, 30, 120);
        sorrir.setStroke(Color.BLACK);
        sorrir.setStrokeWidth(5);
        sorrir.setFill(null);
        paneSmille.getChildren().add(sorrir);
        criarEfeito();
    }

    /**
     *
     * @return tipo - boca triste, tamanho 3
	 *
     */
    public void tristeNovo3() {
        sorrir = new Arc(50, 210, 90, 90, 30, 120);
        sorrir.setStroke(Color.BLACK);
        sorrir.setStrokeWidth(5);
        sorrir.setFill(null);
        paneSmille.getChildren().add(sorrir);
        criarEfeito();
    }

    /**
     *
     * @return tipo - sorriso, tamanho 1
	 *
     */
    public void sorrisoNovo1() {
        sorrir = new Arc(50, 70, 30, 30, 210, 120);
        sorrir.setStroke(Color.BLACK);
        sorrir.setStrokeWidth(5);
        sorrir.setFill(null);
        paneSmille.getChildren().add(sorrir);
        criarEfeito();
    }

    /**
     *
     * @return tipo - sorriso, tamanho 2
	 *
     */
    public void sorrisoNovo2() {
        sorrir = new Arc(50, 70, 50, 50, 210, 120);
        sorrir.setStroke(Color.BLACK);
        sorrir.setStrokeWidth(5);
        sorrir.setFill(null);
        paneSmille.getChildren().add(sorrir);
        criarEfeito();
    }

    /**
     *
     * @return tipo - sorriso, tamanho 3
	 *
     */
    public void sorrisoNovo3() {
        sorrir = new Arc(50, 20, 90, 90, 210, 120);
        sorrir.setStroke(Color.BLACK);
        sorrir.setStrokeWidth(5);
        sorrir.setFill(null);
        paneSmille.getChildren().add(sorrir);
        criarEfeito();
    }

    /**
     *
     * @return aplicar efeito na boca
	 *
     */
    private void criarEfeito() {

        efeitoScaleTransition = ScaleTransitionBuilder.create()
                .node(sorrir)
                .duration(Duration.seconds(2))
                .byX(0.5).byY(0.5)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();

        arcPlay();

    }

    /**
     *
     * @return resetar "boca"
	 *
     */
    public void resetSorriso() {
        sorrir.setVisible(false);
        sorrir.setDisable(true);
    }
}
