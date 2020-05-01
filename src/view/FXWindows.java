package view;

import controller.AlunoConfController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import matleb.Config;
import matleb.MatLeb;
import controller.AlunoController;
import controller.AlunoPontuacaoController;
import controller.FaseController;
import controller.ModuloController;
import controller.QuestaoController;
import controller.SelecionarAlunoController;
import controller.SobreController;
import controller.TeclaAtalhoController;
import controller.ViewController;
import entidades.Aluno;
import entidades.Questao;
import java.util.ArrayList;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import jfxtras.labs.scene.control.window.Window;

public class FXWindows extends Application {

    static final Group root = new Group();
    ImageView background = new ImageView();
    Aluno aluno; //nome default
    double w; //largura do stage
    double h; //altura do stage    
    Stage stageLogin;
    Stage stage = null;

    public FXWindows(Aluno aluno, Stage stageLogin) {
        this.aluno = aluno;
        this.stageLogin = stageLogin;
        //testes
//        ArrayList<Modulo_has_fases> m = MatLebController.getModuloFaseSeq(1);
//        System.out.println("size: " + m.size());
//        for (int i = 0; i < m.size(); i++) {
//            System.out.println("fase[" + i + "]: " + m.get(i).getFkIdFase());
//        }
    }

    //obrigatorio construtor vazio explicito para execução
    public FXWindows() {
    }
    private static Scene sceneMenu;

    private MenuBar getMenu(final Stage stage, final Scene sceneMenu) {
        this.sceneMenu = sceneMenu;
        MenuBar menuBar = new MenuBar();
        MenuItem menuAlunos = new MenuItem("Alunos");
        MenuItem menuFases = new MenuItem("Fases");
        MenuItem menuQuestoes = new MenuItem("Questoes");
        MenuItem menuJogo = new MenuItem("Iniciar Jogo");
        MenuItem menuEscolherModulo = new MenuItem("Escolher Jogo");
        MenuItem menuModulo = new MenuItem("Jogo");
        MenuItem menuSelecionar = new MenuItem("Selecionar Alunos");
        MenuItem menuAlunoPontuacao = new MenuItem("Pontuacao");
        MenuItem menuItemSobre = new MenuItem("Sobre");
        MenuItem menuTeclaAtalho = new MenuItem("Teclas de Atalho");

        MenuItem esc = MenuItemBuilder.create().accelerator(new KeyCodeCombination(KeyCode.ESCAPE))
                .onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        }).build();

        esc.setVisible(false);

        MenuItem f = MenuItemBuilder.create().accelerator(new KeyCodeCombination(KeyCode.F))
                .onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setIniciarJogo();
            }
        }).build();

        f.setVisible(false);

        Menu menu = new Menu("Cadastro");
        Menu menuConf = new Menu("Configuração");
        Menu menuSobre = new Menu("Sobre");

        if (aluno.getNivel().equals("Administrador")) {
            menu.getItems().addAll(menuAlunos, menuFases, menuQuestoes, menuModulo, menuSelecionar);
            menuConf.getItems().addAll(menuAlunoPontuacao, menuEscolherModulo, esc, menuJogo, f);
            menuSobre.getItems().addAll(menuItemSobre, menuTeclaAtalho);
        } else if (aluno.getNivel().equals("Aluno")) {
            menuConf.getItems().addAll(menuAlunoPontuacao, menuEscolherModulo, esc, menuJogo, f);
            menuSobre.getItems().addAll(menuItemSobre, menuTeclaAtalho);
            menu.setVisible(false);
        } else {
            System.out.println("wtf the aluno is: " + aluno.getNivel());
        }

        menuEscolherModulo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (AlunoConfController.vac == null) {
                    root.getChildren().add(AlunoConfController.vac = new ViewAlunoConf("Escolher Jogo"));
                }
            }
        });

        menuJogo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                setIniciarJogo();
            }
        });
        menuAlunos.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (AlunoController.va == null) {
                    root.getChildren().add(AlunoController.va = new ViewAluno("Tela Alunos"));
                }
            }
        });

        menuQuestoes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (QuestaoController.vq == null) {
                    root.getChildren().add(QuestaoController.vq = new ViewQuestao("Cadastrar Questões"));
                }
            }
        });

        menuFases.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (FaseController.vf == null) {
                    root.getChildren().add(FaseController.vf = new ViewFase("Tela Fases"));
                }
            }
        });

        menuModulo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (ModuloController.vm == null) {
                    root.getChildren().add(ModuloController.vm = new ViewModulo("Tela de Jogo"));
                }
            }
        });

        menuAlunoPontuacao.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (AlunoPontuacaoController.vap == null) {
                    root.getChildren().add(AlunoPontuacaoController.vap = new ViewAlunoPontuacao("Pontuação do Aluno"));
                }
            }
        });

        menuItemSobre.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (SobreController.vs == null) {
                    Thread thre = new Thread(SobreController.vs = new ViewSobre("Sobre"));
                    thre.start();
                    root.getChildren().add(SobreController.vs);
                }
            }
        });

        menuTeclaAtalho.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (TeclaAtalhoController.vta == null) {
                    root.getChildren().add(TeclaAtalhoController.vta = new ViewTeclaAtalhos("Teclas de Atalho"));
                }
            }
        });

        menuSelecionar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (SelecionarAlunoController.vsa == null) {
                    root.getChildren().add(SelecionarAlunoController.vsa = new ViewSelecionarAluno("Selecionar Alunos"));
                }
            }
        });

        menuBar.getMenus().addAll(menu, menuConf, menuSobre);
