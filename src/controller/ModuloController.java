package controller;

import banco.Dao;
import static controller.ViewController.alertaNoTxt;
import entidades.Fase;
import entidades.Modulo;
import entidades.Modulo_has_alunos;
import entidades.Modulo_has_fases;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import matleb.Config;
import tabela.TabelaFase;
import view.ViewModulo;

/**
 *
 * @author Daniel K. Morita
 */
public class ModuloController extends ViewController {

    public static ViewModulo vm = null; // atributo para controlar as janela
    public static TextField txtNomeModulo;
    public static TabelaFase tabelaFase;
    public static TabelaFase tbFaseNova;
    public static Button btnOk;
    public static Button btnLimpar;
    public static ObservableList<Fase> listaNova = FXCollections.observableArrayList();
    public static ObservableList<Fase> lista = TabelaFase.getListaFases();
    public static Label lbNomeModulo = new Label("Nome do Jogo: ");
    private static int idModulo;
    protected Handler handler = new Handler();

    protected void setOnMousePolygon2(MouseEvent t) {
        int tamanho = lista.size();
        for (int i = 0; i < tamanho; i++) {
            listaNova.add(lista.get(0));
            lista.remove(0);
        }
        tbFaseNova.setItems(listaNova);
    }

    protected void setOnMousePolygon1(MouseEvent t) {
        int tamanho = listaNova.size();
        for (int i = 0; i < tamanho; i++) {
            lista.add(listaNova.get(0));
            listaNova.remove(0);
        }
        tabelaFase.setItems(lista);
    }

