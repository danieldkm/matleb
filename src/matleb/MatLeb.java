package matleb;

import banco.*;
import controller.ViewController;
import entidades.Aluno;
import entidades.Aluno_and_fases;
import entidades.Fase;
import entidades.Modulo;
import entidades.Modulo_has_fases;
import entidades.Questao;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text; //Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));
import javafx.stage.Stage;
import utils.MersenneTwisterFast;

public class MatLeb extends Scene {

    private boolean disablePause = true; //se o pause pode ser habilitado ou n
    Scene sceneMenu;//referencia da scene do menu.
    Stage stage;
    Group root; //Group root que rá conter todos os componentes do tela principal.    
    Fase faseAtual;
    public ArrayList<Fase> fases;
    ArrayList<Questao> questoes;
    private static int TIME; //tempo máximo que o usuário terá para responder as questões.
    boolean pausado = false;
    boolean pausaPontuacao = false;
    Task taskTime; //controla uma thread separada para o time.
    Task taskPont; //controla uma thread para a pontuacao
    int currentExpression = -2;  //expressão atual que aparecera na tela, ou seja, a questão.
    int faseNum = -2; //contador do faseNum. //utilizada tambem como parametro para fkIdFase
    int acertos = 0; //contador dos acertos.
    int erros = 0; //contador dos erros.
//    int bonificacaoTempo = 0; //valor da bonificação do usuário que irá obter. //no momento não esta sendo usado
    int score = 0; //contador de pontuação por fase.
    int i = 0;
    Aluno aluno;
    double pontuacaoMinima;
    int numeroFases = 0;
    double w; //largura da scene
    double h; //altura da scene
    Modulo moduloAtual;
    boolean isGameOver;
    int concatenaScore = 0;
    int countTimer = 0;
    int contagemRegressiva = 3; //conta 3 2 1 e inicia o jogo
    int questaoAtual = 0;
    boolean primeiraVez = true; //muito chato esse tempo para debugar, somente deixar true em versão release

    public MatLeb(Parent parent, double d, double d1, Paint paint) {
        super(parent, d, d1, paint);
        root = (Group) parent;
        getStylesheets().add("css/Buttons.css");
    }

    public ArrayList<Integer> getIdFasesNoModulo() {
        ArrayList<Modulo_has_fases> m = ViewController.getModuloFaseSeq(moduloAtual.getIdModulo());
        ArrayList<Integer> f = new ArrayList();
        for (int j = 0; j < m.size(); j++) {
            f.add(m.get(j).getFkIdFase());
        }
        return f;
    }

    /**
     * Busca as fases desse módulo e seta para o jogo.
     *
     * @return
     */
    public ArrayList<Fase> getFasesNoModulo() {
        ArrayList<Modulo> modulos;
        modulos = ViewController.getModulos(); //atualiza a variavel modulos                

        moduloAtual = modulos.get(Config.MODULO_ESCOLHIDO);
        fases = new ArrayList();

        ArrayList<Integer> fksFases = getIdFasesNoModulo();
        ArrayList<Fase> todasFases = ViewController.getFases();

        for (int j = 0; j < fksFases.size(); j++) {
            for (int k = 0; k < todasFases.size(); k++) {
                if (fksFases.get(j) == todasFases.get(k).getIdFase()) {
                    fases.add(todasFases.get(k));
                }
            }
        }
        return fases;
    }

    /**
     * Pega todas as questões da primeira fase do módulo escolhido.
     *
     * @return
     */
    public ArrayList<Questao> getAndSetQuestoes() {
        faseNum = Config.FASE_COD;
        getFasesNoModulo();

        //se voce quiser todas as fases que estao cadastradas no banco
        //descomente essa parte       

        if (fases.isEmpty()) {
            Dialogs.showErrorDialog(null, "Nunhuma fase cadastrada!", "Impossível iniciar o jogo", "");
        } else {
            faseAtual = fases.get(faseNum - 1);
            questoes = ViewController.getQuestaoSeq(faseAtual.getIdFase());
            if (questoes == null || questoes.isEmpty()) {
                Dialogs.showErrorDialog(null, "Nunhuma questão cadastrada!", "Impossível iniciar o jogo", "");
            }
            return questoes;
        }
        return questoes;
    }

