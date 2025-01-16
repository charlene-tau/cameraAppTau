import React,{useState,useEffect } from 'react';
import {StyleSheet, View, TouchableOpacity, Text} from 'react-native';
import {NativeModules} from 'react-native';
const {CameraModule} = NativeModules;

const CameraScreen=({navigation}: {navigation: any})=>{
    const [flash, setFlash] = useState(false);
    useEffect(() => {
    // Call the openCamera method when the screen loads
    CameraModule.openCamera();
  }, []);
    const toggleFlash = async () => {
    try {
    console.log("Toggle Flash clicked")
      const newFlashState = await CameraModule.toggleFlashMode();
      setFlash(newFlashState);
    } catch (error) {
      console.error('Error toggling flash:', error);
    }
  };

  const takePhoto = async () => {
    try {
      const photoPath = await CameraModule.openCamera();
      console.log("take photo function called")
      navigation.navigate('PhotoPreviewScreen', {photoPath});
    } catch (error) {
      console.error('Error taking photo:', error);
    }

  };

  return (
    <View style={styles.container}>
        <Text>Camera is opening...</Text>
      {/* Camera Preview Placeholder */}
      <View style={styles.cameraPreview} />

      {/* Flash Toggle Button */}
      <TouchableOpacity style={styles.flashButton} onPress={toggleFlash}>
        <Text style={styles.flashText}>Flash: {flash ? 'On' : 'Off'}</Text>
      </TouchableOpacity>

      {/* Capture Button */}
      <TouchableOpacity style={styles.captureButton} onPress={takePhoto}>
        <Text style={styles.captureText}>Capture</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'blue',
    justifyContent: 'flex-end',
    alignItems: 'center',
  },
  cameraPreview: {
    flex: 1,
    width: '100%',
  },
  flashButton: {
    position: 'absolute',
    top: 10,
    right: 20,
    backgroundColor: 'white',
    padding: 10,
    borderRadius: 5,
  },
  flashText: {
    color: 'blue',
    fontWeight: 'bold',
  },
  captureButton: {
    backgroundColor: 'white',
    width: 80,
    height: 80,
    borderRadius: 50,
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: 20,
  },
  captureText: {
    color: 'black',
    fontWeight: 'bold',
  },
});

export default CameraScreen;
