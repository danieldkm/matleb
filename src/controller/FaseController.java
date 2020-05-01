package controller;

import banco.*;
import entidades.Aluno_and_fases;
import entidades.Fase;
import entidades.Modulo_has_fases;
import entidades.Questao;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import tabela.TabelaFase;
import tabela.TabelaQuestao;
import view.ViewFase;
import static view.ViewFase.padraoCampos;

public class FaseController extends ViewController {

    private static int pontoMinimo = 0;
    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    protected static final int LARGURA_WINDOW = 1000;
    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    protected static final int ALTURA_WINDOW = 650;
    public static ViewFase vf = null; // atributo para controlar a janela
    protected static TabelaFase tabelaFase = new TabelaFase();
    protected static TextField txtBusca;
    protected static TextField txtNomeFase;
    protected static TextField txtQtdQuestaoLiberadas;
    protected static TextField txtPontosQuestao;
    protected static TextField txtTempoMaxQuestao;
    protected static TextField txtTolerancia;
    protected static TextField txtDesconto;
    protected static TextField txtPontoMinMudancaFase;
    protected static TextField txtPontoMaxPossivel;
    protected static CheckBox chAleatorio;
    private static boolean isCadastrar = false;

//    public static void getProgessTable() {
//        try{
//        Task<ObservableList<Fase>> task = new TabelaFase.TaskTableFase();
//        p.progressProperty().bind(task.progressProperty());
//        region.visibleProperty().bind(task.runningProperty());
//        p.visibleProperty().bind(task.runningProperty());
//        tabelaFase.itemsProperty().bind(task.valueProperty());
//        new Thread(task).start();
//        } catch(Exception e){
//            e.printStackTrace();
//            System.err.println("Erro ao atualizar a tabela, metodo: getProgessTable()\nClasse: FaseController.java");
//        }
//    }
    public static int getPontoMinimo() {
        return pontoMinimo;
    }

    public static void setPontoMinimo(int p) {
        pontoMinimo = p;
    }

//    public static int getPontoMaximo() {
//        return Config.pontoMaximo;
//    }
    public static void actionButtonNovo(ActionEvent t) {
        ViewFase.limparCampos();
        changeButtonDisable("a");
    }

    public static void actionButtonAlterar(ActionEvent t) {
        isCadastrar = false;
        if (validarCampos()) {
            try {
                Dao<Fase> dao = new Dao(Fase.class);
                Fase f = new Fase();
//                        f.setIdFase(Integer.parseInt(txtCodFase.getText()));
                f.setIdFase(codFase);
                f.setDesconto(Integer.parseInt(txtDesconto.getText()));
                f.setNomeFase(txtNomeFase.getText());
                f.setNumQuestao(Integer.parseInt(txtQtdQuestaoLiberadas.getText()));
                f.setPontQuestao(Integer.parseInt(txtPontosQuestao.getText()));
                f.setTempoMax(Integer.parseInt(txtTempoMaxQuestao.getText()));
                f.setTolerancia(Integer.parseInt(txtTolerancia.getText()));
                f.setPontMin(Float.parseFloat(txtPontoMinMudancaFase.getText()) / 100);
                int aleatorio = -1;
                if (chAleatorio.isSelected()) {
                    aleatorio = 0;
                } else {
                    aleatorio = 1;
                }
                f.setAleatorio(aleatorio);
                dao.update(f);
                changeButtonDisable("a");
                atualizaTabela();
                erro = false;
            } catch (Exception ex) {
                ex.printStackTrace();
                erro = true;
                Dialogs.showErrorDialog(null, "Erro ao alterar a Fase!", "Erro", "");
            }
            if (erro == false) {
                Dialogs.showInformationDialog(null, "Alterado com sucesso!", "Concluído", "");
            }
        }
    }

