import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'rn-device-integrity' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const RnDeviceIntegrity = NativeModules.RnDeviceIntegrity
  ? NativeModules.RnDeviceIntegrity
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export const getDeviceToken = async () => {
  return RnDeviceIntegrity.getDeviceToken();
};
