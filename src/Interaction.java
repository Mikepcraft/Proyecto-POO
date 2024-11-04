import javax.swing.*;

public class Interaction {
    private UMLInterpreter uml;
    private FileConfirmer fc;
    private PopUp popUp;

    public Interaction() {
        this.uml = new UMLInterpreter();
        this.popUp = new PopUp();
        this.fc = new FileConfirmer();
        this.fc.setInteraction(this);
        this.popUp.setInteraction(this);
    }

    public void iniciar(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PopUp().setVisible(true);
            }
        });
    }

    public void verificador() {
        boolean confirm = fc.verificador();
        if (confirm) {
            // Si el archivo existe, preguntar el idioma de salida
            popUp.requestLanguageSelection();
        } else {
            // Si el archivo no existe, mostrar mensaje de error
            popUp.errorMessage();
        }
    }

    public String archivo(){
        String archivo = popUp.requestFileName();
        return archivo;
    }
}
