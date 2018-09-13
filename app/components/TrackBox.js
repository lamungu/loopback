import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  Text,
} from 'react-native';

export default class TrackBox extends Component {
    render() {
      return (
        <View style={styles.container}>
          <View style={[styles.trackBox]}>
            <View style={[styles.trackAlbum]}>
              <View style={[styles.picture]}></View>
            </View>
            <View style={[styles.trackDetails]}>
              <Text style={[styles.trackName]}>{this.props.track.name}</Text>
              <Text style={[styles.trackArtist]}>{this.props.track.artistName}</Text>
            </View>
            <View style={[styles.trackDuration]}>
              <Text>0:00/{getTime(this.state.track.durationMs)}</Text>
            </View>
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
      flex:2,
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
      backgroundColor: '#2196F3'
    }
  });
  