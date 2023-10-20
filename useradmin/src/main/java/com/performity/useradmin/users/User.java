package com.performity.useradmin.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@Entity
@Table(name = "users")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User {
  @Id
  private String email;
  private String firstName;
  private String lastName;
  private List<String> teams;
  @Builder.Default
  private float vacationDays = 0;

  // Clone constructor
  public User(User originalUser) {
    this.firstName = originalUser.getFirstName();
    this.lastName = originalUser.getLastName();
    this.email = originalUser.getEmail();
    this.teams = originalUser.getTeams();
    this.vacationDays = originalUser.getVacationDays();
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> objEffectiveClass = o instanceof HibernateProxy
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() :
        this.getClass();
    if (thisEffectiveClass != objEffectiveClass) {
      return false;
    }
    User user = (User) o;
    return getEmail() != null && Objects.equals(getEmail(), user.getEmail());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() :
        getClass().hashCode();
  }
}
