package shinydorky.mos.law_generator_frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LawOptionDto {
    protected Long id;
    protected String signature;
    protected String name;
    protected String desc;
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
}
