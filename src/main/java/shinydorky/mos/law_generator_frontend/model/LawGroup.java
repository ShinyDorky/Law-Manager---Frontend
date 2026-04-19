package shinydorky.mos.law_generator_frontend.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
public class LawGroup extends BasicLawFile{
    protected String desc;
    protected Long lawTypeId;
}
