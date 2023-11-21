package org.voetsky.dispatcherBot.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.voetsky.dispatcherBot.UserState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
//@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tg_users")
public class TgUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tgUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderClient> orderList;

    @CreationTimestamp
    private LocalDateTime firstLoginDate;

    private Long telegramUserId;
    private String firstName;
    private String lastName;
    private String username;
    private String nameAsClient;

    private java.lang.Long currentSongId;
    private java.lang.Long currentOrderId;

    @Enumerated(EnumType.STRING)
    private UserState userState;

}
