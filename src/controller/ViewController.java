package controller;

import banco.*;
import entidades.Fase;
import entidades.Aluno;
import entidades.Aluno_and_fases;
import entidades.Modulo;
import entidades.Modulo_has_alunos;
import entidades.Modulo_has_fases;
import entidades.Questao;
import java.util.ArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Dialogs;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Region;
import jfxtras.labs.scene.control.window.Window;
import tabela.TabelaQuestao;

public abstract class ViewController extends Window {

    protected static final int LARGURA_WINDOW = 900;
    protected static final int ALTURA_WINDOW = 500;
    protected static int codQuestao;
    protected static int codFase;
    protected static String nomeFase;
    protected static Button btnCadastrar;
    protected static Button btnDeletar;
    protected static Button btnAlterar;
    protected static Button btnNovo;
    protected static boolean erro;
    protected static int maxLength = 5;
    protected static TabelaQuestao tabelaQuestao = new TabelaQuestao();
    protected static Region region;
    protected static ProgressBar p;
    public static String idStyleSheet = "dark-blue";
    protected static ObservableList strings; // popular uma combo box  
    public static ComboBox cbModulos;
    public static ComboBox cbAlunos;

    /**
     * utilizar o atributo strings para popular a combo box cbAlunos
     *
     */
    public static ObservableList getPopularcbAlunos() {
        try {
            ArrayList<Aluno> alunos = getAlunos();
            strings = FXCollections.observableArrayList("");
            if (!alunos.isEmpty()) {
                for (Aluno aluno : alunos) {
                    if (!strings.isEmpty()) {
                        if (aluno.getNivel().equals("Aluno")) {
                            strings.add(aluno.getLoginAluno().trim());
                        }
                    } else {
                    }
                }
            }
            return strings;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("erro: ao popular a combo box. Metodo: getPopularcbAlunos. Classe ViewController.");
        }
        return null;
    }

    /**
     * utilizar o atributo strings para popular a combo box cbModulos
     *
     */
    public static ObservableList getPopularcbModulos() {
        try {
            ArrayList<Modulo> modulos = getModulos();
            strings = FXCollections.observableArrayList("");
            if (!modulos.isEmpty()) {
                for (Modulo modulo : modulos) {
                    if (!strings.isEmpty()) {
//                        System.out.println("name: " + modulo.getNomeModulo());
                        strings.add(modulo.getNomeModulo().trim());
                    } else {
//                        System.err.println("strings null.");
                    }
                }
            }
//            cbModulos.setItems(strings);
            return strings;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("erro: ao popular a combo box. Metodo: setPopularcbModulos. Classe ViewController.");
        }
        return null;
    }

