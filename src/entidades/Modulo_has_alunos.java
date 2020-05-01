package entidades;
public class Modulo_has_alunos
{
    
    private int fkIdModulo;
    private int fkIdAluno;

    public int getFkIdModulo() {
        return fkIdModulo;
    }

    public void setFkIdModulo(int fkIdModulo) {
        this.fkIdModulo = fkIdModulo;
    }

    public int getFkIdAluno() {
        return fkIdAluno;
    }

    public void setFkIdAluno(int fkIdAluno) {
        this.fkIdAluno = fkIdAluno;
    }
    
}