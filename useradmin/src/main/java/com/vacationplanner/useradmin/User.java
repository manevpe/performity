package com.vacationplanner.useradmin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> teams;
    private float vacationDays;

    public User(String firstName, String lastName, String email, List<String> teams, float vacationDays) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.teams = teams;
        this.vacationDays = vacationDays;
    }
}
