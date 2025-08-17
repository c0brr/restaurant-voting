package ru.erulaev.restaurantvoting.common.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.erulaev.restaurantvoting.common.validation.NoHtml;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class NamedTo extends BaseTo {

    @NotBlank
    @Size(max = 128)
    @NoHtml
    protected String name;

    public NamedTo(Integer id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + '[' + name + ']';
    }
}