    public void init(Aluno aluno, Scene sceneMenu, double w, double h, Stage stage) {
        ArrayList<EndInfo> endInfo = new ArrayList();
        ImageView background = new ImageView();
        isGameOver = false;
        faseNum = Config.FASE_COD;
//        pausado = false;

//        textResp.setText("0");
//        textScore.setText("" + String.valueOf(score));

        this.aluno = aluno;

        this.stage = stage;
        this.sceneMenu = sceneMenu;

        this.w = w;
        this.h = h;

        background.setImage(Config.getImages().get(Config.BACKGROUND));
        //if using fullscreen
//        background.setFitWidth(Config.SCREENW);
//        background.setFitHeight(Config.SCREENH);

        //if using maximized
        background.setFitWidth(w);
        background.setFitHeight(h);

        //stage.initStyle(StageStyle.UTILITY); //retira o fechar e o minimizar                        

        pontuacaoMinima = Config.PONTO_MAXIMO * faseAtual.getPontMin();

        TIME = faseAtual.getTempoMax();

        if (faseAtual.getAleatorio() == 0) {
            Config.isAleatorio = true;
        } else {
            Config.isAleatorio = false;
        }

        if (Config.isAleatorio) {
            sorteiaArray();
        } else {
            currentExpression = 0;
        }

        for (int i = 0; i < fases.size(); i++) {
            numeroFases++;
        }

//        stage.setFullScreen(true);

        Animacao animacao = new Animacao();
        Pane paneMenino = animacao.getPane(Config.SCREENW * 0.5 + Config.SCREENW * 0.15, Config.SCREENH * 0.5);
        Text textTimeLeft = new Text("\n\t" + String.valueOf(TIME - i));
        Text textExpression = new Text();
        Text textScore = new Text(String.valueOf(score));
        Text textFase = new Text(String.valueOf(faseNum));
        Text textResp = new Text("0");
        ProgressBar progressBarPont = new ProgressBar(0);
        Button buttonBackMainMenu = new Button("Voltar para menu principal");
        final ProgressBar progressBar = new ProgressBar(0);
        Text tbar = new Text("|");
        Text textRegres = new Text("");

        if (root.getChildren().size() < 3) {
            root.getChildren().addAll(background, getProgressPont(progressBarPont), getScore(textScore, progressBarPont), getButtons(textTimeLeft, textScore, textExpression, textResp, textFase, tbar, endInfo, paneMenino, progressBar, textRegres), getHotkeys(textScore, textExpression, textResp, textFase, animacao, endInfo, tbar, progressBarPont), getTextResp(textResp), getTextExpression(textExpression), getFase(textFase), paneMenino, getTimeLeft(textTimeLeft), pause(textScore, textTimeLeft, textExpression, textFase, textResp, animacao, background,
                    paneMenino, buttonBackMainMenu, tbar, progressBar, progressBarPont), getButtonBackMainMenu(buttonBackMainMenu), getContagemRegressiva(textRegres), getI(tbar, progressBarPont));
        }


        System.out.println("numeroFases: " + numeroFases);
    }

