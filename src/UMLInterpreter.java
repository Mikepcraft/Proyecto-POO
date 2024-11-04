import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

public class UMLInterpreter {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder umlTextBuilder = new StringBuilder();
        System.out.println("Ingresa el diagrama UML (escribe '@end' para finalizar la entrada):");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("@end")) break;
            umlTextBuilder.append(line).append("\n");
        }
        String umlText = umlTextBuilder.toString();
        scanner.close();

        // Patrones para detectar clases, clases abstractas e interfaces
        Pattern classPattern = Pattern.compile("(abstract\\s+class|class|interface)\\s+(\\w+)(\\s+extends\\s+\\w+)?(\\s+implements\\s+\\w+(,\\s*\\w+))?\\s\\{?");

        // Patrón para detectar solo atributos que no tengan paréntesis en la línea
        Pattern attributePattern = Pattern.compile("([+\\-#])\\s*(final\\s+)?(\\w+(\\[\\])?)\\s+(\\w+)\\s*(=\\s*[^;]+)?\\s*;?(?!.*\\()");

        // Patrón para detectar solo métodos (líneas con paréntesis)
        Pattern methodPattern = Pattern.compile("([+\\-#])\\s*(\\w+)\\s+(\\w+)\\(([^)])\\)\\s;?");

        // Patrón para detectar constructores explícitos (mismo nombre de la clase, sin tipo de retorno)
        Pattern constructorPattern = Pattern.compile("([+\\-#])\\s*" + "(\\w+)\\s*\\(([^)])\\)\\s;?");

        Matcher classMatcher = classPattern.matcher(umlText);

        while (classMatcher.find()) {
            String classType = classMatcher.group(1); // class, abstract class, interface
            String className = classMatcher.group(2);

            // Condición especial para la clase Main
            if (className.equals("Main")) {
                System.out.println("public class Main {\n" +
                        "    public void main(String[] args) {\n" +
                        "        // Implementación del método\n" +
                        "    }\n" +
                        "}");
                continue; // Salta al siguiente ciclo sin procesar atributos o métodos
            }

            // Procesamiento estándar para otras clases
            String extendsClass = classMatcher.group(3) != null ? classMatcher.group(3).replace("extends ", "") : null;
            String implementsInterfaces = classMatcher.group(4) != null ? classMatcher.group(4).replace("implements ", "") : null;

            // Generar declaración de clase
            StringBuilder classCode = new StringBuilder();
            classCode.append("public ").append(classType).append(" ").append(className);
            if (extendsClass != null) classCode.append(" extends ").append(extendsClass);
            if (implementsInterfaces != null) classCode.append(" implements ").append(implementsInterfaces);
            classCode.append(" {\n");

            // Variables para atributos y métodos
            boolean hasAttributes = false;
            boolean hasExplicitConstructor = false;

            // Extraer el cuerpo de la clase
            int startIndex = classMatcher.end();
            int endIndex = umlText.indexOf("}", startIndex);
            String classBody = umlText.substring(startIndex, endIndex != -1 ? endIndex : umlText.length());

            // Atributos de la clase
            Matcher attributeMatcher = attributePattern.matcher(classBody);
            while (attributeMatcher.find()) {
                String visibility = attributeMatcher.group(1).equals("+") ? "public" : (attributeMatcher.group(1).equals("-") ? "private" : "protected");
                String modifier = attributeMatcher.group(2) != null ? attributeMatcher.group(2) : "";
                String type = attributeMatcher.group(3);
                String name = attributeMatcher.group(5);
                String initializer = attributeMatcher.group(6) != null ? attributeMatcher.group(6) : "";

                classCode.append("    ").append(visibility).append(" ").append(modifier).append(type).append(" ").append(name).append(initializer).append(";\n");
                hasAttributes = true;
            }

            // Verificar si hay un constructor explícito en el UML
            Matcher constructorMatcher = constructorPattern.matcher(classBody);
            while (constructorMatcher.find()) {
                String constructorName = constructorMatcher.group(2);
                if (constructorName.equals(className)) {
                    hasExplicitConstructor = true;
                    break;
                }
            }

            // Generar constructor si hay atributos o si el UML especifica un constructor
            if ((classType.equals("class") || classType.equals("abstract class")) && (hasAttributes || hasExplicitConstructor)) {
                classCode.append("    public ").append(className).append("() {\n");
                classCode.append("        // Constructor de la clase\n");
                classCode.append("    }\n");
            }

            // Métodos de la clase
            Matcher methodMatcher = methodPattern.matcher(classBody);
            while (methodMatcher.find()) {
                String visibility = methodMatcher.group(1).equals("+") ? "public" : (methodMatcher.group(1).equals("-") ? "private" : "protected");
                String returnType = methodMatcher.group(2);
                String methodName = methodMatcher.group(3);
                String parameters = methodMatcher.group(4);

                // Para interfaces, solo declara el método sin cuerpo
                if (classType.equals("interface")) {
                    classCode.append("    ").append(visibility).append(" ").append(returnType).append(" ").append(methodName).append("(").append(parameters).append(");\n");
                } else {
                    // Para clases y clases abstractas, incluye la implementación del método
                    classCode.append("    ").append(visibility).append(" ").append(returnType).append(" ").append(methodName).append("(").append(parameters).append(") {\n");
                    classCode.append("        // Implementación del método\n");

                    // Añadir return 0 o return null según el tipo de retorno
                    if (!returnType.equals("void")) {
                        classCode.append("        return ").append(returnType.equals("String") ? "null" : "0").append(";\n");
                    }
                    classCode.append("    }\n");
                }
            }

            classCode.append("}\n");
            System.out.println(classCode.toString());
        }
    }

    // Método para convertir visibilidad a partir del símbolo UML
    private static String convertVisibility(String symbol) {
        switch (symbol) {
            case "+": return "public";
            case "-": return "private";
            case "#": return "protected";
            default: return "";
        }
    }
}