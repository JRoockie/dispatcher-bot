package org.voetsky.dispatcherBot.repository.tgVoice;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.voetsky.dispatcherBot.repository.binaryContent.BinaryContent;
import org.voetsky.dispatcherBot.repository.song.Song;

import javax.persistence.*;
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

    private String telegramFileId;

    @ManyToOne
    @JoinColumn(name = "song_id", referencedColumnName = "id")
    private Song song;

    @OneToOne(cascade = CascadeType.MERGE)
    private BinaryContent binaryContent;
    private String mimeType;
    private Long fileSize;


}