import 'dart:io';

import 'package:avancer/others/shared_preferences_api.dart';
import 'package:flutter/material.dart';
import 'package:package_info/package_info.dart';
import 'package:provider/provider.dart';
import 'package:url_launcher/url_launcher.dart';
import 'models/user.dart';
import 'others/remote_config_api.dart';
import 'package:firebase_crashlytics/firebase_crashlytics.dart';

import 'package:avancer/screens/request_loan_screen.dart';
import 'package:avancer/screens/all_loans_screen.dart';
import 'package:avancer/screens/main_screen.dart';
import 'package:avancer/screens/signin_screen.dart';
import 'package:avancer/screens/welcome_screen.dart';
import 'package:avancer/screens/profile_screen.dart';

void main() {

  Crashlytics.instance.enableInDevMode = true;
  FlutterError.onError = Crashlytics.instance.recordFlutterError;

  runApp(
    ChangeNotifierProvider(
      create: (context) => User(),
      child: MyApp(),
    ),
  );
}

class MyApp extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    
    return GestureDetector(
      onTap: () {
        FocusScopeNode currentFocus = FocusScope.of(context);

        if (!currentFocus.hasPrimaryFocus) {
          currentFocus.unfocus();
        }
      },
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        title: 'Avancer',

        onGenerateRoute: (settings) {
          var args = settings.arguments as Map<String, dynamic>;

          switch(settings.name){

            case '/app': return MaterialPageRoute<bool>(
              builder: (context) => MyApp(),
            );

            case '/welcome': return MaterialPageRoute<bool>(
              builder: (context) => WelcomeScreen()
            );

            case '/signin': return MaterialPageRoute<bool>(
              builder: (context) => SignInScreen()
            );

            case '/main': return MaterialPageRoute<bool>(
              builder: (context) => MainScreen(),
            );

            case '/requestLoan': return MaterialPageRoute<bool>(
              builder: (context) => RequestLoanScreen(args['amount']),
            );

            case '/allLoans': return MaterialPageRoute<bool>(
              builder: (context) => AllLoansScreen()
            );

            case '/profile': return MaterialPageRoute<bool>(
              builder: (context) => ProfileScreen()
            );

            default:
              throw Exception('Invalid route name');

          }
        },

        theme: ThemeData(
          primaryColor: Color.fromRGBO(69, 39, 160, 1),
          buttonTheme: Theme.of(context).buttonTheme.copyWith(
            buttonColor: Color.fromRGBO(69, 39, 160, 1),
            textTheme: ButtonTextTheme.primary,
          ),
          visualDensity: VisualDensity.adaptivePlatformDensity,
        ),

        home: Builder(
          builder: (context){
            return FutureBuilder(
              future: loadApp(context),
              builder: (context, snapshot){

                if(snapshot.hasError){
                  return buildErrorScreen(context);
                }

                switch(snapshot.connectionState){
                  
                  case ConnectionState.done:
                    return MainScreen();
                    break;

                  default:
                    return buildLoadingScreen(context);

                }
              } 
            );
          }
        ),
      ) 
    );
  }


  Future<void> loadApp(BuildContext context) async {

    await InternetAddress.lookup('google.com');

    await RemoteConfigApi.instance().initialize();
    await LocalStorageApi.instance().initialize();


    // Check for mandatory updates

    var appVersion = versionStrToInt((await PackageInfo.fromPlatform()).version);
    var mandatoryVersion = versionStrToInt(RemoteConfigApi.instance().lastMandatoryVersion);

    if(appVersion < mandatoryVersion){
      while(true){
        await showDialog(
          context: context,
          barrierDismissible: false,
          child: buildUpdateDialog(context)
        );
      }
    }

    // Check for first open

    if(LocalStorageApi.instance().firstOpen ?? true){
      LocalStorageApi.instance().firstOpen = false;
      await Navigator.of(context).pushNamed('/welcome');
    }


    // Check for login

    var user = Provider.of<User>(context, listen: false);

    await user.loadCurrentUser();

    while(! user.isSignedIn())
      await Navigator.of(context).pushNamed('/signin');

    await user.initializeData();

  }


  Widget buildLoadingScreen(BuildContext context){
    
    return Scaffold(
      backgroundColor: Theme.of(context).primaryColor,
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Stack(
                alignment: Alignment.center,
                children: <Widget>[
                  
                  Image.asset(
                    'assets/logo_white.png',
                    width: 35, height: 35
                  ),

                  SizedBox(
                    child: CircularProgressIndicator(
                      valueColor: new AlwaysStoppedAnimation<Color>(Colors.grey[400]),
                      backgroundColor: Colors.white,
                    ),
                    height: 50,
                    width: 50
                  ),

                ]
              )
            ),

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                'Avancer',
                style: Theme.of(context).textTheme.headline6.copyWith(color: Colors.white),
              ),
            )
            
          ],
        ),
      )
    );

  }


  Widget buildErrorScreen(BuildContext context){
    
    return Scaffold(
      backgroundColor: Theme.of(context).primaryColor,
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Image.asset(
                'assets/logo_small.png',
                width: 35, height: 35
              ),
            ),

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                'Avancer',
                style: Theme.of(context).textTheme.headline6.copyWith(color: Colors.white),
              ),
            ),

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                'Lo sentimos, se produjo un error. Por favor, verificá tu conexión a Internet',
                textAlign: TextAlign.center,
                style: Theme.of(context).textTheme.subtitle1.copyWith(color: Colors.white),
              ),
            )

          ],
        ),
      )
    );

  }


  Widget buildUpdateDialog(BuildContext context){
    return AlertDialog(
      title: Text('Debés actualizar la app'),
      content: Text('Por favor, descargá la última versión'),
      actions: <Widget>[
        FlatButton(
          child: Text('Ir a la Play Store'),
          onPressed: () => launch('https://play.google.com/store/apps/details?id=com.avancer.avancer'),
        )
      ],
    );
  }


  int versionStrToInt(String version){

    var nums = version.split('.');

    return int.parse(nums[0])*100 + int.parse(nums[1])*10 + int.parse(nums[2]);

  }

}