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

export default class HomeScreen extends React.Component {
  constructor() {
    super();
    this.state = {
      loading: false,
      tracks: []
    };
    this.logout = this.logout.bind(this);
  }

  logout() {
    AsyncStorage.removeItem('token');
    this.props.navigation.navigate('Auth');
  }

  componentDidMount() {
      this.setState({loading:true});
      SpotifyModule.loadTracks().then((bundle) => {
        console.log(bundle);
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
          <ListItem key={key} thumbnail>
          <Left>
            <Thumbnail square source={{ uri: 'Image URL' }} />
          </Left>
          <Body>
            <Text>{track.name}</Text>
            <Text note numberOfLines={1}>{track.artists}</Text>
          </Body>
          <Right>
            <Button transparent>
              <Text>{Math.floor(moment.duration(parseInt(track.duration_ms)).asMinutes())}:{moment.duration(parseInt(track.duration_ms)).seconds() < 10 ? '0' : ''}{moment.duration(parseInt(track.duration_ms)).seconds()}</Text>
            </Button>
          </Right>
        </ListItem>
        ))}
      </List>
        <Button onPress={this.logout}><Text>Logout</Text></Button>
      </ScrollView>
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