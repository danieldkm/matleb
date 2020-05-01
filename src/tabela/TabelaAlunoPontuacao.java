package tabela;

import controller.ViewController;
import entidades.Aluno;
import entidades.Aluno_and_fases;
import entidades.Fase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import matleb.Config;

public class TabelaAlunoPontuacao extends TableView {

    public TabelaAlunoPontuacao() {
        TableColumn nomeAlunoCol = new TableColumn("Aluno");
        nomeAlunoCol.setCellValueFactory(new PropertyValueFactory("loginAluno"));
        nomeAlunoCol.setMinWidth(150);
        nomeAlunoCol.setMaxWidth(150);
        TableColumn nomeModuloCol = new TableColumn("Fase");
        nomeModuloCol.setCellValueFactory(new PropertyValueFactory("nomeFase"));
        nomeModuloCol.setMinWidth(150);
        nomeModuloCol.setMaxWidth(150);
        TableColumn pontoCol = new TableColumn("Ponto");
        pontoCol.setCellValueFactory(new PropertyValueFactory("score"));
        pontoCol.setMinWidth(150);
        pontoCol.setMaxWidth(150);

        getColumns().addAll(nomeAlunoCol, nomeModuloCol, pontoCol);

        setMaxSize(470, 400);
        setMinSize(470, 400);
    }

    public static ObservableList<TabelaAlunoPontuacao.AlunoPontuacao> atualizarTabelaAluno() {
        ObservableList<AlunoPontuacao> lista = FXCollections.observableArrayList();
        ObservableList<Aluno_and_fases> alunoAndFases = FXCollections.observableArrayList(ViewController.getAlunoAndFases());
        ObservableList<Fase> fases = FXCollections.observableArrayList(ViewController.getFases());
        ObservableList<Aluno> alunos = FXCollections.observableArrayList(ViewController.getAlunos());
        int idAluno = 0;
        for (Aluno aluno : alunos) {
            if (aluno.getLoginAluno().equals(Config.ALUNO_LOGADO.getLoginAluno())) {
                idAluno = aluno.getIdAluno();
            }
        }
        for (Aluno_and_fases aluno_and_fases : alunoAndFases) {
            if (aluno_and_fases.getFkIdAluno() == idAluno) {
                for (Fase fase : fases) {
                    if (aluno_and_fases.getFkIdFase() == fase.getIdFase()) {
//                            System.out.println("encontrei o nome da fase " + fase.getNomeFase());
                        AlunoPontuacao ap = new AlunoPontuacao();
                        ap.setLoginAluno(Config.ALUNO_LOGADO.getLoginAluno());
                        ap.setNomeFase(fase.getNomeFase());
                        ap.setScore(aluno_and_fases.getScore());
                        lista.add(ap);
                    }
                }
            }
        }
        return lista;
    }

    public static ObservableList<TabelaAlunoPontuacao.AlunoPontuacao> atualizarTabelaAdmin(String login) {
        ObservableList<Aluno> alunos = FXCollections.observableArrayList(ViewController.getAlunos());
        ObservableList<Fase> fases = FXCollections.observableArrayList(ViewController.getFases());
        ObservableList<AlunoPontuacao> lista = FXCollections.observableArrayList(); // lista que irá popular a tabela de pontuacao
        ObservableList<Aluno_and_fases> alunoAndFases = null;
        int idAluno = 0;
        for (Aluno aluno : alunos) {
            if (aluno.getLoginAluno().equals(Config.ALUNO_LOGADO.getLoginAluno())) {
                idAluno = aluno.getIdAluno();
            }
        }


        if (login.equals("Todos")) {
            alunoAndFases = FXCollections.observableArrayList(ViewController.getAlunoAndFases());
            String nomeAl = "";
            String nomeFase = "";
            for (Aluno_and_fases aluno_and_fases : alunoAndFases) {
                for (Fase fase : fases) {
                    if (fase.getIdFase() == aluno_and_fases.getFkIdFase()) {
                        nomeFase = fase.getNomeFase();
                        break;
                    }
                }
                for (Aluno aluno : alunos) {
                    if (aluno.getIdAluno() == aluno_and_fases.getFkIdAluno()) {
                        System.out.println("nome aluno " + aluno.getLoginAluno());
                        nomeAl = aluno.getLoginAluno();
                        break;
                    }
                }
                AlunoPontuacao ap = new AlunoPontuacao();
                ap.setLoginAluno(nomeAl);
                ap.setNomeFase(nomeFase);
                ap.setScore(aluno_and_fases.getScore());
                lista.add(ap);
            }

            return lista;
        } else {
            System.out.println("id do aluno  " + idAluno);
            alunoAndFases = FXCollections.observableArrayList(ViewController.getAlunoAndFasesById(idAluno, 0));
            String nomeAl = "";
            String nomeFase = "";
            for (Aluno_and_fases aluno_and_fases : alunoAndFases) {
                for (Fase fase : fases) {
                    if (fase.getIdFase() == aluno_and_fases.getFkIdFase()) {
                        nomeFase = fase.getNomeFase();
                        break;
                    }
                }
                for (Aluno aluno : alunos) {
                    if (aluno.getIdAluno() == aluno_and_fases.getFkIdAluno()) {
                        System.out.println("nome aluno " + aluno.getLoginAluno());
                        nomeAl = aluno.getLoginAluno();
                        break;
                    }
                }
                AlunoPontuacao ap = new AlunoPontuacao();
                ap.setLoginAluno(nomeAl);
                ap.setNomeFase(nomeFase);
                ap.setScore(aluno_and_fases.getScore());
                lista.add(ap);
            }

            return lista;
        }
    }

