import javax.swing.*;

public class Interaction {
    private FileConfirmer fc;
    private PopUp popUp;

    public Interaction() {
        this.popUp = new PopUp();
        this.fc = new FileConfirmer();
        this.fc.setInteraction(this);
    }

    public void setInteraction(PopUp popUp) {
        this.popUp = popUp;
    }

    public void verificador() {
        while(true) {
            boolean confirm = fc.verificador();
            if (confirm) {
                // Si el archivo existe, preguntar el idioma de salida
                popUp.requestLanguageSelection();
                break;
            } else {
                // Si el archivo no existe, mostrar mensaje de error
                popUp.errorMessageArchive();
                continue;
            }
        }
    }

    public String archivo(){
        String archivo = popUp.requestFileName();
        return archivo;
    }
}
