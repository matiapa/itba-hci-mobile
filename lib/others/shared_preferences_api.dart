import 'package:shared_preferences/shared_preferences.dart';

class LocalStorageApi{

  static LocalStorageApi _singleton = LocalStorageApi._internal();

  LocalStorageApi._internal();

  factory LocalStorageApi.instance() {
    return _singleton;
  }

  SharedPreferences _sharedPrefs;

  Future<void> initialize() async {
    _sharedPrefs = await SharedPreferences.getInstance();
  }

  bool get firstOpen => _sharedPrefs.getBool('firstOpen');

  set firstOpen(value) => _sharedPrefs.setBool('firstOpen', value);

}