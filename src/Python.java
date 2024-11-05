import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class Python {
    private Interaction inte;

    public void setInteraction(Interaction inte) {
        this.inte = inte;
    }

    public void PythonWriter(){
        String umlText = null;
        String nombreArchivo = "";
        nombreArchivo = inte.nuevoArchivo();
        nombreArchivo = (nombreArchivo + ".py");
        umlText = inte.umlLector();
        FileWriter writer = null;
        try {
            writer = new FileWriter(nombreArchivo, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Patrón para detectar clases y herencia
        Pattern classPattern = Pattern.compile("(abstract\\s+class|class)\\s+(\\w+)(\\s+extends\\s+(\\w+))?");
        Matcher classMatcher = classPattern.matcher(umlText);

        while (classMatcher.find()) {
            String classType = classMatcher.group(1);      // Tipo de clase (abstract class o class)
            String className = classMatcher.group(2);      // Nombre de la clase
            String extendsClass = classMatcher.group(4);   // Clase base si la hay

            // Generar la declaración de la clase en Python
            try  { writer.write("class" + className + "\n");
            } catch (IOException e) {
                System.out.println("Error al registrar la acción: " + e.getMessage());
            }
            if (classType.equals("abstract class")) {
                try  { writer.write( "ABC" + "\n");
                } catch (IOException e) {
                    System.out.println("Error al registrar la acción: " + e.getMessage());
                }
                if (extendsClass != null) {
                    try  { writer.write(", "+ extendsClass + "\n");
                    } catch (IOException e) {
                        System.out.println("Error al registrar la acción: " + e.getMessage());
                    }
                }
                try  { writer.write("):" + "\n");
                } catch (IOException e) {
                    System.out.println("Error al registrar la acción: " + e.getMessage());
                }
            } else {
                if (extendsClass != null) {
                    try  { writer.write("(" + extendsClass + ")" + "\n");
                    } catch (IOException e) {
                        System.out.println("Error al registrar la acción: " + e.getMessage());
                    }

                }
                try  { writer.write(":" + "\n");
                } catch (IOException e) {
                    System.out.println("Error al registrar la acción: " + e.getMessage());
                }
            }

            // Extraer el cuerpo de la clase específica para esta iteración
            int startIndex = classMatcher.end();
            int endIndex = umlText.indexOf("}", startIndex);
            String classBody = umlText.substring(startIndex, endIndex != -1 ? endIndex : umlText.length());

            // Procesar atributos, métodos y constructores en un solo ciclo
            Pattern attributePattern = Pattern.compile("([+\\-#])\\s*(final\\s+)?(\\w+(\\[\\])?)\\s+(\\w+)(\\s*=\\s*([^;]+))?\\s*;?(?!.*\\()");
            Pattern methodPattern = Pattern.compile("([+\\-#])\\s*(abstract\\s+)?(\\w+)\\s+(\\w+)\\(([^)]*)\\)");
            Pattern constructorPattern = Pattern.compile("([+\\-#])\\s*(\\w+)\\(([^)]*)\\)");

            String[] classLines = classBody.split("\n");
            for (String line : classLines) {
                line = line.trim();

                // Procesar atributos
                Matcher attributeMatcher = attributePattern.matcher(line);
                if (attributeMatcher.matches()) {
                    String visibility = attributeMatcher.group(1);
                    String dataType = attributeMatcher.group(3);
                    String attributeName = attributeMatcher.group(5);
                    String initialValue = attributeMatcher.group(7) != null ? attributeMatcher.group(7).trim() : getDefaultForType(dataType);

                    // Ajustar visibilidad del atributo
                    if (visibility.equals("#") || visibility.equals("-")) {
                        attributeName = "" + attributeName;
                    }
                    try  { writer.write("    " + attributeName + " = " + initialValue+ "\n");
                    } catch (IOException e) {
                        System.out.println("Error al registrar la acción: " + e.getMessage());
                    }
                }

                // Procesar métodos
                Matcher methodMatcher = methodPattern.matcher(line);
                if (methodMatcher.matches()) {
                    String abstractWord = methodMatcher.group(2) != null ? methodMatcher.group(2) : "";
                    String methodName = methodMatcher.group(4);
                    String parameters = methodMatcher.group(5);

                    // Eliminar tipos de datos de los parámetros
                    String[] paramNames = parameters.split(",\\s*");
                    StringBuilder paramBuilder = new StringBuilder();
                    for (String param : paramNames) {
                        String[] parts = param.split("\\s+");
                        if (parts.length > 1) {
                            paramBuilder.append(parts[1]).append(", ");
                        }
                    }
                    String finalParams = paramBuilder.toString().replaceAll(", $", "");

                    // Generar definición del método
                    String method = "";
                    if (classType.equals("abstract class") && abstractWord.equals("abstract")) {
                        method += "    @abstractmethod\n";
                    }
                    method += "    def " + methodName + "(self";
                    if (!finalParams.isEmpty()) {
                        method += ", " + finalParams;
                    }
                    method += "):\n        pass";
                    try  { writer.write( method + "\n");
                    } catch (IOException e) {
                        System.out.println("Error al registrar la acción: " + e.getMessage());
                    }
                }
            }

            // Imprimir constructor _init_ vacío sin super()
            try  { writer.write("    def _init_(self):" + "\n");
            } catch (IOException e) {
                System.out.println("Error al registrar la acción: " + e.getMessage());
            }
            try  { writer.write("        pass" + "\n");
            } catch (IOException e) {
                System.out.println("Error al registrar la acción: " + e.getMessage());
            }
            try  { writer.write( "\n");
            } catch (IOException e) {
                System.out.println("Error al registrar la acción: " + e.getMessage());
            }// Separador de clases
        }
    }

    private static String getDefaultForType(String dataType) {
        switch (dataType) {
            case "String":
                return "\"\"";
            case "int":
            case "double":
                return "0";
            case "boolean":
                return "False";
            default:
                return "None";
        }
    }
}