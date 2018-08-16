import React, { Component } from 'react';
import Video from 'react-native-video';
import SpotifyModule from './SpotifyModule';

import {
  Platform,
  StyleSheet,
  Text,
  View
} from 'react-native';
import {
    Button,
    Icon
} from 'native-base';
const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

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
        <View style={styles.container}>
            <Button onPress={this.loginSpotify} style={styles.button} iconLeft success>
                <Icon name="spotify" type="FontAwesome"/>
                <Text style={styles.text}>Login with Spotify</Text>
            </Button>
        </View>
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
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  button: {},
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
