import React, { Component } from 'react';
import { AsyncStorage } from 'react-native';
import Video from 'react-native-video';
import SpotifyModule from '../modules/SpotifyModule';
import {
  Text,
  View,
  StyleSheet
} from 'react-native';

import {
    Button,
    Icon,
} from 'native-base';

export default class LoginScreen extends Component {
    static navigationOptions = {
        header: null
    }
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

    async loginSpotify() {
        SpotifyModule.login().then(async (response) => {
            console.log(response.accessToken);
            console.log(response.expiresIn);
            await AsyncStorage.setItem('userToken', response.accessToken);
            await AsyncStorage.setItem('token', JSON.stringify(response));
            this.props.navigation.navigate('App');
        }).catch(error => {
            console.log(error);
        });
    }

    render() {
      return (
        <View style={styles.alignWrap}>
            <Video source={require('../resources/login-hero.mp4')}   // Can be a URL or a local file.
                ref={(ref) => {
                    this.player = ref
                }}
                repeat
                resizeMode="cover"
                onBuffer={this.onBuffer}                // Callback when remote video is buffering
                onEnd={this.onEnd}                      // Callback when playback finishes
                onError={this.videoError}               // Callback when video cannot be loaded
                style={styles.backgroundVideo} />
            <Button onPress={this.loginSpotify} success style={styles.button}>
                <Icon name="spotify" type="FontAwesome"/>
                <Text style={styles.text}>Login with Spotify&nbsp;</Text>
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
  body: {
      flexDirection: "row", 
      flex: 1,
      justifyContent: "center",
      alignItems: 'center'
  },
  button: {
      alignSelf:'center'
  },
  alignWrap: {
      flex: 1,
      justifyContent: "center",
      alignItems:"center"
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