//        menuBar.setEffect(new BoxBlur(5, 6, 9));
        return menuBar;
    }

    @Override
    public void start(Stage s) throws Exception {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage = s;
        w = bounds.getWidth();
        h = bounds.getHeight();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(w);
        stage.setHeight(h);

        stage.getIcons().add(new Image(Config.class.getResourceAsStream("images/icon2.png")));

        background.setImage(Config.getImages().get(Config.BACKGROUND_VERDE));

        //if using fullscreen
//        background.setFitWidth(Config.SCREENW);
//        background.setFitHeight(Config.SCREENH);

        //if using maximized screen, geralmente maximized 30px a menos comparado ao fullScreen
        background.setFitWidth(bounds.getWidth());
        background.setFitHeight(bounds.getHeight());

        Scene sceneMenu = new Scene(root, Config.SCREENW, Config.SCREENH);
        root.getChildren().addAll(getGrupo(), getMenu(stage, sceneMenu));
        stage.setResizable(false);
        sceneMenu.getStylesheets().add("css/Buttons.css");
        stage.setScene(sceneMenu); //default(abre o menu)
//        stage.setScene(autoInitJogoPrincipal(sceneMenu, stage)); //automatic(inicializa o jogo direto)
//        stage.setScene(autoInitGameOver(sceneMenu, stage)); //automatic(inicializa game over)
        stage.show();
        criarEfeitodeTransição();

//        Image i = new Image(Config.class.getResourceAsStream("images/giz.gif"));
//        ImageCursor ic = new ImageCursor(i);
//        root.setCursor(ic);
    }
    private TranslateTransition translateTransition;
    private Text t = new Text("Teste");

    private void criarEfeitodeTransição() {
        translateTransition = new TranslateTransition(Duration.seconds(4), t);
        translateTransition.setFromX(20);
        translateTransition.setToX(380);
        translateTransition.setCycleCount(Timeline.INDEFINITE);
        translateTransition.setAutoReverse(true);
        translateTransition = TranslateTransitionBuilder.create()
                .duration(Duration.seconds(10))
                .node(t)
                .fromX(20)
                .toX(Config.SCREENW * 0.85)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();
        play();
    }

    public void play() {
        translateTransition.play();
    }

    @Override
    public void stop() {
        translateTransition.stop();
    }
    MatLeb ml;

    private void setIniciarJogo() {
        Group g = new Group();
        ml = new MatLeb(g, Config.SCREENW, Config.SCREENH, Color.BLACK);
        if (Config.MODULO_ESCOLHIDO == -1) {
            Dialogs.showErrorDialog(null, "Nunhuma jogo cadastrado!", "Impossível iniciar o jogo", "");
        } else {
            ArrayList<Questao> questoes = ml.getAndSetQuestoes();
            if (questoes != null) {
                if (!questoes.isEmpty()) {
                    Config.RESTO_DIVISAO = Config.PONTO_MAXIMO % questoes.size();
                    System.out.println("resto direto: " + Config.RESTO_DIVISAO);
                    if (Config.FASE_ESCOLHIDA == -13) {
                        Config.FASE_ESCOLHIDA = ml.fases.get(0).getIdFase();
                        ml.init(aluno, sceneMenu, w, h, stage);
                        stage.setScene(ml);
                    } else {
                        ml.init(aluno, sceneMenu, w, h, stage);
                        stage.setScene(ml);
                    }
                }
            }
        }
    }

    private Text getT() {
        t = new Text("Iniciar o jogo");
        t.setTranslateX(Config.SCREENW * 0.5);
        t.setTranslateY(Config.SCREENH * 0.8);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.rgb(50, 50, 50, .588));
        t.setEffect(dropShadow);
        t.setFont(Font.font("Mistral", FontWeight.BOLD, FontPosture.REGULAR, 50));
        t.setFill(Color.WHITESMOKE);
        t.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                setIniciarJogo();
            }
        });
        return t;
    }

    private Group getGrupo() {
        Group g = new Group();
        Button b = new Button("Sair");
        g.getChildren().addAll(background, getTextModulo(), getButtonSair(b), getT(), getButtonLogout(b));
        return g;
    }

    private Text getTextModulo() {
        ViewAlunoConf.getModuloDiponivel();
        Config.setTextoModuloAndAlunoLogado();
        return Config.TEXTO_MODULO;
    }

    private Button getButtonSair(Button b) {

        b.setTranslateX(Config.SCREENW * 0.92);
        b.setTranslateY(Config.SCREENH * 0.04);
        b.setId(ViewController.idStyleSheet);

        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });
        return b;
    }

    private Button getButtonLogout(Button bSair) {
        Button bLogOut = new Button("Logout");
        bLogOut.setTranslateX(bSair.getTranslateX() - 100);
        bLogOut.setTranslateY(Config.SCREENH * 0.04);
        bLogOut.setId(ViewController.idStyleSheet);

        bLogOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stageLogin.show();
                stage.hide();
            }
        });
        return bLogOut;
    }

    //metodo para debug para autoInicializar o jogo
    public MatLeb autoInitJogoPrincipal(Scene sceneMenu, Stage stage) {
        Group g = new Group();
        MatLeb ml = new MatLeb(g, Config.SCREENW, Config.SCREENH, Color.BLACK);
        ArrayList<Questao> questoes = ml.getAndSetQuestoes();
        if (questoes == null || questoes.isEmpty()) {
            Dialogs.showErrorDialog(null, "Nunhuma questão cadastrada!", "Impossível iniciar o jogo", "");
            ml = null;
            g = null;
        } else {
            if (Config.FASE_ESCOLHIDA == -13) {
                Config.FASE_ESCOLHIDA = ml.fases.get(0).getIdFase();
                ml.init(aluno, sceneMenu, w, h, stage);
                stage.setScene(ml);
            } else { //caso o usuario escolha uma fase para iniciar, no momento eh impossivel
                ml.init(aluno, sceneMenu, w, h, stage);
                stage.setScene(ml);
            }
        }
        return ml;
    }

    //metodo para debug para autoInicializar o winGame
    public WinGame autoInitGameOver(Scene sceneMenu, Stage stage) {
        Group g = new Group();
        view.WinGame wg = new view.WinGame(g, Config.SCREENW, Config.SCREENH, Color.BLACK, null, null);
        wg.init(stage, sceneMenu, w, h, 0);
        return wg;
    }

    public Group getRoot() {
        return root;
    }

    public static void setRoot(Window w) {
        root.getChildren().add(w);

//        if (SelecionarAlunoController.vsa == null) {
//                    root.getChildren().add(SelecionarAlunoController.vsa = new ViewSelecionarAluno("Selecionar Alunos"));
//                }
    }
}