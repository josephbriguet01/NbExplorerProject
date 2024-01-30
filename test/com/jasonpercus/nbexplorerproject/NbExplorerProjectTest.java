/*
 * Copyright (C) JasonPercus Systems, Inc - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by JasonPercus, 01/2024
 */
package com.jasonpercus.nbexplorerproject;



import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



/**
 * Cette classe permet de tester les méthodes de la classe {@link NbExplorerProject}
 * @author JasonPercus
 * @version 1.0
 */
public class NbExplorerProjectTest {
    
    
    
//CONSTANTES
    /**
     * Représente pour les tests le véritable fichier contenant la regex à utiliser dans le programme
     */
    private final String TRUE_REGEX_FILE   = "regex.txt";
    
    /**
     * Représente pour les tests le faux fichier contenant la regex à utiliser dans le programme (mais qui ne sera pas utilisée)
     */
    private final String FALSE_REGEX_FILE  = "MaRegex.txt";
    
    /**
     * Représente pour les tests le véritable fichier contenant le nom du groupe à extraire de la regex à utiliser dans le programme
     */
    private final String TRUE_GROUP_FILE   = "group_name_regex.txt";
    
    /**
     * Représente pour les tests le faux fichier contenant le nom du groupe à extraire de la regex à utiliser dans le programme (mais qui ne sera pas utilisé)
     */
    private final String FALSE_GROUP_FILE  = "MonGroupe.txt";
    
    
    
//CONSTRUCTOR
    /**
     * Crée un objet {@link NbExplorerProjectTest} (Constructeur par défaut)
     */
    public NbExplorerProjectTest() {
        
    }
    
    
    
//BEFORE & AFTER
    /**
     * Cette méthode est appelée avant chaque test
     */
    @Before
    public void before() {
        deleteFiles();
    }
    
    /**
     * Cette méthode est appelée après chaque test
     */
    @After
    public void after() {
        deleteFiles();
    }
    
    
    
//TEST
    /**
     * Cette méthode teste la méthode {@link NbExplorerProject#getProjects()}
     */
    @Test
    public void getProjects() {
        String[] projects = NbExplorerProject.getProjects();
        Assert.assertTrue(containsNbExplorerProject(projects));
    }
    
    /**
     * Cette méthode teste la méthode {@link NbExplorerProject#getPathNbProjectUI()}
     */
    @Test
    public void getPathNbProjectUI() {
        java.io.File file;
        
        Assert.assertEquals(System.getProperty("user.home") + "\\AppData\\Roaming\\NetBeans\\12.6\\config\\Preferences\\org\\netbeans\\modules\\projectui.properties", NbExplorerProject.getPathNbProjectUI());
        
        file = new java.io.File("path_nb_projectui.txt");
        writeValue("path_nb_projectui.txt", System.getProperty("user.home") + "\\AppData\\Roaming\\NetBeans\\12.6\\config\\Preferences\\org\\netbeans\\modules\\projectui.properties");
        Assert.assertEquals(System.getProperty("user.home") + "\\AppData\\Roaming\\NetBeans\\12.6\\config\\Preferences\\org\\netbeans\\modules\\projectui.properties", NbExplorerProject.getPathNbProjectUI());
        if(file.exists()) file.delete();
        
        file = new java.io.File("path_nb_projectui.txt");
        writeValue("path_nb_projectui.txt", "UnTextAuPif");
        Assert.assertEquals(System.getProperty("user.home") + "\\AppData\\Roaming\\NetBeans\\12.6\\config\\Preferences\\org\\netbeans\\modules\\projectui.properties", NbExplorerProject.getPathNbProjectUI());
        if(file.exists()) file.delete();
        
        file = new java.io.File("path_nb_projectui.txt");
        writeValue("path_nb_projectui.txt", "test");
        Assert.assertEquals("test", NbExplorerProject.getPathNbProjectUI());
        if(file.exists()) file.delete();
        
        file = new java.io.File("UnFichier.txt");
        writeValue("UnFichier.txt", "UnTextAuPif");
        Assert.assertEquals(System.getProperty("user.home") + "\\AppData\\Roaming\\NetBeans\\12.6\\config\\Preferences\\org\\netbeans\\modules\\projectui.properties", NbExplorerProject.getPathNbProjectUI());
        if(file.exists()) file.delete();
    }
    
    /**
     * Cette méthode teste la méthode {@link NbExplorerProject#getGroupNameRegex()}
     */
    @Test
    public void getGroupNameRegex() {
        java.io.File file;
        
        Assert.assertEquals("name", NbExplorerProject.getGroupNameRegex());
        
        file = new java.io.File(FALSE_GROUP_FILE);
        writeValue(FALSE_GROUP_FILE, "NetBeans\nEclipse");
        Assert.assertEquals("name", NbExplorerProject.getGroupNameRegex());
        if(file.exists()) file.delete();
        
        file = new java.io.File(TRUE_GROUP_FILE);
        writeValue(TRUE_GROUP_FILE, "NetBeans\nEclipse");
        Assert.assertEquals("NetBeans", NbExplorerProject.getGroupNameRegex());
        if(file.exists()) file.delete();
    }
    
