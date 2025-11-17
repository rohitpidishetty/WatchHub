package com.videostreaming.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class HLSSever {

    public Map<String, String> hlsize(String filename) {
        try {

            String ffmpegPath = "\"C:/ffmpeg/bin/ffmpeg.exe\""; // adjust if needed
            String inputFile = ("src/main/resources/static/" + filename).replace("\\", "/");

            // Create dynamic output folder per video
            String outputFolder = "src/main/resources/static/ffmpeg_type/" + filename.replaceAll("\\.[^.]+$", "");
            Files.createDirectories(Path.of(outputFolder));
            String outputFile = outputFolder + "/playlist.m3u8";

            System.out.println("Converting: " + inputFile);

            // FFmpeg command
            String command = String.format(
                    "%s -i \"%s\" -codec: copy -start_number 0 -hls_time 10 -hls_list_size 0 -f hls \"%s\"",
                    ffmpegPath, inputFile, outputFile
            );

            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Read FFmpeg logs
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                return Map.of("hlsized-status", "true", "playlist", outputFile);
            }

        } catch (Exception e) {
            return Map.of("hlsized-status", e.getMessage());
        }

        return Map.of("hlsized-status", "false");
    }

    @Autowired
    private ResourceLoader resourceLoader;

    public ResponseEntity<Resource> stream(String path) throws Exception {

        Resource resource = new ClassPathResource("static/streamer/hls/" + path);

        if (!resource.exists()) {
            System.out.println("File not found: " + path);
            return ResponseEntity.notFound().build();
        }

        String contentType = path.endsWith(".m3u8")
                ? "application/vnd.apple.mpegurl"
                : "video/MP2T";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }

}
