package org.voetsky.dispatcherBot.repository.song;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.TypeDef;
import org.voetsky.dispatcherBot.WhoWillSing;
import org.voetsky.dispatcherBot.repository.orderClient.OrderClient;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "orderClient"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "songs")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Entity
public class Song implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime date;

    private LocalDateTime deletedWhen;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_client_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private OrderClient orderClient;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private TgAudio tgAudio;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private TgVoice tgVoice;

    @Enumerated(EnumType.STRING)
    private WhoWillSing whoWillSing;

    private boolean isFilled;
    private String songName;
    private String link;
    private Integer singerCount;
    private Boolean hasAudio = false;

}
