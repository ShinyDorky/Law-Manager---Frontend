package shinydorky.mos.law_generator_frontend.replacer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import shinydorky.mos.law_generator_frontend.model.LawGroup;
import shinydorky.mos.law_generator_frontend.model.LawOption;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Vector;

import static shinydorky.mos.law_generator_frontend.generator.FileGenerator.WriteToFile;

public class LawGroupReplacer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LawGroupReplacer.class.getName());

    public static Vector<String> GenerateFile(LawGroup lawGroup, File template) {
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
                    line = line.replaceAll("<X:name>", lawGroup.getName());
                }
                if (line.contains("<X:signature>")){
                    line = line.replaceAll("<X:signature>", lawGroup.getSignature());
                }
                if (line.contains("<X:desc>")){
                    line = line.replaceAll("<X:desc>", lawGroup.getDesc());
                }
                if (line.contains("<X:lawType>")){
                    line = line.replaceAll("<X:lawType>", lawGroup.getParentLawType().getSignature());
                }



                if (line.contains("<X:lawOptions>")) {
                    //                        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                    File optionTemplate = new File("templates/LawOption/" + template.getName());
//                        Resource optionTemplate = resolver.getResource("templates/LawOption/" + template.getName());

                    for (LawOption lawOption : lawGroup.getChildOptions()) {
                        Vector<String> contentLines = LawOptionReplacer.GenerateFile(lawOption, optionTemplate);
                        result.addAll(contentLines);
                    }
                }
                else {
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

    public static void WriteAllFiles(LawGroup lawGroup) {
        LOGGER.info("GENERATING FILES FOR: " + lawGroup.getSignature());
        File templatesFolder = new File("templates/LawGroup");

        for (File file : Objects.requireNonNull(templatesFolder.listFiles())) {
            Vector<String> lines = GenerateFile(lawGroup, file);
            WriteToFile(lines, file.getName(), "GROUPS/" + lawGroup.getSignature());
        }
    }
}
