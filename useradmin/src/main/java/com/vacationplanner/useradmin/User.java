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
    private String email;
    private String firstName;
    private String lastName;
    private List<String> teams;
    private float vacationDays;
}
