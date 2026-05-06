package shinydorky.mos.law_generator_frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LawGroupDto {
    protected Long id;
    protected String signature;
    protected String name;
    protected String desc;
    protected Long lawTypeId;
}
