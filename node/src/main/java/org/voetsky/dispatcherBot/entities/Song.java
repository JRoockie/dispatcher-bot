package org.voetsky.dispatcherBot.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = {"OrderId","SongId"})
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Songs")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Song implements Serializable {
    //    Order order;
    //TODO Добавить интеграцию с другой таблицей

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int OrderId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int SongId;

    String chatID;
    String name;
    String link;

    @Type(type = "jsonb")
    @Column(name = "mp3")
    Object mp3;

    int singerCount;

    @Type(type = "jsonb")
    @Column(name = "voiceMessage")
    Object singerVoiceMessage;
}
