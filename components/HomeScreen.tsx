import React,{ useEffect } from 'react';
import {Button, View, StyleSheet,NativeModules,NativeEventEmitter,DeviceEventEmitter } from 'react-native';
import { useFocusEffect } from '@react-navigation/native';

const {CameraModule} = NativeModules;
const HomeScreen = ({navigation}: {navigation: any}) => {

useEffect(() => {
  console.log("hello world")
    const subscription = DeviceEventEmitter.addListener('ImageCaptured', (data) => {
      console.log("in Homescreen useEffect")
        if (data === 'success') {
            navigation.replace('NextScreen');
        }
    });

    return () => {
        subscription.remove();
    };
}, []);


  const openCamera = async () => {
    try {
      console.log('openCamera called Success disneypg');
      // Call the native `openCamera` method
      const result = await CameraModule.openCamera();
      console.log('Camera Success', `Result: ${result}`);
    } catch (error) {
      console.error('Error opening camera:', error);
      //Alert.alert('Camera Error', error.message || 'Failed to open camera');
    }
  };
  return (
    <View style={styles.container}>
      <Button
        title="Open Camera test"
        onPress={openCamera}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'pink',
  },
});

export default HomeScreen;
