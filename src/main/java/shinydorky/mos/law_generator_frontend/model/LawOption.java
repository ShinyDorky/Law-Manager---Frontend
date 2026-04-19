package shinydorky.mos.law_generator_frontend.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LawOption extends BasicLawFile{
    private String desc;
    private String canKeep;
    private String canPass;
    private String effects;
    private Integer placeInOrder;
    private String passCost;
    private String onPass;
    private boolean isDefault;


    private Integer statePowerOpinion;
    private Integer militaryOpinion;
    private Integer religiousUnityOpinion;
    private Integer culturalToleranceOpinion;
    private Integer populismOpinion;

    private Long lawGroupId;
}
