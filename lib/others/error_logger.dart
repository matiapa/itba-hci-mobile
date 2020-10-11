import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:device_info/device_info.dart';
import 'package:flutter/foundation.dart';
import 'package:package_info/package_info.dart';

class ErrorLogger{

  static void log({@required String context, @required String error, String userUID}) async {

    AndroidDeviceInfo deviceInfo = await DeviceInfoPlugin().androidInfo;

    await Firestore.instance.collection('errors').add({
      'error': error,
      'context': context,
      'userRef': Firestore.instance.collection('users').document(userUID),
      'date': DateTime.now(),
      'appVersion': (await PackageInfo.fromPlatform()).version,
      'device': {
        'model': deviceInfo.model,
        'os': {
          'name': deviceInfo.version.baseOS,
          'version': deviceInfo.version.sdkInt
        },
      }
    });

    throw Exception('$context: $error');

  }

}