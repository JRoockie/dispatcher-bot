package org.voetsky.dispatcherBot.entity;

import lombok.*;

import javax.persistence.*;

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
    private String telegramFileId;

    @ManyToOne
    @JoinColumn(name = "song_id", referencedColumnName = "id")
    private Song song;

    @OneToOne(cascade = CascadeType.MERGE)
    private BinaryContent binaryContent;
    private String mimeType;
    private Long fileSize;


}