    /**
     * Cria uma taskTime para inicializar o timer e atualiza a barra a de acordo
     * com o tempo passado.
     */
    public Task createTask(final Text textTimeLeft, final Text textScore, final Text textExpression,
            final Text textResp, final Text textFase, final ArrayList endInfo, final Pane paneMenino,
            final Text tBar, final ProgressBar progressBarPont, final Text textRegres) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if (primeiraVez) {
//                    pausado = true;
                    paneMenino.setVisible(false);
                    progressBarPont.setVisible(false);
                    textFase.setVisible(false);
                    textResp.setVisible(false);
                    tBar.setVisible(false);
                    textTimeLeft.setVisible(false);
                    textExpression.setVisible(false);
                    textScore.setVisible(false);
                    for (int j = 0; j < 3; j++) {
                        textRegres.setText("" + contagemRegressiva);
                        contagemRegressiva--;
                        Thread.sleep(700);
                    }
                    primeiraVez = false;
                    textRegres.setVisible(false);
//                    pausado = false;
                    paneMenino.setVisible(true);
                    progressBarPont.setVisible(true);
                    textFase.setVisible(true);
                    textResp.setVisible(true);
                    tBar.setVisible(true);
                    textTimeLeft.setVisible(true);
                    textExpression.setVisible(true);
                    textScore.setVisible(true);
                }

                for (; i < ((TIME * 20) + 20); i++) {
                    while (pausado) {
                        Thread.sleep(50);
                    }
                    Thread.sleep(50);   //50 * 20 = 1000ms
                    if (i == 0) {
//                        ready.setVisible(false);
//                        textResp.setVisible(true);
                    }
//                    i -= bonificacaoTempo;
                    updateProgress(i, (TIME * 20));
//                    bonificacaoTempo = 0;
                    if (i != 0) {
                        if (((TIME * 20) - i) % 20 == 0) //modulo para atualizar o tempo somente a cada 1s
                        {
                            countTimer++;
                        }
                    }
                    if (i >= ((TIME * 20))) {
                        //Opção 1: ao errar a questao ele passa para a proxima questão
                        //sem ganhar nenhuma pontuaçao
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                                questoes.remove(currentExpression);
//                                i = 0;
//                                countTimer = 0;
//                                trocaQuestao();                                
//                            }
//                        });

                        //Opção 2: ao errar a questao ele volta para primeira questao da fase atual
//                        this works  :-0

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                pausado = true;
                                showDialog();
                                pausado = false;
                            }
                        });
                        questoes = ViewController.getQuestaoSeq(faseAtual.getIdFase());
                        i = 0;
                        countTimer = 0;
                        trocaQuestao(textScore, textFase, textExpression, endInfo, tBar, progressBarPont);
                        score = 0;
                        acertos = 0;
                        erros = 0;
                        textScore.setText("" + String.valueOf(score));
                    }
                    textTimeLeft.setText("\n\t" + String.valueOf(TIME - (countTimer)));
                }
                return true;
            }
        };
    }

    public void showDialog() {
        Config.getAudios().get(Config.ERRADO).play();
        Dialogs.showInformationDialog(null, "O Tempo acabou, a fase foi reiniciada", "Tempo Esgotado", "");
    }

    public Task createTaskPont() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                //efeito que buga tudo essa merda, a thread morre, o motivo eu nao sei
