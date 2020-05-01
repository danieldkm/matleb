package view;

import controller.AlunoConfController;
import entidades.Modulo;
import entidades.Modulo_has_alunos;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import matleb.Config;

/**
 *
 * @author Daniel K. Morita
 */
public class ViewAlunoConf extends AlunoConfController {

    public ViewAlunoConf(String content) {
        getStylesheets().add("css/Buttons.css");
        handle = new AlunoConfHandle();
        setTitle(content);
        setPrefSize(LARGURA_WINDOW, ALTURA_WINDOW);
        setLayoutX((Config.SCREENW * 0.5) - (LARGURA_WINDOW * 0.5));
        setLayoutY((Config.SCREENH * 0.5) - (ALTURA_WINDOW * 0.5));
        getLeftIcons().add(new CloseIcon(this));
        getRightIcons().add(new MinimizeIcon(this));
        Group grupo = new Group();
        grupo.getChildren().add(getHBox());
        getContentPane().getChildren().add(grupo);
        actionFechar();
    }

    public void actionFechar() {
        setOnCloseAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                vac = null;
            }
        });
    }

    private HBox geralTela() {
        return null;
    }

    private HBox getHBox() {
        HBox hb = new HBox(10);
        hb.getChildren().addAll(lblFase(), getComboBox1(), btnSalvar());
        return hb;
    }

    private Button btnSalvar() {
        btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(handle);
        btnSalvar.setId(idStyleSheet);
        return btnSalvar;
    }

    private Label lblFase() {
        return lbl;
    }

    public static ArrayList<Modulo> getModuloDiponivel() {
        try {
            ArrayList<Modulo_has_alunos> mha = getModuloWithAlunos();
            ArrayList<Modulo> m = getModulos();
            ArrayList<Modulo> mNovo = new ArrayList<>();
            boolean ok = false;
            if(Config.ALUNO_LOGADO.getNivel().equals("Administrador")){
                Config.MODULO_ESCOLHIDO = 0;
                return m;
            } else if (mha.isEmpty()) {
                return null;
            } else {
                for (Modulo_has_alunos modulo_has_alunos : mha) {
                    if (Config.ALUNO_LOGADO.getIdAluno() == modulo_has_alunos.getFkIdAluno()) {
                        int n = 0;
                        for (Modulo mo : m) {
                            if (mo.getIdModulo() == modulo_has_alunos.getFkIdModulo()) {
                                mNovo.add(mo);
                                if (!ok) { // sempre pegar o primeiro "jogo" disponível para o usuário atual
                                    Config.MODULO_ESCOLHIDO = n;
                                    ok = true;
                                }
                            }
                            n++;
                        }
                    }
                }
                return mNovo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("erro: buscar modulos cadastrados p/ o aluno logado. Metodo: getSelect. Classe ViewAlunoConf.");
        }
        return null;
    }

    private ComboBox getComboBox1() {
        comboBox = new ComboBox();
        comboBox.setMinWidth(150);
        comboBox.setMaxWidth(150);
//        ArrayList<Modulo> modulos = getModulos();
        ArrayList<Modulo> modulos = getModuloDiponivel();
        ObservableList strings = FXCollections.observableArrayList("");
        if (modulos != null) {
            if (!modulos.isEmpty()) {
                for (int i = 0; i < modulos.size(); i++) {
                    if (!strings.isEmpty()) {
                        strings.add(modulos.get(i).getNomeModulo());
                    }
                }
            }
        }
        comboBox.setItems(strings);
        comboBox.setOnAction(handle);
        return comboBox;
    }
}