    /**
     * Retorna uma lista de Modulo_has_fases ordenado pela coluna sequencia do
     * bd
     *
     * @return Modulo_has_fases
     */
    public static ArrayList<Modulo_has_fases> getModuloFaseSeq(int idModulo) {
        Dao<Modulo_has_fases> dao = new Dao(Modulo_has_fases.class);
        ArrayList<Modulo_has_fases> m = null;
        try {
            m = dao.selectById(idModulo, 0, "sequenciaFase");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return m;
    }

    /**
     * Retorna uma lista de Modulo_has_alunos
     *
     * @return modulos
     */
    public static ArrayList<Modulo_has_alunos> getModuloWithAlunos() {
        Dao<Modulo_has_alunos> daoModulo = new Dao(Modulo_has_alunos.class);
        ArrayList<Modulo_has_alunos> modulos = null;
        try {
            modulos = daoModulo.select();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return modulos;
    }

    /**
     * Retorna uma lista de Modulo_has_fases
     *
     * @return modulos
     */
    public static ArrayList<Modulo_has_fases> getModuloWithFases() {
        Dao<Modulo_has_fases> daoModulo = new Dao(Modulo_has_fases.class);
        ArrayList<Modulo_has_fases> modulos = null;
        try {
            modulos = daoModulo.select();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return modulos;
    }

    /**
     * Retorna uma lista de Modulos
     *
     * @return modulos
     */
    public static ArrayList<Modulo> getModulos() {
        Dao<Modulo> daoModulo = new Dao(Modulo.class);
        ArrayList<Modulo> modulos = null;
        try {
            modulos = daoModulo.select();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return modulos;
    }

    /**
     * Retorna uma lista de Alunos
     *
     * @return alunos
     */
    public static ArrayList<Aluno> getAlunos() {
        Dao<Aluno> daoAluno = new Dao(Aluno.class);
        ArrayList<Aluno> alunos = null;
        try {
            alunos = daoAluno.select();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return alunos;
    }

    /**
     * Retorna uma lista de fases
     *
     * @return fases
     */
    public static ArrayList<Fase> getFases() {
        Dao<Fase> daoFase = new Dao(Fase.class);
        ArrayList<Fase> fases = null;
        try {
            fases = daoFase.select();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return fases;
    }

    /**
     * Retorna uma lista de questões ordenado pela sequenciaQuestao
     *
     * @return questoes
     */
    public static ArrayList<Questao> getQuestaoSeq(int idFase) {
        Dao<Questao> daoQ = new Dao(Questao.class);
        ArrayList<Questao> questoes = null;
        try {
            questoes = daoQ.selectById(idFase, 3, "sequenciaQuestao");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return questoes;
    }

    /**
     * Retorna uma lista de questões
     *
     * @return questoes
     */
    public static ArrayList<Questao> getQuestoes() {
        Dao<Questao> daoQuestao = new Dao(Questao.class);
        ArrayList<Questao> questoes = null;
        try {
            questoes = daoQuestao.select();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return questoes;
    }

    public static ArrayList<Aluno_and_fases> getAlunoAndFases() {
        Dao<Aluno_and_fases> dao = new Dao(Aluno_and_fases.class);
        ArrayList<Aluno_and_fases> alunosAndFases = null;
        try {
            alunosAndFases = dao.select();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alunosAndFases;
    }

    public static ArrayList<Aluno_and_fases> getAlunoAndFasesById(int fkId, int pos) {
        Dao<Aluno_and_fases> dao = new Dao(Aluno_and_fases.class);
        ArrayList<Aluno_and_fases> alunosAndFases = null;
        try {
            alunosAndFases = dao.selectById(fkId, pos, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alunosAndFases;
    }

    public static void changeButtonDisable(String tipo) {
        if (tipo.equals("cadastrar")) {
            btnAlterar.setDisable(false);
            btnDeletar.setDisable(false);
            btnCadastrar.setDisable(true);
        } else {
            btnAlterar.setDisable(true);
            btnDeletar.setDisable(true);
            btnCadastrar.setDisable(false);
        }
    }

    /**
     * Método para limitar a quantidade de caracteres no textField
     *
     * @param String oldValue -> valor antigo
     * @param String newValue -> valor novo
     * @param TextField texto-> texto a ser redefinido
     */
    public static void verificaQtdDigit(String oldValue, String newValue, TextField texto) {
        try {
            // forçar valor numérico, redefinindo a valor antigo se a exceção é lançada
//                    Integer.parseInt(newValue);
            // forçar comprimento correto, redefinindo para o valor antigo, se for mais do que maxLength
            if (newValue.length() > maxLength) {
                texto.setText(oldValue);
            }
        } catch (Exception e) {
            texto.setText(oldValue);
        }
    }

    /**
     * Método para setar o TextField e receber apenas caracteres númerico
     */
    public static TextField setTextFieldDigit() {
        TextField t = new TextField() {
            @Override
            public void replaceText(int start, int end, String text) {
                if (!text.matches("[a-z]")) {
                    super.replaceText(start, end, text);
                }
            }

            @Override
            public void replaceSelection(String text) {
                if (!text.matches("[a-z]")) {
                    super.replaceSelection(text);
                }
            }
        };
        return t;
    }

    /**
     * Validação Text Field
     */
    public static void validate(Object control) {
        if (control instanceof TextField) {
            TextField t = (TextField) control;
            t.setStyle("-fx-text-fill:yellow;   "
                    + "-fx-prompt-text-fill:gray;  "
                    + "-fx-highlight-text-fill:black ;  "
                    + "-fx-highlight-fill: gray;  "
                    + "-fx-background-color: red;");
        } else if (control instanceof ComboBox) {
            ComboBox c = (ComboBox) control;
            c.setStyle("-fx-text-fill:yellow;   "
                    + "-fx-prompt-text-fill:gray;  "
                    + "-fx-highlight-text-fill:black ;  "
                    + "-fx-highlight-fill: gray;  "
                    + "-fx-background-color: red;");
        }
    }

    public static class ValidationResult {

        public enum Type {

            ERROR, WARNING, SUCCESS
        }
        private final String message;
        private final Type type;

        public ValidationResult(String message, Type type) {
            this.message = message;
            this.type = type;
        }

        public final String getMessage() {
            return message;
        }

        public final Type getType() {
            return type;
        }
    }

    public static interface Validator<C extends Control> {

        public ValidationResult validate(C control);
    }

    public static class ValidationEvent extends Event {

        public static final EventType<ValidationEvent> ANY =
                new EventType<ValidationEvent>(Event.ANY, "VALIDATION");
        private final ValidationResult result;

        public ValidationEvent(ValidationResult result) {
            super(ANY);
            this.result = result;
        }

        public final ValidationResult getResult() {
            return result;
        }
    }

    public static abstract class ValidatorPane<C extends Control> extends Region {

        /**
         * The content for the validator pane is the control it should work
         * with.
         */
        private ObjectProperty<C> content = new SimpleObjectProperty<C>(this, "content", null);

        public final C getContent() {
            return content.get();
        }

        public final void setContent(C value) {
            content.set(value);
        }

        public final ObjectProperty<C> contentProperty() {
            return content;
        }
        /**
         * The validator
         */
        private ObjectProperty<Validator<C>> validator = new SimpleObjectProperty<Validator<C>>(this, "validator");

        public final Validator<C> getValidator() {
            return validator.get();
        }

        public final void setValidator(Validator<C> value) {
            validator.set(value);
        }

        public final ObjectProperty<Validator<C>> validatorProperty() {
            return validator;
        }
        /**
         * The validation result
         */
        private ReadOnlyObjectWrapper<ValidationResult> validationResult = new ReadOnlyObjectWrapper<ValidationResult>(this, "validationResult");

        public final ValidationResult getValidationResult() {
            return validationResult.get();
        }

        public final ReadOnlyObjectProperty<ValidationResult> validationResultProperty() {
            return validationResult.getReadOnlyProperty();
        }
        /**
         * The event handler
         */
        private ObjectProperty<EventHandler<ValidationEvent>> onValidation =
                new SimpleObjectProperty<EventHandler<ValidationEvent>>(this, "onValidation");

        public final EventHandler<ValidationEvent> getOnValidation() {
            return onValidation.get();
        }

        public final void setOnValidation(EventHandler<ValidationEvent> value) {
            onValidation.set(value);
        }

        public final ObjectProperty<EventHandler<ValidationEvent>> onValidationProperty() {
            return onValidation;
        }

        public ValidatorPane() {
            content.addListener(new ChangeListener<Control>() {
                public void changed(ObservableValue<? extends Control> ov, Control oldValue, Control newValue) {
                    if (oldValue != null) {
                        getChildren().remove(oldValue);
                    }
                    if (newValue != null) {
                        getChildren().add(0, newValue);
                    }
                }
            });
        }

        protected void handleValidationResult(ValidationResult result) {
            getStyleClass().removeAll("validation-error", "validation-warning");
            if (result != null) {
                if (result.getType() == ValidationResult.Type.ERROR) {
                    getStyleClass().add("validation-error");
                } else if (result.getType() == ValidationResult.Type.WARNING) {
                    getStyleClass().add("validation-warning");
                }
            }
            validationResult.set(result);
            fireEvent(new ValidationEvent(result));
        }

        @Override
        protected void layoutChildren() {
            Control c = content.get();
            if (c != null) {
                c.resizeRelocate(0, 0, getWidth(), getHeight());
            }
        }

        @Override
        protected double computeMaxHeight(double d) {
            Control c = content.get();
            return c == null ? super.computeMaxHeight(d) : c.maxHeight(d);
        }

        @Override
        protected double computeMinHeight(double d) {
            Control c = content.get();
            return c == null ? super.computeMinHeight(d) : c.minHeight(d);
        }

        @Override
        protected double computePrefHeight(double d) {
            Control c = content.get();
            return c == null ? super.computePrefHeight(d) : c.prefHeight(d);
        }

        @Override
        protected double computePrefWidth(double d) {
            Control c = content.get();
            return c == null ? super.computePrefWidth(d) : c.prefWidth(d);
        }

        @Override
        protected double computeMaxWidth(double d) {
            Control c = content.get();
            return c == null ? super.computeMaxWidth(d) : c.maxWidth(d);
        }

        @Override
        protected double computeMinWidth(double d) {
            Control c = content.get();
            return c == null ? super.computeMinWidth(d) : c.minWidth(d);
        }
    }

    public static class TextInputValidatorPane<C extends TextInputControl> extends ValidatorPane<C> {

        public InvalidationListener textListener = new InvalidationListener() {
            @Override
            public void invalidated(javafx.beans.Observable o) {
                final Validator v = getValidator();
                final ValidationResult result = v != null
                        ? v.validate(getContent())
                        : new ValidationResult("", ValidationResult.Type.SUCCESS);

                handleValidationResult(result);
            }
        };

        public TextInputValidatorPane() {
            contentProperty().addListener(new ChangeListener<C>() {
                @Override
                public void changed(ObservableValue<? extends C> ov, C oldValue, C newValue) {
                    if (oldValue != null) {
                        oldValue.textProperty().removeListener(textListener);
                    }
                    if (newValue != null) {
                        newValue.textProperty().addListener(textListener);
                    }
                }
            });
        }

        public TextInputValidatorPane(C field) {
            this();
            setContent(field);
        }
    }
    /*
     FXDialog.showMessageDialog("ComboBox não foi preenchido", "Aviso:", Message.WARNING);
     validate(comboBox);
     comboBox.requestFocus();
     */

    protected static void alertaNoCB(ComboBox<String> comboBox, String titulo, String mensagem) {
        validate(comboBox);
        comboBox.requestFocus();
        Dialogs.showWarningDialog(null, mensagem, titulo, "");
    }

    protected static void alertaNoTxt(TextField texto, String titulo, String mensagem) {
        validate(texto);
        texto.requestFocus();
        Dialogs.showWarningDialog(null, mensagem, titulo, "");
    }

    protected static boolean parseTxtIsNumber(TextField texto, String titulo, String mensagem) {
        try {
            int valor = Integer.parseInt(texto.getText());
        } catch (NumberFormatException e) {
            alertaNoTxt(texto, titulo, mensagem);
            return false;
        }
        return true;
    }

    public static ProgressBar getStyleProgressBar() {
        ProgressBar pr = new ProgressBar();
        pr.setMinWidth(300);
        pr.setMinHeight(20);
        pr.setStyle("-fx-base: rgba(0, 0, 0, 0.8)");
        return pr;
    }
}
