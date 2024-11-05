import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class Java {
    private Interaction inte;

    public void setInteraction(Interaction inte) {
        this.inte = inte;
    }

    public void JavaWriter() {
        String umlText = null;
        String nombreArchivo = "";
        nombreArchivo = inte.nuevoArchivo();
        nombreArchivo = (nombreArchivo + ".java");
        umlText = inte.umlLector();
        FileWriter writer = null;
        try {
            writer = new FileWriter(nombreArchivo, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Patrones para detectar clases, clases abstractas e interfaces, incluyendo implements
        Pattern classPattern = Pattern.compile("(abstract\\s+class|class|interface)\\s+(\\w+)(\\s+extends\\s+(\\w+))?(\\s+implements\\s+(\\w+(,\\s*\\w+)*))?");

        Pattern methodPattern = Pattern.compile("([+\\-#])\\s*(static\\s+)?(\\w+)\\s+(\\w+)\\(([^)])\\)\\s;?");
        Pattern constructorPattern = Pattern.compile("([+\\-#])\\s*(\\w+)\\(([^)]*)\\)");

        Matcher classMatcher = classPattern.matcher(umlText);

        while (classMatcher.find()) {
            String classType = classMatcher.group(1);             // Tipo de clase (abstract class, class, interface)
            String className = classMatcher.group(2);             // Nombre de la clase
            String extendsClass = classMatcher.group(4) != null ? classMatcher.group(4) : null;  // Clase base si la hay
            String implementsInterfaces = classMatcher.group(6);  // Interfaces implementadas si las hay

            // Construir la declaración de la clase
            StringBuilder classDeclaration = new StringBuilder("public " + classType + " " + className);
            if (extendsClass != null) {
                classDeclaration.append(" extends ").append(extendsClass);
            }
            if (implementsInterfaces != null) {
                classDeclaration.append(" implements ").append(implementsInterfaces);
            }
            try  { writer.write(classDeclaration + "\n");
            } catch (IOException e) {
                System.out.println("Error al registrar la acción: " + e.getMessage());
            }// Imprimir la declaración con una sola llave de apertura

            // Extraer el cuerpo de la clase
            int startIndex = classMatcher.end();
            int endIndex = umlText.indexOf("}", startIndex);
            String classBody = umlText.substring(startIndex, endIndex != -1 ? endIndex : umlText.length());

            // Procesamiento de atributos y métodos
            String[] classLines = classBody.split("\n");
            for (String line : classLines) {
                line = line.trim();

                // Identificar atributos (sin paréntesis)
                if (!line.contains("(") && !line.contains(")")) {
                    // Cambiar símbolo de visibilidad por palabras clave
                    String visibility = line.startsWith("+") ? "public " : line.startsWith("-") ? "private " : line.startsWith("#") ? "protected " : "";
                    line = line.replaceFirst("^[+\\-#]", visibility);

                    // Verificar que es un atributo y agregar punto y coma al final si falta
                    if (!line.endsWith(";") && visibility.length() > 0) {
                        line += ";";
                    }
                    try  { writer.write("    " + line + "\n");
                    } catch (IOException e) {
                        System.out.println("Error al registrar la acción: " + e.getMessage());
                    }
                }
                // Identificar métodos (con paréntesis)
                else {
                    Matcher methodMatcher = methodPattern.matcher(line);
                    if (methodMatcher.find()) {
                        String visibilitySymbol = methodMatcher.group(1);
                        String visibility = visibilitySymbol.equals("+") ? "public" : (visibilitySymbol.equals("-") ? "private" : "protected");
                        String isStatic = methodMatcher.group(2) != null ? "static " : "";  // Verificar si el método es estático
                        String returnType = methodMatcher.group(3);
                        String methodName = methodMatcher.group(4);
                        String parameters = methodMatcher.group(5);

                        try  { writer.write("    " + visibility + " " + isStatic + returnType + " " + methodName + "(" + parameters + ") {" + "\n");
                        } catch (IOException e) {
                            System.out.println("Error al registrar la acción: " + e.getMessage());
                        }

                        // Determinar el valor de retorno según el tipo
                        if (!returnType.equals("void")) {
                            if (returnType.equals("String")) {
                                try  { writer.write("        return null;" + "\n");
                                } catch (IOException e) {
                                    System.out.println("Error al registrar la acción: " + e.getMessage());
                                }
                            } else if (returnType.equals("double")) {
                                try  { writer.write("        return 0;" + "\n");
                                } catch (IOException e) {
                                    System.out.println("Error al registrar la acción: " + e.getMessage());
                                }
                            } else if (returnType.equals("boolean")) {
                                try  { writer.write("        return false;" + "\n");
                                } catch (IOException e) {
                                    System.out.println("Error al registrar la acción: " + e.getMessage());
                                }
                            } else {
                                try  { writer.write("        return 0;" + "\n");
                                } catch (IOException e) {
                                    System.out.println("Error al registrar la acción: " + e.getMessage());
                                }
                            }
                        }
                        try  { writer.write("    }" + "\n");
                        } catch (IOException e) {
                            System.out.println("Error al registrar la acción: " + e.getMessage());
                        }
                    }
                }
            }

            // Constructor y super() para clases que heredan
            Matcher constructorMatcher = constructorPattern.matcher(classBody);

            while (constructorMatcher.find()) {
                String visibilitySymbol = constructorMatcher.group(1);
                String visibility = visibilitySymbol.equals("+") ? "public" : (visibilitySymbol.equals("-") ? "private" : "protected");
                String constructorName = constructorMatcher.group(2);
                String parameters = constructorMatcher.group(3);

                // Verificar si el constructor coincide con el nombre de la clase actual
                if (constructorName.equals(className)) { // Es un constructor
                    try  { writer.write("    " + visibility + " " + constructorName + "(" + parameters + ") {" + "\n");
                    } catch (IOException e) {
                        System.out.println("Error al registrar la acción: " + e.getMessage());
                    }
                    // Llamado vacío a super()
                    if (extendsClass != null && !classType.contains("abstract")) {
                        try  { writer.write("        super();" + "\n");
                        } catch (IOException e) {
                            System.out.println("Error al registrar la acción: " + e.getMessage());
                        }
                    }
                    try  { writer.write("    }" + "\n");
                    } catch (IOException e) {
                        System.out.println("Error al registrar la acción: " + e.getMessage());
                    }
                }
            }
            try  { writer.write("}" + "\n");
            } catch (IOException e) {
                System.out.println("Error al registrar la acción: " + e.getMessage());
            } // Cierra la definición de la clase al final
        }
    }
}