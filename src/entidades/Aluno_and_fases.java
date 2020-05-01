package entidades;
public class Aluno_and_fases
{
    private int fkIdAluno;
    private int fkIdFase;
    private int score;

    public int getFkIdAluno() {return fkIdAluno;}
    public void setFkIdAluno(int fkIdAluno) {this.fkIdAluno = fkIdAluno;}
    public int getFkIdFase() {return fkIdFase;}
    public void setFkIdFase(int fkIdFase) {this.fkIdFase = fkIdFase;}
    public int getScore() {return score;}
    public void setScore(int score) {this.score = score;} 
}