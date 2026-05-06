package shinydorky.mos.law_generator_frontend.replacer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import shinydorky.mos.law_generator_frontend.model.LawOption;
import shinydorky.mos.law_generator_frontend.model.LawOptionOpinionType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class LawOptionReplacer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LawOptionReplacer.class.getName());

    public static Vector<String> GenerateFile(LawOption lawOption, File template) {
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
                if (line.contains("<X:desc>")){
                    line = line.replaceAll("<X:desc>", lawOption.getDesc());
                }
                if (line.contains("<X:signature>")){
                    line = line.replaceAll("<X:signature>", lawOption.getSignature());
                }
                if (line.contains("<X:group>")){
                    line = line.replaceAll("<X:group>", lawOption.getParentLawGroup().getSignature());
                }
                if (line.contains("<X:can_keep>")){
                    line = line.replaceAll("<X:can_keep>", lawOption.getCanKeep());
                }
                if (line.contains("<X:can_pass>")){
                    line = line.replaceAll("<X:can_pass>", lawOption.getCanPass());
                }
                if (line.contains("<X:effects>")){
                    line = line.replaceAll("<X:effects>", lawOption.getEffects());
                }


                if (line.contains("<X:OPINIONS_UPGRADE>")){
//                    StringBuilder upgradeOpinionsString = new StringBuilder();
//                    for (LawOptionOpinionType type: LawOptionOpinionType.values()){
//                        float opinionMultiplier = lawOption.getParentLawGroup().GetChangeOpinionPos(
//                                type, lawOption.getPlaceInOrder(), -1);
//                        float opinionMultiplierNeg = lawOption.getParentLawGroup().GetChangeOpinionNeg(
//                                type, lawOption.getPlaceInOrder(), -1);
//                        if (opinionMultiplier != 0){
//                            upgradeOpinionsString.append("\t\t\tif = {");
//                            upgradeOpinionsString.append("\t\t\t\tlimit = {");
//                            upgradeOpinionsString.append("\t\t\t\t\tvar:MOS_law_opinion_").append(type.toString().toLowerCase()).append(" > 0");
//                            upgradeOpinionsString.append("\t\t\t\t}");
//                            upgradeOpinionsString.append("\t\t\t\tmultiply = ").append(opinionMultiplier);
//                            upgradeOpinionsString.append("\t\t\t}");
//                            upgradeOpinionsString.append("\t\t\telse = {");
//                            upgradeOpinionsString.append("\t\t\t\tmultiply = ").append(opinionMultiplierNeg);
//                            upgradeOpinionsString.append("\t\t\t}");
//                        }
//                    }
                    line = line.replaceAll("<X:OPINIONS_UPGRADE>", GenerateOpinionsString(lawOption, 1));
                }
                if (line.contains("<X:OPINIONS_DOWNGRADE>")){
                    line = line.replaceAll("<X:OPINIONS_DOWNGRADE>", GenerateOpinionsString(lawOption, -1));
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
                }
                else if (line.contains("<X:neighbours-1>")){
                    for (LawOption neighbour: lawOption.getParentLawGroup().getChildOptions()){
                        if (neighbour.getPlaceInOrder() == lawOption.getPlaceInOrder() - 1){
                            String lineCopy = line;
                            lineCopy = lineCopy.replaceAll("<X:neighbours-1>", neighbour.getName());
                            lineCopy = lineCopy + "\n";
                            result.add(lineCopy);
                        }
                    }
                }
                else if (line.contains("<X:neighbours+1>")){
                    for (LawOption neighbour: lawOption.getParentLawGroup().getChildOptions()){
                        if (neighbour.getPlaceInOrder() == lawOption.getPlaceInOrder() + 1){
                            String lineCopy = line;
                            lineCopy = lineCopy.replaceAll("<X:neighbours+1>", neighbour.getName());
                            lineCopy = lineCopy + "\n";
                            result.add(lineCopy);
                        }
                    }
                }

                //TODO: REPLACE OPINION VALUES IN VOTING OPINION FILE
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

    /**
     * Exactly what it says on the tin - we write the lines to the desired LawOption's folder in the output directory.
     * @param lines The contents of the file created
     * @param filename Name of the file created
     * @param optionName Name of the sub-folder in which the file will be saved
     */
    public static void WriteToFile(Vector<String> lines, String filename, String optionName){
        String directory = "OUTPUT/OPTIONS/" + optionName;
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

    /**
     * WriteAllFiles generates files from all templates within templates/LawOption resource directory and saves them
     * in the OUTPUT/{lawOption.signature} folder.
     *
     * @param lawOption Which law option do we want to generate the files for
     */
    public static void WriteAllFiles(LawOption lawOption){
        LOGGER.info("GENERATING FILES FOR: " + lawOption.getParentLawGroup().getSignature() + "_" + lawOption.getSignature());
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] templates = resolver.getResources("templates/LawOption/*.txt");
            for (Resource template: templates){
                Vector<String> lines = GenerateFile(lawOption, template.getFile());
                WriteToFile(lines, template.getFilename(), lawOption.getParentLawGroup().getSignature() + "/" + lawOption.getSignature());
            }
        } catch (IOException e){
            System.out.println("ERROR READING PATTERN FILES");
        }
    }

    /**
     * GenerateOpinionsString creates a string which MOS AI characters will use in order to determine their stance
     * on debates and other political actions involving this LawOption
     * <p>
     * This code does passable job of generating desired string with one edge case - laws that do not border
     * any other law on either side of the LawOrder. It generates values for impossible changes (from -1 to 1).
     * This is not a deal-breaker by any means, but it does result in some extra work.
     * After generating voting_opinion.txt you have to manually remove invalid voting weights and set proper triggers.
     *
     * @param lawOption What law option is the target of the change
     * @param orderChange By how many positions are we jumping in order to implement the target
     * @return A string coding the opinion multipliers for voters for given law change
     */
    private static String GenerateOpinionsString(LawOption lawOption, int orderChange){
        StringBuilder upgradeOpinionsString = new StringBuilder();
        for (LawOptionOpinionType type: LawOptionOpinionType.values()){
            float opinionMultiplier = lawOption.getParentLawGroup().GetChangeOpinionPos(
                    type, lawOption.getPlaceInOrder(), orderChange);
            float opinionMultiplierNeg = lawOption.getParentLawGroup().GetChangeOpinionNeg(
                    type, lawOption.getPlaceInOrder(), orderChange);
            if (opinionMultiplier != 0){
                upgradeOpinionsString.append("\t\t\tif = {\n");
                upgradeOpinionsString.append("\t\t\t\tlimit = {\n");
                upgradeOpinionsString.append("\t\t\t\t\tvar:MOS_law_opinion_").append(type.toString().toLowerCase()).append(" > 0\n");
                upgradeOpinionsString.append("\t\t\t\t}\n");
                upgradeOpinionsString.append("\t\t\t\tmultiply = ").append(opinionMultiplier).append("\n");
                upgradeOpinionsString.append("\t\t\t}\n");
                upgradeOpinionsString.append("\t\t\telse = {\n");
                upgradeOpinionsString.append("\t\t\t\tmultiply = ").append(opinionMultiplierNeg).append("\n");
                upgradeOpinionsString.append("\t\t\t}\n\n");
            }
        }
//        line = line.replaceAll("<X:effects>", lawOption.getEffects());
        return upgradeOpinionsString.toString();
    }
}
