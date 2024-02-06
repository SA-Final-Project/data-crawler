import logo from "./logo.svg";
import "./App.css";
import {
  AppBar,
  Box,
  Button,
  Container,
  CssBaseline,
  Grid,
  List,
  Typography,
  ListItem,
  Divider,
  ListItemText,
  TextField,
} from "@mui/material";
import useWebSocket, { ReadyState } from "react-use-websocket";
import { useState, useEffect } from "react";
import ReactTimeago from "react-timeago";

function App() {
  const { sendMessage, lastMessage, readyState } = useWebSocket(
    "ws://localhost:8080/topics",
    {
      onOpen: () => console.log("opened"),
      shouldReconnect: (closeEvent) => true,
    }
  );

  const connectionStatus = {
    [ReadyState.CONNECTING]: "Connecting",
    [ReadyState.OPEN]: "Open",
    [ReadyState.CLOSING]: "Closing",
    [ReadyState.CLOSED]: "Closed",
    [ReadyState.UNINSTANTIATED]: "Uninstantiated",
  }[readyState];

  const [topic, setTopic] = useState("");
  const [msg, setMsg] = useState("");
  const [messageHistory, setMessageHistory] = useState([]);

  const handleMsg = () => {
    sendMessage(JSON.stringify({ topic, msg }));
    setTopic("");
    setMsg("");
  };

  useEffect(() => {
    if (lastMessage !== null) {
      setMessageHistory((prev) => [JSON.parse(lastMessage.data), ...prev]);
      console.log(messageHistory)
    }
  }, [lastMessage, setMessageHistory]);

  return (
    <>
      <CssBaseline />
      <Box>
        <AppBar sx={{ padding: ".75rem" }} position="static">
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Kafka UI
          </Typography>
          <Typography variant="body" component="div" sx={{ flexGrow: 1 }}>
            {connectionStatus}
          </Typography>
        </AppBar>
      </Box>
      <Box>
        <Container>
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <Box sx={{ padding: "1rem" }}>
                <TextField
                  id="outlined-basic"
                  label="Topic"
                  variant="outlined"
                  fullWidth
                  sx={{ marginBottom: "1rem" }}
                  value={topic}
                  onChange={(e) => setTopic(e.target.value)}
                />
                <TextField
                  id="outlined-basic"
                  label="Message"
                  variant="outlined"
                  fullWidth
                  multiline
                  sx={{ marginBottom: "1rem" }}
                  value={msg}
                  onChange={(e) => setMsg(e.target.value)}
                />
                <Button onClick={handleMsg} fullWidth variant="contained">
                  Send
                </Button>
              </Box>
            </Grid>
            <Grid item xs={6}>
              <List sx={{ width: "100%" }}>
                {messageHistory.map((msg, i) => (
                  <>
                    <ListItem alignItems="flex-start">
                      <ListItemText>
                        <Typography variant="body">
                          {msg.msg && msg.msg}
                        </Typography>
                        <br />
                        <Typography variant="caption">
                          {msg.topic && msg.topic} &bull;{" "}
                          <ReactTimeago date={msg.ts} />{" "}
                        </Typography>
                      </ListItemText>
                    </ListItem>
                    <Divider component="li" />
                  </>
                ))}

                {messageHistory.length === 0 ? (
                  <>
                    <ListItem alignItems="flex-start">
                      <ListItemText primary="Nothing to show here" secondary="Send some messages to see them here" />
                    </ListItem>
                    <Divider component="li" />
                  </>
                ) : null}
              </List>
            </Grid>
          </Grid>
        </Container>
      </Box>
    </>
  );
}

export default App;
