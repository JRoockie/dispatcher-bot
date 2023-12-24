package org.voetsky.dispatcherBot.repository.viewEntities.AdminUser;

import lombok.*;
import org.voetsky.dispatcherBot.repository.viewEntities.Role;

import javax.persistence.*;
import java.util.Set;

@Builder
@Getter
@Setter
//@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usr")
@Entity
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "usr_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

}
