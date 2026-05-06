package shinydorky.mos.law_generator_frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LawOption extends LawType{
    private String desc;
    private String canKeep;
    private String canPass;
    private String effects;//
    private Integer placeInOrder;
    private String passCost;
    private String onPass; // NOT IN USE CURRENTLY - FOR FUTURE IMPLEMENTATION
//    private boolean isDefault; // NOT IN USE CURRENTLY - FOR FUTURE IMPLEMENTATION


    private Integer statePowerOpinion;
    private Integer militaryOpinion;
    private Integer religiousUnityOpinion;
    private Integer culturalToleranceOpinion;
    private Integer populismOpinion;

    private Long lawGroupId;
    @Override
    public String toString() {
        return name;
    }
}
