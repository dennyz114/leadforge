package com.leadforge.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = {
  @UniqueConstraint(name = "uk_users_email", columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false, length = 150)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, length = 150)
  private String fullName;

  @Column(nullable = false)
  private String companyId;

  @Column(nullable = false, length = 50)
  private String role; // "ADMIN", "USER", etc.
}
