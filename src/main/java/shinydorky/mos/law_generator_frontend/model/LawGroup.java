package shinydorky.mos.law_generator_frontend.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LawGroup extends LawType{
    protected String desc;
    protected Long lawTypeId;
    @Override
    public String toString() {
        return name;
    }

    private LawType parentLawType;
    private List<LawOption> childOptions;

    /**
     * GetChangeOpinionPos generates Voter Opinion Multiplier of a law change for characters that are proponents of
     * the considered political value.
     * @param opinionType The type of opinion we want to get
     * @param targetPlaceInOrder The place in order of the law we want to enact
     * @param orderChange How must we change our place in order to get to the Target law
     * @return A floating point multiplier for the Voting Opinion of the MOS Voter Character
     */
    public float GetChangeOpinionPos(LawOptionOpinionType opinionType, int targetPlaceInOrder, int orderChange){
        int previousOpinion = 0;
        int newOpinion = 0;
        for (LawOption lawOption: childOptions){
            if (lawOption.getPlaceInOrder() == targetPlaceInOrder - orderChange && previousOpinion == 0){
                switch (opinionType){
                    case STATE_POWER -> previousOpinion = lawOption.getStatePowerOpinion();
                    case MILITARY -> previousOpinion = lawOption.getMilitaryOpinion();
                    case RELIGIOUS_UNITY -> previousOpinion = lawOption.getReligiousUnityOpinion();
                    case CULTURAL_TOLERANCE -> previousOpinion = lawOption.getCulturalToleranceOpinion();
                    case POPULISM -> previousOpinion = lawOption.getPopulismOpinion();
                    default -> {
                        return 0;
                    }
                }
            }
            if (lawOption.getPlaceInOrder() == targetPlaceInOrder && newOpinion == 0){
                switch (opinionType){
                    case STATE_POWER -> newOpinion = lawOption.getStatePowerOpinion();
                    case MILITARY -> newOpinion = lawOption.getMilitaryOpinion();
                    case RELIGIOUS_UNITY -> newOpinion = lawOption.getReligiousUnityOpinion();
                    case CULTURAL_TOLERANCE -> newOpinion = lawOption.getCulturalToleranceOpinion();
                    case POPULISM -> newOpinion = lawOption.getPopulismOpinion();
                    default -> {
                        return 0;
                    }
                }
            }
            if (newOpinion != 0 && previousOpinion != 0){
                break;
            }
        }
        return ((float) (newOpinion - previousOpinion)) / 100;
    }

    /**
     * GetChangeOpinionPos generates Voter Opinion Multiplier of a law change for characters that are opponents of
     * the considered political value.
     * @param opinionType The type of opinion we want to get
     * @param targetPlaceInOrder The place in order of the law we want to enact
     * @param orderChange How must we change our place in order to get to the Target law
     * @return A floating point multiplier for the Voting Opinion of the MOS Voter Character
     */
    public float GetChangeOpinionNeg(LawOptionOpinionType opinionType, int targetPlaceInOrder, int orderChange){
        return GetChangeOpinionPos(opinionType, Math.abs(GetMaxOrderValue() - targetPlaceInOrder), orderChange * (-1));
    }

    /**
     *
     * @return The highest value of the PlaceInOrder among the children of this LawGroup
     */
    public int GetMaxOrderValue(){
        int result = 0;
        for (LawOption lawOption: childOptions){
            result = Math.max(result, lawOption.getPlaceInOrder());
        }
        return result;
    }
}