    private static void setCorrigirListaFase() {
        try {
            boolean ok = false;
            for (int i = 0; i < listaNova.size(); i++) {
                for (int j = 0; j < lista.size(); j++) {
                    if (listaNova.get(i).getIdFase() != lista.get(j).getIdFase()) {
                        ok = true;
                    }
                }
                if (lista.isEmpty()) {
                    ok = true;
                }
                if (ok) {
                    lista.add(listaNova.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("erro: atualizar a lista da fase. Metodo: setCorrigerListaFase. Classe ModuloController.");
        }
    }

    public static void setActionCbModulo(ComboBox cb) {
        try {
            setCorrigirListaFase();
            listaNova = FXCollections.observableArrayList();
            if (cb != null && cb.getValue() != null) {
                if (!cb.getValue().equals("")) {
                    txtNomeModulo.setText(cb.getValue().toString());
//                    txtNomeModulo.setEditable(false);
                }
            }
            ArrayList<Modulo> modulos = getModulos();
            ArrayList<Fase> fases = getFases();
            int idModulo = 0;
            for (Modulo modulo : modulos) {
                if (modulo.getNomeModulo().equals(txtNomeModulo.getText())) {
                    idModulo = modulo.getIdModulo();
                }
            }
            ArrayList<Modulo_has_fases> moduloFases = getModuloFaseSeq(idModulo);
            if (moduloFases != null) {
                for (int i = 0; i < moduloFases.size(); i++) {
                    if (idModulo == moduloFases.get(i).getFkIdModulo()) {
                        for (Fase fase : fases) {
                            if (fase.getIdFase() == moduloFases.get(i).getFkIdFase()) {
                                listaNova.add(fase);
                                for (int j = 0; j < lista.size(); j++) {
                                    if (fase.getIdFase() == lista.get(j).getIdFase()) {
                                        lista.remove(j);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            tbFaseNova.setItems(listaNova);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("erro: ao atualizar a combo Box. Metodo: setActionCbModulo. Classe ModuloController.");
        }
    }

    public static void setOnActionBtnLimpar(ActionEvent t) {
        try {
            try {
                lista = TabelaFase.getListaFases();
                tabelaFase.setItems(lista);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Erro: ao atualizar a tabela de fase, metodo: setOnActionBtnLimpar, classe: ModuloController");
            }
            int tamanho = listaNova.size();
            // reseta a tabela de modulo - inicio -
            for (int i = (tamanho - 1); i >= 0; i--) {
                listaNova.remove(i);
            }
            // reseta a tabela de modulo - fim -
            if (txtNomeModulo != null) {
                txtNomeModulo.setText("");
                txtNomeModulo.setEditable(true);
            }
            if (cbModulos != null) {
                getPopularcbModulos();
                cbModulos.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro: ao limpar os campos -> metodo: setOnActionBtnLimpar -> classe: ModuloController");
        }
    }

    private static boolean getValidarCampo() {
        if (txtNomeModulo.getText().equals("")) {
            alertaNoTxt(txtNomeModulo, "Erro: ", "O nome do Modulo não foi preenchido!!");
            return false;
        }
        txtNomeModulo.setStyle(null);
        return true;
    }

    private static boolean getVerificarNomeModulo() {
        ArrayList<Modulo> modulos = getModulos();
        for (Modulo modulo : modulos) {
            if (txtNomeModulo.getText().equals(modulo.getNomeModulo())) {
                return false;
            }
        }
        return true;
    }

    private static void atualizarComboBox() {
//        cbModulos.set;
        cbModulos.setItems(getPopularcbModulos());
    }

    public static void setOnActionBtnCadastrar(ActionEvent t) {
        try {
            if (getValidarCampo()) {
                if (getVerificarNomeModulo()) {
                    Dao<Modulo_has_fases> daoModuloWithfases = new Dao(Modulo_has_fases.class);
                    Dao<Modulo> daoModulo = new Dao(Modulo.class);
                    Modulo m = new Modulo();
                    m.setNomeModulo(txtNomeModulo.getText());
                    daoModulo.insert(m);
                    ArrayList<Modulo> modulos = getModulos();
                    int idModulo = modulos.get(modulos.size() - 1).getIdModulo();
                    for (int i = 0; i < listaNova.size(); i++) {
                        Modulo_has_fases mf = new Modulo_has_fases();
                        mf.setFkIdFase(listaNova.get(i).getIdFase());
                        mf.setFkIdModulo(idModulo);
                        mf.setSequenciaFase(i + 1);
                        daoModuloWithfases.insert(mf);
                    }
                    atualizarComboBox();
                    Dialogs.showInformationDialog(null, "Cadastrado com sucesso", "Aviso!", "");
                } else {
                    Dialogs.showErrorDialog(null, "Nome já existe!\nVerifique, cadastre um nome válido.", "Erro", "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro: ao gravar fases no modulo. Metodo: setOnActionBtnCadastrar. Classe: ModuloController");
        }
    }

    private static void acharIdModulo() {
        ArrayList<Modulo> modulos = getModulos();
        idModulo = -1;// achar o ID do modulo 
        for (Modulo modulo : modulos) {
            if (modulo.getNomeModulo().equals(cbModulos.getSelectionModel().getSelectedItem())) {
                idModulo = modulo.getIdModulo();
            }
        }
    }

    public static void setOnActionBtnAlterar(ActionEvent t) {
        boolean ok = true;
//        Dialogs.DialogResponse n = Dialogs.showConfirmDialog(null, "Deseja Alterar?", "Este Modulo será alterado, assim como todo o seu relacionamento com a tabela Aluno e o Modulo!!", "Deletar Fase", Dialogs.DialogOptions.YES_NO_CANCEL);
        try {
//            if (n.name().equals("YES")) {
            if (true) {
                Dao<Modulo_has_fases> daoModuloWithfases = new Dao(Modulo_has_fases.class);
                acharIdModulo();
//            int idModulo = -1;// achar o ID do modulo 
//            for (Modulo modulo : modulos) {
//                if (modulo.getNomeModulo().equals(txtNomeModulo.getText())) {
//                    idModulo = modulo.getIdModulo();
//                }
//            }
                Dao<Modulo> daoM = new Dao(Modulo.class);
                Modulo m = new Modulo();
                m.setIdModulo(idModulo);
                m.setNomeModulo(txtNomeModulo.getText());
                daoM.update(m);

                daoModuloWithfases.delete(idModulo, 0);
                if (idModulo != -1) {
                    for (int i = 0; i < listaNova.size(); i++) {
                        Modulo_has_fases mf = new Modulo_has_fases();
                        mf.setFkIdFase(listaNova.get(i).getIdFase());
                        mf.setFkIdModulo(idModulo);
                        mf.setSequenciaFase(i + 1);
                        daoModuloWithfases.insert(mf);
//                    System.out.println("Fase " + listaNova.get(i).getNomeFase() + " sequencia " + mf.getSequenciaFase() + " i " + i);
                    }
                }
                atualizarComboBox();
            } else {
                ok = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
            System.err.println("Erro: ao alterar o modulo. Metodo: setOnActionBtnAlterar. Classe: ModuloController");
        }
        if (ok) {
            Dialogs.showInformationDialog(null, "Jogo selecionado foi alterado com sucesso", "Aviso", "");
        }
    }

    public static void setOnActionBtnDeletar(ActionEvent t) {
        boolean ok = true;
        Dialogs.DialogResponse n = Dialogs.showConfirmDialog(null, "Deseja realmente deletar?", "Este JOGO será deletado, assim como todo o seu relacionamento com Aluno e Fase!!", "Deletar Jogo", Dialogs.DialogOptions.YES_NO_CANCEL);
        try {
            if (n.name().equals("YES")) {
                acharIdModulo();
                Dao<Modulo_has_fases> daoModuloWithfases = new Dao(Modulo_has_fases.class);
                Dao<Modulo_has_alunos> daoModuloAlunos = new Dao(Modulo_has_alunos.class);
                Dao<Modulo> daoModulo = new Dao(Modulo.class);
                daoModuloWithfases.delete(idModulo, 0);
                daoModuloAlunos.delete(idModulo, 0);
                daoModulo.delete(idModulo, 0);
            } else {
                ok = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
            System.err.println("Erro: ao deletar o modulo. Metodo: setOnActionBtnDeletar. Classe: ModuloController");
        }
        if (ok) {
            atualizarComboBox();
            Dialogs.showInformationDialog(null, "Jogo selecionado foi deletado com sucesso!", "Aviso", "");
        }
    }

    public static void setOnMouseActionPolygonAcima(MouseEvent t) {
        try {
            int n = tbFaseNova.getSelectionModel().getSelectedIndex();
            Fase f = (Fase) tbFaseNova.getSelectionModel().getSelectedItem();
            if (n > 0) {
                if (f != null) {
                    listaNova.remove(n);
                    listaNova.add(n - 1, f);
                } else {
                    Dialogs.showInformationDialog(null, "Selecione um item da tabela!", "Aviso", "");
                }
            }
            tbFaseNova.getSelectionModel().select(n - 1);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro: remover ou alterar a posição dos itens. Metodo: setOnMouseActionPolygonAcima. Classe: ModuloController");
        }
    }

    public static void setOnMouseActionPolygonAbaixo(MouseEvent t) {
        try {
            int n = tbFaseNova.getSelectionModel().getSelectedIndex();
            Fase f = (Fase) tbFaseNova.getSelectionModel().getSelectedItem();
            if (n < (listaNova.size() - 1)) {
                if (f != null) {
                    listaNova.remove(n);
                    listaNova.add(n + 1, f);
                } else {
                    Dialogs.showInformationDialog(null, "Selecione um item da tabela!", "Aviso", "");
                }
            }
            tbFaseNova.getSelectionModel().select(n + 1);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro: remover ou alterar a posição dos itens. Metodo: setOnMouseActionPolygonAcima. Classe: ModuloController");
        }

    }

    public class Handler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent t) {
            if (t.getSource() == cbModulos) {
                setActionCbModulo(cbModulos);
                changeButtonDisable("cadastrar");

            } else if (t.getSource() == btnDeletar) {
                setOnActionBtnDeletar(t);
                setChangeBtnAndFireClean();

            } else if (t.getSource() == btnAlterar) {
                setOnActionBtnAlterar(t);
                setChangeBtnAndFireClean();
                Config.setTextoModuloAndAlunoLogado();
            } else if (t.getSource() == btnCadastrar) {
                setOnActionBtnCadastrar(t);
                setChangeBtnAndFireClean();

            } else if (t.getSource() == btnLimpar) {
                setOnActionBtnLimpar(t);
                changeButtonDisable("");
            }

        }

        private void setChangeBtnAndFireClean() {
            btnLimpar.arm();
            btnLimpar.fire();
            changeButtonDisable("");
        }
    }
}
