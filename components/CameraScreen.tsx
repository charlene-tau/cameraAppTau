import React from 'react';
import { requireNativeComponent } from 'react-native';

const CameraView = requireNativeComponent('CameraView');

const CameraScreen = () => {
    return (
        <CameraView style={{ flex: 1 }} />
    );
};

export default CameraScreen;
