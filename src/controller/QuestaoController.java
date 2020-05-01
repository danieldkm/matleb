package controller;

import banco.*;
import static controller.ViewController.tabelaQuestao;
import entidades.Fase;
import entidades.Questao;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TextField;
import view.ViewQuestao;
import javafx.scene.input.MouseEvent;
import matleb.Config;
import tabela.TabelaQuestao;

public class QuestaoController extends ViewController {

    public static final int LARGURA_WINDOW = 1000;
    public static final int ALTURA_WINDOW = 500;
    public static TextField txtDigUm;
    public static TextField txtDigDois;
    public static TextField txtDigResult;
    public static String concatenaQuestao;
    public static TextField txtQtdQuestaoCadastrada;
    public static ComboBox<String> cbQuestao;
    public static ComboBox<String> comboBox;
    public static ViewQuestao vq = null;
    private static ArrayList<Fase> fases;
    private static Dao<Fase> dao;

//    
//    public static void getProgessTable() {
//        Task<ObservableList<TabelaQuestao.QuestaoEspecial>> task = new TabelaQuestao.TaskTableQuestao();
//        p.progressProperty().bind(task.progressProperty());
//        region.visibleProperty().bind(task.runningProperty());
//        p.visibleProperty().bind(task.runningProperty());
//        tabelaQuestao.itemsProperty().bind(task.valueProperty());
//        new Thread(task).start();
//    }
    public static void actionButtonAlterar(ActionEvent t) {
        Dao<Questao> daoQuestao = new Dao(Questao.class);
        if (validaCampos()) {
            Questao q = new Questao(codQuestao, concatenaQuestao, txtDigResult.getText(), codFase, tabelaQuestao.getSelectionModel().getSelectedIndex());
            try {
                for (int i = 0; i < TabelaQuestao.lista.size(); i++) {
//                    System.out.println("sequencia[" + i + "]: " + TabelaQuestao.lista.get(i).getSequencia() + " nome questao " + TabelaQuestao.lista.get(i).getNomeQuestao());
//                    System.out.println("-----------------");
                    daoQuestao.updateOrdemDaQuestao(TabelaQuestao.lista.get(i).getIdQuestao(), TabelaQuestao.lista.get(i).getSequencia());
                }
                daoQuestao.update(q);
                atualizarFaseBD("alterar", 0);
                changeButtonDisable("");
                tabelaQuestao.setItems(TabelaQuestao.atualizarTabela(codFase));
                limparCampos();
                codQuestao = 0;
                codFase = 0;
            } catch (Exception ex) {
                ex.printStackTrace();
                erro = true;
            }
        }
        if (!erro) {
            Dialogs.showInformationDialog(null, "Alteração realizado com sucesso!", "Concluído", "");
        }
    }

    public static void actionButtonDeletar(ActionEvent t) {
        Dao<Questao> dao = new Dao(Questao.class);
        if (validaCampos()) {
            try {
                changeButtonDisable("a");
                dao.delete(codQuestao, 0);
                atualizarFaseBD("delete", codFase);
                limparCampos();
                atualizaComboBox();
                tabelaQuestao.setItems(TabelaQuestao.atualizarTabela(codFase));
                codQuestao = 0;
            } catch (Exception ex) {
                ex.printStackTrace();
                erro = true;
            }
        }
        if (!erro) {
            Dialogs.showInformationDialog(null, "Deletado com sucesso!", "Concluído", "");
        }
    }

