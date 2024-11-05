import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PopUp extends JFrame {

    private Interaction interaction;

    public PopUp() {
        // Configuración de la ventana principal
        setTitle("Bienvenido al lector de Archivos");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Etiqueta de pregunta
        JLabel questionLabel = new JLabel("¿Desea convertir su Diagrama de clases en código?", JLabel.CENTER);
        mainPanel.add(questionLabel, BorderLayout.NORTH);

        // Panel para los botones
        JPanel buttonPanel = new JPanel();

        // Botón "Sí"
        JButton yesButton = new JButton("Sí");
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar la ventana principal y abrir la ventana de recordatorio
                dispose();
                showReminderWindow();
            }
        });

        // Botón "No"
        JButton noButton = new JButton("No");
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cerrar el programa
                System.exit(0);
            }
        });

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // Método para mostrar la ventana de recordatorio
    private void showReminderWindow() {
        // Crear un panel para el mensaje de recordatorio
        JPanel reminderPanel = new JPanel();
        reminderPanel.setLayout(new BorderLayout());

        JLabel reminderLabel = new JLabel(
                "<html>Querido usuario, recuerde que el archivo deberá:<br>" +
                        "- Tener formato específico (.txt)<br>" +
                        "- Tener un nombre sin caracteres especiales (solo se permite _)<br>" +
                        "- Estar escrito estrictamente en PlantUML<br>" +
                        "- Estar en la carpeta donde se encuentra el código<br>" +
                        "- Tener los modificadores de acceso establecidos con (#,+. -)<br>" +
                        "- No poner las relaciones de clases (solo se permiten implements y extends)</html>",
                JLabel.CENTER
        );
        reminderPanel.add(reminderLabel, BorderLayout.CENTER);

        // Botón "OK" para que el usuario ingrese el nombre del archivo
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Solicitar al usuario el nombre del archivo
                    accion();
                // Cerrar la ventana de recordatorio
                SwingUtilities.getWindowAncestor(reminderPanel).dispose();
            }
        });
        reminderPanel.add(okButton, BorderLayout.SOUTH);

        // Configurar y mostrar la ventana de recordatorio
        JFrame reminderFrame = new JFrame("Requisitos del Archivo");
        reminderFrame.setSize(400, 300);
        reminderFrame.setLocationRelativeTo(null);
        reminderFrame.add(reminderPanel);
        reminderFrame.setVisible(true);
    }

    // Método para solicitar el nombre del archivo y validar su existencia
    public String requestFileName() {
        String fileName;
        while (true) {
            // Solicitar el nombre del archivo al usuario con opciones "Aceptar" y "Cancelar"
            fileName = (String) JOptionPane.showInputDialog(
                    this,
                    "Ingrese el nombre del archivo:",
                    "Seleccionar Archivo",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null
            );

            // Validar si el usuario presionó "Cancelar" o dejó el campo vacío
            if (fileName == null) {
                // Si el usuario presionó "Cancelar", se cierra el cuadro de diálogo y se sale del método
                break;
            }

            // Validar si el usuario ingresó un nombre vacío
            if (fileName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un nombre de archivo.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (fileName != null){
                fileName = (fileName+".txt");
                break;
            }
        }
        return fileName;
    }

    public String requestNewFileName() {
        String newFileName;
        while (true) {
            // Solicitar el nombre del archivo al usuario con opciones "Aceptar" y "Cancelar"
            newFileName = (String) JOptionPane.showInputDialog(
                    this,
                    "Ingrese el nombre del archivo que se creará",
                    "Seleccionar Archivo",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null
            );

            // Validar si el usuario presionó "Cancelar" o dejó el campo vacío
            if (newFileName == null) {
                // Si el usuario presionó "Cancelar", se cierra el cuadro de diálogo y se sale del método
                break;
            }

            // Validar si el usuario ingresó un nombre vacío
            if (newFileName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un nombre de archivo.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (newFileName != null){
                break;
            }
        }
        return newFileName;
    }

    public void errorMessageArchive(){
        JOptionPane.showMessageDialog(this, "El archivo no existe. Intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Método para mostrar la selección de idioma
    public int requestLanguageSelection() {
        // Crear opciones de idioma
        Object[] options = {"Python", "Java"};
        int choice = JOptionPane.showOptionDialog(this,
                "Seleccione el lenguaje de salida para el código:",
                "Seleccionar Idioma",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        return choice;
    }

    private void accion() {
        this.interaction = new Interaction();
        interaction.verificador();
    }
}