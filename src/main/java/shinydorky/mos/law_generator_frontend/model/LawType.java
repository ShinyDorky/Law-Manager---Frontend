package shinydorky.mos.law_generator_frontend.model;

import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LawType{
    protected int itemDepth;
    protected Long id;
    protected String signature;
    protected String name;

    @Override
    public String toString() {
        return name;
    }
}