    public static void actionButtonCadastrar(ActionEvent t) {
        if (validaCampos()) {
            try {
                Dao<Questao> dao = new Dao(Questao.class);
                ArrayList<Fase> fases = getFases();
                int id = 0;
                int ultimoNumeroSequencial = 0;
                for (Fase fase : fases) {
                    if (fase.getNomeFase().equals(comboBox.getValue())) {
                        id = fase.getIdFase();
                        break;
                    }
                }

                atualizarFaseBD("cadastro", id);
                ArrayList<Questao> questoes = getQuestaoSeq(id);
                ultimoNumeroSequencial = questoes.size();

                Questao q = new Questao(0, concatenaQuestao, txtDigResult.getText(), id, ultimoNumeroSequencial);
                dao.insert(q);
                tabelaQuestao.setItems(TabelaQuestao.atualizarTabela(id));
                txtDigUm.requestFocus();
                atualizaComboBox();
                limparCampos();
            } catch (Exception ex) {
                ex.printStackTrace();
                erro = true;
            }
            if (!erro) {
//                Dialogs.showInformationDialog(null, "Cadastro realizado com sucesso!", "Concluido", ""); //mto chato esse dialog
            }
//        } else {
//            JOptionPane.showMessageDialog(null, "Erro: ", "Alguns campos não foram preenchidos", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void setOnMouseTabela(MouseEvent me) {
        try {

            String digUm, digDois;
            int index;
            TabelaQuestao.QuestaoEspecial qEspecial = (TabelaQuestao.QuestaoEspecial) tabelaQuestao.getSelectionModel().getSelectedItem();
            if (qEspecial != null) {
                ArrayList<Fase> fases = getFases();
                codQuestao = qEspecial.getIdQuestao();
                codFase = qEspecial.getFaseFkId();
                txtDigResult.setText(qEspecial.getRespostaQuestao());
                //valores default, necessarios para debug
                digUm = "1";
                digDois = "1";
                cbQuestao.getSelectionModel().select("+");
                if (!qEspecial.getNomeQuestao().contains(" ")) {
//                        System.out.println("nao contem espaco");
                } else {
                    index = qEspecial.getNomeQuestao().indexOf(" ");
                    digUm = qEspecial.getNomeQuestao().substring(0, index);
                    digDois = qEspecial.getNomeQuestao().substring((index + 3));
                    cbQuestao.getSelectionModel().select(qEspecial.getNomeQuestao().substring((index + 1), (index + 2)));
                }
                txtDigUm.setText(digUm);
                txtDigDois.setText(digDois);
                for (Fase fase : fases) {
                    if (qEspecial.getFaseFkId() == fase.getIdFase()) {
                        comboBox.getSelectionModel().select(fase.getNomeFase());
                    }
                }
                changeButtonDisable("cadastrar");
            }
            padraoCampos();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro: ao clicar na tabela Aluno. Metodo: setOnMouseTabela. Classe: QuestaoController");
        }
    }

    public static void atualizarFaseBD(String tipo, int id) {
        ArrayList<Fase> fases = getFases();
        Dao<Fase> dao = new Dao(Fase.class);
        if (!fases.isEmpty()) {
            if (tipo.equals("cadastro")) {
                for (Fase fase : fases) {
                    if (fase.getIdFase() == id) {
                        fase.setNumQuestao(fase.getNumQuestao() + 1);
                        fase.setPontQuestao(Config.PONTO_MAXIMO / fase.getNumQuestao());
                        Config.RESTO_DIVISAO = Config.PONTO_MAXIMO % fase.getNumQuestao();
                        System.out.println("resto divisao: " + Config.RESTO_DIVISAO);
                        try {
                            dao.update(fase);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Dialogs.showErrorDialog(null, "", "Erro", "");
                        }
                        break;
                    }
                }
            } else if (tipo.equals("delete")) {
                for (Fase fase : fases) {
                    if (fase.getIdFase() == id) {
                        if ((fase.getNumQuestao() - 1) == 0) {
                            fase.setPontQuestao(0);
                            fase.setNumQuestao(fase.getNumQuestao() - 1);
                        } else {
                            fase.setPontQuestao(Config.PONTO_MAXIMO / (fase.getNumQuestao() - 1));
                            fase.setNumQuestao(fase.getNumQuestao() - 1);
                            Config.RESTO_DIVISAO = Config.PONTO_MAXIMO % fase.getNumQuestao();
                        }
                        try {
                            dao.update(fase);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Dialogs.showErrorDialog(null, "Erro ao DELETAR o número de questões da fase", "Erro", "");
                        }
                        break;
                    }
                }
            } else if (tipo.equals("alterar")) {
                Dao<Fase> daoFase = new Dao(Fase.class);
                ArrayList<Questao> questoes = getQuestoes();
                for (Fase fase : fases) {
                    if (comboBox.getValue().equals(fase.getNomeFase())) { //verifica qual fase esta selecionado no combo box
                        codFase = fase.getIdFase(); // codFase recebe o id da fase que está selecionada na combo
                        for (Questao questao : questoes) {
                            if (codQuestao == questao.getIdQuestao()) {
                                if (codFase != questao.getFkIdFase()) { // caso a fase for diferente do selecionado aumenta + 1 do numero de questões e -1 para a fase que estava cadastrada
                                    for (Fase fase2 : fases) {
                                        if (fase2.getIdFase() == questao.getFkIdFase()) {
                                            fase2.setNumQuestao(fase2.getNumQuestao() - 1);
                                            if (fase2.getNumQuestao() != 0) {
                                                fase2.setPontQuestao(Config.PONTO_MAXIMO / fase2.getNumQuestao());
                                                Config.RESTO_DIVISAO = Config.PONTO_MAXIMO % fase.getNumQuestao();
                                            } else {
                                                fase2.setPontQuestao(0);
                                            }
                                            try {
                                                daoFase.update(fase2);
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                                Dialogs.showErrorDialog(null, "Erro ao atualizar o número de questões da fase", "Erro", "");
                                            }
                                        }
                                    }
                                    try {
                                        fase.setNumQuestao(fase.getNumQuestao() + 1);
                                        fase.setPontQuestao(Config.PONTO_MAXIMO / fase.getNumQuestao());
                                        daoFase.update(fase);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        Dialogs.showErrorDialog(null, "Erro ao atualizar o número de questões da fase", "Erro", "");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public static void padraoCampos() {
        if (!txtDigUm.getText().equals("")) {
            txtDigUm.setStyle(null);
        }
        if (!txtDigDois.getText().equals("")) {
            txtDigDois.setStyle(null);
        }
        if (!txtDigResult.getText().equals("")) {
            txtDigResult.setStyle(null);
        }

        if (comboBox.getValue() != null) {
            if (!comboBox.getSelectionModel().isEmpty() || !comboBox.getValue().equals("")) {
                comboBox.setStyle(null);
            }
        }

        if (cbQuestao.getValue() != null) {
            cbQuestao.setStyle(null);
            if (!cbQuestao.getSelectionModel().isEmpty() || !cbQuestao.getValue().equals("")) {
            }
        }
    }
    private static String erroVazioComboBoxTitle = "Aviso:";
    private static String erroVazioComboBoxMessage = "ComboBox não foi preenchido";
    private static String erroVazioCbQuestaoTitle = "Aviso:";
    private static String erroVazioCbQuestaoMessage = "Campo 2 não foi preenchido";
    private static String erroVazioTxtDigUmTitle = "Aviso:";
    private static String erroVazioTxtDigUmMessage = "Campo 1 não foi preenchido";
    private static String erroVazioTxtDigDoisTitle = "Aviso:";
    private static String erroVazioTxtDigDoisMessage = "Campo 3 não foi preenchido";
    private static String erroVazioTxtDigTresTitle = "Aviso:";
    private static String erroVazioTxtDigTresMessage = "Campo 4 não foi preenchido";
    private static String erroValortxtDigUmTitle = "Aviso:";
    private static String erroValorTxtDigUmMessage = "Campo 1 não foi preenchido";
    private static String erroValorTxtDigDoisTitle = "Aviso:";
    private static String erroValorTxtDigDoisMessage = "Campo 3 não foi preenchido";
    private static String erroValorTxtDigTresTitle = "Aviso:";
    private static String erroValorTxtDigTresMessage = "Campo 4 não foi preenchido";

    public static boolean validaCampos() {
        concatenaQuestao = txtDigUm.getText() + " " + cbQuestao.getSelectionModel().getSelectedItem() + " " + txtDigDois.getText();
        padraoCampos();
        if (comboBox.getValue() == null) {
            alertaNoCB(comboBox, erroVazioComboBoxTitle, erroVazioComboBoxMessage);
            return false;
        }

        if (comboBox.getSelectionModel().isEmpty() || comboBox.getValue().equals("")) {
            alertaNoCB(comboBox, erroVazioComboBoxTitle, erroVazioComboBoxMessage);
            return false;
        } else if (txtDigUm.getText().equals("")) {
            alertaNoTxt(txtDigUm, erroVazioTxtDigUmTitle, erroVazioTxtDigUmMessage);
            return false;
        } else if (cbQuestao.getValue() == null) {
            alertaNoCB(cbQuestao, erroVazioCbQuestaoTitle, erroVazioCbQuestaoMessage);
            return false;
        } else if (cbQuestao.getSelectionModel().isEmpty() || cbQuestao.getValue().equals("")) {
            alertaNoCB(cbQuestao, erroVazioCbQuestaoTitle, erroVazioCbQuestaoMessage);
            return false;
        } else if (txtDigDois.getText().equals("")) {
            alertaNoTxt(txtDigDois, erroVazioTxtDigDoisTitle, erroVazioTxtDigDoisMessage);
            return false;
        } else if (txtDigResult.getText().equals("")) {
            alertaNoTxt(txtDigResult, erroVazioTxtDigTresTitle, erroVazioTxtDigTresMessage);
            return false;
        }
        if (!txtDigUm.getText().equals("")) {
            if (!parseTxtIsNumber(txtDigUm, erroValortxtDigUmTitle, erroValorTxtDigUmMessage)) {
                return false;
            }
        }

        if (!txtDigDois.getText().equals("")) {
            if (!parseTxtIsNumber(txtDigDois, erroValorTxtDigDoisTitle, erroValorTxtDigDoisMessage)) {
                return false;
            }
        }
        if (!txtDigResult.getText().equals("")) {
            if (!parseTxtIsNumber(txtDigResult, erroValorTxtDigTresTitle, erroValorTxtDigTresMessage)) {
                return false;
            }
        }
        return true;
    }

    public static void limparCampos() {
        txtDigUm.clear();
        txtDigDois.clear();
        cbQuestao.getSelectionModel().selectFirst();
//        comboBox.getSelectionModel().selectFirst();
        txtDigResult.clear();

        txtDigUm.setStyle(null);
        txtDigDois.setStyle(null);
        cbQuestao.setStyle(null);
        comboBox.setStyle(null);
        txtDigResult.setStyle(null);

    }

    public static void atualizaComboBox() {
        final ArrayList<Fase> fases2 = QuestaoController.getFases();
        int idFase = 0;
        for (Fase fase : fases2) {
            if (fase != null) {
                if (comboBox.getValue().equals(fase.getNomeFase())) {
                    txtQtdQuestaoCadastrada.setText(Integer.toString(fase.getNumQuestao()));
                    idFase = fase.getIdFase();
                } else if (comboBox.getValue().equals("")) {
                    txtQtdQuestaoCadastrada.setText("");
                }
            }
        }


        tabelaQuestao.setItems(TabelaQuestao.atualizarTabela(idFase));
    }

    public static void setOnMouseActionPolygonAcima(MouseEvent t) {
        try {
            int n = tabelaQuestao.getSelectionModel().getSelectedIndex();
            TabelaQuestao.QuestaoEspecial qEspecial = (TabelaQuestao.QuestaoEspecial) QuestaoController.tabelaQuestao.getSelectionModel().getSelectedItem();
            if (n > 0) {
                if (qEspecial != null) {
//                    System.out.println("nome da fase selecionada " + qEspecial.getNomeFase());
                    TabelaQuestao.lista.get(n).setSequencia(n - 1);
                    TabelaQuestao.lista.get(n - 1).setSequencia(n);
                    TabelaQuestao.lista.remove(n);
                    TabelaQuestao.lista.add(n - 1, qEspecial);
//                    System.out.println("n " + n);
                } else {
                    Dialogs.showInformationDialog(null, "Selecione um item da tabela!", "Aviso", "");
                }
            }
            tabelaQuestao.getSelectionModel().select(n - 1);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro: remover ou alterar a posição dos itens. Metodo: setOnMouseActionPolygonAcima. Classe: QuestaoController");
        }
    }

    public static void setOnMouseActionPolygonAbaixo(MouseEvent t) {
        try {
            int n = tabelaQuestao.getSelectionModel().getSelectedIndex();
            TabelaQuestao.QuestaoEspecial qEspecial = (TabelaQuestao.QuestaoEspecial) QuestaoController.tabelaQuestao.getSelectionModel().getSelectedItem();
//            System.out.println("index " + n);
            if (n < (TabelaQuestao.lista.size() - 1)) {
                if (qEspecial != null) {
//                    System.out.println("nome da fase selecionada " + qEspecial.getNomeFase());
                    TabelaQuestao.lista.get(n).setSequencia(n + 1);
                    TabelaQuestao.lista.get(n + 1).setSequencia(n);
                    TabelaQuestao.lista.remove(n);
                    TabelaQuestao.lista.add(n + 1, qEspecial);
                } else {
                    Dialogs.showInformationDialog(null, "Selecione um item da tabela!", "Aviso", "");
                }
            }
            tabelaQuestao.getSelectionModel().select(n + 1);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro: remover ou alterar a posição dos itens. Metodo: setOnMouseActionPolygonAcima. Classe: QuestaoController");
        }
    }
}
