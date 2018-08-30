import React from 'react';
import SpotifyModule from '../modules/SpotifyModule';
import moment from 'moment';
import {
  ActivityIndicator,
  AsyncStorage,
  StatusBar,
  StyleSheet,
  View,
} from 'react-native';

export default class AuthLoadingScreen extends React.Component {
  constructor(props) {
    super(props);
    this._bootstrapAsync();
  }

  // Fetch the token from storage then navigate to our appropriate place
  _bootstrapAsync = async () => {
    const token = await AsyncStorage.getItem('token');
    if (token) {
      //Parse the token to init the function. if its not yet expired, init right away
      let parsedToken = JSON.parse(token);
      if (moment().isAfter(moment.unix(parsedToken.expiresIn))) {
        //Invalidate token, as it has expired;
        await AsyncStorage.removeItem('token');
        this.props.navigation.navigate('Auth');    
      } else {
        await SpotifyModule.init(parsedToken);
        this.props.navigation.navigate('App');
        return;    
      }
    }
    // This will switch to the App screen or Auth screen and this loading
    // screen will be unmounted and thrown away.
    this.props.navigation.navigate('Auth');
  };

  // Render any loading content that you like here
  render() {
    return (
      <View style={styles.container}>
        <ActivityIndicator />
        <StatusBar barStyle="default" />
      </View>
    );
  }
}

const styles = StyleSheet.create({
    container: {
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