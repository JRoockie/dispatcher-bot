package org.voetsky.dispatcherBot.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.voetsky.dispatcherBot.services.FileOperationsService.FileOperations;

@Log4j
@AllArgsConstructor
@RestController

//@RequestMapping("bot/data")
@RequestMapping("/data")
public class DownloadController {

    private final FileOperations fileOperations;

    @RequestMapping(method = RequestMethod.GET, value = "/getAudio")
    public void getAudio(@RequestParam("id") Long id, HttpServletResponse response) {
        fileOperations.downloadAudio(id, response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getVoice")
    public void getVoice(@RequestParam("id") Long id, HttpServletResponse response) {
        fileOperations.downloadVoice(id, response);
    }

}
