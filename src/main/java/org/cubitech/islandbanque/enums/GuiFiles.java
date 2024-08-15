package org.cubitech.islandbanque.enums;

public enum GuiFiles {
    ARGENT("argent.yml"),
    EXP("exp.yml"),
    FARMPOINTS("farmpoints.yml"),
    LOGS_ARGENTS("logs_argent.yml"),
    LOGS_EXP("logs_exp.yml"),
    LOGS_FARMPOINTS("logs_farmpoints.yml"),
    PRINCIPAL("principal.yml");

    private final String fileName;
    GuiFiles(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
