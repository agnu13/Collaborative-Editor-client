package compile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Compiler {

    private String filePath;
    private String fileName;

    public Compiler() {
    }

    public Compiler(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public void compile() {
        boolean flag = checkCompilation();
        if (flag) {
            performExecution();
        } else {
            showCompilationError();
        }
    }

    public boolean checkCompilation() {
        try {
            Runtime r = Runtime.getRuntime();
            r.exec("cmd /c del " + getFilePath() + "\\" + "a.exe");
            Process compile = Runtime.getRuntime().exec("cmd /c g++ " + getFilePath() + "\\" + getFileName());
            compile.waitFor();
            return compile.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception : " + e);
        }
        return false;
    }

    public void showCompilationError() {
        try {
            Runtime r = Runtime.getRuntime();
            BufferedWriter br = new BufferedWriter(new FileWriter("out1.bat"));
            br.write("cd " + getFilePath());
            br.newLine();
            br.write("cmd /c g++ " + getFileName());
            br.flush();
            Process p = r.exec("cmd /c start /wait out1.bat");
            p.waitFor();
            r.exec("cmd /c del out1.bat");
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception : " + e);
        }
    }

    public void performExecution() {
        try {
            Runtime r = Runtime.getRuntime();
            BufferedWriter br = new BufferedWriter(new FileWriter("out2.bat"));
            br.write("cmd /c cd " + getFilePath());
            br.newLine();
            br.write("a.exe");
            br.flush();
            Process p = r.exec("cmd /c start /wait out2.bat");
            p.waitFor();
            r.exec("cmd /c del out1.bat");
            r.exec("cmd /c del out2.bat");
            r.exec("cmd /c del a.exe");
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception : " + e);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}