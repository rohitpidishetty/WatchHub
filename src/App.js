'use-client';
import axios from 'axios';
import './App.css';
import React from 'react';
function App() {

  const [data, setData] = React.useState([]);
  const [video_file, set_video_file] = React.useState(null);
  const player = React.useRef(null);
  document.body.style.backgroundColor = "#0D0D0D";

  React.useEffect(() => {


    (async () => {
      let data = await axios.get("http://localhost:8080/streamer/videos", {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      setData(data.data.Files);
    })();


  }, [])



  return (
    <div className="App">
      <center>
        <div style={{
          display: "flex",
          backgroundImage: "linear-gradient(to top, #0e2727ff 0%, #1d0f2eff 100%)"
        }}>
          <img style={{
            padding: "0.7%",
            paddingRight: "1%"
          }} src='player.png' />
          <p style={{
            padding: "0.7%"
          }}>WatchHub</p>
        </div>
        <video ref={player} width="" style={{
          borderRadius: "20px"
        }} height="500" controls >
          <source src={video_file} type="video/mp4" />
        </video>
        <div className='grid'>

          {data && data.map((e, key) => {
            return (

              <button key={key} style={{

                padding: "2%",
                backgroundImage: "linear-gradient(to top, #0e2727ff 0%, #1d0f2eff 100%)",
                color: "white",
                border: "none",
                outline: "none",
                borderRadius: "10px",
                marginBottom: "0.4%",
                cursor: "pointer"
              }} onClick={() => {
                var file = e.split("\\")[4];
                var response = `http://localhost:8080/streamer/target_file?video_name=${file}`;
                set_video_file(response);
                if (player.current) {
                  player.current.load();
                  player.current.play();
                }
              }}>
                {e.split("\\")[4]}
              </button>

            )
          })}
        </div>
      </center>
    </div>
  );
}

export default App;
