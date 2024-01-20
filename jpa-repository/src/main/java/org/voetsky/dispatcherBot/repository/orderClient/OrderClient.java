package org.voetsky.dispatcherBot.repository.orderClient;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.voetsky.dispatcherBot.repository.song.Song;
import org.voetsky.dispatcherBot.repository.tgUser.TgUser;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Entity
public class OrderClient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private TgUser tgUser;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "orderClient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Song> songs;

    private String comment;
    private String price;
    private Boolean isAccepted;
    private Boolean successful;

    @CreationTimestamp
    private LocalDateTime date;

    private LocalDateTime deletedWhen;

    private String phoneNumber;
    private String nameAsClient;

}
