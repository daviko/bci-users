package com.bci.user.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue
  @Column(columnDefinition = "UUID")
  private UUID id;

  private String name;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  private LocalDateTime created;
  private LocalDateTime lastLogin;
  private String token;
  private Boolean isActive;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private List<Phone> phones;
}
