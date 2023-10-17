package org.voetsky.dispatcherBot.entity;


import lombok.*;
import org.voetsky.dispatcherBot.UserState;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderClient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private TgUser tgUser;

    @OneToMany(mappedBy = "orderClient")
    private List<Song> songs;

    private String chatId;
    private String inputName;
    private String comment;
    private String price;
    private boolean isAccepted;
    private String date;
    private Long phoneNumber;

}
