package org.voetsky.dispatcherBot.repository.tgVoice;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.voetsky.dispatcherBot.repository.binaryContent.BinaryContent;
import org.voetsky.dispatcherBot.repository.song.Song;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tg_voice")
@Entity
public class TgVoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime date;

    private LocalDateTime deletedWhen;

    @JsonIgnore
    private String telegramFileId;

    @JsonIgnore
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    @OneToOne
    @JoinColumn(name = "song_id", referencedColumnName = "id")
    private Song song;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.MERGE)

    private BinaryContent binaryContent;
    private String mimeType;
    private Long fileSize;

}