//                int j = 1;
//                while (score <= Config.PONTO_MAXIMO) //thread dedicada a atualizar a barra de pontuacao
//                {
//                    if (!pausaPontuacao) {
//                        for (; j <= score; j++) {
//                            Thread.sleep(1);
//                            updateProgress(j, Config.PONTO_MAXIMO);
//                        }
//                        updateProgress(j, Config.PONTO_MAXIMO);
//                        System.out.println("j: " + j);
//                        System.out.println("s: " + score);
//                        System.out.println("\n");
//                        Thread.sleep(100);
//                        if (j >= Config.PONTO_MAXIMO) {
//                            j = 1;
//                            updateProgress(j, Config.PONTO_MAXIMO);
//                            faseNum++;
//                            System.out.println("zerou score: " + j);
//                        }
//                    }
//                }

                while (score <= Config.PONTO_MAXIMO) //thread dedicada a atualizar a barra de pontuacao
                {
                    Thread.sleep(100);
                    updateProgress(score, Config.PONTO_MAXIMO);
                }
                return true;
            }
        };
    }

    private ProgressBar getProgressPont(ProgressBar progressBarPont) {
        progressBarPont.setPrefSize(400, 40);
        progressBarPont.setTranslateX(w * 0.225 - (progressBarPont.getPrefWidth() / 2));

        progressBarPont.setTranslateY(h * 0.844);

        System.out.println("translateY in getProg: " + progressBarPont.getTranslateY());

        taskPont = createTaskPont();

        progressBarPont.progressProperty().unbind();
        progressBarPont.setProgress(0);
        progressBarPont.progressProperty().bind(taskPont.progressProperty());

        try {
            Thread threadBarPont = new Thread(taskPont);
            threadBarPont.setName("Thread-Pontuacao");
            threadBarPont.setDaemon(true);
            threadBarPont.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
//         progressBar.setStyle("-fx-color: black;");  //para o background
//         progressBar.setStyle("-fx-shadow-highlight: green;"); //nao afeta neste acaso
        progressBarPont.setStyle("-fx-accent: purple;");
        return progressBarPont;
    }

    //metodo para marcar na barra de progresso a pontuacao minima almejada
    private Text getI(Text tBar, ProgressBar progressBarPont) {
        Font f = new Font("|", 40);
//        tBar.setTranslateX((w * 0.066) + (progressBarPont.getPrefWidth() * faseAtual.getPontMin()));
        tBar.setTranslateX(progressBarPont.getTranslateX() + (progressBarPont.getPrefWidth() * faseAtual.getPontMin()) - 5);
        tBar.setTranslateY(progressBarPont.getTranslateY() + progressBarPont.getPrefHeight() - 10);
        System.out.println("translateX in tBar: " + tBar.getTranslateY());
        tBar.setFill(Color.YELLOW);
        tBar.setFont(f);
        return tBar;
    }

    /**
     * Buttons invisiveis para inicio, pausa e finalização do time. <br/> São
     * invisíveis justamente porque a contagem irá começar automaticamente.
     * <br/> Futuramente se for possível pausar, a lógica para pausa deverá ser
     * implementada.
     */
    private HBox getButtons(final Text textTimeLeft, final Text textScore, final Text textExpression,
            final Text textResp, final Text textFase, final Text tBar, final ArrayList<EndInfo> endInfo,
            final Pane paneMenino, final ProgressBar progressBar, final Text textRegres) {
        final HBox hb = new HBox();
        final TextField tf = new TextField(String.valueOf(TIME));

        progressBar.setPrefSize(Config.SCREENW * 0.8, 20);
        final Button bIniciar = new Button("iniciar");
        final Button bReiniciar = new Button("reiniciar tempo");
        ToggleButton bPausar = new ToggleButton("pause");
        bPausar.setSelected(false);
        bPausar.setVisible(false);
        if (!bPausar.isSelected()) {
            bPausar.setOnAction(new EventHandler<ActionEvent>() {
                public synchronized void handle(ActionEvent e) {
                    progressBar.progressProperty().unbind();
                }
            });
        } else {
            bPausar.setOnAction(new EventHandler<ActionEvent>() {
                public synchronized void handle(ActionEvent e) {
                    progressBar.progressProperty().bind(taskTime.progressProperty());
                }
            });
        }

        bIniciar.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                bIniciar.setDisable(true);
                bReiniciar.setDisable(false);
                taskTime = createTask(textTimeLeft, textScore, textExpression, textResp,
                        textFase, endInfo, paneMenino, tBar, progressBar, textRegres);

                progressBar.progressProperty().unbind();
                progressBar.setProgress(0);
                progressBar.progressProperty().bind(taskTime.progressProperty());

                Thread threadTime = new Thread(taskTime);
                threadTime.setName("Thread-Tempo");
                threadTime.setDaemon(true);
                threadTime.start();
            }
        });

        bReiniciar.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                bReiniciar.setDisable(true);
                bIniciar.setDisable(false);
                taskTime.cancel(true);
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0);
            }
        });

        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
