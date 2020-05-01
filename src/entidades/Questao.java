package entidades;
public class Questao
{
    private int idQuestao;
    private String nomeQuestao;
    private String respostaQuestao;
    private int fkIdFase;
    private int sequenciaQuestao;

    public Questao() {
    }

    public Questao(int idQuestao, String nomeQuestao, String respostaQuestao, int fkIdFase, int sequencia) {
        this.idQuestao = idQuestao;
        this.nomeQuestao = nomeQuestao;
        this.respostaQuestao = respostaQuestao;
        this.fkIdFase = fkIdFase;
        sequenciaQuestao = sequencia;
    }

    public int getIdQuestao(){return idQuestao;}
    public void setIdQuestao(int idQuestao){this.idQuestao = idQuestao;}
    public String getNomeQuestao(){return nomeQuestao;}
    public void setNomeQuestao(String nomeQuestao){this.nomeQuestao = nomeQuestao;}
    public String getRespostaQuestao(){return respostaQuestao;}
    public void setRespostaQuestao(String respostaQuestao){this.respostaQuestao = respostaQuestao;}
    public void setFkIdFase(int fkIdFase){this.fkIdFase = fkIdFase;}
    public int getFkIdFase(){return fkIdFase;}
    public int getSequenciaQuestao() {return sequenciaQuestao;}
    public void setSequenciaQuestao(int sequenciaQuestao) {this.sequenciaQuestao = sequenciaQuestao;}
}
