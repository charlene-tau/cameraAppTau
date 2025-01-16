import React from 'react';
import {StyleSheet, View, Image, TouchableOpacity, Text} from 'react-native';

const PhotoPreviewScreen = ({route, navigation}: {route: any; navigation: any}) => {
  console.log("inside photo preview screen ")
    const {photoPath} = route.params;
console.log("phjotopath: ", photoPath)
  return (
    <View style={styles.container}>
      <Image source={{uri: 'file://' + photoPath}} style={styles.image} />
      <TouchableOpacity
        style={styles.button}
        onPress={() => navigation.navigate('CameraScreen')}>
        <Text style={styles.buttonText}>Retake Photo</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'black',
  },
  image: {
    width: '100%',
    height: '80%',
    resizeMode: 'contain',
  },
  button: {
    backgroundColor: 'white',
    padding: 10,
    borderRadius: 5,
    marginTop: 20,
  },
  buttonText: {
    color: 'black',
    fontWeight: 'bold',
  },
});

export default PhotoPreviewScreen;