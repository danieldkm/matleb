package tabela;

import banco.*;
import controller.QuestaoController;
import entidades.Fase;
import entidades.Questao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @author Administrador
 */
public class TabelaQuestao extends TableView {

    public TabelaQuestao() {

        TableColumn questaoCol = new TableColumn("Questão");
        questaoCol.setCellValueFactory(new PropertyValueFactory("nomeQuestao"));
        questaoCol.setMinWidth(150);
        questaoCol.setMaxWidth(150);
        TableColumn resultCol = new TableColumn("Resultado");
        resultCol.setCellValueFactory(new PropertyValueFactory("respostaQuestao"));
        resultCol.setMinWidth(150);
        resultCol.setMaxWidth(150);
        TableColumn faseCol = new TableColumn("Fase");
        faseCol.setCellValueFactory(new PropertyValueFactory("nomeFase"));
        faseCol.setMinWidth(150);

//        atualizarTabela();

        getColumns().addAll(questaoCol, resultCol, faseCol);

        setMaxSize(470, 400);
        setMinSize(470, 400);
    }
    public static ObservableList<QuestaoEspecial> lista = null;

    public static ObservableList<QuestaoEspecial> atualizarTabela(int idFase) {
        Dao<Questao> daoQuestao = new Dao(Questao.class);
        Dao<Fase> daoFase = new Dao(Fase.class);
        ObservableList<Fase> listaFase = null;
        ObservableList<Questao> listaQuestao = null;

        try {
            listaQuestao = FXCollections.observableArrayList(daoQuestao.selectById(idFase, 3, "sequenciaQuestao"));
            listaFase = FXCollections.observableArrayList(daoFase.select());
            lista = FXCollections.observableArrayList();
            if (QuestaoController.comboBox != null) {
                if (QuestaoController.comboBox.getValue() == null) {
                    System.err.println("erro: metodo call(), classe TabelaQuestao.java");
                } else if (QuestaoController.comboBox.getValue() != null || !QuestaoController.comboBox.getValue().equals("")) {
                    for (Fase f : listaFase) {
                        for (Questao q : listaQuestao) {
                            if (QuestaoController.comboBox.getValue().equals(f.getNomeFase())) {
                                if (q.getFkIdFase() == f.getIdFase()) {
                                    QuestaoEspecial t = new QuestaoEspecial();
                                    t.setIdQuestao(q.getIdQuestao());
                                    t.setNomeQuestao(q.getNomeQuestao());
                                    t.setRespostaQuestao(q.getRespostaQuestao());
                                    t.setFaseFkId(q.getFkIdFase());
                                    t.setNomeFase(f.getNomeFase());
                                    t.setSequencia(q.getSequenciaQuestao());
                                    lista.add(t);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    public static class QuestaoEspecial {

        int idQuestao;
        String nomeQuestao;
        String respostaQuestao;
        String nomeFase;
        int faseFkId;
        int sequencia;

        public int getSequencia() {
            return sequencia;
        }

        public void setSequencia(int sequencia) {
            this.sequencia = sequencia;
        }

        public int getIdQuestao() {
            return idQuestao;
        }

        public void setIdQuestao(int idQuestao) {
            this.idQuestao = idQuestao;
        }

        public int getFaseFkId() {
            return faseFkId;
        }

        public void setFaseFkId(int faseFkId) {
            this.faseFkId = faseFkId;
        }

        public String getRespostaQuestao() {
            return respostaQuestao;
        }

        public void setRespostaQuestao(String resultado) {
            this.respostaQuestao = resultado;
        }

        public String getNomeQuestao() {
            return nomeQuestao;
        }

        public void setNomeQuestao(String nomeQuestao) {
            this.nomeQuestao = nomeQuestao;
        }

        public String getNomeFase() {
            return nomeFase;
        }

        public void setNomeFase(String nomeFase) {
            this.nomeFase = nomeFase;
        }
    }
    private static int tempoProgessBar = 5;

    public static int getTempoProgessBar() {
        return tempoProgessBar;
    }

    public static void setTempoProgalessBar(int tempoProgessBar) {
        TabelaQuestao.tempoProgessBar = tempoProgessBar;
    }

    public static class TaskTableQuestao extends Task<ObservableList<QuestaoEspecial>> {

        @Override
        protected ObservableList<QuestaoEspecial> call() throws Exception {
            for (int i = 0; i < tempoProgessBar * 100; i++) {
                updateProgress(i, tempoProgessBar * 100);
                Thread.sleep(tempoProgessBar);
            }
//            Dao<Questao> daoQuestao = new Dao(Questao.class);
//            Dao<Fase> daoFase = new Dao(Fase.class);
//            ObservableList<Fase> listaFase = null;
//            ObservableList<Questao> listaQuestao = null;
//            ObservableList<QuestaoEspecial> lista = null;
//
//            try {
//                listaQuestao = FXCollections.observableArrayList(daoQuestao.select());
//                listaFase = FXCollections.observableArrayList(daoFase.select());
//                lista = FXCollections.observableArrayList();
//                if (QuestaoController.comboBox != null ) {
//                    if(QuestaoController.comboBox.getValue() == null ){
//                        System.err.println("erro: metodo call(), classe TabelaQuestao.java");
//                    } else 
//                    if (QuestaoController.comboBox.getValue() != null || !QuestaoController.comboBox.getValue().equals("")) {
//                        for (Fase f : listaFase) {
//                            for (Questao q : listaQuestao) {
//                                if (QuestaoController.comboBox.getValue().equals(f.getNomeFase())) {
//                                    if (q.getFkIdFase() == f.getIdFase()) {
//                                        QuestaoEspecial t = new QuestaoEspecial();
//                                        t.setIdQuestao(q.getIdQuestao());
//                                        t.setNomeQuestao(q.getNomeQuestao());
//                                        t.setRespostaQuestao(q.getRespostaQuestao());
//                                        t.setFaseFkId(q.getFkIdFase());
//                                        t.setNomeFase(f.getNomeFase());
//                                        lista.add(t);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }

//            return lista;
            return null;
        }
    }
}
