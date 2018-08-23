import React from 'react';
import {
  StyleSheet,
  View,
  Text
} from 'react-native';

export default class HomeScreen extends React.Component {
  // Render any loading content that you like here
  render() {
    return (
      <View style={styles.container}>
        <Text>Hi, you're authenticated</Text>
      </View>
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