

public class Interaction {
    private FileConfirmer fc;
    private PopUp popUp;
    private Lector lector;
    private Java java;
    private Python python;
    public String archivo;
    public String nuevoArchivo;

    public Interaction() {
        this.popUp = new PopUp();
        this.lector = new Lector();
        this.java = new Java();
        this.python = new Python();
        this.fc = new FileConfirmer();
        this.fc.setInteraction(this);
        this.lector.setInteraction(this);
        this.java.setInteraction(this);
        this.python.setInteraction(this);
    }

    public void setInteraction(PopUp popUp) {
        this.popUp = popUp;
    }

    public void verificador() {
        while(true) {
            boolean confirm = fc.verificador();
            if (confirm) {
                // Si el archivo existe, preguntar el idioma de salida
                Lenguaje();
                break;
            } else {
                // Si el archivo no existe, mostrar mensaje de error
                popUp.errorMessageArchive();
                continue;
            }
        }
    }

    public String archivo(){
        String archivo = getArchivo();
        if (archivo==null){
            setArchivo(popUp.requestFileName());
        }
        archivo = getArchivo();
        return archivo;
    }

    public String nuevoArchivo(){
        String archivo = getNuevoArchivo();
        if (archivo==null){
            setNuevoArchivo(popUp.requestNewFileName());
        }
        archivo = getNuevoArchivo();
        return archivo;
    }

    public String getNuevoArchivo(){
        return nuevoArchivo;
    }

    public void setNuevoArchivo(String nuevoArchivo){
        this.nuevoArchivo = nuevoArchivo;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String umlLector()  {
        return lector.content();
    }

    public void Lenguaje() {
        int choice = popUp.requestLanguageSelection();
        if (choice==0){
            python.PythonWriter();
        } else {
            java.JavaWriter();
        }
    }
}
