import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity } from 'react-native';
import * as Iflytek  from '../../src';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();
  const text = "本示例为讯飞语音Android平台开发者提供语音听写、语法识别、语义理解和语音合成等代码样例，旨在让用户能够依据该示例快速开发出基于语音接口的应用程序。"
  React.useEffect(() => {
    Iflytek.init("72488635");
    (async () => {
      const res = await Iflytek.createSynthesizer();
      console.log(res);
    })()

    return () => {
      Iflytek.stopSpeaking();
      Iflytek.destroy();
    }
  }, []);

  const startSpeaking = () => {
    Iflytek.startSpeaking(text, (...e) => {
      console.log(e);
    })
  }

  const stopSpeaking = () => {
    Iflytek.stopSpeaking()
  }

  const pauseSpeaking = () => {
    Iflytek.pauseSpeaking()
  }

  const resumeSpeaking = () => {
    Iflytek.resumeSpeaking()
  }

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <TouchableOpacity onPress={startSpeaking}>
        <Text>开始合成</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={stopSpeaking}>
        <Text>暂停合成</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={pauseSpeaking}>
        <Text>暂停播放</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={resumeSpeaking}>
        <Text>继续播放</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
