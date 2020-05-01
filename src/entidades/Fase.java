package entidades; //todos os tempos em segundos
public class Fase
{
    private int idFase;
    private String nomeFase;
    private int numQuestoes;
    private int pontQuestao;
    private float pontMin;
    private int tempoMax; 
    private int tolerancia;
    private int desconto;
    private int aleatorio;
    public int getIdFase(){return idFase;}
    public void setIdFase(int idFase){this.idFase = idFase;}
    public String getNomeFase(){return nomeFase;}
    public void setNomeFase(String nomeFase){this.nomeFase = nomeFase;}
    public int getNumQuestao(){return numQuestoes;}
    public void setNumQuestao(int numQuestao) {this.numQuestoes = numQuestao;}
    public int getPontQuestao(){return pontQuestao;}
    public void setPontQuestao(int pontQuestao){this.pontQuestao = pontQuestao;}
    public float getPontMin(){return pontMin;}
    public void setPontMin(float pontMin){this.pontMin = pontMin;}
    public int getTempoMax(){return tempoMax;}
    public void setTempoMax(int tempoMax){this.tempoMax = tempoMax;}
    public int getTolerancia(){return tolerancia;}
    public void setTolerancia(int tolerancia){this.tolerancia = tolerancia;}
    public int getDesconto(){return desconto;}
    public void setDesconto(int desconto){this.desconto = desconto;}
    public int getAleatorio() {return aleatorio;}
    public void setAleatorio(int aleatorio){this.aleatorio = aleatorio;}
        
    @Override
    public String toString() {
        return "Fase{" + "idFase=" + idFase + ", nomeFase=" + nomeFase + ", numQuestoes=" + numQuestoes + ", pontQuestao=" + pontQuestao + ", porcentagemMin=" + pontMin + ", tempoMax=" + tempoMax + ", tolerancia=" + tolerancia + ", desconto=" + desconto + '}';
    }
}