    /**
     * Cette méthode teste la méthode {@link NbExplorerProject#getRegex()}
     */
    @Test
    public void getRegex() {
        String newGroupeName   = "NetBeans\nEclipse";
        
        String expectedBase126 = "^(?<name>.*) - Apache NetBeans IDE 12\\.6$";
        String expectedSet126  = "^(?<NetBeans>.*) - Apache NetBeans IDE 12\\.6$";
        String expectedBase82  = "(?<name>.*) - NetBeans IDE 8\\.2";
        String expectedSet82   = "(?<NetBeans>.*) - NetBeans IDE 8\\.2";
        
        Assert.assertEquals(expectedBase126, regex(null,             null,           null,             null         ));
        Assert.assertEquals(expectedBase126, regex(null,             null,           FALSE_GROUP_FILE, newGroupeName));
        Assert.assertEquals(expectedSet126,  regex(null,             null,           TRUE_GROUP_FILE,  newGroupeName));
        Assert.assertEquals(expectedBase126, regex(FALSE_REGEX_FILE, expectedBase82, FALSE_GROUP_FILE, newGroupeName));
        Assert.assertEquals(expectedSet126,  regex(FALSE_REGEX_FILE, expectedBase82, TRUE_GROUP_FILE,  newGroupeName));
        Assert.assertEquals(expectedBase82,  regex(TRUE_REGEX_FILE,  expectedBase82, FALSE_GROUP_FILE, newGroupeName));
        Assert.assertEquals(expectedBase82,  regex(TRUE_REGEX_FILE,  expectedBase82, TRUE_GROUP_FILE,  newGroupeName));
        Assert.assertEquals(expectedSet82,   regex(TRUE_REGEX_FILE,  expectedSet82,  TRUE_GROUP_FILE,  newGroupeName));
    }
    
    /**
     * Cette méthode teste la méthode {@link NbExplorerProject#loadVariable(java.lang.String, java.lang.String)}
     */
    @Test
    public void loadVariable() {
        String       filename = "MonFichier.txt";
        java.io.File file     = new java.io.File(filename);
        writeValue(filename, "NetBeans\nEclipse");
        Assert.assertEquals("NetBeans", NbExplorerProject.loadVariable(filename, "IDE"));
        Assert.assertEquals("IDE", NbExplorerProject.loadVariable("AutreFichier.txt", "IDE"));
        Assert.assertNull(NbExplorerProject.loadVariable("AutreFichier.txt", null));
        file.delete();
    }
    
    /**
     * Cette méthode teste la méthode {@link NbExplorerProject#getNameProjectActive(java.lang.String[])}
     */
    @Test
    public void getNameProjectActive() {
        String projectActive = NbExplorerProject.getNameProjectActive(NbExplorerProject.getNameWindows());
        Assert.assertEquals("NbExplorerProject", projectActive);
        
        projectActive = NbExplorerProject.getNameProjectActive(new String[0]);
        Assert.assertNull(projectActive);
    }

    /**
     * Cette méthode teste la méthode {@link NbExplorerProject#getNameWindows()}
     */
    @Test
    public void getNameWindows() {
        String[] array = NbExplorerProject.getNameWindows();
        Assert.assertNotNull(getNetbeansWindows(array));
    }
    
    
    
//METHODES PRIVATES
    /**
     * Parcourt le tableau de noms de fenêtre et renvoie le nom de la fenêtre NetBeans
     * @param array Correspond au tableau de noms de fenêtre
     * @return Retourne le nom de la fenêtre NetBeans
     */
    private String getNetbeansWindows(String[] array) {
        for(String name : array) {
            if(name.contains("NetBeans IDE"))
                return name;
        }
        return null;
    }
    
    /**
     * Parcourt le tableau de projets netbeans ouverts et renvoie si ce projet fait partie de ce tableau
     * @param array Correspond au tableau à parcourir
     * @return Retourne true si ce projet fait partie de ce tableau, sinon false
     */
    private boolean containsNbExplorerProject(String[] array) {
        String thisProjectPath = new java.io.File("").getAbsolutePath();
        for(String name : array) {
            if(name.equals(thisProjectPath))
                return true;
        }
        return false;
    }
    
    /**
     * Ecrit une chaîne de caractères dans un fichier texte
     * @param filename Correspond au nom du fichier texte à écrire
     * @param value Correspond au texte à écrire dans le fichier texte
     */
    private void writeValue(String filename, String value) {
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(filename))) {
            bw.write(value);
        } catch (java.io.IOException ex) {
            Assert.fail("Normalement, le fichier " + filename + " aurait du être créé !");
        }
    }
    
    /**
     * Renvoie la regex principale utilisée par le programme
     * @param filenameRegex Correspond au chemin éventuel du fichier contenant la regex
     * @param regexToWrite Correspond à la regex à écrire dans le fichier contenant la regex
     * @param filenameGroupName Correspond au chemin éventuel du fichier contenant le nom du groupe à capturer dans la regex
     * @param groupNameToWrite Correspond au nom du groupe à capturer dans la regex à écrire dans le fichier contenant le nom du groupe
     * @return Retourne la regex principale utilisée par le programme
     */
    private String regex(String filenameRegex, String regexToWrite, String filenameGroupName, String groupNameToWrite) {
        java.io.File fileRegex     = null;
        java.io.File fileGroupName = null;
        if(filenameRegex != null) {
            fileRegex = new java.io.File(filenameRegex);
            writeValue(filenameRegex, regexToWrite);
        }
        if(filenameGroupName != null) {
            fileGroupName = new java.io.File(filenameGroupName);
            writeValue(filenameGroupName, groupNameToWrite);
        }
        String regex = NbExplorerProject.getRegex().toString();
        if(fileRegex != null)
            fileRegex.delete();
        if(fileGroupName != null)
            fileGroupName.delete();
        return regex;
    }
    
    /**
     * Supprime tous les fichiers en attribut
     */
    private void deleteFiles() {
        String[] fileNames = {TRUE_REGEX_FILE, FALSE_REGEX_FILE, TRUE_GROUP_FILE, FALSE_GROUP_FILE};
        for (String fileName : fileNames) {
            java.io.File file = new java.io.File(fileName);
            if(file.exists())
                file.delete();
        }
    }

    
    
}