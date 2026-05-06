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

    public float GetChangeOpinionNeg(LawOptionOpinionType opinionType, int targetPlaceInOrder, int orderChange){
        return GetChangeOpinionPos(opinionType, Math.abs(GetMaxOrderValue() - targetPlaceInOrder), orderChange * (-1));
    }

    public int GetMaxOrderValue(){
        int result = 0;
        for (LawOption lawOption: childOptions){
            result = Math.max(result, lawOption.getPlaceInOrder());
        }
        return result;
    }
}