    public static void actionButtonDeletar(ActionEvent t) {

        Dialogs.DialogResponse n = Dialogs.showConfirmDialog(null, "Deseja realmente deletar?", "Esta FASE será deletado, assim como todo o seu relacionamento com a tabela Aluno e o Modulo!!", "Deletar Fase", Dialogs.DialogOptions.YES_NO_CANCEL);
        try {
            if (n.name().equals("YES")) {
                Dao<Aluno_and_fases> daoAlunoFases = new Dao(Aluno_and_fases.class);
                Dao<Modulo_has_fases> daoModuloFases = new Dao(Modulo_has_fases.class);
                Dao<Fase> daoFase = new Dao(Fase.class);
                Dao<Questao> daoQuestao = new Dao(Questao.class);
                ArrayList<Questao> questoes = ViewController.getQuestoes();
                for (Questao questao : questoes) {
                    if (questao.getFkIdFase() == codFase) {
                        daoQuestao.delete(questao.getIdQuestao(), 0);
                    }
                }
                daoAlunoFases.delete(codFase, 1);
                daoModuloFases.delete(codFase, 1);
                daoFase.delete(codFase, 0);
                ViewFase.limparCampos();
                changeButtonDisable("");
//                tabelaQuestao = new TabelaQuestao();
//                QuestaoController.getProgessTable();
                TabelaQuestao.atualizarTabela(codFase);
                erro = false;
                atualizaTabela();
            }
        } catch (Exception ex) {
            Logger.getLogger(ViewFase.class.getName()).log(Level.SEVERE, null, ex);
            erro = true;
            Dialogs.showErrorDialog(null, "Erro ao deletar a Fase!", "Erro", "");

        }
        if (erro
                == false) {
            Dialogs.showInformationDialog(null, "Deletado com sucesso!", "Concluído", "");
        }
    }

    public static void actionButtonCadastrar(ActionEvent t) {
        isCadastrar = true;
        if (validarCampos()) {
            try {
                Dao<Fase> dao = new Dao(Fase.class);
                Fase f = new Fase();
//                        f.setIdFase(0);

                f.setDesconto(Integer.parseInt(txtDesconto.getText()));
                f.setNomeFase(txtNomeFase.getText());
                f.setPontQuestao(Integer.parseInt(txtPontosQuestao.getText()));
                f.setTempoMax(Integer.parseInt(txtTempoMaxQuestao.getText()));
                f.setTolerancia(Integer.parseInt(txtTolerancia.getText()));
                f.setPontMin(Float.parseFloat(txtPontoMinMudancaFase.getText()) / 100);
                int aleatorio = -1;

                if (chAleatorio.isSelected()) {
                    aleatorio = 0;
                } else {
                    aleatorio = 1;
                }

                f.setAleatorio(aleatorio);

                dao.insert(f);

                atualizaTabela();

                changeButtonDisable(
                        "cadastrar");
                ViewFase.limparCampos();
                erro = false;
            } catch (NumberFormatException n) {
                n.printStackTrace();
                erro = true;
                Dialogs.showErrorDialog(null, "Alguns campos não foram preenchidos corretamente\nInforme o valor corretamente.", "Erro", "");

            } catch (Exception ex) {
                Logger.getLogger(ViewFase.class
                        .getName()).log(Level.SEVERE, null, ex);
                Dialogs.showErrorDialog(
                        null, "Erro ao tentar cadastrar uma nova fase no SGDB..", "Erro", "");

                ex.printStackTrace();
                erro = true;
            }
            if (erro == false) {
                Dialogs.showInformationDialog(null, "Cadastrado com sucesso!", "Concluído", "");
            }

        }
    }

