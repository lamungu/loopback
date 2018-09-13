import React from "react";
import TrackBox from '../components/TrackBox';
import {
  Left,
  Right,
  Body,
  Thumbnail,
  Icon,
  List,
  ListItem
} from "native-base";
import { DeviceEventEmitter, StyleSheet, View, Text } from "react-native";
import SpotifyModule from "../modules/SpotifyModule";

export default class PlayerScreen extends React.Component {
  constructor() {
    super();
    this.state = {
      track: {},
      value: 0,
    };
  }

  componentWillMount() {
    DeviceEventEmitter.addListener("player.metadata-changed", e => {
      console.log(e);
      this.setState({
        track: { ...e }
      });
    });
  }

  componentDidMount() {
    const { navigation } = this.props;

    console.log("its coming bois");
    const trackUri = navigation.getParam("trackUri", "NO-URI");
    this.setState({ loading: true });
    SpotifyModule.initPlayer().then(() => {
      console.warn(trackUri);
      SpotifyModule.loadTrack(trackUri)
        .then(bundle => {
          this.setState({
            loading: false,
          });
        })
        .catch(err => {
          console.log("an error occured!");
          console.log(err);
        });
    });
  }

  // Render any loading content that you like here
  render() {
    return (
      <View>
        {Object.keys(this.state.track).length > 0 && (
            <TrackBox track={this.state.track}/>
        )}
      </View>
    );
  }
}
