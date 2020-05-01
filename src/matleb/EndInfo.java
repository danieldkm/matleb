package matleb;
public class EndInfo
{
    int score;
    int acertos;
    int errors;
        
    public EndInfo(int score, int acertos, int errors) {
        this.score = score;
        this.acertos = acertos;
        this.errors = errors;
    }
        
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getAcertos() {
        return acertos;
    }

    public void setAcertos(int acertos) {
        this.acertos = acertos;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }        
}
