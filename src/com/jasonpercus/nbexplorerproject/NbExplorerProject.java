
package com.jasonpercus.nbexplorerproject;

import com.jasonpercus.nbexplorerproject.WinUser.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 *
 * @author JasonPercus
 * @version 1.0
 */
public class NbExplorerProject {

    
    
    /**
     * @param args the command line arguments
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
                File file = new File(project);
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
    
    public static String[] getProjects() {
        String pathNbProjectUI = getPathNbProjectUI();
        if(pathNbProjectUI == null || pathNbProjectUI.isEmpty()|| pathNbProjectUI.isBlank())
            return null;
        java.util.List<String> projects = new java.util.ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathNbProjectUI));
            String line;
            while((line = br.readLine()) != null){
                if(line.startsWith("openProjectsURLs")) {
                    File file = new File(line.split("=file:/")[1]);
                    if(file.exists())
                        projects.add(file.getAbsolutePath());
                }
            }
        } catch (IOException ex) {
            return null;
        }
        String[] array = new String[projects.size()];
        array = projects.toArray(array);
        return array;
    }
    
    public static String getPathNbProjectUI() {
        File file = new File("path_nb_projectui.txt");
        String path = null;
        if(file.exists()){
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                path = reader.readLine();
            } catch (IOException ex) {
                path = null;
            }
        }
        if (path == null)
            path = "C:\\Users\\jbriguet\\AppData\\Roaming\\NetBeans\\12.6\\config\\Preferences\\org\\netbeans\\modules\\projectui.properties";
        
        return (new File(path).exists()) ? path : null;
    }
    
    public static String getGroupNameRegex() {
        File file = new File("group_name_regex.txt");
        String name = null;
        if(file.exists()){
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                name = reader.readLine();
            } catch (IOException ex) {
                name = null;
            }
        }
        if (name == null)
            name = "name";
        return name;
    }
    
    public static Pattern getRegex() {
        File file = new File("regex.txt");
        String regex = null;
        if(file.exists()){
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                regex = reader.readLine();
            } catch (IOException ex) {
                regex = null;
            }
        }
        if (regex == null)
            regex = "^(?<" + getGroupNameRegex() + ">.*) - Apache NetBeans IDE 12\\.6$";
        return Pattern.compile(regex);
    }
    
    public static String getNameProjectActive(String[] list) {
        if(list == null || list.length == 0)
            return null;
        java.util.List<String> names = new java.util.ArrayList<>();
        String groupNameRegex = getGroupNameRegex();
        Pattern pattern = getRegex();
        
        for(String str : list) {
            Matcher matcher = pattern.matcher(str);
            if(matcher.find()) {
                String value = matcher.group(groupNameRegex);
                names.add(value);
            }
        }
        if(names.isEmpty() || names.size() > 1)
            return null;
        else
            return names.get(0);
    }
    
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
    
    
    
}