package entidades;
public class Aluno
{
    private int idAluno;
    private String loginAluno;
    private String senhaAluno;
    private String nivel;
    public String getNivel(){return nivel;}
    public void setNivel(String nivel){this.nivel = nivel;}  
    public int getIdAluno(){return idAluno;}
    public void setIdAluno(int idAluno){this.idAluno = idAluno;}
    public String getLoginAluno(){return loginAluno;}
    public void setLoginAluno(String loginAluno){this.loginAluno = loginAluno;}
    public String getSenhaAluno(){return senhaAluno;}
    public void setSenhaAluno(String senhaAluno){this.senhaAluno = senhaAluno;}        
}