//        hb.getChildren().addAll(bIniciar, bReiniciar, bPausar, progressBar, tf);
        hb.getChildren().addAll(progressBar);
        hb.setTranslateY(h * 0.05);
        hb.setTranslateX(w * 0.08);

        tf.setVisible(false);

        bIniciar.setVisible(false);
        bReiniciar.setVisible(false);

        bIniciar.arm();
        bIniciar.fire(); //semelhante ao doClick do swing

        return hb;
    }

    private void sorteiaArray() {
        MersenneTwisterFast m = new MersenneTwisterFast();
        if (!questoes.isEmpty()) {
            currentExpression = m.nextInt(questoes.size());
        }
    }

    public void trocaQuestao(Text textScore, Text textFase, Text textExpression, ArrayList<EndInfo> endInfo, Text tBar, ProgressBar progressBarPont) {
//        if (!questoes.isEmpty()) {
            if (Config.isAleatorio) {
                sorteiaArray();
            }
            if (trocaFase(textScore, endInfo)) {
                Config.RESTO_DIVISAO = Config.PONTO_MAXIMO % questoes.size();
                System.out.println("resto direto: " + Config.RESTO_DIVISAO);
                Config.getAudios().get(Config.CORRETO).play();
                pausado = true;
                Dialogs.showInformationDialog(null, "Você passou para a fase " + faseAtual.getNomeFase(), "Passou de fase", "");
                pausado = false;
                score = 0;
                acertos = 0;
                erros = 0;
                pontuacaoMinima = Config.PONTO_MAXIMO * faseAtual.getPontMin();
                TIME = faseAtual.getTempoMax();
                tBar.setTranslateX((w * 0.0752) + (progressBarPont.getPrefWidth() * faseAtual.getPontMin()));
                textScore.setText("" + String.valueOf(score));
                textFase.setText(faseAtual.getNomeFase());
            }
            if (!isGameOver) {
                textExpression.setText(questoes.get(currentExpression).getNomeQuestao());
                questaoAtual++;
            }
//        }
    }

    public void atualizaAlunoEFases() {
        Dao<Aluno_and_fases> dao = new Dao(Aluno_and_fases.class);
        Aluno_and_fases alunoFases = null;
        ArrayList<Aluno_and_fases> alunosAndFases = ViewController.getAlunoAndFases();
        boolean existe = false;

        for (int j = 0; j < alunosAndFases.size(); j++) {
            if (alunosAndFases.get(j).getFkIdAluno() == aluno.getIdAluno()) {
                if (alunosAndFases.get(j).getFkIdFase() == faseAtual.getIdFase()) {
                    existe = true;
                    if (alunosAndFases.get(j).getScore() < score) {
                        try {
                            alunosAndFases.get(j).setScore(score);
                            dao.updateAlunoAndFases(alunosAndFases.get(j));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (!existe) {
            alunoFases = new Aluno_and_fases();
            alunoFases.setScore(score);
            alunoFases.setFkIdAluno(aluno.getIdAluno());
            alunoFases.setFkIdFase(faseAtual.getIdFase());

            try {
                dao.insert(alunoFases);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean trocaFase(Text textScore, ArrayList<EndInfo> endInfo) {
        if (questoes.isEmpty()) {
            if (score < pontuacaoMinima) {
                pausado = true;
                Dialogs.showInformationDialog(null, "Você não passou de fase"
                        + " \nPontuação atingida: " + score
                        + "\nPontuação necessária para passar de fase: " + pontuacaoMinima, "Não passou de fase", "");

                pausado = false;
                try {
                    questoes = ViewController.getQuestaoSeq(faseAtual.getIdFase()); //ta pegando com a fkId1
                    if (Config.isAleatorio) {
                        sorteiaArray();
                    } else {
                        currentExpression = 0;
                    }
                    atualizaAlunoEFases();
                    score = 0;
                    textScore.setText("" + String.valueOf(score));
                    return false;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                faseNum++;
                if (faseNum == (numeroFases + 1)) {
                    //ganhou o jogo
                    endInfo.add(new EndInfo(score, acertos, erros));
                    System.out.println("this is win game");
                    pausado = true;
                    Dialogs.showInformationDialog(null, "Você completou todas as fases deste módulo", "Game Over", "");

                    Group grupo = new Group(); //uma instancia de grupo para o parametro, o parametro nao pode ser null
                    view.WinGame wg = new view.WinGame(grupo, Config.SCREENW, Config.SCREENH, Color.BLACK, fases,
                            endInfo);
                    wg.init(stage, sceneMenu, w, h, numeroFases);
                    atualizaAlunoEFases();
                    stage.setScene(wg);
                    score = 0;
                    isGameOver = true;
                    return false;
                    //apos uma nova cena, todos os componentes antigos nao sao mais acessados                    
//                        faseNum = 1; //reseto a fase(debug)
                } else {
                    try {
                        endInfo.add(new EndInfo(score, acertos, erros));
                        atualizaAlunoEFases();
                        faseAtual = fases.get(faseNum - 1);
                        questoes = ViewController.getQuestaoSeq(faseAtual.getIdFase());
                        if (faseAtual.getAleatorio() == 0) {
                            Config.isAleatorio = true;
                        } else {
                            Config.isAleatorio = false;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            if (Config.isAleatorio) {
                sorteiaArray();
            } else {
                currentExpression = 0;
            }
            return true;
        }
        return false;
    }

    /**
     * Método para inicializar as hotkeys da sceneMenu.<br/> Configura o
     * textPause para respostas deacordo com o que o usuário digita.
     *
     * @return o textfield requerido
     */
    private MenuBar getHotkeys(final Text textScore, final Text textExpression,
            final Text textResp, final Text textFase, final Animacao animacao, final ArrayList<EndInfo> endInfo,
            final Text tBar, final ProgressBar progressBarPont) {
        MenuBar mb;
        MenuItem m = MenuItemBuilder.create().accelerator(new KeyCodeCombination(KeyCode.ESCAPE))
                .onAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        }).build();

        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent t) {
                if (pausado) {
                } else {
                    String numero;
                    if (t.getCode().getName().contains("Numpad")) {
                        numero = t.getCode().getName().substring(7);
                    } else {
                        numero = t.getCode().getName();
                    }

                    if (textResp.getText().length() < 9) {
                        if (t.getCode().isDigitKey() || t.getCode().isKeypadKey()) {
                            if (textResp.getText().equals("0")) {
                                textResp.setText(numero);
                            } else {
                                textResp.setText(textResp.getText() + numero);
                            }
                        }
                    }
                    if (t.getCode().equals(t.getCode().BACK_SPACE)) {
                        if (textResp.getText().length() == 1) {
                            textResp.setText("0");
                        } else {
                            textResp.setText(textResp.getText().substring(0, textResp.getText().length() - 1));
                        }
                    }
                    if (t.getCode().equals(KeyCode.ENTER)) {
//                        System.out.println("thread pont is alive: " + threadBarPont.isAlive());
//                        suspendeTimer = true;
//                        FXDialog.showMessageDialog("Are you sure?"
//                + " \n", "noob", Message.INFORMATION);
//                        suspendeTimer = false;
                        if (Integer.parseInt(textResp.getText()) == Integer.parseInt(
                                (questoes.get(currentExpression).getRespostaQuestao()))) {
                            questoes.remove(currentExpression);
                            acertos++;
                            //                        bonificacaoTempo = 2;

                            //menino feliz
                            animacao.setFeliz();
                            animacao.startAnimacao();

                            if (faseAtual.getTolerancia() < countTimer) {
                                int scoreLocal;

                                int multiplicadorDoDesconto = 0;
                                if (countTimer - faseAtual.getTolerancia() <= 0) {
                                    multiplicadorDoDesconto = 1;
                                } else {
                                    multiplicadorDoDesconto = countTimer - faseAtual.getTolerancia();
                                }

                                scoreLocal = faseAtual.getPontQuestao() - (multiplicadorDoDesconto * faseAtual.getDesconto());

                                if (scoreLocal < 1) {
                                    score += 1; //caso menor que 1, entao é acrescentado a pontuacao minima no score
                                } else {
                                    score += scoreLocal; //é acrecentado o desconto calculado, sendo entre 1-10
                                }
                                //0 + 10 - ((5 - 2) * 2) = 10 - 6 = 4
//                       System.out.println("score conquistado: " + scoreLocal);
                            } else {
                                score += faseAtual.getPontQuestao(); //é acrescentado integralmente o ponto da questao no score
                            }
                            concatenaScore += score;
                            if (questaoAtual == 1) {
                                score += Config.RESTO_DIVISAO;
                            }
                            System.out.println("resto divisao: " + Config.RESTO_DIVISAO);
                            i = 0; //reseta o time
                            countTimer = 0;
                            textScore.setText("" + String.valueOf(score));
                            trocaQuestao(textScore, textFase, textExpression, endInfo, tBar, progressBarPont);
                            System.out.println("questaoAtual: " + questaoAtual);

                        } else {
//                        bonificacaoTempo = -2;                            
                            erros++;
                            //menino triste
                            //                            animacao.pauseAnimacao();
                            animacao.setTriste();
                            animacao.startAnimacao();
//                            animacao.stopAnimacao();
                        }
                        textResp.setText("0");
                    }
                }
            }
        });
        Menu menu = new Menu("menu");
        menu.getItems().addAll(m);
        mb = new MenuBar();
        mb.getMenus().add(menu);
        mb.setVisible(false);
        return mb;
    }

    public Text getContagemRegressiva(Text textRegres) {
        Font serif = Font.font("Serif", 100);
        textRegres.setTranslateY(h * 0.5);
        textRegres.setTranslateX(w * 0.54 - (textRegres.getText().length() / 2) - serif.getSize());
        textRegres.setFill(Color.GREEN);
        textRegres.setFont(serif);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.rgb(50, 50, 50, .588));
        textRegres.setEffect(dropShadow);
        return textRegres;
    }

    /**
     * O {@link javafx.sceneMenu.textPause.Text} onde o usuário digita os
     * números para responder às expressões.
     * <br/>Configura conteúdo inicial, fonte, posição, cor, efeitos entre
     * outras coisas.
     */
    public Text getTextResp(Text textResp) {
        Font serif = Font.font("Serif", 85);
        textResp.setTranslateY(h * 0.6);
        textResp.setTranslateX(w * 0.46 - (textResp.getText().length() / 2) - serif.getSize());
        textResp.setFill(Color.BLUE);
        textResp.setFont(serif);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.rgb(50, 50, 50, .588));
        textResp.setEffect(dropShadow);
        return textResp;
    }

    /**
     * O {@link javafx.sceneMenu.textPause.Text} onde irá conter as expressões
     * para o usuário responder.
     * <br/>Configura conteúdo inicial, fonte, posição, cor, efeitos entre
     * outras coisas.
     */
    public Text getTextExpression(Text textExpression) {
        textExpression.setText(questoes.get(currentExpression).getNomeQuestao());
        Font serif = Font.font("Serif", 85);
        textExpression.setTranslateX(w * 0.45 - (textExpression.getText().length() / 2) - serif.getSize());
        textExpression.setTranslateY(h * 0.25);
        textExpression.setFill(Color.BLACK);
        textExpression.setFont(serif);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.rgb(50, 50, 50, .588));
        textExpression.setEffect(dropShadow);
        return textExpression;
    }

    /**
     * O {@link javafx.sceneMenu.textPause.Text} onde irá atualizar de acordo
     * com o contador de pontMaxReached.
     * <br/>Configura conteúdo inicial, fonte, posição, cor, efeitos entre
     * outras coisas.
     */
    private Text getScore(Text textScore, ProgressBar progressBarPont) {
        textScore.setText("" + String.valueOf(score));
        Font serif = Font.font("Serif", 40);
        textScore.setTranslateX(w * 0.12 - (textScore.getText().length() / 2) - serif.getSize());
        textScore.setTranslateY(progressBarPont.getTranslateY() + progressBarPont.getPrefHeight() - 10);
        textScore.setFill(Color.BLACK);
        textScore.setFont(serif);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.rgb(50, 50, 50, .588));
        textScore.setEffect(dropShadow);
        return textScore;
    }

    /**
     * O {@link javafx.sceneMenu.textPause.Text} onde irá atualizar de acordo
     * com o contador de faseNum.
     * <br/>Configura conteúdo inicial, fonte, posição, cor, efeitos entre
     * outras coisas.
     */
    private Text getFase(Text textFase) {
        textFase.setText(faseAtual.getNomeFase());
        Font serif = Font.font("Serif", 30);
        textFase.setTranslateY(h * 0.077);
        textFase.setTranslateX(w * 0.84 - (textFase.getText().length() / 2) - serif.getSize());
        textFase.setFill(Color.BLUE);
        textFase.setFont(serif);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.rgb(50, 50, 50, .588));
        textFase.setEffect(dropShadow);
        return textFase;
    }

    private Text getTimeLeft(Text textTimeLeft) {
        Font serif = Font.font("Serif", 27);
        textTimeLeft.setTranslateX(w * 0.02
                - (textTimeLeft.getText().length() / 2) - serif.getSize());
        textTimeLeft.setTranslateY(h * 0.035);
        textTimeLeft.setFill(Color.BLACK);
        textTimeLeft.setFont(serif);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.rgb(50, 50, 50, .588));
        textTimeLeft.setEffect(dropShadow);
        return textTimeLeft;
    }
      
    //metodo para realizar o pause
    public Text pause(final Text textScore, final Text textTimeLeft, final Text textExpression, final Text textFase, final Text textResp, final Animacao animacao, final ImageView background,
            final Pane paneMenino, final Button buttonBackMainMenu, final Text tBar, final ProgressBar progressBar,
            final ProgressBar progressBarPont) {
        final Text textPause = new Text("             Pausado\nPressine P para continuar");
        textPause.setVisible(false);

        this.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
              if(!disablePause)
                if (t.getCode().equals(KeyCode.P)) {
                    if (!primeiraVez) {
                        pausado = !pausado;
                        pausaPontuacao = !pausaPontuacao;
                        double sombra = 1;
                        double sombraMenino = 1;
                        if (pausado) {
                            sombra = 0.2;
                            sombraMenino = 0.05;
                            animacao.stopAnimacao();
                        } else {
                            animacao.startAnimacao();
                        }
                        Font serif = Font.font("Serif", 40);
                        textPause.setFont(serif);
                        textPause.setFill(Color.WHITE);
                        textPause.setTranslateX(w * 0.5 - 200);
                        textPause.setTranslateY(h * 0.5 - 100);

                        textScore.setOpacity(sombra);
                        textTimeLeft.setOpacity(sombra);
                        textExpression.setOpacity(sombra);
                        background.setOpacity(sombra);
                        paneMenino.setOpacity(sombraMenino);
                        progressBarPont.setOpacity(sombra);
                        textFase.setOpacity(sombra);
                        progressBar.setOpacity(sombra);
                        textResp.setOpacity(sombra);
                        tBar.setOpacity(sombra);
                        textPause.setVisible(pausado);
                        buttonBackMainMenu.setVisible(pausado);
                    }
                }
            }
        });
        return textPause;
    }

    public Button getButtonBackMainMenu(Button buttonBackMainMenu) {
        buttonBackMainMenu.setVisible(false);
        buttonBackMainMenu.setTranslateX(w * 0.58 - 200);
        buttonBackMainMenu.setTranslateY(h * 0.8 - 200);

        buttonBackMainMenu.setId("dark-blue");

        buttonBackMainMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                score = 0;
                stage.setScene(sceneMenu);
            }
        });
        return buttonBackMainMenu;
    }
}