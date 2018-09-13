import React from 'react';
import moment from 'moment';
import {
  Right,
  Body,
  List,
  ListItem
} from 'native-base';
import {
  ScrollView,
  StyleSheet,
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

  async componentDidMount() {
      this.setState({loading:true});
      this.props.navigation.navigate('Player', {trackId: 'spotify:track:0zXGYS4MVxtxCFmhCtDJsr', trackUri: 'spotify:track:0zXGYS4MVxtxCFmhCtDJsr'});
      let cachedTracks = await AsyncStorage.getItem('tracks');
      if (!cachedTracks) {
        SpotifyModule.loadTracks().then(async (bundle) => {
            await AsyncStorage.setItem('tracks', JSON.stringify(bundle.tracks))
            this.setState({
                loading:false,
                tracks: bundle.tracks
            });
        }).catch((err) => console.log(err));
      } else {
        this.setState({
            loading:false,
            tracks: JSON.parse(cachedTracks)
        });
      }
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