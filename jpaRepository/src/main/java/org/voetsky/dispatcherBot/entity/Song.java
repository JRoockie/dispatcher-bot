package org.voetsky.dispatcherBot.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id", "orderClient"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "songs")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Song implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_client_id", referencedColumnName = "id")
    private OrderClient orderClient;


    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TgAudio> tgAudios;


    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TgVoice> tgVoices;

    private String songName;
    private String link;
    private Long singerCount;
    private boolean isFilled;

}
