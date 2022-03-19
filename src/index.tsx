import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-iflytek' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const Iflytek = NativeModules.Iflytek
  ? NativeModules.Iflytek
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function init(appID: string) {
  return Iflytek.init(appID);
}

export function createSynthesizer(): Promise<string> {
  return Iflytek.createSynthesizer();
}

export function startSpeaking(text: string, callback: (e: string, percent?: number, beginPos?: number, endPos?: number) => void) {
  return Iflytek.startSpeaking(text, callback);
}

export function stopSpeaking() {
  return Iflytek.stopSpeaking();
}

export function pauseSpeaking() {
  return Iflytek.pauseSpeaking();
}

export function resumeSpeaking() {
  return Iflytek.resumeSpeaking();
}

export function destroy() {
  return Iflytek.destroy();
}
