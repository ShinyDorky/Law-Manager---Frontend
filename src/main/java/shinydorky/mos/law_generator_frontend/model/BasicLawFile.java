package shinydorky.mos.law_generator_frontend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicLawFile {
    protected Long id;
    protected String signature;
    protected String name;

    @Override
    public String toString() {
        return name;
    }
}
