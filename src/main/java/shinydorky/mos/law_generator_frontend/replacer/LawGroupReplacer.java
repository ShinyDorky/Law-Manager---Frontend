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
                    line = line.replaceAll("<X:signature>", lawGroup.getName());
                }
                if (line.contains("<X:desc>")){
                    line = line.replaceAll("<X:desc>", lawGroup.getDesc());
                }
                if (line.contains("<X:lawType>")){
                    line = line.replaceAll("<X:lawType>", lawGroup.getParentLawType().getSignature());
                }



                if (line.contains("<X:lawOptions>")){
                    try {
                        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                        Resource optionTemplate = resolver.getResource("templates/LawOption/" + template.getName());

                        for (LawOption lawOption: lawGroup.getChildOptions()){
                            Vector<String> contentLines = LawOptionReplacer.GenerateFile(lawOption, optionTemplate.getFile());
                            result.addAll(contentLines);
                        }
                    } catch (IOException e){
                        System.out.println("ERROR READING PATTERN FILES");
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

    public static void WriteAllFiles(LawGroup lawGroup){
        LOGGER.info("GENERATING FILES FOR: " + lawGroup.getSignature());
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] templates = resolver.getResources("templates/LawGroup/*.txt");
            for (Resource template: templates){
                Vector<String> lines = GenerateFile(lawGroup, template.getFile());
                WriteToFile(lines, template.getFilename(), "OPTIONS/" + lawGroup.getSignature());
            }
        } catch (IOException e){
            System.out.println("ERROR READING PATTERN FILES");
        }
    }

}
