package shinydorky.mos.law_generator_frontend.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

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
}
