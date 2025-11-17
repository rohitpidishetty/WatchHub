package com.videostreaming.project.controller;

import com.videostreaming.project.service.HLSSever;
import com.videostreaming.project.service.VideoServer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/streamer")

public class MainActivity {
    @Autowired
    private HLSSever hls_obj;
    @Autowired
    private VideoServer vs_obj;

    @GetMapping("/videos")
    public Map<String, List<String>> list() {
        return this.vs_obj.getVideos();
    }

    @GetMapping("/target_file")
    public ResponseEntity<Resource> stream(@RequestParam String video_name) {
        return this.vs_obj.stream(video_name);
    }

    @GetMapping("/video")
    public ResponseEntity<InputStreamResource> message(@RequestParam String filename) throws Exception {
        return this.vs_obj.serveVideo(filename);
    }

    @GetMapping("/custom_bytes")
    public ResponseEntity<Resource> read_custom_bytes() throws IOException {
        return this.vs_obj.customByteRead();
    }

    @GetMapping("/hlsizer/{video_name}")
    public Map<String, String> HLSize(@PathVariable String video_name) throws Exception {
        String url = video_name;
        return this.hls_obj.hlsize(url);
    }

    @GetMapping("/hls/{folder}/{file:.+}")
    public ResponseEntity<Resource> serveHls(@PathVariable String folder, @PathVariable String file) throws Exception {
        String path = folder + "/" + file;
        return this.hls_obj.stream(path);
    }

}
