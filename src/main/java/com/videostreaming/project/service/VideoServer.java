package com.videostreaming.project.service;

import com.fasterxml.jackson.databind.ser.std.StdArraySerializers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

@Service
public class VideoServer {
    final String path = "src/main/resources/static/";

    public ResponseEntity<Resource> stream(String filename) {
        ClassPathResource cp_obj = new ClassPathResource("static/" + filename);
        if (cp_obj.exists()) {
            try {
                InputStream inps = cp_obj.getInputStream();
                InputStreamResource res = new InputStreamResource(inps);
                return ResponseEntity
                        .ok()
                        .contentLength(cp_obj.contentLength())
                        .contentType(MediaType.valueOf("video/mp4"))
                        .body(res);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return ResponseEntity.badRequest().build();
    }

    public Map<String, List<String>> getVideos() {
        File file = new File(this.path);
        List<String> files = new ArrayList<>();
        if (file.isDirectory()) {
            File[] buffer = file.listFiles();
            for (int i = 0; i < buffer.length; i++) {
                String __file__ = buffer[i].toString();
                if (__file__.contains(".mp4"))
                    files.add(__file__);
            }

        }
        return Map.of("Files", files);
    }

    public ResponseEntity<InputStreamResource> serveVideo(String video_name) throws Exception {

        ClassPathResource videoFile = new ClassPathResource("static/" + video_name);
        if (videoFile.exists()) {

            InputStream inps = videoFile.getInputStream();
            byte[] bytes = inps.readAllBytes();
            inps.close();

            String base64 = Base64.getEncoder().encodeToString(bytes);

            FileWriter writer = new FileWriter(new File("src/main/resources/static/", "xyz1.txt"));
            BufferedWriter write = new BufferedWriter(writer);
            write.write(base64);
            write.close();
            writer.close();

            InputStreamResource res = new InputStreamResource(videoFile.getInputStream());

            return ResponseEntity.ok()
                    .contentLength(videoFile.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(res);
        }
        return ResponseEntity.badRequest().build();
    }

    @Cacheable("videos")
    private byte[] getCachedData(String read) {
        return Base64.getDecoder().decode(read);
    }

    public ResponseEntity<Resource> customByteRead() throws IOException {
        try {
            String read = new String(new ClassPathResource("static/xyz1.txt").getInputStream().readAllBytes());
            byte[] vf = getCachedData(read);

            int new_range = (vf.length / 2);
            byte[] render = Arrays.copyOf(vf, new_range);

            return ResponseEntity
                    .ok()
                    .contentLength(new_range)
                    .contentType(MediaType.valueOf("video/mp4"))
                    .body(new ByteArrayResource(render));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}