    protected void setOnMouseTabela(MouseEvent me) {
        Fase f = (Fase) tabelaFase.getSelectionModel().getSelectedItem();
        if (f != null) {
            codFase = f.getIdFase();
            txtDesconto.setText(Integer.toString(f.getDesconto()));
            txtNomeFase.setText(f.getNomeFase());
            txtPontosQuestao.setText(Integer.toString(f.getPontQuestao()));
            txtQtdQuestaoLiberadas.setText(Integer.toString(f.getNumQuestao()));
            txtTempoMaxQuestao.setText(Integer.toString(f.getTempoMax()));
            txtTolerancia.setText(Integer.toString(f.getTolerancia()));
            float pontMin = f.getPontMin() * 100;
            int pontMinAsInteger = (int) pontMin;
            txtPontoMinMudancaFase.setText(String.valueOf(pontMinAsInteger));
            boolean aleatorio = false;
            if (f.getAleatorio() == 0) {
                aleatorio = true;
            } else {
                aleatorio = false;
            }
            chAleatorio.setSelected(aleatorio);
            changeButtonDisable("cadastrar");
        }
        padraoCampos();
    }
    private static String erroVazioNomeFaseTitle = "Campo não preenchido:";
    private static String erroVazioNomeFaseMessage = "Campo Nome da fase não foi preenchido\nFavor preencher corretamente";
    private static String erroNomeExistenteFaseTitle = "Nome da fase já existe";
    private static String erroNomeExistenteFaseMessage = "Nome informado já existe\nAltere o nome!";
    private static String erroVazioTempoMaxQuestaoTitle = "Campo não preenchido:";
    private static String erroVazioTempoMaxQuestaoMessage = "Campo 'Tempo Máximo' não foi preenchido\nFavor preencher corretamente";
    private static String erroValorTempoMaxQuestaoTitle = "Valor Inválido:";
    private static String erroValorTempoMaxQuestaoMessage = "Valor do campo Tempo Máximo não é válido\nFavor preencher corretamente";
    private static String erroVazioToleranciaTitle = "Campo não preenchido:";
    private static String erroVazioToleranciaMessage = "Campo Tolerância não foi preenchido\nFavor preencher corretamente";
    private static String erroValorToleranciaTitle = "Valor Inválido:";
    private static String errovalorToleranciaMessage = "Valor do campo Tolerancia nao é válido\nFavor preencher corretamente";
    private static String erroVazioDescontoTitle = "Campo não preenchido:";
    private static String erroVazioDescontoMessage = "Campo Desconto não foi preenchido\nFavor preencher corretamente";
    private static String erroValorDescontoTitle = "Valor Inválido:";
    private static String erroValorDescontoMessage = "Valor do campo Desconto não é válido\nFavor preencher corretamente";
    private static String erroVazioPontoMinMudancaFaseTitle = "Campo não preenchido:";
    private static String erroVazioPontoMinMudancaFaseMessage = "Campo Ponto Mínimo não foi preenchido\nFavor preencher corretamente";
    private static String erroValorPontoMinMudancaFaseTitle = "Valor Inválido:";
    private static String erroValorPontoMinMudancaFaseMessage = "Valor do campo Ponto Mínimo não é válido\nFavor preencher corretamente";
    private static String erroMaior100PontoMinMudancaFaseTitle = "Erro:";
    private static String erroMaior100PontoMinMudancaFaseMessage = "Pnto mínimo é em porcentagem\nNao deve ultrapassar os 100 %";

