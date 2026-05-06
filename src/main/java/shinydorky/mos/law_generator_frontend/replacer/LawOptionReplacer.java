package shinydorky.mos.law_generator_frontend.replacer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import shinydorky.mos.law_generator_frontend.model.LawOption;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Vector;

public class LawOptionReplacer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LawOptionReplacer.class.getName());

    public static Vector<String> GenerateFile(LawOption lawOption, File template) {
        LOGGER.info("GENERATING FOR: " + lawOption.getName());
        try {
            Vector<String> lines = new Vector<>();
            Scanner sc = new Scanner(template);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                lines.add(line);
            }

            if (lines.isEmpty()){
                return new Vector<>();
            }
            Vector<String> result = new Vector<>();
            for (String line: lines){
                if (line.contains("<X:name>")){
                    line = line.replaceAll("<X:name>", lawOption.getName());
                }
                if (line.contains("<X:group>")){
                    line = line.replaceAll("<X:group>", lawOption.getParentLawGroup().getName());
                }


                if (line.contains("<X:neighbours>")){
                    for (LawOption neighbour: lawOption.getParentLawGroup().getChildOptions()){
                        if (LawOption.AreNeighbours(lawOption, neighbour)){
                            String lineCopy = line;
                            lineCopy = lineCopy.replaceAll("<X:neighbours>", neighbour.getName());
                            lineCopy = lineCopy + "\n";
                            result.add(lineCopy);
                        }
                    }
                } else {
                    line = line + "\n";
                    result.add(line);
                }
            }
            return result;

        } catch (FileNotFoundException e){
            System.out.println("NO FILE FOUND");
        }
        return new Vector<>();
    }

    public static void WriteToFile(Vector<String> lines, String filename, String optionName){
        String directory = "OUTPUT/" + optionName;
        File dirFile = new File(directory);
        if (!dirFile.exists()){
            dirFile.mkdirs();
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

    public static void WriteAllFiles(LawOption lawOption){
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] templates = resolver.getResources("templates/LawOption/*.txt");
            for (Resource template: templates){
                Vector<String> lines = GenerateFile(lawOption, template.getFile());
                WriteToFile(lines, template.getFilename(), lawOption.getName());
            }
        } catch (IOException e){
            System.out.println("ERROR READING PATTERN FILES");
        }
    }
}
