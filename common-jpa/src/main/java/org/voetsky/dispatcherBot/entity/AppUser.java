package org.voetsky.dispatcherBot.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.voetsky.dispatcherBot.entities.OrderClient;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = "appUserId")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appUserId;

    @OneToMany(mappedBy = "appUser_id")
    private List<OrderClient> orderList;

    private Long telegramUserId;
//    @OneToMany
//    private Long orderId;

    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    private String firstName;
    private String lastName;
    private String username;

}
