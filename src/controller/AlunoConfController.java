package controller;

import static controller.ViewController.getModulos;
import entidades.Aluno;
import entidades.Modulo;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import matleb.Config;

import view.ViewAlunoConf;

/**
 *
 * @author Daniel K. Morita
 */
public class AlunoConfController extends ViewController {

    protected static final int LARGURA_WINDOW = 500;
    protected static final int ALTURA_WINDOW = 200;
    public static ViewAlunoConf vac = null;
    protected static ComboBox comboBox;
    protected Button btnSalvar;
    protected AlunoConfHandle handle;
    protected Label lbl = new Label("Selecione o Jogo:");
    private static ArrayList<Modulo> modulos;

    public static Aluno getAlunoLogado() {
        ArrayList<Aluno> alunos = getAlunos();

//        for (int i = 0; i < alunos.size(); i++) {
//            System.out.println("aluno[" + i + "]: " + alunos.get(i).getLoginAluno());
//        }

        for (Aluno aluno : alunos) {
            if (aluno.getLoginAluno().equals(Config.ALUNO_LOGADO.getLoginAluno())) {
                return aluno;
            }
        }
        return null;
    }

//    public static ArrayList<Fase> getFaseMaxPossivel() {
//        ArrayList<Fase> fases = getFases();
//        ArrayList<Fase> fasesMax = new ArrayList();
//        Aluno aluno = getAlunoLogado();
//
////        System.out.println("faseMax do aluno: " + aluno.getFaseMax());
//        for (int i = 0; i < fases.size(); i++) {
//            if (aluno.getFaseMax() >= fases.get(i).getIdFase()) {
//                fasesMax.add(fases.get(i));
//            }
//        }
//        return fasesMax;
//    }
    public static void atualizarJogoEscolhido(ComboBox cb) {
        modulos = getModulos();
        if (modulos != null) {
            for (int i = 0; i < modulos.size(); i++) {
                if (cb.getValue().equals(modulos.get(i).getNomeModulo())) {
                    Config.MODULO_ESCOLHIDO = i;
                    Config.setTextoModuloAndAlunoLogado();
                    Dialogs.showInformationDialog(null, "\nJogo escolhido: " + modulos.get(i).getNomeModulo(), "Jogo!", "");
//                    FXDialog.showMessageDialog("\nJogo escolhido: " + modulos.get(i).getNomeModulo(), "Jogo!", Message.INFORMATION);
                    //FXDialog nao funciona no .jar
                }
            }
        }
    }

    public class AlunoConfHandle implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent t) {
            if (t.getSource() == comboBox) {
//                fases = getFaseMaxPossivel();
//                for (Fase fase : fases) {
//                    if (comboBox.getValue().equals(fase.getNomeFase())) {
//                        Config.COD_FASE = fase.getIdFase();
//                    }
//                }
            } else if (t.getSource() == btnSalvar) {
                atualizarJogoEscolhido(comboBox);
//                modulos = getModulos();
//                if (modulos != null) {
//                    for (int i = 0; i < modulos.size(); i++) {
//                        if (comboBox.getValue().equals(modulos.get(i).getNomeModulo())) {
//                            Config.MODULO_ESCOLHIDO = i;
//                            Config.setTextoModuloAndAlunoLogado();
//                            FXDialog.showMessageDialog("\nJogo escolhido: " + modulos.get(i).getNomeModulo(), "Jogo!", Message.INFORMATION);
//                        }
//                    }
//                }
            }
        }
    }
}
