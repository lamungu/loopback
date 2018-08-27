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
  ScrollView,
  StyleSheet,
  View,
  Text,
  AsyncStorage
} from 'react-native';
import SpotifyModule from '../modules/SpotifyModule';

const getTime = (time) => {
  return Math.floor(moment.duration(parseInt(time)).asMinutes()) + ':' + (moment.duration(parseInt(time)).seconds() < 10 ? '0' : '') + moment.duration(parseInt(time)).seconds();
}
export default class HomeScreen extends React.Component {
  static navigationOptions = {
    drawerLabel: 'Home',
  };

  constructor() {
    super();
    this.state = {
      loading: false,
      tracks: []
    };
  }

  componentDidMount() {
      this.setState({loading:true});
      SpotifyModule.loadTracks().then((bundle) => {
        this.setState({
           loading:false,
           tracks: bundle.tracks
        });
      }).catch((err) => console.log(err));
  }

  // Render any loading content that you like here
  render() {
    return (
      <ScrollView style={styles.container}>
        <List>
          {this.state.tracks.map((track, key) => (
            <ListItem key={key} onPress={() => this.props.navigation.navigate('Player', {trackId: track.id, trackUri: track.uri})} button={true}>
              <Body>
                <Text style={styles.trackName}>{track.name}</Text>
                <Text note numberOfLines={1}>{track.artists.join(', ')}</Text>
              </Body>
              <Right>
                  <Text>{getTime(track.duration_ms)}</Text>      
              </Right>
            </ListItem>
          ))}
        </List>
      </ScrollView>
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