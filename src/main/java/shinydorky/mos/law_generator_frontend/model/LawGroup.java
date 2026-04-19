package shinydorky.mos.law_generator_frontend.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LawGroup extends BasicLawFile{
    protected String desc;
    protected Long lawTypeId;
}
