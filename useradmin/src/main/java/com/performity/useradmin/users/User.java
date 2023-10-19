package com.performity.useradmin.users;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
