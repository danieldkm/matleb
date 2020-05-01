package banco;

import entidades.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Dao<Type> {

    Class<Type> type;
    private static Connection con;

    public Dao(Class type) {
        this.type = type;
        if (con == null) {
            con = Conexao.getInstance();
        }
    }

    public static void setCon(Connection c) {
        con = c;
    }

    public static Connection getCon() {
        return con;
    }

    public int deleteFaseOfModulo(int idModulo, int idFase) throws Exception {
        Field[] f = type.getDeclaredFields();
        PreparedStatement s = con.prepareStatement("delete"
                + " from " + type.getSimpleName()
                .toLowerCase() + " where " + f[0].getName() + "=? and " + f[1].getName() + "=?");
        s.setInt(1, idModulo);
        s.setInt(2, idFase);
        int r = s.executeUpdate();
        s.close();
        return r;
    }
    
    public int updateAlunoAndFases(Aluno_and_fases e) throws Exception {
        PreparedStatement s = con.prepareStatement("update " + type.getSimpleName().toLowerCase()
                + " set score=? WHERE fkIdAluno = ? and fkIdFase = ?");

        s.setInt(1, e.getScore());
        s.setInt(2, e.getFkIdAluno());
        s.setInt(3, e.getFkIdFase());

        int r = s.executeUpdate();
        s.close();
        return r;
    }

    public int updateOrdemDaQuestao(int idQuestao, int seq) throws Exception {
        PreparedStatement s = con.prepareStatement("update " + type.getSimpleName().toLowerCase()
                + " set sequenciaQuestao=? WHERE idQuestao = ?");

        s.setInt(1, seq);
        s.setInt(2, idQuestao);

        int r = s.executeUpdate();
        s.close();
        return r;
    }
    
    /**
     * Select por id e order by. Realiza o resgaste de determinada entidade do
     * banco de dados de acordo com os campos declarados na classe.
     *
     * <br><br>Exemplo:<br>
     * select * from type.getSimpleName().toLowerCase() where 'primeiro field da
     * classe' order by 'clausula do parametro orderBy';
     *
     * @param id A ser buscado pela tabela.
     * @param pos Posição do field da clase da cima para baixo iniciando-se do
     * zero.
     * @param orderBy Nome da tabela a ser ordenada pela query. Se não for
     * necessário ordenação, simplemente passe o orderBy como null.
     * @return Um {@link ArrayList} com os dados resgatados em banco.
     * @throws Exception
     */
    public ArrayList<Type> selectById(int id, int pos, String orderBy) throws Exception {
        Field[] f = type.getDeclaredFields();
        PreparedStatement s = con.prepareStatement("select * from " + type.getSimpleName().toLowerCase()
                + " where " + f[pos].getName() + "=? order by " + orderBy);
        s.setInt(1, id);
        ResultSet r = s.executeQuery();
        ArrayList<Type> a = new ArrayList();

        while (r.next()) {
            Type obj = type.newInstance();

            for (int i = 0; i < f.length; i++) {
                f[i].setAccessible(true);
                if (f[i].getType().equals(int.class)) {
                    f[i].setInt(obj, r.getInt(i + 1));
                } else {
                    f[i].set(obj, r.getObject(i + 1));
                }
                f[i].setAccessible(false);
            }
            a.add(obj);
        }
        return a;
   }   

    /**
     * Select geral. Realiza o resgaste de determinada entidade do banco de
     * dados de acordo com os campos declarados na classe.
     *
     * @param o Objeto a ser resgatado.
     * @return Um {@link ArrayList} com os dados resgatados em banco.
     * @throws Exception
     */
    public ArrayList<Type> select() throws Exception {
        PreparedStatement s = con.prepareStatement("select * from " + type.getSimpleName().toLowerCase());
        ResultSet r = s.executeQuery();
        ArrayList<Type> a = new ArrayList();

        while (r.next()) {
            Type obj = type.newInstance();
            Field[] f = type.getDeclaredFields(); //cada getField ele cria uma nova instancia

            for (int i = 0; i < f.length; i++) {
                f[i].setAccessible(true);
                if (f[i].getType().equals(int.class)) {
                    f[i].setInt(obj, r.getInt(i + 1));
                } else {
                    f[i].set(obj, r.getObject(i + 1));
                }
                f[i].setAccessible(false);
            }
            a.add(obj);
        }
        return a;
    }

    /**
     * Realiza a inserção de determinada entidade do banco de dados de acordo
     * com os campos declarados na classe.
     *
     * @param o Objeto a ser inserido.
     * @return O numero de linhas que foram alteradas pela query.
     * @throws Exception
     */
    public int insert(Object o) throws Exception //metodo inacabado
    {
        String interrogations = "";
        Field[] f = type.getDeclaredFields();
        for (int i = 0; i < f.length; i++) {
            if (i == 0) {
                interrogations += "?";
            } else {
                interrogations += ",?";
            }
        }

        String statement = "insert into " + type.getSimpleName()
                .toLowerCase() + " values(" + interrogations + ")";

        PreparedStatement s = con.prepareStatement(statement);

        for (int i = 0; i < f.length; i++) {
            f[i].setAccessible(true);
            if (f[i].getType().equals(int.class)) {
                s.setInt(i + 1, f[i].getInt(o));
            } else {
                s.setObject(i + 1, f[i].get(o));
            }
            f[i].setAccessible(false);
        }
        int r = s.executeUpdate();
        s.close();
        return r;
    }

    /**
     * Realiza a alteração de determinada entidade do banco de dados de acordo
     * com os campos declarados na classe.
     *
     * @param o Objeto a ser alterado.
     * @return O numero de linhas que foram alteradas pela query.
     * @throws Exception
     */
    public int update(Object o) throws Exception //metodo inacabado
    {
        Field[] f = type.getDeclaredFields();
        String query = "";
        for (int i = 0; i < f.length; i++) {
            if (i == 0) //i = 0 é o id, entao nao nos interessa no começo
            {
            } else if (i == (f.length - 1)) {
                query += f[i].getName() + "=? ";
            } else {
                query += f[i].getName() + "=?, ";
            }
        }

        String statement = "update "
                + type.getSimpleName().toLowerCase()
                + " set " + query + "where " + f[0].getName() + "=?";

//        System.out.println("STATEMENT UPDATE:\n" + statement);

        PreparedStatement s = con.prepareStatement(statement);
        //update set loginAluno=?, senhaAluno=?, faseMax=?,
        //pontMax=? where idAluno=?        


        for (int i = 0; i < f.length; i++) {
            f[i].setAccessible(true);
            if (i == 0) {
                //do nothing, pq eh a nossa primary key
            } else {
                if (f[i].getType().equals(int.class)) {
                    s.setInt(i, f[i].getInt(o));
                } else {
                    s.setObject(i, f[i].get(o));
                }
            }
            f[i].setAccessible(false);
        }

        f[0].setAccessible(true);
        s.setInt(f.length, f[0].getInt(o));
        f[0].setAccessible(false);
        int r = s.executeUpdate();
        s.close();
        return r;
    }

    /**
     * Realiza a remoção de determinada entidade do banco de dados de acordo com
     * os campos declarados na classe. O primeiro campo declarado deve ser o id
     * obrigatoriamente.
     *
     * @param id o id referente ao objeto a ser removido.
     * @return O numero de linhas que foram alteradas pela query.
     * @throws Exception
     */
    public int delete(int id, int pos) throws Exception {
        Field[] f = type.getDeclaredFields();
        PreparedStatement s = con.prepareStatement("delete"
                + " from " + type.getSimpleName()
                .toLowerCase() + " where " + f[pos].getName() + "=?");
        s.setInt(1, id);
        int r = s.executeUpdate();
        s.close();
        return r;
    }   
}