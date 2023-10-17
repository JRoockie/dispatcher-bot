package org.voetsky.dispatcherBot.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
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
    //    Order order;
    //TODO Добавить интеграцию с другой таблицей
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_client_id", referencedColumnName = "id")
    private OrderClient orderClient;

    private String songName;
    private String link;

    @Type(type = "jsonb")
    @Column(name = "mp3")
    private Object mp3;
    private int singerCount;

    @Type(type = "jsonb")
    @Column(name = "voiceMessage")
    private Object singerVoiceMessage;

}
