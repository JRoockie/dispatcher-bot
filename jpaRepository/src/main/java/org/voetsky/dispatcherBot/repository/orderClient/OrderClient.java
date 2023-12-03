package org.voetsky.dispatcherBot.repository.orderClient;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "orderClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Song> songs;

    private String comment;
    private String price;
    private boolean isAccepted;

    @CreationTimestamp
    private LocalDateTime date;
    private java.lang.Long phoneNumber;

}