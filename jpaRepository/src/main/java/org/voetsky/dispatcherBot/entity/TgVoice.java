package org.voetsky.dispatcherBot.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "tg_voice")
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