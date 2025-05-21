package ru.erulaev.restaurantvoting.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.erulaev.restaurantvoting.common.validation.NoHtml;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public abstract class AbstractNamedEntity extends AbstractBaseEntity {

    @Column(name = "name", nullable = false)
    @NotBlank
    @Size(max = 128)
    @NoHtml   // https://stackoverflow.com/questions/17480809
    protected String name;

    @Override
    public String toString() {
        return super.toString() + '[' + name + ']';
    }
}