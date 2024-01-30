
package com.jasonpercus.nbexplorerproject;



import com.jasonpercus.nbexplorerproject.WinUser.Window;



/**
 * Cette classe représente la classe principale du projet qui permet d'obtenir la liste des projets ouverts dans Netbeans ainsi que de connaitre celui qui est en cours d'édition
 * @author JasonPercus
 * @version 1.0
 */
public class NbExplorerProject {

    
    
//MAIN
    /**
     * Lance l'application
     * @param args Correspond aux éventuels arguments
     */
    public static void main(String[] args) {
        String[] list = getNameWindows();
        if(args.length != 1 || !args[0].equalsIgnoreCase("CHARSET"))
            System.setOut(new java.io.PrintStream(System.out, true, System.console().charset()));
        String active = getNameProjectActive(list);
        String[] projects = getProjects();
        if(projects != null){
            StringBuilder builder = new StringBuilder("");
            for(int i=0;i<projects.length;i++){
                String project = projects[i];
                java.io.File file = new java.io.File(project);
                if(i > 0)
                    builder.append("\n");
                if(file.getName().equals(active))
                    builder.append("[ACTIVE] ").append(project);
                else
                    builder.append("[*] ").append(project);
            }
            System.out.println(builder.toString());
        }
       
    }
    
    
    
//METHODES PRIVATES
    /**
     * Renvoie un tableau de chemin de projets ouverts dans NetBeans
     * @return Retourne un tableau de chemin de projets ouverts dans NetBeans
     */
    public static String[] getProjects() {
        String pathNbProjectUI = getPathNbProjectUI();
        if(pathNbProjectUI == null || pathNbProjectUI.isEmpty()|| pathNbProjectUI.isBlank())
            return null;
        java.util.List<String> projects = new java.util.ArrayList<>();
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(pathNbProjectUI));
            String line;
            while((line = br.readLine()) != null){
                if(line.startsWith("openProjectsURLs")) {
                    String right = line.split("=file:/")[1];
                    if(right.contains("\\u")){
                        java.util.regex.Pattern p = java.util.regex.Pattern.compile("(?<special>\\\\u[0-9a-f]{4})");
                        java.util.regex.Matcher m = p.matcher(right);
                        
                        while(m.find()){
                            String base = m.group("special");
                            right = right.replace(base, "" + unicodeToChar(base));
                        }
                    }
                    java.io.File file = new java.io.File(right);
                    if(file.exists())
                        projects.add(file.getAbsolutePath());
                }
            }
        } catch (java.io.IOException ex) {
            return null;
        }
        String[] array = new String[projects.size()];
        array = projects.toArray(array);
        return array;
    }
    
    /**
     * Renvoie le chemin du fichier où sont stockés les chemins des projets netbeans ouverts
     * @return Retourne le chemin du fichier où sont stockés les chemins des projets netbeans ouverts
     */
    public static String getPathNbProjectUI() {
        String defaultPath = System.getProperty("user.home") + "\\AppData\\Roaming\\NetBeans\\12.6\\config\\Preferences\\org\\netbeans\\modules\\projectui.properties";
        String path        = loadVariable("path_nb_projectui.txt", defaultPath);
        return new java.io.File(path).exists() ? path : new java.io.File(defaultPath).exists() ? defaultPath : null;
    }
    
    /**
     * Renvoie le nom du group à capturer dans la regex {@link #getRegex()}
     * @return Retourne le nom du group à capturer dans la regex {@link #getRegex()}
     */
    public static String getGroupNameRegex() {
        return loadVariable("group_name_regex.txt", "name");
    }
    
    /**
     * Renvoie la regex à utiliser pour récupérer le nom du projet actif sur le nom d'une fenêtre NetBeans
     * @return Retourne la regex à utiliser pour récupérer le nom du projet actif sur le nom d'une fenêtre NetBeans
     */
    public static java.util.regex.Pattern getRegex() {
        String regex = loadVariable("regex.txt", "^(?<" + getGroupNameRegex() + ">.*) - Apache NetBeans IDE 12\\.6$");
        return java.util.regex.Pattern.compile(regex);
    }
    
    /**
     * Renvoie une valeur stocké dans un fichier ou la valeur par défaut si le fichier n'existe pas
     * @param filename Correspond au fichier dont on cherche à lire la valeur stockée
     * @param defaultValue Correspond à la valeur par défaut si le fichier n'existe pas
     * @return Retourne une valeur stocké dans un fichier ou la valeur par défaut si le fichier n'existe pas
     */
    public static String loadVariable(String filename, String defaultValue) {
        java.io.File file = new java.io.File(filename);
        String line = null;
        if(file.exists()){
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                line = reader.readLine();
            } catch (java.io.IOException ex) {
                line = null;
            }
        }
        if (line == null)
            line = defaultValue;
        return line;
    }
    
    /**
     * Renvoie le nom du projet actif dans netbeans
     * @param list Correspond à la liste des noms de fenêtre sur laquel on essaye d'extraire le nom du projet en cours d'édition dans NetBeans
     * @return Retourne le nom du projet actif dans netbeans
     */
    public static String getNameProjectActive(String[] list) {
        if(list == null || list.length == 0)
            return null;
        
        java.util.List<String> names = new java.util.ArrayList<>();
        
        String                  groupNameRegex = getGroupNameRegex();
        java.util.regex.Pattern pattern        = getRegex();
        
        for(String str : list) {
            java.util.regex.Matcher matcher = pattern.matcher(str);
            if(matcher.find())
                names.add(matcher.group(groupNameRegex));
        }
        return (names.isEmpty() || names.size() > 1) ? null : names.get(0);
    }
    
    /**
     * Renvoie le nom de toutes les fenêtres windows ouvertes
     * @return Retourne le nom de toutes les fenêtres windows ouvertes
     */
    public static String[] getNameWindows() {
        java.util.List<String> list = new java.util.ArrayList<>();
        Window[] wins = WinUser.listOpenedWindows();
        for(Window win : wins) {
            if(win != null)
                list.add(win.getName());
        }
        String[] array = new String[list.size()];
        array = list.toArray(array);
        return array;
    }
    
    
    
//METHODE PRIVATE
    /**
     * Transforme une chaîne de caractères représentant un caractère unicode sous la forme d'un caractère
     * @param unicodeString Correspond à la chaîne de caractères à transformer
     * @return Retourne le caractère
     */
    private static char unicodeToChar(String unicodeString) {
        // Extraction du code Unicode de la chaîne
        String unicodeHex = unicodeString.substring(2);
        
        // Convertir le code Unicode en entier
        int unicodeValue = Integer.parseInt(unicodeHex, 16);
        
        // Convertir l'entier en caractère
        return (char) unicodeValue;
    }
    
    
    
}