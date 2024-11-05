import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Lector {
    private Interaction inte;

    public void setInteraction(Interaction inte) {
        this.inte = inte;
    }

    public String content()  {
        String nombreArchivo = inte.archivo();
        Path filePath = Paths.get(nombreArchivo);
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
