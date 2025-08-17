package ru.erulaev.restaurantvoting.user.to;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.erulaev.restaurantvoting.common.HasIdAndEmail;
import ru.erulaev.restaurantvoting.common.to.NamedTo;
import ru.erulaev.restaurantvoting.common.validation.NoHtml;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class UserTo extends NamedTo implements HasIdAndEmail {

    @Email
    @NotBlank
    @Size(max = 128)
    @NoHtml   // https://stackoverflow.com/questions/17480809
    String email;

    @NotBlank
    @Size(max = 128)
    String password;

    public UserTo(Integer id, String name, String email, String password) {
        super(id, name);
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserTo:" + id + '[' + email + ']';
    }
}