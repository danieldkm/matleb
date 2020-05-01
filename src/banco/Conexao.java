package banco;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import javafx.scene.control.Dialogs;
//con = DriverManager.getConnection("jdbc:mysql://sql3.freesqldatabase.com:3306/sql323266","sql323266","nB3%mA5%");

//escritaTxt = new FileOutputStream(matleb.Config.class.getResourceAsStream("fileIP/ip.txt").toString()); //cria um novo arquivo
//le arquivo do jar: BufferedReader in = new BufferedReader(new InputStreamReader(
//                    matleb.Config.class.getResourceAsStream("fileIP/ip.txt")));
public class Conexao {

    public static String message;
    private static String ip; //vai ser lido atraves de uma arquivo  = "192.168.68.9"
    private static String user = "root";
    private static String pass = "root";
    private static String nomeDataBase = "matleb";

    private static class UnicaConexao {

        private static Connection con = new Conexao().getConnection();
    }

    public static Connection getInstance() {
        return UnicaConexao.con;
    }

    private Conexao() {
    }

    private Connection getConnection() {
        leituraTxt();
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + nomeDataBase, user, pass);
        } catch (Exception sq) {
            try {
                boolean erro = false;
                do {
                    try {
                        ip = Dialogs.showInputDialog(null, "Informe o IP correto\nIP inserido: " + ip
                                + "", "IP da máquina: " + InetAddress.getLocalHost().getHostAddress(), "", "");
                        if (!ip.isEmpty()) {
                            con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + nomeDataBase, user, pass);
                            erro = false;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        if (ip.equals("exit") || ip.equals("sair") || ip.equals("escape")) {
                            System.exit(0);
                        }
                        erro = true;
                    }
                } while (erro);
                escritaTxt();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
        return con;
    }

    public void leituraTxt() {
        try {
            BufferedReader buf = new BufferedReader(new FileReader("ip.txt"));

            while (buf.ready()) {
                ip = buf.readLine();
            }
            System.out.println("ip lido: " + ip);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void escritaTxt() {
        BufferedWriter buf = null;
        try {
            buf = new BufferedWriter(new FileWriter("ip.txt"));
            buf.write(ip);
            System.out.println("ip escrito: " + ip);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                buf.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}