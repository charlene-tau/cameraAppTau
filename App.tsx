import React from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import HomeScreen from './components/HomeScreen';
import CameraScreen from './components/CameraScreen';
import PhotoPreviewScreen from './components/PhotoPreviewScreen';
import 'react-native-gesture-handler';
const Stack = createStackNavigator();

const App = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="HomeScreen">
        <Stack.Screen name="HomeScreen" component={HomeScreen} />
        <Stack.Screen name="CameraScreen" component={CameraScreen} />
        <Stack.Screen name="PhotoPreviewScreen" component={PhotoPreviewScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default App;