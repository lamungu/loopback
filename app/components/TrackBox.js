import React, { Component } from 'react';
import {getTime} from '../helpers'
import {
  StyleSheet,
  View,
  Text,
  Image,
} from 'react-native';
import {
    Button,
    Icon,
    Slider
} from 'react-native-elements';

export default class TrackBox extends Component {
    render() {
      return (
        <View style={styles.container}>
          <View style={[styles.trackBox]}>
            <View style={[styles.trackAlbum]}>
                <Image
                    style={[styles.picture]}
                    source={{uri: this.props.track.albumCoverWebUrl}}
                />
            </View>
            <View style={[styles.trackDetails]}>
              <Text style={[styles.trackName]}>{this.props.track.name}</Text>
              <Text style={[styles.trackArtist]}>{this.props.track.artistName}</Text>
            </View>
            <View style={[styles.trackDuration]}>
              <Text>0:00/{getTime(this.props.track.durationMs)}</Text>
            </View>
          </View>
          <View style={[styles.trackSlider]}>
                <Slider/>
          </View>
          <View style={[styles.trackControls]}>    
            <Button icon={{ name: 'replay-5', color: '#000', size: 75 }} backgroundColor="transparent" buttonStyle={[ styles.trackControlSmall ]}/>
            <Button icon={{ name: 'play-circle-outline', color: '#000', size: 100 }} backgroundColor="transparent" buttonStyle={[ styles.trackControlLarge ]}/>
            <Button icon={{ name: 'forward-5', color: '#000', size: 75 }} backgroundColor="transparent" buttonStyle={[ styles.trackControlSmall ]}/>
          </View>
        </View>
      );
    }
  }
  
  const styles = StyleSheet.create({
    container: {
      flex: 1,
      flexDirection: 'column'
    },
    trackBox: {
      padding: 20,
      paddingLeft: 40,
      paddingRight: 40,
      height: 120,
      flexDirection: 'row',
    },
    trackSlider: {
      height: 100,
      paddingLeft: 40,
      paddingRight: 40,
    },
    trackControls: {
      flexDirection: 'row',
      justifyContent: 'center',
      paddingLeft: 40,
      paddingRight: 40,
    },
    trackControlLarge: {
      width: 126,
      height: 126
    },
    trackControlSmall: {
      width: 100,
      height: 100
    },
    trackName: {
    },
    trackArtist: {
    },
    trackDetails: {
      flex: 4,
      flexDirection: 'column',
      justifyContent: 'center'
    },
    trackDuration: {
      flex: 2,
      flexDirection: 'column',
      justifyContent: 'center'
    },
    trackAlbum: {
      flex: 2,
      justifyContent: 'center'
    },
    picture: {
      width: 55,
      height: 55,
      borderRadius: 50,
    }
  });
  