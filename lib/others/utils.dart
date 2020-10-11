import 'dart:io';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sprintf/sprintf.dart';

class Utils{

  static String formatDate(DateTime date){

    return sprintf("%d/%d/%d", [date.day, date.month, date.year]);

  }


  static String enumToStr(dynamic enumm) => enumm.toString().split('.')[1].toUpperCase();


  static showSnackbar(String text, BuildContext context){

    Scaffold.of(context).hideCurrentSnackBar();
    
    Scaffold.of(context).showSnackBar(
      SnackBar(
        content: Text(text),
        duration: Duration(seconds: 4),
      )
    );

  }


  static checkInternetConnection(BuildContext context) async {

    try {

      await InternetAddress.lookup('google.com');

    } on SocketException catch (_) {
      
      showDialog(
        barrierDismissible: false,
        context: context,
        child: AlertDialog(
          title: Text('No hay conexión'),
          content: Text('Por favor, verificá tu conexión a internet y volvé a intentarlo'),
        )
      );
      
    }

  }


  static Widget buildColumnDialog({Widget title, List<Widget> children, double height=400, double width=300}){

    return AlertDialog(
      title: title,
      content: Container(
        height: height,
        width: width,
        child: ListView(
          shrinkWrap: true,
          children: children
        )
      )
    );

  }


  static showLoadingDialog(BuildContext context, {@required String title, @required String description}){
    showDialog(
      barrierDismissible: false,
      context: context,
      child: AlertDialog(
        title: Text('Iniciando sesión'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text('Por favor, espera'),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: CircularProgressIndicator(),
            )
          ],
        ),
      )
    );
  }

}