package shinydorky.mos.law_generator_frontend.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class FileGenerator {
    /**
     * Exactly what it says on the tin - we write the lines to the desired LawOption's folder in the output directory.
     * @param lines The contents of the file created
     * @param filename Name of the file created
     * @param folderName Name of the sub-folder in which the file will be saved
     */
    public static void WriteToFile(Vector<String> lines, String filename, String folderName){
        String directory = "OUTPUT/" + folderName;
        File dirFile = new File(directory);
        if (!dirFile.exists()){
            boolean created = dirFile.mkdirs();
            if (!created){
                return;
            }
        }
        String outputPath = directory + "/"  + filename;
        try(FileWriter fw = new FileWriter(outputPath);){
            for (String line: lines) {
                fw.write(line);
            }
        } catch (IOException e){
            System.out.println("ERROR WRITING TO FILE");
        }
    }
}
