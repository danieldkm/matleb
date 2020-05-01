package view;

import banco.*;
import controller.ViewController;
import entidades.Aluno;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import matleb.Config;

/**
 * @author Daniel K. Morita
 */
public class ViewLogin extends Application {

    private Aluno aluno;
    private TextField txtNome = new TextField();
    private static PasswordField txtSenha = new PasswordField();
    private Label lblNome = new Label("Login");
    private Label lblSenha = new Label("Senha");
    private Button btnLogin = new Button("Login");
    StackPane root;

    @Override
    public void start(Stage stage) {
        System.out.println("diretorio: " + System.getProperty("user.dir"));
        try {
            setLogs();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (verificarConexão()) {
            Config.init();
            txtSenha.setText("");
            stage.getIcons().add(new Image(Config.class.getResourceAsStream("images/icon2.png")));
            root = new StackPane();
            root.getStylesheets().add("css/Buttons.css");
            setButtons(stage);
            VBox vb = new VBox(4);
            vb.getChildren().addAll(lblNome, txtNome, lblSenha, txtSenha);
            Group grupo = new Group();
            grupo.getChildren().add(vb);
            grupo.setTranslateY(-25);
            root.getChildren().addAll(grupo, btnLogin, getHotkeys());

            stage.setResizable(false);
            Scene scene = new Scene(root, 300, 250);
            stage.setTitle("Login");

//        txtSenha.requestFocus();

            stage.setScene(scene);
            stage.show();

            //executa dps do contrutor da classe
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    txtNome.requestFocus();
                }
            });
        }
    }

    private boolean verificarConexão() {
        Dao.setCon(Conexao.getInstance());
        if (Dao.getCon() != null) {
            return true;
        }
        return false;
    }

    private void setButtons(final Stage primaryStage) {
        btnLogin.setTranslateY(75);
        btnLogin.setId(ViewController.idStyleSheet);
        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (isAutentic()) {
//                    FXDialog.showMessageDialog("Usuario aceito"
//                            + " \ndialog.", "Login", Message.INFORMATION);
                    try {
                        Stage s = new Stage(StageStyle.TRANSPARENT);
                        new FXWindows(aluno, primaryStage).start(s);
                        primaryStage.hide();
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Dialogs.showErrorDialog(null, "Login ou senha inválidos.", "Login", "");
                    txtSenha.setText("");
                }
            }
        });
    }

    private MenuBar getHotkeys() {
        MenuItem m = MenuItemBuilder.create().accelerator(new KeyCodeCombination(KeyCode.ESCAPE))
                .onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        }).build();

        Menu menu = new Menu("Menu");
        menu.getItems().add(m);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);

        txtNome.setMaxWidth(200);

        root.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    btnLogin.arm();
                    btnLogin.fire();
                }
            }
        });
        txtSenha.setMaxWidth(200);
        menuBar.setVisible(false);
        return menuBar;
    }

    private boolean isAutentic() {
        ArrayList<Aluno> a = null;
        Dao<Aluno> dao = new Dao(Aluno.class);
        boolean achou = false;
        try {
            a = dao.select();
            for (int i = 0; i < a.size(); i++) {
                if (a.get(i).getLoginAluno().equals(txtNome.getText())
                        && a.get(i).getSenhaAluno().equals(txtSenha.getText())) {
                    achou = true;
                    this.aluno = a.get(i);
                    Config.ALUNO_LOGADO = a.get(i);
                    break;
                }
            }
        } catch (Exception e) {
            Dialogs.showErrorDialog(null, "Erro ao buscar alunos", "Erro", "");
            e.printStackTrace();
            return false;
        }
        return achou;
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private static void setLogs() throws IOException {
        GregorianCalendar date = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy hh'h'mm'm'ss");
        String agora = format.format(date.getTime());

        File out = new File("logs/System.out");
        File err = new File("logs/System.err");
        if (!out.exists()) {
            out.mkdirs();
        }
        if (!err.exists()) {
            err.mkdirs();
        }
        System.setOut(new PrintStream(new FileOutputStream("logs/System.out/" + agora + ".txt", true)));
        System.setErr(new PrintStream(new FileOutputStream("logs/System.err/" + agora + ".txt", true)));
    }
}