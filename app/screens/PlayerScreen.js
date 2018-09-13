import React from "react";
import {Slider, Button} from "react-native-elements";
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
import {getTime} from '../helpers';

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

const styles = StyleSheet.create({
  trackName: {
    fontSize: 16,
    fontWeight: "700"
  },
  backgroundVideo: {
    position: "absolute",
    top: 0,
    left: 0,
    bottom: 0,
    right: 0
  },
  body: {
    flexDirection: "row",
    flex: 1,
    justifyContent: "center",
    alignItems: "center"
  },
  button: {
    alignSelf: "center"
  },
  alignWrap: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center"
  },
  welcome: {
    fontSize: 20,
    textAlign: "center",
    margin: 10
  },
  text: {
    color: "#fff"
  },
  instructions: {
    textAlign: "center",
    color: "#333333",
    marginBottom: 5
  }
});
