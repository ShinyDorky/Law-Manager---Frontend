package shinydorky.mos.law_generator_frontend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class BasicLawFile {
    protected Long id;
    protected String signature;
    protected String name;

    @Override
    public String toString() {
        return name;
    }
}
