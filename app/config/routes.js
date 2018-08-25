import React from 'react';
import LoginScreen from '../screens/LoginScreen';
import AuthLoadingScreen from '../screens/AuthLoadingScreen';
import HomeScreen from '../screens/HomeScreen';
import {
  AsyncStorage,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import {
  Button
} from 'native-base';
import TouchableItem from 'react-navigation-drawer/dist/views/TouchableItem';
import { DrawerItems, SafeAreaView, createSwitchNavigator, createDrawerNavigator, createStackNavigator } from 'react-navigation';


const AppStack = createDrawerNavigator({
   Home: HomeScreen,
},{
  contentComponent: props => 
    (
    <ScrollView>
      <DrawerItems {...props}/>
      <TouchableItem onPress={()=>{AsyncStorage.removeItem('token'); props.navigation.navigate('Auth');}} delayPressIn={0}>
        <SafeAreaView forceInset={{left: 'always','right': 'never', vertical: 'never'}}>
          <View style={[styles.item]}>
            <Text style={[styles.label]}>
              Logout
            </Text>
          </View>
        </SafeAreaView>
      </TouchableItem>
    </ScrollView>
    )
});
const AuthStack = createStackNavigator({Login: LoginScreen});

export default RootStack = createSwitchNavigator(
  {
    AuthLoading: AuthLoadingScreen,
    App: AppStack,
    Auth: AuthStack,
  },
  {
    initialRouteName: 'AuthLoading',
  }
);


const styles = StyleSheet.create({
  container: {
    paddingVertical: 4
  },
  item: {
    flexDirection: 'row',
    alignItems: 'center'
  },
  icon: {
    marginHorizontal: 16,
    width: 24,
    alignItems: 'center'
  },
  inactiveIcon: {
    /*
     * Icons have 0.54 opacity according to guidelines
     * 100/87 * 54 ~= 62
     */
    opacity: 0.62
  },
  label: {
    margin: 16,
    fontWeight: 'bold'
  }
});
