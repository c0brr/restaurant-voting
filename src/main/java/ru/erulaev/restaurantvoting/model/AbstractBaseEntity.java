package ru.erulaev.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.util.Assert;

import static ru.erulaev.restaurantvoting.util.HibernateProxyHelper.getClassWithoutInitializingProxy;

@MappedSuperclass
@Access(AccessType.FIELD) //  https://stackoverflow.com/a/6084701/548473
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public abstract class AbstractBaseEntity implements Persistable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    public int id() {
        Assert.notNull(id, "Entity must have id");
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    //    https://stackoverflow.com/questions/1638723
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClassWithoutInitializingProxy(this) != getClassWithoutInitializingProxy(o)) return false;
        return getId() != null && getId().equals(((AbstractBaseEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return getClassWithoutInitializingProxy(this).hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + getId();
    }
}
