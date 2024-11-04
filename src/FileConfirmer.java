import java.io.File;


public class FileConfirmer {
    private Interaction inte;


    public void setInteraction(Interaction inte) {
        this.inte = inte;
    }

    public boolean verificador(){
        String fileName = inte.archivo();
        boolean confirm;
        File file = new File(fileName);
        confirm = file.exists() && file.isFile();
        return confirm;
    }
}