    /**
     * @return false se entrar em alguma condição e true se não entrar.
     */
    public static boolean validarCampos() {
        ViewFase.padraoCampos();
        if (txtNomeFase.getText().equals("")) {
            alertaNoTxt(txtNomeFase, erroVazioNomeFaseTitle, erroVazioNomeFaseMessage);
            return false;
        } else {
            if (isCadastrar) {
                for (Fase fase : ViewController.getFases()) {
                    if (txtNomeFase.getText().equals(fase.getNomeFase())) {
                        alertaNoTxt(txtNomeFase, erroNomeExistenteFaseTitle, erroNomeExistenteFaseMessage);
                        return false;
                    }
                }
            }
        }
        if (txtTempoMaxQuestao.getText().equals("")) {
            alertaNoTxt(txtTempoMaxQuestao, erroVazioTempoMaxQuestaoTitle, erroVazioTempoMaxQuestaoMessage);
            return false;
        }
        if (!txtTempoMaxQuestao.getText().equals("")) {
            if (!parseTxtIsNumber(txtTempoMaxQuestao, erroValorTempoMaxQuestaoTitle, erroValorTempoMaxQuestaoMessage)) {
                return false;
            }
        }
        if (txtTolerancia.getText().equals("")) {
            alertaNoTxt(txtTolerancia, erroVazioToleranciaTitle, erroVazioToleranciaMessage);
            return false;
        }
        if (!txtTolerancia.getText().equals("")) {
            if (!parseTxtIsNumber(txtTolerancia, erroValorToleranciaTitle, errovalorToleranciaMessage)) {
                return false;
            }
        }
        if (txtDesconto.getText().equals("")) {
            alertaNoTxt(txtDesconto, erroVazioDescontoTitle, erroVazioDescontoMessage);
            return false;
        }
        if (!txtDesconto.getText().equals("")) {
            if (!parseTxtIsNumber(txtDesconto, erroValorDescontoTitle, erroValorDescontoMessage)) {
                return false;
            }
        }
        if (txtPontoMinMudancaFase.getText().equals("")) {
            alertaNoTxt(txtPontoMinMudancaFase, erroVazioPontoMinMudancaFaseTitle, erroVazioPontoMinMudancaFaseMessage);
            return false;
        }
        if (!txtPontoMinMudancaFase.getText().equals("")) {
            if (!parseTxtIsNumber(txtPontoMinMudancaFase, erroValorPontoMinMudancaFaseTitle, erroValorPontoMinMudancaFaseMessage)) {
                return false;
            }
        }
        pontoMinimo = Integer.parseInt(txtPontoMinMudancaFase.getText());
        if (pontoMinimo > 100) {
            alertaNoTxt(txtPontoMinMudancaFase, erroMaior100PontoMinMudancaFaseTitle, erroMaior100PontoMinMudancaFaseMessage);
            return false;
        }
        return true;
    }

    /**
     * @deprecated metodo inutil do daniel san
     */
    public static void carregarDaniel() {
//        System.out.println("metodo carregar");
        Dao<Fase> dao = new Dao(Fase.class);
        ArrayList<Fase> fases = null;

        try {
            fases = dao.select();
        } catch (Exception ex) {
            Logger.getLogger(ViewFase.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
//            System.out.println("Erro ao fazer select da fase");
        }

        txtNomeFase.setText(
                "ML " + Integer.toString(fases.get(fases.size() - 1).getIdFase() + 1));
        txtQtdQuestaoLiberadas.setText(
                "0");
//        txtPontosQuestao.setText("0");
        txtTempoMaxQuestao.setText(
                "10");
        txtTolerancia.setText(
                "2");
        txtDesconto.setText(
                "5");
        txtPontoMaxPossivel.setText(
                "1000");
        txtPontoMinMudancaFase.setText(
                "0");
    }

    public static void atualizaTabela() throws Exception {
//        TabelaFase.setTempoProgessBar(2);
//        getProgessTable();
        tabelaFase.setItems(TabelaFase.getListaFases());
    }
    /**
     * // * Método para limitar a quantidade de caracteres no textField // * //
     *
     * @param String oldValue -> valor antigo //
     * @param String newValue -> valor novo //
     * @param TextField texto-> texto a ser redefinido //
     */
//    public static void verificaQtdDigit(String oldValue, String newValue, TextField texto) {
//        try {
//            // forçar valor numérico, redefinindo a valor antigo se a exceção é lançada
////                    Integer.parseInt(newValue);
//            // forçar comprimento correto, redefinindo para o valor antigo, se for mais do que maxLength
//            if (newValue.length() > maxLength) {
//                texto.setText(oldValue);
//            }
//        } catch (Exception e) {
//            texto.setText(oldValue);
//        }
//    }
}
