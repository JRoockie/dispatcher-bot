package org.voetsky.dispatcherBot.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.voetsky.dispatcherBot.services.FileOperations;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j
@AllArgsConstructor
@RestController
@RequestMapping("/data")
public class DownloadController {

    private final FileOperations fileOperations;

    @RequestMapping(method = RequestMethod.GET, value = "/getAudio")
    public void getAudio(@RequestParam("id") Long id, HttpServletResponse response) {
        //TODO для формирования badRequest добавить ControllerAdvice
        var audio = fileOperations.getTgAudio(id);
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

    @RequestMapping(method = RequestMethod.GET, value = "/getVoice")
    public void getVoice(@RequestParam("id") Long id, HttpServletResponse response) {
        var voice = fileOperations.getTgVoice(id);
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
