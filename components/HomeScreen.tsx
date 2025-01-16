import React from 'react';
import {Button, View, StyleSheet} from 'react-native';

const HomeScreen = ({navigation}: {navigation: any}) => {
  return (
    <View style={styles.container}>
      <Button
        title="Open Camera test"
        onPress={() => navigation.navigate('CameraScreen')}
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
