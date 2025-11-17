import React, { useRef } from "react";
import Hls from "hls.js";

function HLS() {
  const videoRef = useRef();
  document.body.style.backgroundColor = "#0D0D0D";

  const handlePlay = () => {
    const video = videoRef.current;
    if (Hls.isSupported()) {
      const hls = new Hls();
      hls.loadSource('http://localhost:8080/streamer/hls/master.m3u8');
      hls.attachMedia(video);
      hls.on(Hls.Events.MANIFEST_PARSED, () => video.play());
    }
  };

  return (
    <div>
      <center>


        <video ref={videoRef} controls width={1080} height={609}></video>
        <br />
        <button style={{
          width: "10%",
          padding: "0.5%",
          backgroundColor: "#0095f6",
          color: "white",
          border: "none",
          outline: "none",
          borderRadius: "10px",
          marginBottom: "0.4%",
          cursor: "pointer"
        }} onClick={handlePlay}>Play Video</button>
      </center>
    </div>
  );
}

export default HLS;
