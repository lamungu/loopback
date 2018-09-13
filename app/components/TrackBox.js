import React, { Component } from 'react';
import {getTime} from '../helpers'
import {
  StyleSheet,
  View,
  Text,
  Image,
} from 'react-native';

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
    }
  });
  