    public static ObservableList<TabelaAlunoPontuacao.AlunoPontuacao> atualizarTabela(int fkIdAluno) {
        try {
            ObservableList<Aluno> alunos = FXCollections.observableArrayList(ViewController.getAlunos());
            ObservableList<Fase> fases = FXCollections.observableArrayList(ViewController.getFases());
            ObservableList<Aluno_and_fases> alunoAndFases = null;
            if (Config.ALUNO_LOGADO.getNivel().equals("Aluno")) {
                alunoAndFases = FXCollections.observableArrayList(ViewController.getAlunoAndFasesById(fkIdAluno, 0));
//                AlunoPontuacaoController.cbNomeALuno.setVisible(false);
            } else if (Config.ALUNO_LOGADO.getNivel().equals("Administrador")) {
                if (fkIdAluno == -1 || fkIdAluno == 0) {
                    alunoAndFases = FXCollections.observableArrayList(ViewController.getAlunoAndFases());
                } else {
                    alunoAndFases = FXCollections.observableArrayList(ViewController.getAlunoAndFasesById(fkIdAluno, 0));
                }

            } else {
                System.err.println("Erro: nível não encontrado. Metodo: atualizarTabela. classe: TabelaAlunoPontuacao");
            }

            ObservableList<AlunoPontuacao> lista = FXCollections.observableArrayList(); // lista que irá popular a tabela de pontuacao
            int idAluno = 0;
            for (Aluno aluno : alunos) {
                if (aluno.getLoginAluno().equals(Config.ALUNO_LOGADO.getLoginAluno())) {
                    idAluno = aluno.getIdAluno();
                }
            }
            String nomeAl = "";
            String nomeFase = "";
            if (Config.ALUNO_LOGADO.getNivel().equals("Aluno")) {
                for (Aluno_and_fases aluno_and_fases : alunoAndFases) {
                    if (aluno_and_fases.getFkIdAluno() == idAluno) {
                        for (Fase fase : fases) {
                            if (aluno_and_fases.getFkIdFase() == fase.getIdFase()) {
//                            System.out.println("encontrei o nome da fase " + fase.getNomeFase());
                                AlunoPontuacao ap = new AlunoPontuacao();
                                ap.setLoginAluno(Config.ALUNO_LOGADO.getLoginAluno());
                                ap.setNomeFase(fase.getNomeFase());
                                ap.setScore(aluno_and_fases.getScore());
                                lista.add(ap);
                            }
                        }
                    }
                }
            } else if (Config.ALUNO_LOGADO.getNivel().equals("Administrador")) {
                for (Aluno_and_fases aluno_and_fases : alunoAndFases) {
                    for (Fase fase : fases) {
                        if (fase.getIdFase() == aluno_and_fases.getFkIdFase()) {
                            nomeFase = fase.getNomeFase();
                            break;
                        }
                    }
                    for (Aluno aluno : alunos) {
                        if (aluno.getIdAluno() == aluno_and_fases.getFkIdAluno()) {
                            nomeAl = aluno.getLoginAluno();
                            break;
                        }
                    }
                    AlunoPontuacao ap = new AlunoPontuacao();
                    ap.setLoginAluno(nomeAl);
                    ap.setNomeFase(nomeFase);
                    ap.setScore(aluno_and_fases.getScore());
                    lista.add(ap);
                }
            }
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro: ao atualiar a tabela de pontuacação do aluno. Metodo: atualizarTabela. classe: TabelaAlunoPontuacao");
        }
        return null;


    }

    public static class AlunoPontuacao {

        private String loginAluno;
        private String nomeFase;
        private int score;

        public String getLoginAluno() {
            return loginAluno;
        }

        public void setLoginAluno(String loginAluno) {
            this.loginAluno = loginAluno;
        }

        public String getNomeFase() {
            return nomeFase;
        }

        public void setNomeFase(String nomeFase) {
            this.nomeFase = nomeFase;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}