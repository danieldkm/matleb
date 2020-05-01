package testes;

import entidades.Aluno;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import matleb.Config;
import view.FXWindows;
/*
 classe para iniciar a FXWindows diretamente com as 
 configuraçoes da stage necessarias
 */
public class Initializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Aluno aluno = new Aluno();
        aluno.setLoginAluno("a");
        aluno.setSenhaAluno("a");
        aluno.setIdAluno(1);
        aluno.setNivel("Administrador");
        Config.init();
        Config.ALUNO_LOGADO = aluno;
        Config.isAleatorio = true;
        Stage s = new Stage(StageStyle.TRANSPARENT);
        try {
            new FXWindows(aluno, s).start(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        stage.close();
    }
}
