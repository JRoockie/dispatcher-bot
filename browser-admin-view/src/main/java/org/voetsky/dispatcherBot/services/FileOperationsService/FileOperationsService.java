package org.voetsky.dispatcherBot.services.FileOperationsService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.voetsky.dispatcherBot.repository.song.SongRepository;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;

import java.io.IOException;

@AllArgsConstructor
@Log4j
@Service
public class FileOperationsService implements FileOperations {

    private final SongRepository songRepository;

    @Override
    public TgAudio getTgAudio(Long songId) {
        return songRepository.findSongById(songId).getTgAudio();
    }

    @Override
    public TgVoice getTgVoice(Long songId) {
        return songRepository.findSongById(songId).getTgVoice();
    }

    @Override
    public void downloadAudio(Long id, HttpServletResponse response) {
        var audio = getTgAudio(id);
        if (audio == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.setContentType(MediaType.parseMediaType(audio.getMimeType()).toString());
        response.setHeader("Content-disposition", "attachment; filename=" + audio.getAudioName());
        response.setStatus(HttpServletResponse.SC_OK);

        var binaryContent = audio.getBinaryContent();
        try {
            var out = response.getOutputStream();
            out.write(binaryContent.getFileAsArrayOfBytes());
            out.close();
        } catch (IOException e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void downloadVoice(Long id, HttpServletResponse response) {
        var voice = getTgVoice(id);
        if (voice == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        response.setContentType(MediaType.parseMediaType(voice.getMimeType()).toString());
        response.setHeader("Content-disposition", "attachment;");
        response.setStatus(HttpServletResponse.SC_OK);

        var binaryContent = voice.getBinaryContent();
        try {
            var out = response.getOutputStream();
            out.write(binaryContent.getFileAsArrayOfBytes());
            out.close();
        } catch (IOException e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
