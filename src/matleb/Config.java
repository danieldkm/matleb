package matleb;

import controller.ViewController;
import entidades.Aluno;
import entidades.Modulo;
import java.net.InetAddress;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Config {

    private static final java.awt.Dimension SCREENSIZE = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public static final int SCREENH = SCREENSIZE.height;
    public static final int SCREENW = SCREENSIZE.width;
    /**
     * @deprecated subtituido por entidades.Fase.tempoMax;
     */
    public static final int TIME_OF_GAME = 10;
    public static final int PONTO_MAXIMO = 1000; // ponto máximo da fase
    public static final int PENALIZACAO_NAO_PASSOU_DE_FASE = 500; //valor default
    public static final int PENALIZACAO_ERROU_A_RESPOSTA = 100; //valor default
    public static final int BACKGROUND = 0;
    public static final int GAMEOVER = 1;
    public static final int BACKGROUND_VERDE = 2;
    public static final int ICONE = 3;
    private static final String IMG_DIR = "images/";
    public static final int CORRETO = 0;
    public static final int ERRADO = 1;
    public static final int OTHER_SOUND = 2;
    private static final String AUD_DIR = "audio/";
    private static final String[] IMAGES_NAMES = new String[]{
        "background.png",
        "gameover.png",
        "background8.png",
        "icon2.png"
    };
    private static final String[] AUDIO_NAMES = new String[]{
        "correto.wav",
        "errado.wav",
        "other.wav",};
    private static ObservableList<Image> images = FXCollections.observableArrayList();

    public static ObservableList<Image> getImages() {
        return images;
    }
    private static ObservableList<AudioClip> audios = FXCollections.observableArrayList();

    public static ObservableList<AudioClip> getAudios() {
        return audios;
    }

    private Config() {
    }

    public static void init() {
        for (String name : IMAGES_NAMES) {
            Image image = new Image(Config.class.getResourceAsStream(IMG_DIR + name));
            images.add(image);
        }

        for (String name : AUDIO_NAMES) {
            AudioClip audioClip = new AudioClip(Config.class.getResource(AUD_DIR + name).toString());
            audios.add(audioClip);
        }
    }
    public static final int A1 = 0;
    public static final int A2 = 1;
    public static final int A3 = 2;
    public static final int ERRO = 3;
    public static final int A4 = 4;
    public static final int A5 = 5;
    private static final String IMG_DIR_ANI = IMG_DIR + "animacao/";
    private static final String[] IMAGES_ANIMACOES = new String[]{
        "a1.png",
        "a2.png",
        "a3.png",
        "erro.png",
        "a4.jpg",
        "a5.png"
    };

    public static Image getImage(int n) {
        return new Image(Config.class.getResourceAsStream(IMG_DIR_ANI + IMAGES_ANIMACOES[n]));
    }
    public static Aluno ALUNO_LOGADO;
    public static int FASE_COD = 1; //para acessar o index do array de fases
    public static int FASE_ESCOLHIDA = -13; //para iniciar com a fase desejada pelo usuario
    public static boolean isAleatorio;
    public static Text TEXTO_MODULO = new Text();
    public static int MODULO_ESCOLHIDO = -1;
    public static int RESTO_DIVISAO = 0;

    public static void setTextoModuloAndAlunoLogado() {
        try {
            ArrayList<Modulo> modulos = ViewController.getModulos();
            String t = "";
            if (ALUNO_LOGADO.getNivel().equals("Administrador")) {
                t += "IP da máquina: " + InetAddress.getLocalHost().getHostAddress() + "\n";
            }
            t += "Aluno: " + ALUNO_LOGADO.getLoginAluno();
            if (MODULO_ESCOLHIDO == -1) {
                t += "\nJogo Atual: ";
            } else {
                for (int i = 0; i < modulos.size(); i++) {
                    if (i == (MODULO_ESCOLHIDO)) {
                        t += "\nJogo Atual: " + modulos.get(i).getNomeModulo().toUpperCase();
                        break;
                    }
                }
            }
            TEXTO_MODULO.setText(t);
            TEXTO_MODULO.setTranslateX(SCREENW * 0.5);
            TEXTO_MODULO.setTranslateY(SCREENH * 0.5);


            DropShadow dropShadow = new DropShadow();
            dropShadow.setOffsetX(2.0);
            dropShadow.setOffsetY(2.0);
            dropShadow.setColor(Color.rgb(50, 50, 50, .588));
            TEXTO_MODULO.setEffect(dropShadow);
//            TEXTO_MODULO.setFont(Font.font("Mistral", FontWeight.BOLD, FontPosture.REGULAR, 30));
            TEXTO_MODULO.setFont(Font.font("Brush Script MT", FontWeight.BOLD, FontPosture.REGULAR, 50));
            TEXTO_MODULO.setFill(Color.WHITESMOKE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public static void mataProcesso() {
//
//
//        try {
//
////Executa comando que lista todos os processos ativos de java
//            Process p = Runtime.getRuntime().exec("jps");
//
////Ler lista de processos
//            InputStream is = p.getInputStream();
//            InputStreamReader readerInput = new InputStreamReader(is);
//            BufferedReader reader = new BufferedReader(readerInput);
//
//            String linha = "";
////Enquanto linha não for nula, exibe linha
//            while ((linha = reader.readLine()) != null) {
//                System.out.println("linha " + linha);
////Se existir o processo abaixo, divide a String em 2, PID e NomeProcesso.
////nomeDoProcesso é o nome do processo que  mataremos. 
//                if (linha.contains("Aluno")) {
//
//                    String[] getPID = linha.split(" ");
//
//                    System.out.println("PID: " + getPID[0]);
//                    System.out.println("Process name: " + getPID[1]);
//
////Mata Processo pelo pid
//                    Runtime.getRuntime().exec("taskkill /f /pid " + getPID[0]);
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
