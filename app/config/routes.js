import LoginScreen from '../screens/LoginScreen';
import AuthLoadingScreen from '../screens/AuthLoadingScreen';
import HomeScreen from '../screens/HomeScreen';
import { createSwitchNavigator, createStackNavigator } from 'react-navigation';

const AppStack = createStackNavigator({ Home: HomeScreen});
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