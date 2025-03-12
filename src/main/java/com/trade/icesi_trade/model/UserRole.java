package com.trade.icesi_trade.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USER_ROLES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USERS_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ROLES_id")
    private Role role;
}