package org.voetsky.dispatcherBot.services.repoServices.fileService;

import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.voetsky.dispatcherBot.exceptions.ServiceException;
import org.voetsky.dispatcherBot.repository.binaryContent.BinaryContent;
import org.voetsky.dispatcherBot.repository.binaryContent.BinaryContentRepository;
import org.voetsky.dispatcherBot.repository.tgAudio.TgAudio;
import org.voetsky.dispatcherBot.repository.tgVoice.TgVoice;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Log4j
@Service
public class FileServiceImpl implements FileService {
    @Value("${bot.token}")
    private String token;
    @Value("${service.file_info.uri}")
    private String fileInfoUri;
    @Value("${service.file_storage.uri}")
    private String fileStorageUri;
    private final BinaryContentRepository binaryContentRepository;

    public FileServiceImpl(BinaryContentRepository binaryContentRepository) {
        this.binaryContentRepository = binaryContentRepository;
    }

    @Override
    public TgAudio processAudio(Update telegramMessage) {
        String fileId = telegramMessage.getMessage().getAudio().getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            BinaryContent persistentBinaryContent = getPersistentBinaryContent(response);
            Audio tgAudio = telegramMessage.getMessage().getAudio();
            return buildTransientTgVoice(tgAudio, persistentBinaryContent);
        }
        throw new ServiceException(String.format(
                "Bad response from telegram service: %s", response));
    }

    @Override
    public TgVoice processVoice(Update update) {
        String fileId = update.getMessage().getVoice().getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            BinaryContent persistentBinaryContent = getPersistentBinaryContent(response);
            Voice voice = update.getMessage().getVoice();
            return buildTransientTgVoice(voice, persistentBinaryContent);
        }
        throw new ServiceException(String.format(
                "Bad response from telegram service: %s", response));
    }

    private BinaryContent getPersistentBinaryContent(ResponseEntity<String> response) {
        String filePath = getFilePath(response);
        byte[] fileInByte = downloadFile(filePath);
        BinaryContent transientBinaryContent = BinaryContent.builder()
                .fileAsArrayOfBytes(fileInByte)
                .build();
        BinaryContent persistentBinaryContent = binaryContentRepository.save(transientBinaryContent);
        return persistentBinaryContent;
    }

    private static String getFilePath(ResponseEntity<String> response) {
        JSONObject jsonObject = new JSONObject(response.getBody());
        String filePath = String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));
        return filePath;
    }


    private TgAudio buildTransientTgVoice(Audio audio, BinaryContent persistentBinaryContent) {
        return TgAudio.builder()
                .telegramFileId(audio.getFileId())
                .audioName(audio.getFileName())
                .binaryContent(persistentBinaryContent)
                .mimeType(audio.getMimeType())
                .fileSize(audio.getFileSize())
                .build();
    }

    private TgVoice buildTransientTgVoice(Voice voice, BinaryContent persistentBinaryContent) {

        return TgVoice.builder()
                .telegramFileId(voice.getFileId())
                .binaryContent(persistentBinaryContent)
                .mimeType(voice.getMimeType())
                .fileSize(voice.getFileSize())
                .build();
    }

    private ResponseEntity<String> getFilePath(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                fileInfoUri,
                HttpMethod.GET,
                request,
                String.class,
                token, fileId
        );
    }

    private byte[] downloadFile(String filePath) {
        String fullUri = fileStorageUri.replace("{token}", token)
                .replace("{filePath}", filePath);
        URL urlObj = null;
        try {
            urlObj = new URL(fullUri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        //TODO подумать над оптимизацией
        try (InputStream is = urlObj.openStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(urlObj.toExternalForm(), e);
        }
    }
}