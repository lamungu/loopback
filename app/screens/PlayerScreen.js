import React from 'react';
import moment from 'moment';
import {
  Button,
  Left,
  Right,
  Body,
  Thumbnail,
  List,
  ListItem
} from 'native-base';
import {
  StyleSheet,
  View,
  Text,
} from 'react-native';
import SpotifyModule from '../modules/SpotifyModule';

const getTime = (time) => {
  return Math.floor(moment.duration(parseInt(time)).asMinutes()) + ':' + (moment.duration(parseInt(time)).seconds() < 10 ? '0' : '') + moment.duration(parseInt(time)).seconds();
}
export default class PlayerScreen extends React.Component {
  constructor() {
    super();
    this.state = {
      track: {}
    };
  }

  async componentWillMount() {
    await SpotifyModule.initPlayer();
  }

  componentDidMount() {
      const { navigation } = this.props;
      
      const trackUri = navigation.getParam('trackUri', 'NO-URI');
      this.setState({loading:true});
      SpotifyModule.loadTrack(trackUri).then((bundle) => {
        this.setState({
           loading:false,
           track: {
             name: trackUri
           }
        });
      }).catch((err) => console.log(err));
  }

  // Render any loading content that you like here
  render() {
    return (
      <View>
        <Text>
          {this.state.track.name}
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  trackName: {
      fontSize:16,
      fontWeight:"700",
  },
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