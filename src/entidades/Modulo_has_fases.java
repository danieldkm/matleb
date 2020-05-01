package entidades;
public class Modulo_has_fases
{
    private int fkIdModulo;
    private int fkIdFase;
    private int sequenciaFase;

    public int getFkIdModulo() {return fkIdModulo;}
    public void setFkIdModulo(int fkIdModulo) {this.fkIdModulo = fkIdModulo;}
    public int getFkIdFase() {return fkIdFase;}
    public void setFkIdFase(int fkIdFase) {this.fkIdFase = fkIdFase;}
    public int getSequenciaFase() {return sequenciaFase;}
    public void setSequenciaFase(int sequenciaFase) {this.sequenciaFase = sequenciaFase;}    
}