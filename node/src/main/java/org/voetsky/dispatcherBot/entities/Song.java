package org.voetsky.dispatcherBot.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = "songId")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Songs")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Song implements Serializable {
    //    Order order;
    //TODO Добавить интеграцию с другой таблицей


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songId;

    @ManyToOne
    @JoinColumn(name = "orderId", referencedColumnName = "orderClient_Id")
    private OrderClient orderClient;

    private String name;
    private String link;

    @Type(type = "jsonb")
    @Column(name = "mp3")
    private Object mp3;
    private int singerCount;

    @Type(type = "jsonb")
    @Column(name = "voiceMessage")
    private Object singerVoiceMessage;

}
