import React, { Component } from 'react';
import Video from 'react-native-video';
import SpotifyModule from './SpotifyModule';

import {
  Text,
  View,
  StyleSheet
} from 'react-native';

import {
    Button,
    Container,
    Header,
    Body,
    Content,
    Icon,
} from 'native-base';

type Props = {};
export default class App extends Component<Props> {
    constructor() {
        super();
        this.onBuffer = this.onBuffer.bind(this);
        this.onEnd = this.onEnd.bind(this);
        this.videoError = this.videoError.bind(this);
        this.loginSpotify = this.loginSpotify.bind(this);
    }

    onBuffer() {
        console.log('buffered')
    }

    onEnd() {
        console.log('ended')
    }

    videoError() {
        console.log('error')
    }

    loginSpotify() {
        SpotifyModule.login();
    }

    render() {
      return (
          <Container>
              <Video source={require('./Untitled.mp4')}   // Can be a URL or a local file.
                     ref={(ref) => {
                         this.player = ref
                     }}
                     repeat
                     resizeMode="cover"
                     onBuffer={this.onBuffer}                // Callback when remote video is buffering
                     onEnd={this.onEnd}                      // Callback when playback finishes
                     onError={this.videoError}               // Callback when video cannot be loaded
                     style={styles.backgroundVideo} />
              <Content>
                  <Body style={styles.body}>
                      <Button onPress={this.loginSpotify} iconLeft block success>
                        <Icon name="spotify" type="FontAwesome"/>
                        <Text style={styles.text}>&nbsp;Login with Spotify</Text>
                      </Button>
                  </Body>
              </Content>
          </Container>

      );
    }
}

const styles = StyleSheet.create({
  backgroundVideo: {
      position: 'absolute',
      top: 0,
      left: 0,
      bottom: 0,
      right: 0,
  },
    body: {
        flexDirection: "row", justifyContent: "center"
    },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  text: {
    color: '#fff'
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
